package com.technocouple.promptGenerator.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.technocouple.promptGenerator.api.dao.UserInput;
import com.technocouple.promptGenerator.api.repository.DialogflowCXIntegration;
import com.technocouple.promptGenerator.api.repository.OpenAIIntegration;

//PreRequisities:
//1.OpenAI account, API key
//2.Google Cloud account - Dialogflow CX virtualagent, GCP bearer token
//3.Please use log library for logging the flow as per your requirement.
//4.We can cache the API key, access_token as per your requirement
@RestController
public class PhraseGeneratorController {
	
	static String intentName = "";
	static String[] openAIPhrases = {};
		
	/**
	 * this PostMapping method helps in fetching TrainingPhrases from OpenAI.
	 * @param messages
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin
	@PostMapping(path="/phrases")
	public String[] getTrainingPhrases(@RequestBody UserInput messages) throws Exception {
		System.out.println("User entered intent : " + messages.getInput());
		intentName = messages.getInput();
		//call to OpenAI Completions API
		String[] entries = OpenAIIntegration.openAIPromptGenerator(messages.getInput()).split("\n");
		int noOfPhrase = entries.length;
		StringBuilder entryText = new StringBuilder();
		//Ensuring the Bullets and numberings are removed from the OpenAI response and add newline feed.
		for(int i=0;i<noOfPhrase; i++) {
			if(entries[i].startsWith("1") || entries[i].startsWith("2") || entries[i].startsWith("3") || entries[i].startsWith("4") ||
					entries[i].startsWith("5") || entries[i].startsWith("6") || entries[i].startsWith("7") || entries[i].startsWith("8") ||
					entries[i].startsWith("9") || entries[i].startsWith("10")) {
					entryText.append(entries[i].substring(3));
					entryText.append("\n");
			}else {
				entryText.append(entries[i]);
				entryText.append("\n");
				}
		}
		//finalize the phrases as per requirement
		openAIPhrases =  entryText.toString().replaceAll("\n\n", "").replaceAll("\"", "").split("\n");
		System.out.println("Phrases : " + openAIPhrases);
		return openAIPhrases;
	}
	
	/**
	 * This PostMapping helps in training the Dialogflow CX virtual agent with the phrases generated for Intent
	 * @param messages
	 * @return
	 * @throws Exception
	 */
	@CrossOrigin
	@PostMapping(path="/trainDfcx")
	public String trainDFCXVirtualAgent(@RequestBody UserInput messages) throws Exception {
		//please use log lib as required
		System.out.println("User entered intent : " + messages.getInput());
		//System.out.println("DFCX Phrases  : " + openAIPhrases);
		DialogflowCXIntegration.createDfcxVirtualAgent(openAIPhrases,intentName);
		return "Sucessfully Updated into your Dialogflow CX Console!";
	}
	
}
