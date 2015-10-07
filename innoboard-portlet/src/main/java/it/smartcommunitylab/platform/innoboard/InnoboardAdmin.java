package it.smartcommunitylab.platform.innoboard;

import it.smartcommunitylab.platform.innoboard.converter.ArticleData;
import it.smartcommunitylab.platform.innoboard.converter.ProjectConverter;
import it.smartcommunitylab.platform.innoboard.converter.XLSConverter;
import it.smartcommunitylab.platform.innoboard.converter.model.Project;
import it.smartcommunitylab.platform.innoboard.converter.model.RowValues;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.google.common.collect.Maps;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class InnoboardAdmin
 */
public class InnoboardAdmin extends MVCPortlet {

	public static final String PROJECT_STRUCTURE_NAME = "TSC Project structure 2";
	public static final String PROJECT_TEMPLATE_NAME = "TSC Project template";
	
	public void uploadData(ActionRequest actionRequest, ActionResponse actionResponse) {
		UploadPortletRequest request = PortalUtil.getUploadPortletRequest(actionRequest);
		
		File file = request.getFile("file");
		System.err.println("FILE UPLOADED " + file.getName());
		
		try {
//			deleteSavedJournals();
			
			long groupdId = ServiceContextFactory.getInstance(actionRequest).getScopeGroupId();
			long userId = ServiceContextFactory.getInstance(actionRequest).getUserId();
			
			XLSConverter xlsConverter = new XLSConverter();
			InputStream inp = new FileInputStream(file);
			List<RowValues> valuesList = xlsConverter.readExcel(inp);
			Map<String, Project> projects = xlsConverter.convert(valuesList);

			System.err.println("FILE PARSED " + file.getName());
			
			ProjectConverter projectConverter = new ProjectConverter();
			
			Map<String, ArticleData> contents = projectConverter.buildArticleDatas(projects);	
			Map<String, JournalArticle> saved = getSavedJournals();
			
			projectConverter.modifyContents(contents, saved);
			
			addArticles(groupdId, userId, contents, saved);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addArticles(long groupId, long userId, Map<String, ArticleData> contents, Map<String, JournalArticle> saved) throws Exception {
		ServiceContext serviceContext = new ServiceContext();
		
		List<DDMStructure> structs = DDMStructureLocalServiceUtil.getStructures();
		DDMStructure struct = null;
		for (DDMStructure s : structs) {
			if (s.getName().contains(PROJECT_STRUCTURE_NAME)) {
				struct = s;
				break;
			}
		}
		
		List<DDMTemplate> templates = DDMTemplateLocalServiceUtil.getTemplatesByClassPK(groupId, struct.getPrimaryKey());
		DDMTemplate templ = null;
		for (DDMTemplate t : templates) {
			if (t.getName().contains(PROJECT_TEMPLATE_NAME)) {
				templ = t;
				break;
			}
		}
		
		String sk = (struct != null)?struct.getStructureKey():null;
		String tk = (templ != null)?templ.getTemplateKey():null;
		
		serviceContext.setScopeGroupId(groupId);
		serviceContext.setUserId(userId);			
		
		for (String title : contents.keySet()) {
			if (saved.containsKey(title)) {
				JournalArticle article = saved.get(title);
				serviceContext.setAssetCategoryIds(contents.get(title).getCategoriesIds());
				JournalArticleLocalServiceUtil.updateArticle(userId, groupId, 0, article.getArticleId(), article.getVersion(), contents.get(title).getContent(), serviceContext);
			} else {
				Map<Locale, String> map = Maps.newHashMap();
				map.put(Locale.US, title);
				map.put(Locale.ITALY, title);
				serviceContext.setAssetCategoryIds(contents.get(title).getCategoriesIds());
//				serviceContext.setAssetTagNames(contents.get(title).getSubcategories().toArray(new String[0]));
				JournalArticle added = JournalArticleLocalServiceUtil.addArticle(userId, groupId, 0, map, map, contents.get(title).getContent(), sk, tk, serviceContext);
			}

		}
	}
	
	private void deleteSavedJournals() throws Exception {
		List<DDMStructure> structs = DDMStructureLocalServiceUtil.getStructures();
		DDMStructure struct = null;
		for (DDMStructure s : structs) {
			if (s.getName().contains(PROJECT_STRUCTURE_NAME)) {
				struct = s;
				break;
			}
		}	
		
		Map<String, JournalArticle> result = Maps.newTreeMap();
		List<JournalArticle> articles = JournalArticleLocalServiceUtil.getArticles();
		for (JournalArticle article: articles) {
			if (article.getStructureId().equals(struct.getStructureKey())) {
				JournalArticleLocalServiceUtil.deleteArticle(article);
			}
		}		
	}
	
	private Map<String, JournalArticle> getSavedJournals() throws Exception {
		List<DDMStructure> structs = DDMStructureLocalServiceUtil.getStructures();
		DDMStructure struct = null;
		for (DDMStructure s : structs) {
			if (s.getName().contains(PROJECT_STRUCTURE_NAME)) {
				struct = s;
				break;
			}
		}	
		
		Map<String, JournalArticle> result = Maps.newTreeMap();
		List<JournalArticle> articles = JournalArticleLocalServiceUtil.getArticles();
		for (JournalArticle article: articles) {
			if (article.getStructureId().equals(struct.getStructureKey()) && !article.isInTrash()) {
				if (!result.containsKey(article.getTitle(Locale.ITALY)) || result.get(article.getTitle(Locale.ITALY)).getVersion() < article.getVersion()) {
					result.put(article.getTitle(Locale.ITALY), article);
				}
			}
		}
		
		return result;
	}
	
}
