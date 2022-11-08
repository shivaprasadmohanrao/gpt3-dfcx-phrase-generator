package com.satpra.WhatsAppmeta.api.messages.dao;

public class Template {
	
	String name;
	Language language;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	@Override
	public String toString() {
		return "Template [name=" + name + ", language=" + language + ", getName()=" + getName() + ", getLanguage()="
				+ getLanguage() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
