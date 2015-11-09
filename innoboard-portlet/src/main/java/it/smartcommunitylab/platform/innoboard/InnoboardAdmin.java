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
import java.util.TreeMap;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.google.common.collect.Maps;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
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

	public static final String PROJECT_STRUCTURE_NAME = "TSC Project structure";
	public static final String PROJECT_TEMPLATE_NAME = "TSC Project template";

	public void uploadData(ActionRequest actionRequest, ActionResponse actionResponse) {
		UploadPortletRequest request = PortalUtil.getUploadPortletRequest(actionRequest);

		File file = request.getFile("file");
		System.err.println("FILE UPLOADED " + file.getName());

		try {
			// deleteSavedJournals();

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

	private void addArticles(long groupId, long userId, Map<String, ArticleData> contents,
			Map<String, JournalArticle> saved) throws Exception {
		ServiceContext serviceContext = new ServiceContext();

		List<DDMStructure> structs = DDMStructureLocalServiceUtil.getStructures();
		DDMStructure struct = null;
		for (DDMStructure s : structs) {
			if (s.getName().contains(PROJECT_STRUCTURE_NAME)) {
				struct = s;
				break;
			}
		}

		List<DDMTemplate> templates = DDMTemplateLocalServiceUtil.getTemplatesByClassPK(groupId,
				struct.getPrimaryKey());
		DDMTemplate templ = null;
		for (DDMTemplate t : templates) {
			if (t.getName().contains(PROJECT_TEMPLATE_NAME)) {
				templ = t;
				break;
			}
		}

		String sk = (struct != null) ? struct.getStructureKey() : null;
		String tk = (templ != null) ? templ.getTemplateKey() : null;

		long companyId = struct.getCompanyId();

		serviceContext.setScopeGroupId(groupId);
		serviceContext.setUserId(userId);

		for (String title : contents.keySet()) {
			if (saved.containsKey(title)) {
				JournalArticle article = saved.get(title);
				serviceContext.setAssetCategoryIds(contents.get(title).getCategoriesIds());
				System.out.print("Updating: " + title + "...");

				JournalArticle newArticle = JournalArticleLocalServiceUtil.updateArticleTranslation(groupId,
						article.getArticleId(), article.getVersion(), Locale.US, article.getTitle(Locale.US),
						article.getDescription(Locale.US), contents.get(title).getContent(),
						new TreeMap<String, byte[]>(), serviceContext);
				// Role role = RoleLocalServiceUtil.getRole(companyId,
				// RoleConstants.SITE_MEMBER);
				// ResourcePermissionLocalServiceUtil.addResourcePermission(companyId,
				// JournalArticle.class.getName(),
				// ResourceConstants.SCOPE_INDIVIDUAL,
				// Long.toString(newArticle.getResourcePrimKey()),
				// role.getRoleId(), ActionKeys.VIEW);
				JournalArticleLocalServiceUtil.updateArticleTranslation(groupId, article.getArticleId(),
						newArticle.getVersion(), Locale.ITALY, article.getTitle(Locale.ITALY),
						article.getDescription(Locale.ITALY), contents.get(title).getContent(),
						new TreeMap<String, byte[]>(), serviceContext);
				JournalArticleLocalServiceUtil.addArticleResources(newArticle, true, true);
				System.out.println(" ...Updated.");
			} else {
				Map<Locale, String> titleMap = Maps.newHashMap();
				titleMap.put(Locale.US, title);
				titleMap.put(Locale.ITALY, title);

				Map<Locale, String> descriptionMap = Maps.newHashMap();
				descriptionMap.put(Locale.US, contents.get(title).getDescription());
				descriptionMap.put(Locale.ITALY, contents.get(title).getDescription());

				serviceContext.setAssetCategoryIds(contents.get(title).getCategoriesIds());
				// serviceContext.setAssetTagNames(contents.get(title).getSubcategories().toArray(new
				// String[0]));
				System.out.print("Creating: " + title + "...");
				JournalArticle added = JournalArticleLocalServiceUtil.addArticle(userId, groupId, 0, titleMap,
						descriptionMap, contents.get(title).getContent(), sk, tk, serviceContext);
				// Role role = RoleLocalServiceUtil.getRole(companyId,
				// RoleConstants.SITE_MEMBER);
				// ResourcePermissionLocalServiceUtil.addResourcePermission(companyId,
				// JournalArticle.class.getName(),
				// ResourceConstants.SCOPE_INDIVIDUAL,
				// Long.toString(added.getResourcePrimKey()), role.getRoleId(),
				// ActionKeys.VIEW);
				JournalArticleLocalServiceUtil.addArticleResources(added, true, true);
				System.out.println(" ...Created.");
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
		for (JournalArticle article : articles) {
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
		for (JournalArticle article : articles) {
			if (article.getStructureId().equals(struct.getStructureKey()) && !article.isInTrash()) {
				if (!result.containsKey(article.getTitle(Locale.ITALY))
						|| result.get(article.getTitle(Locale.ITALY)).getVersion() < article.getVersion()) {
					result.put(article.getTitle(Locale.ITALY), article);
				}
			}
		}

		return result;
	}

}
