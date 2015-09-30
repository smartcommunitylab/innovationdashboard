package it.smartcommunitylab.platform.innoboard;

import java.io.File;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class InnoboardAdmin
 */
public class InnoboardAdmin extends MVCPortlet {

	public void uploadData(ActionRequest actionRequest, ActionResponse actionResponse) {
		UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);
		File file = uploadPortletRequest.getFile("file");
		System.err.println("FILE UPLOADED "+file.getName());
}
 

}
