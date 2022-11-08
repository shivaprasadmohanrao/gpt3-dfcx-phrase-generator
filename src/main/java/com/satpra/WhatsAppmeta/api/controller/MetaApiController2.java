package com.satpra.WhatsAppmeta.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.satpra.WhatsAppmeta.api.dao.Image;
import com.satpra.WhatsAppmeta.api.dao.Messages;
import com.satpra.WhatsAppmeta.api.dao.MessagesRes;
import com.satpra.WhatsAppmeta.api.dao.OpenAIReq;
import com.satpra.WhatsAppmeta.api.dao.OpenAIRes;
import com.satpra.WhatsAppmeta.api.dao.Text;
import com.satpra.WhatsAppmeta.api.dao.WaMsgResponse;
import com.satpra.WhatsAppmeta.api.messages.dao.MessageRequest;


@RestController
public class MetaApiController2 {
	String wamid = "top";
	boolean firstMsgDone = false;
	private static String name;
	
	
	/** Webhook verification API.
	 * For proper validation, set your own token value and set it in the loca variable of this class or in environment variable.
	 * In meta developer portal, we need to verify this application. For that webhook URL (ngrok URL) and token is passed.
	 * If the method invoked by webhook API returns the hub.challenge integer then meta will verify your application, else it will fail.
	 * 
	 * App sending message payload : https://developers.facebook.com/docs/whatsapp/cloud-api/guides/send-messages#media-messages
	 * App receiving message from Users : https://developers.facebook.com/docs/whatsapp/cloud-api/webhooks/payload-examples
	 * 
	 * */
	
//	private final static String TOKEN = "SOME_TOKEN";
	
	@GetMapping(path="/rcvmsg2")
	public Integer findUser(@RequestParam(name = "hub.challenge") Integer metaBody, @RequestParam(name = "hub.verify_token") String token) {
		System.out.println("received token from " + metaBody);
		// compare TOKEN with query param token
		return metaBody;// Return the hub.challenge received in the query param
	}
	
	@PostMapping(path="/rcvmsg2")
	public void getMessage(@RequestBody MessagesRes messages) throws Exception {
		//System.out.println("Inside Body " + messages);
		Messages[] getMessages = messages.getEntry()[0].getChanges()[0].getValue().getMessages();
		
		//open ai image call
//		String responseImageURL;
		String to = "919113669741";
		String welcomeText = "Welcome to AI Artist - text-to-image Generator. I can help you generate an Image using AI based on the text you provide. May I know your name, please?";
		String imageGenText = "Please be cautious about your text/words. Now please go on and type a "
				+ "text/word/meaning full short sentence which I can use to generate meaningful Image.";
		String thankYouText = "Hope you liked the image. Thanks for using AI Artist. Bye till you comeback";
//		String body="";
//		String tti;
		
		
		
		if(getMessages != null) {
			String userUtterance = getMessages[0].getText().getBody();
			System.out.println("userUtterance = " + userUtterance);
			if("hi".equalsIgnoreCase(userUtterance)) {
				// Send welcome message and ask to enter the subject for image generation
				sendWhatsappMesg("text", to, null, welcomeText);
			} else {
				// generate image and send it back
				sendWhatsappMesg("image", to, textToImageURL(userUtterance), null);
				Thread.sleep(3000);
				sendWhatsappMesg("text", to, null, thankYouText);
			}
			
		}
	}

	//this method uses Whatsapp cloud api to send out a message, uses bearer token, which has 23 hours expiry date.
	private String sendWhatsappMesg(String textOrImage, String to, String responseImageURL,String textBody) {
		String url = "https://graph.facebook.com/v15.0/107259991994522/messages";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		List<MediaType> list = new ArrayList<MediaType>();
	
		list.add(MediaType.ALL);
		headers.setAccept(list);
		//set this value every 23 hours, copy from whatsapp configuration portal
		headers.setBearerAuth("EABpQp0sCqSsBAD7rIjhqymjOyMNAsCU63PKzYHa2HHzMr82E3jz32ZCv6fIJs3pYVZBMyDdF5SMgVaQZCgM0DmbIZBofSndKTfJiNYwZAYWt4ZB1FLRRGF1KV3FvkHEmndb8YtMjWhZBt704pWRzRarhZBGrpZBwk1quoMDW7qcR05wN7Il6135Xhyp0pOOgcQfXydPFQ8qZBqyDTXs6Wf13s2");
		
		MessageRequest mRequest = new MessageRequest();
		mRequest.setMessaging_product("whatsapp");
		mRequest.setRecipient_type("individual");
		mRequest.setTo(to);
		
		if(textOrImage.equalsIgnoreCase("text")) {
			Text text = new Text();
			text.setPreview_url(false);
			text.setBody(textBody);
			mRequest.setType("text");
			mRequest.setText(text);
		}
		
		if(textOrImage.equalsIgnoreCase("image")) {
			Image image = new Image();
			image.setLink(responseImageURL);
			mRequest.setType("image");
			mRequest.setImage(image);
		}
		
		//Template template = new Template();
		//Language lang = new Language();
		//lang.setCode("en_US");
		//template.setLanguage(lang);
		//template.setName("hello_world");
		//mRequest.setTemplate(template);
		//mRequest.setType("template");
		
		HttpEntity<MessageRequest> waRequest = new HttpEntity<MessageRequest>(mRequest, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<WaMsgResponse> response = restTemplate.exchange(url, HttpMethod.POST, waRequest, WaMsgResponse.class);
		return response.getBody().getMessages()[0].getId();
		
	}

	private String textToImageURL(String tti) {
		String url = "https://api.openai.com/v1/images/generations";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		List<MediaType> list = new ArrayList<MediaType>();

		list.add(MediaType.ALL);
		headers.setAccept(list);
		headers.setBearerAuth("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");
		
		OpenAIReq mRequest = new OpenAIReq();
		mRequest.setN(1);
		mRequest.setPrompt(tti);
		mRequest.setSize("1024x1024");
		
		HttpEntity<OpenAIReq> waRequest = new HttpEntity<OpenAIReq>(mRequest, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<OpenAIRes> response = restTemplate.exchange(url, HttpMethod.POST, waRequest, OpenAIRes.class);
		System.out.println("URL " + response.getBody().getData()[0].getUrl());
		return response.getBody().getData()[0].getUrl();
	}

}
