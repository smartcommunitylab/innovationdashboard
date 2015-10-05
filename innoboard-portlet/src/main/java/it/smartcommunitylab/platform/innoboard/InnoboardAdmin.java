package it.smartcommunitylab.platform.innoboard;

import it.smartcommunitylab.platform.innoboard.converter.ArticleGenerator;
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
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
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
			XLSConverter converter = new XLSConverter();
			InputStream inp = new FileInputStream(file);
			List<RowValues> valuesList = converter.readExcel(inp);
			List<Project> projects = converter.convert(valuesList);

			System.err.println("FILE PARSED " + file.getName());
			
			Project prj = projects.get(0);
			System.out.println("BUILT:");
			
			ArticleGenerator generator = new ArticleGenerator();
			
			String content = generator.buildProjectContent(prj);	
			System.out.println(content);
			
			ServiceContext serviceContext = new ServiceContext();

			User user = (User) request.getAttribute(WebKeys.USER);

			List<DDMStructure> structs = DDMStructureLocalServiceUtil.getStructures();
			DDMStructure struct = null;
			for (DDMStructure s : structs) {
				if (s.getName().contains(PROJECT_STRUCTURE_NAME)) {
					struct = s;
					break;
				}
			}
			
			List<DDMTemplate> templates = DDMTemplateLocalServiceUtil.getTemplatesByClassPK(ServiceContextFactory.getInstance(actionRequest).getScopeGroupId(), struct.getPrimaryKey());
			DDMTemplate templ = null;
			for (DDMTemplate t : templates) {
				if (t.getName().contains(PROJECT_TEMPLATE_NAME)) {
					templ = t;
					break;
				}
			}
			
			String sk = (struct != null)?struct.getStructureKey():null;
			String tk = (templ != null)?templ.getTemplateKey():null;
			
			serviceContext.setScopeGroupId(ServiceContextFactory.getInstance(actionRequest).getScopeGroupId());
			serviceContext.setUserId(ServiceContextFactory.getInstance(actionRequest).getUserId());			
			
			Map<Locale, String> map = Maps.newHashMap();
			map.put(Locale.US, prj.getTitolo());
			map.put(Locale.ITALY, prj.getTitolo());
			
			JournalArticleLocalServiceUtil.addArticle(ServiceContextFactory.getInstance(actionRequest).getUserId(), ServiceContextFactory.getInstance(actionRequest).getScopeGroupId(), 0, map, map, content, sk, tk, serviceContext);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	private String getContent() throws Exception {
		String s = new String(FileUtil.getBytes(Thread.currentThread().getContextClassLoader().getResourceAsStream("progetto.txt")));
//		System.out.println(s);
//		System.out.println("_______________________");
		return s;
	}
	

}
