package it.smartcommunitylab.platform.innoboard.converter;

import java.util.List;

public class ArticleData {

	private String content;
	private String description;
	private long[] categoriesIds;
	private List<String> subcategories;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long[] getCategoriesIds() {
		return categoriesIds;
	}

	public void setCategoriesIds(long[] categoriesIds) {
		this.categoriesIds = categoriesIds;
	}

	public List<String> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<String> subcategories) {
		this.subcategories = subcategories;
	}

}
