package vttp2022.ssf.miniproject.services;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service
public class MailService {

	@Value(value = "${sendgrid_api_key}")
	private String API_KEY;

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    
    // This class handles the dynamic data for the template
	// Feel free to customise this class our to putted on other file 
	private static class DynamicTemplatePersonalization extends Personalization {

		@JsonProperty(value = "dynamic_template_data")
		private Map<String, String> dynamic_template_data;

		@JsonProperty("dynamic_template_data")
		public Map<String, String> getDynamicTemplateData() {
			if (dynamic_template_data == null) {
				return Collections.<String, String>emptyMap();
			}
			return dynamic_template_data;
		}

		public void addDynamicTemplateData(String key, String value) {
			if (dynamic_template_data == null) {
				dynamic_template_data = new HashMap<String, String>();
				dynamic_template_data.put(key, value);
			} else {
				dynamic_template_data.put(key, value);
			}
		}

	}

    // Sending Template
    public String send(String email, String name) throws IOException {
		// the sender email should be the same as we used to Create a Single Sender Verification
		Email from = new Email("everydaysgapp@gmail.com");
		Email to = new Email(email);
		Mail mail = new Mail();

        // we create an object of our static class feel free to change the class on it's own file 
        // I try to keep every think simple
		DynamicTemplatePersonalization personalization = new DynamicTemplatePersonalization();
		personalization.addTo(to);
		mail.setFrom(from);
		mail.setSubject("Account creation");

        // This is the first_name variable that we created on the template
		personalization.addDynamicTemplateData("first_name", name);
		mail.addPersonalization(personalization);
		mail.setTemplateId("d-5b6cf5c6159e4d26a049a56ab4397046");
		
        // this is the api key
		SendGrid sg = new SendGrid(API_KEY);
		Request request = new Request();

		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			 logger.info(response.getBody());
			return response.getBody();
		} catch (IOException ex) {
			throw ex;
		}
	}
}
