package it.smartcommunitylab.platform.innoboard.converter;

import it.smartcommunitylab.platform.innoboard.converter.model.Project;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.WordUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;

public class ProjectConverter {

	private static final String TEXT = "text";
	
	private static final String IMAGE_TAG = "<dynamic-element name=\"image\"";
	private static final String ROOT_CLOSED_TAG = "</root>";		
	
	private static final String CONTENT_NODE = "<dynamic-element name=\"%s\" type=\"%s\" index-type=\"keyword\" index=\"0\">\n" + "\t<dynamic-content language-id=\"it_IT\"><![CDATA[%s]]>\n"
			+ "\t</dynamic-content>\n" + "</dynamic-element>\n";
	private static final String DOC = "<?xml version=\"1.0\"?>\n" + "<root available-locales=\"it_IT\" default-locale=\"it_IT\">\n" + "%s" + "</root>\n";	
	
	private Properties entries;
	private Map<String, Long> categoriesIds;
	
	private List<String> avanzamento = Lists.newArrayList("Approvato e in attesa di avvio", "Avviato e in sviluppo", "Completato/operativo");
	
	public ProjectConverter() throws Exception {
		entries = new Properties();
		
		entries.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("conversion/articleEntries.properties"));
		
		categoriesIds = Maps.newTreeMap();
		for (AssetCategory cat: AssetCategoryLocalServiceUtil.getCategories()) {
			categoriesIds.put(cat.getName(), cat.getCategoryId());
		}
	}	
	
	public Map<String, ArticleData> buildArticleDatas(Map<String, Project> projects) throws Exception {
		Map<String, ArticleData> result = Maps.newTreeMap();
		
		for (String key: projects.keySet()) {
			result.put(key, buildArticleData(projects.get(key)));
		}
		
		return result;
	}
	
	
	public ArticleData buildArticleData(Project project) throws Exception {
		StringBuffer sb = new StringBuffer();

		ObjectMapper mapper = new ObjectMapper();
		Class<?> clazz = Class.forName("it.smartcommunitylab.platform.innoboard.converter.model.Project");
		for (Object key : entries.keySet()) {
			String entry = key.toString();
			String value = entries.getProperty(entry);
			String text = null;

			String splitValue[] = value.split("-");
			if (splitValue.length == 1) {
				Method method = clazz.getMethod("get" + WordUtils.capitalize(entry));
				text = (String) method.invoke(project);
			} else {
				if ("list".equals(splitValue[1])) {
					Method method = clazz.getMethod("get" + WordUtils.capitalize(entry));
					List list = (List) method.invoke(project);
					text = list.toString().replace("[", "").replace("]", "");
				}
				if ("map".equals(splitValue[1])) {
					Method method = clazz.getMethod("get" + WordUtils.capitalize(entry));
					Map map = (Map) method.invoke(project);
					text = mapper.writeValueAsString(map);
				}
				if ("custom".equals(splitValue[1])) {
					if ("finanziamentoPubblico".equals(entry)) {
						text = Boolean.toString(!project.getFinanziamentoPubblico().isEmpty());
					}
					if ("statoAvanzamento".equals(entry)) {
						text = Integer.toString(avanzamento.indexOf(project.getStatoAvanzamento()) + 1);
					}					
					
				}				
			}
			sb.append(buildNode(entry, splitValue[0], text));
		}
		String content = String.format(DOC, sb.toString());
		
		ArticleData ad = new ArticleData();
		ad.setContent(content);
		ad.setCategoriesIds(buildCategories(project));
		ad.setSubcategories(buildSubcategories(project));
		ad.setDescription(project.getAbstractProgetto());
		
		return ad;
	}
	
	private String buildNode(String name, String type, String content) {
		String res = String.format(CONTENT_NODE, name, type, content);
		return res;
	}	
	
	private long[] buildCategories(Project project) {
		Set<Long> catSet = Sets.newHashSet();
		for (String key: project.getAmbitoPrimario().keySet()) {
			if (categoriesIds.containsKey(key)) {
				catSet.add(categoriesIds.get(key));
			}
		}
		for (String key: project.getAmbitoSecondario().keySet()) {
			if (categoriesIds.containsKey(key)) {
				catSet.add(categoriesIds.get(key));
			}
		}		
		
		List<Long> catList = Lists.newArrayList(catSet);
		long[] res = new long[catList.size()];
		for (Long l: catList) {
			if (catList.indexOf(l) != -1) {
				res[catList.indexOf(l)] = l;
			}
		}
		
		return res;
	}
	
	private List<String> buildSubcategories(Project project) {
		Set<String> subSet = Sets.newHashSet();
		for (String key: project.getAmbitoPrimario().keySet()) {
			subSet.addAll(project.getAmbitoPrimario().get(key));
		}
		for (String key: project.getAmbitoSecondario().keySet()) {
			subSet.addAll(project.getAmbitoSecondario().get(key));
		}		
		
		return Lists.newArrayList(subSet);
	}
	
	public void modifyContents(Map<String, ArticleData> contents, Map<String, JournalArticle> saved) {
		for (String key: contents.keySet()) {
			if (!saved.containsKey(key)) {
				continue;
			}
			String contentString = contents.get(key).getContent();
			String savedString = saved.get(key).getContent();
			int savedIndex = savedString.indexOf(IMAGE_TAG);
			if (savedIndex != -1) {
				String sub = savedString.substring(savedIndex);
				String newContentString = contentString.replace(ROOT_CLOSED_TAG, sub);
				contents.get(key).setContent(newContentString);
			}
		}
	}		
	
}
