package com.technocouple.promptGenerator.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.technocouple.promptGenerator.api.dao.CreateIntentRequest;
import com.technocouple.promptGenerator.api.dao.OpenAITextCompletion;
import com.technocouple.promptGenerator.api.dao.OpenAITextCompletionReq;
import com.technocouple.promptGenerator.api.dao.Parts;
import com.technocouple.promptGenerator.api.dao.TrainingPhrases;
import com.technocouple.promptGenerator.api.dao.UserInput;
import com.technocouple.promptGenerator.api.dao.DFCXApiResponse;

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
		String[] entries = openAIPromptGenerator(messages.getInput()).split("\n");
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
		createDfcxVirtualAgent(openAIPhrases);
		return "Sucessfully Updated into your Dialogflow CX Console!";
	}
	/**
	 * This method connects to OpenAI completions API and return the Phrases
	 * @param intent
	 * @return
	 */
	private static String openAIPromptGenerator(String intent) {
		//OpenAI URL : <base_url> + <completions_api>
		String url = "https://api.openai.com/v1/completions";
		//add Headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		List<MediaType> list = new ArrayList<MediaType>();
		list.add(MediaType.ALL);
		headers.setAccept(list);
		//get this from https://beta.openai.com/account/api-keys 90days validity for free account
		headers.setBearerAuth("<OpenAI_API_KEY>");
		
		//Build Request object for OpenAI Completion API
		OpenAITextCompletionReq mRequest = new OpenAITextCompletionReq();
		//set Model as TEXT_DAVINCI
		mRequest.setModel("text-davinci-003");
		//PromptEngineering - prompt to generate training phrases for a given Intent
		mRequest.setPrompt("Generate 10 professional user utterances for the intent "
				+ "\""+ intent +"\" and display without numbering and bullet points");
		//Temperature helps to create phrases in creative to realistic way based on the configured value
		mRequest.setTemperature(0.2);
		mRequest.setMax_tokens(450);
		mRequest.setTop_p(1);
		mRequest.setFrequency_penalty(0);
		mRequest.setPresence_penalty(0);
		
		HttpEntity<OpenAITextCompletionReq> waRequest = new HttpEntity<OpenAITextCompletionReq>(mRequest, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<OpenAITextCompletion> response = restTemplate.exchange(url, HttpMethod.POST, waRequest, OpenAITextCompletion.class);
		return response.getBody().getChoices()[0].getText();
	}
    /**
     * This method helps in invoking Google Dialogflow CX api's to 
     * 1. Create Intent and upload Training Phrases
     * 2. Train the Virtual Agent
     * @param trainingPhrases
     */
    private static void createDfcxVirtualAgent(String[] trainingPhrases) {
    	int numOfPhrase = trainingPhrases.length;
    	String entryText = "";
    	System.out.println("NumberOfPhrases : " + numOfPhrase);
    	
    	//URL of Dialogflow CX :
    	//POST https://REGION_ID-dialogflow.googleapis.com/v3/projects/PROJECT_ID/locations/REGION_ID/agents/AGENT_ID/intents
    	String url = "https://global-dialogflow.googleapis.com/v3/projects/<<PROJECT_ID>>/locations/"
    			+ "<<REGION_ID>>/agents/<<AGENT_ID>>/intents";
    	//add headers
    	HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		List<MediaType> list = new ArrayList<MediaType>();
	
		list.add(MediaType.ALL);
		//Change this based on GCP project : <PROJECT_ID>
		headers.add("x-goog-user-project", "<PROJECT_ID>");
		headers.setAccept(list);
		//Bearer Token
		//to get temporary GCP token run this command from Google cloud shell "gcloud auth print-access-token"
		//this can also be retrieved through proper Google cloud api and store it in cache as per your requirement
		headers.setBearerAuth("<<GCP_BEARER_TOKEN>>");
		//Request object creation
		CreateIntentRequest createIntentRequest = new CreateIntentRequest();
		List<TrainingPhrases> tPhrases = new ArrayList<>();
		
		//Ensuring the Bullets and numberings are removed from the OpenAI response and add newline feed.
		for(int i=0;i<numOfPhrase; i++) {
			int j =0;
			if(trainingPhrases[i].startsWith("1") || trainingPhrases[i].startsWith("2") || trainingPhrases[i].startsWith("3") || 
					trainingPhrases[i].startsWith("4") || trainingPhrases[i].startsWith("5") || trainingPhrases[i].startsWith("6") 
					|| trainingPhrases[i].startsWith("7") || trainingPhrases[i].startsWith("8") ||
					trainingPhrases[i].startsWith("9") || trainingPhrases[i].startsWith("10")) {
					entryText = trainingPhrases[i].substring(3);
					System.out.println("text " + entryText);
			}else {
				entryText = trainingPhrases[i];
				}
			System.out.println("entry Text " + entryText);
			
			//validation and forming request object in the required format
			/*
			 * { "displayName": "TravelDispName", "trainingPhrases": [ { "parts": [ {
			 * "parameterId": "", "text": "I need to plan my trip" } ], "repeatCount": 1 },
			 * { "parts": [ { "parameterId": "", "text":
			 * "Help me to build itinerary for the upcoming trip" } ], "repeatCount": 1 } ]
			 * }
			 */
			if(entryText !=null && !entryText.isBlank() && !entryText.isEmpty()) {
				List<Parts> partsList1 = new ArrayList<>();
				Parts firstText = new Parts();
				firstText.setParameterId("");
				firstText.setText(entryText);
				partsList1.add(0, firstText);
				
				TrainingPhrases trPh1 = new TrainingPhrases();
				trPh1.setParts(partsList1);
				trPh1.setRepeatCount("1");
				
				tPhrases.add(j, trPh1);
				j++;
				}
			else {

				List<Parts> partsList1 = new ArrayList<>();
				Parts firstText = new Parts();
				firstText.setParameterId("");
				firstText.setText(entryText);
				partsList1.add(0, firstText);
				
				TrainingPhrases trPh1 = new TrainingPhrases();
				trPh1.setParts(partsList1);
				trPh1.setRepeatCount("1");
				
				tPhrases.add(j, trPh1);
				j++;
				
			}
		}
		
		createIntentRequest.setDisplayName(intentName);
		createIntentRequest.setTrainingPhrases(tPhrases);
		
		System.out.println(createIntentRequest);
		
		HttpEntity<CreateIntentRequest> waRequest = new HttpEntity<CreateIntentRequest>(createIntentRequest, headers);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.exchange(url, HttpMethod.POST, waRequest, DFCXApiResponse.class);
	
	}
	
	
	
}
