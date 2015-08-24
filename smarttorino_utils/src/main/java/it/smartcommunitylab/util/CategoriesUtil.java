package it.smartcommunitylab.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.XPath;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

public class CategoriesUtil {

	public static Map<String, String> getCategoriesColors(long groupId) throws SystemException, PortalException {
		return getStructureElementValue(groupId, Constants.CATEGORY_STRUCTURE_COLOR);
	}

	public static Map<String, String> getCategoriesImages(long groupId) throws SystemException, PortalException {
		return getStructureElementValue(groupId, Constants.CATEGORY_STRUCTURE_IMAGE);
	}
	
	public static Map<String, String> getCategoriesSquareImages(long groupId) throws SystemException, PortalException {
		return getStructureElementValue(groupId, Constants.CATEGORY_STRUCTURE_SQUAREIMAGE);
	}
	
	private static Map<String, String> getStructureElementValue(long groupId, String structureElement) throws SystemException, PortalException {
		Map<String, String> map = new HashMap<String, String>();
		AssetEntryQuery entryQuery = new AssetEntryQuery();
		XPath xpath = SAXReaderUtil.createXPath("dynamic-element[@name='" + structureElement + "']");

		entryQuery.setClassNameIds(new long[] { PortalUtil.getClassNameId(JournalArticle.class) });
		entryQuery.setClassTypeIds(
				new long[] { getStructureIdByStructureName(groupId, Constants.CATEGORY_STRUCTURE_NAME) });
		List<AssetEntry> entries = AssetEntryLocalServiceUtil.getEntries(entryQuery);

		if (entries != null && entries.size() > 0) {
			for (AssetEntry entry : entries) {
				if (entry.getCategoryIds() != null && entry.getCategoryIds().length > 0) {
					JournalArticle article = JournalArticleLocalServiceUtil.getLatestArticle(entry.getClassPK());
					try {
						Element el = SAXReaderUtil.read(article.getContent()).getRootElement();
						String elementString = xpath.selectSingleNode(el).getStringValue();
						map.put("" + entry.getCategoryIds()[0], elementString.trim());
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		}
		return map;
	}

	private static long getStructureIdByStructureName(long groupId, String structureName) throws SystemException {
		List<DDMStructure> structures = DDMStructureLocalServiceUtil.getStructures(groupId);

		for (DDMStructure structure : structures) {
			if (structure.getNameCurrentValue().equalsIgnoreCase(structureName)) {
				return structure.getStructureId();
			}
		}

		return -1;
	}

}
