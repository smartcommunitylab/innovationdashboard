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

	/* Colors utilities */
	public static String hex2rgb(String colorStr) {
		return hex2rgba(colorStr, null);
	}

	public static String hex2rgba(String colorStr, Float opacity) {
		String s = "";
		try {
			Integer r = Integer.valueOf(colorStr.substring(1, 3), 16);
			Integer g = Integer.valueOf(colorStr.substring(3, 5), 16);
			Integer b = Integer.valueOf(colorStr.substring(5, 7), 16);

			if (opacity != null && opacity >= 0 && opacity <= 100) {
				s = "rgba(" + r + "," + g + "," + b + "," + opacity + ")";
			} else {
				s = "rgb(" + r + "," + g + "," + b + ")";
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return s;
	}

	/* TSC Categories Utils */
	public static Map<String, String> getCategoriesColors(long groupId) {
		try {
			return getElementValuesByCategoryId(groupId, Constants.CATEGORY_STRUCTURE_NAME,
					Constants.CATEGORY_STRUCTURE_COLOR);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return null;
	}

	public static Map<String, String> getCategoriesImages(long groupId) {
		try {
			return getElementValuesByCategoryId(groupId, Constants.CATEGORY_STRUCTURE_NAME,
					Constants.CATEGORY_STRUCTURE_IMAGE);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return null;
	}

	public static Map<String, String> getCategoriesSquareImages(long groupId) {
		try {
			return getElementValuesByCategoryId(groupId, Constants.CATEGORY_STRUCTURE_NAME,
					Constants.CATEGORY_STRUCTURE_SQUAREIMAGE);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return null;
	}

	/* TSC Projects utils */
	public static Map<String, String> getProjectsImages(long groupId) {
		try {
			return getElementValuesByEntryId(groupId, Constants.PROJECT_STRUCTURE_NAME,
					Constants.PROJECT_STRUCTURE_IMAGE);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return null;
	}

	/* Generic methods and helpers */
	private static Map<String, String> getElementValuesByCategoryId(long groupId, String structureName,
			String structureElement) throws SystemException, PortalException {
		Map<String, String> map = new HashMap<String, String>();
		AssetEntryQuery entryQuery = new AssetEntryQuery();
		XPath xpath = SAXReaderUtil.createXPath("dynamic-element[@name='" + structureElement + "']");

		entryQuery.setClassNameIds(new long[] { PortalUtil.getClassNameId(JournalArticle.class) });
		entryQuery.setClassTypeIds(new long[] { getStructureIdByStructureName(groupId, structureName) });
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

	/* Generic methods and helpers */
	private static Map<String, String> getElementValuesByEntryId(long groupId, String structureName,
			String structureElement) throws SystemException, PortalException {
		Map<String, String> map = new HashMap<String, String>();
		AssetEntryQuery entryQuery = new AssetEntryQuery();
		XPath xpath = SAXReaderUtil.createXPath("dynamic-element[@name='" + structureElement + "']");

		entryQuery.setClassNameIds(new long[] { PortalUtil.getClassNameId(JournalArticle.class) });
		entryQuery.setClassTypeIds(new long[] { getStructureIdByStructureName(groupId, structureName) });
		List<AssetEntry> entries = AssetEntryLocalServiceUtil.getEntries(entryQuery);

		if (entries != null && entries.size() > 0) {
			for (AssetEntry entry : entries) {
				JournalArticle article = JournalArticleLocalServiceUtil.getLatestArticle(entry.getClassPK());
				try {
					Element el = SAXReaderUtil.read(article.getContent()).getRootElement();
					String elementString = xpath.selectSingleNode(el).getStringValue();
					map.put("" + entry.getEntryId(), elementString.trim());
				} catch (Exception e) {
					e.printStackTrace();
					continue;
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
