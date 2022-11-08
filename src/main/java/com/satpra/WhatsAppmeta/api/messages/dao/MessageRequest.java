package com.satpra.WhatsAppmeta.api.messages.dao;

import com.satpra.WhatsAppmeta.api.dao.Image;
import com.satpra.WhatsAppmeta.api.dao.Text;

public class MessageRequest {
	
	String messaging_product;
	String to;
	String type;
	Template template;
	String recipient_type;
	Text text;
	Image image;
	
	
	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public String getMessaging_product() {
		return messaging_product;
	}
	public void setMessaging_product(String messaging_product) {
		this.messaging_product = messaging_product;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
	}
	public String getRecipient_type() {
		return recipient_type;
	}
	public void setRecipient_type(String recipient_type) {
		this.recipient_type = recipient_type;
	}
	public Text getText() {
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "MessageRequest [messaging_product=" + messaging_product + ", to=" + to + ", type=" + type
				+ ", template=" + template + ", getMessaging_product()=" + getMessaging_product() + ", getTo()="
				+ getTo() + ", getType()=" + getType() + ", getTemplate()=" + getTemplate() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
