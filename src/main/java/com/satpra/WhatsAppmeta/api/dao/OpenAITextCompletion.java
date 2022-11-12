package com.satpra.WhatsAppmeta.api.dao;

public class OpenAITextCompletion {
	
	private String id;
	private String object;
	private Choices[] choices;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public Choices[] getChoices() {
		return choices;
	}
	public void setChoices(Choices[] choices) {
		this.choices = choices;
	}

	
}
