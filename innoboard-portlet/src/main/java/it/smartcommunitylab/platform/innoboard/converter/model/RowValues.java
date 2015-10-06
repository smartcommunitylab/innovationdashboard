package it.smartcommunitylab.platform.innoboard.converter.model;

import java.util.List;

import com.google.common.collect.Lists;

public class RowValues {

	private String name;
	private List<RowValues> children;
	private List<String> values;
	private int dupIndex;

	public RowValues() {
		values = Lists.newArrayList();
		children = Lists.newArrayList();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getValues() {
		return values;
	}

	public List<RowValues> getChildren() {
		return children;
	}

	public void setChildren(List<RowValues> children) {
		this.children = children;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public int getDupIndex() {
		return dupIndex;
	}

	public void setDupIndex(int dupIndex) {
		this.dupIndex = dupIndex;
	}

	@Override
	public String toString() {
		return name + "[" + dupIndex + "]=" + values;
	}

}
