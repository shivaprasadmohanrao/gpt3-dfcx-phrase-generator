package com.technocouple.promptGenerator.api.dao;

import java.util.List;

public class CreateIntentRequest {
	
	private String displayName;
	private List<TrainingPhrases> trainingPhrases;
	
	public CreateIntentRequest() {}
	public CreateIntentRequest(String displayName, List<TrainingPhrases> trainingPhrases) {
		super();
		this.displayName = displayName;
		this.trainingPhrases = trainingPhrases;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public List<TrainingPhrases> getTrainingPhrases() {
		return trainingPhrases;
	}
	public void setTrainingPhrases(List<TrainingPhrases> trainingPhrases) {
		this.trainingPhrases = trainingPhrases;
	}
	@Override
	public String toString() {
		return "CreateIntentRequest [displayName=" + displayName + ", trainingPhrases="
				+ trainingPhrases + "]";
	}
	
	

}
