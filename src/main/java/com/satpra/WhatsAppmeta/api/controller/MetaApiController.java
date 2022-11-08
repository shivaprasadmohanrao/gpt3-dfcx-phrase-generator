package com.satpra.WhatsAppmeta.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

//https://github.com/shivaprasadmohanrao/text-to-image-openai-whatsapp_cloud-api.git
@RestController
public class MetaApiController {
	static String wamid = "top";
	static Stack<String> wamidStack = new Stack<>();
	boolean firstMsgDone = false;
	List<String> hiList = new ArrayList<>();
	List<String> endList = new ArrayList<>();
	static String name = "";
	
	{
		wamidStack.add(0, wamid);
		hiList.add("hi");
		hiList.add("hi bot");
		hiList.add("hello bot");
		hiList.add("menu");
		hiList.add("start");
		hiList.add("hello");
		endList.add("close");
		endList.add("exit");
		endList.add("end");
		endList.add("bye");
		endList.add("stop");
		endList.add("terminate");
		
	}
	
	
	/** Webhook verification API.
	 * For proper validation, set your own token value and set it in the loca variable of this class or in environment variable.
	 * In meta developer portal, we need to verify this application. For that webhook URL (ngrok URL) and token is passed.
	 * If the method invoked by webhook API returns the hub.challenge integer then meta will verify your application, else it will fail.
	 * 
	 * App sending message payload : https://developers.facebook.com/docs/whatsapp/cloud-api/guides/send-messages#media-messages
	 * App receiving message from Users : https://developers.facebook.com/docs/whatsapp/cloud-api/webhooks/payload-examples
	 * 
	 * */
	
	/**
	 * Shiva - Steps as I understood this whatsapp cloud api mechanism:
	 * 1. First sign up facebook/meta developer account here : https://developers.facebook.com/apps/7407028842703147/whatsapp-business/wa-settings/?business_id=401121548577887
	 * 2. Create a app which is configured with whatsapp cloud app, I calle dit as BOT app in my account. note down appId etc. 
	 * 3. Products --> Whatsapp section --> configure webhook URL ( make sure your ngrok url is running on your local and youhave springboot controller with getmapping running
	 * in my case I have /rcvmsg GET mapping.) This controller url has to be pinged from meta servers first time from the webhook configuration. This makes sures the communication is established between meta
	 * and you springboot app. please note once you configure this, meta servers opens up a session with your springbpot and keeps pinging every seconds or so. so, its upto us how we manage and 
	 * control this. 
	 * 4. Products --> whatsapp -> getting started --> here you will get temporary OAuth access token with 23 hrs expiry date. this is used to use api of mets to send message to dedicated whatsapp number.
	 * this is used in method sendWhatsappMesg().
	 * 5. now in this app, we are generating image from text using OpenAI api's. which is implemented in textToImageURL().
	 * 6. OpenAI signUp gives 90 days $18 credit. And API key needs to be generated and used along with api call.
	 * */
	
//	private final static String TOKEN = "SOME_TOKEN";
	
	//This mapping helps for meta servers to authenticate your app, kind of verification for the webhook setup
	@GetMapping(path="/rcvmsg")
	public Integer findUser(@RequestParam(name = "hub.challenge") Integer metaBody, @RequestParam(name = "hub.verify_token") String token) {
		System.out.println("received token from " + metaBody);
		// compare TOKEN with query param token
		return metaBody;// Return the hub.challenge received in the query param
	}
	
	@PostMapping(path="/rcvmsg")
	public void getMessage(@RequestBody MessagesRes messages) throws Exception {
		//System.out.println("Inside Body " + messages);
		Messages[] getMessages = messages.getEntry()[0].getChanges()[0].getValue().getMessages();
		
		//open ai image call
		String responseImageURL;
		//String to = "919113669741";
		String to = "919834143672";
		String welcomeText = "✨ Welcome to *AI Artist - text-to-image Generator* 💬 ✨"
				+ "\n" + "Before we start how can i address you in this conversation? ";
		
		String imgText = "";
		
		String thankYouText = "_please note this is an computer generated image_" + "\n" 
				
				+ "\n" +  "Hope you liked the above *Image*. "
				+ "\n" +  "Thanks for using *AI Artist " + name + "*! GoodBye till you comeback 🙏🏻";
		
		String endMsg = "\n" + "To start again, please type *'Hi'* to start a new conversation"
				+ "\n" +  "_credits to *OpenAI API* and *Meta-Whatsapp* cloud API_";
		String byeMsg = "Thanks for using *AI Artist " + name + "*! GoodBye till you comeback 🙏🏻";
		String body="";
		String tti;
		
		
		
		if(getMessages != null) {
			System.out.println("Messages body " + getMessages[0].getText().getBody());
			System.out.println("Message Id " + getMessages[0].getId());
			
			if(endList.contains(getMessages[0].getText().getBody().toLowerCase())) {
				responseImageURL = textToImageURL("bye");
				wamid = sendWhatsappMesg(messages, "image", to, responseImageURL, null);
				Thread.sleep(3000);
				sendWhatsappMesg(messages, "text", to, null, byeMsg);
				sendWhatsappMesg(messages, "text", to, null, endMsg);
			}
			if(hiList.contains(getMessages[0].getText().getBody().toLowerCase())) {
				sendWhatsappMesg(messages, "text", to, null, welcomeText);
				wamidStack.add(0, "name");
				
				body = getMessages[0].getText().getBody();
			}
			else if((getMessages[0].getText().getBody() != null) && wamidStack.get(0).equals("name")) {
				if(getMessages[0].getText().getBody().equals(body)) {
					System.out.println("same 1");
				}else {
					name = getMessages[0].getText().getBody();
					imgText = "Thank you *" + name + "*! ✅ "
							+ "\n" +  "I can help you generate an Image using *AI* based on the text you provide. "
							+ "\n" + "Please be *cautious* about your text/words. Now please go on and type a "
							+ "text/word or a meaningful short sentence which I will use to generate an Image.";
					sendWhatsappMesg(messages, "text", to, null, imgText);
					wamidStack.add(0, "image");
				}
			}
			else if((getMessages[0].getText().getBody() != null) && wamidStack.get(0).equals("image")) {
				if(getMessages[0].getText().getBody().equals(body)) {
					System.out.println("same 2");
				}else {
					sendWhatsappMesg(messages, "text", to, null, "🆗. Generating now....." + "\n");
					Thread.sleep(1000);
					tti = getMessages[0].getText().getBody();
					responseImageURL = textToImageURL(tti);
					wamid = sendWhatsappMesg(messages, "image", to, responseImageURL, null);
					Thread.sleep(3000);
					wamid = sendWhatsappMesg(messages, "text", to, null, thankYouText);
					wamid = sendWhatsappMesg(messages, "text", to, null, endMsg);
					wamidStack.add(0, wamid);
				}
			}
			else{
				responseImageURL = textToImageURL("bye");
				wamid = sendWhatsappMesg(messages, "image", to, responseImageURL, null);
				Thread.sleep(3000);
				sendWhatsappMesg(messages, "text", to, null, byeMsg);
				sendWhatsappMesg(messages, "text", to, null, endMsg);
			}
		}
	}

	//this method uses Whatsapp cloud api to send out a message, uses bearer token, which has 23 hours expiry date.
	private String sendWhatsappMesg(MessagesRes messages, String textOrImage, String to, String responseImageURL,String textBody) {
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
		headers.setBearerAuth("");//provide OpenAI api key here
		
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
