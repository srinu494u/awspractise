package com.amazonaws.s3.lambda.demo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

public class TestAWSSES {
	static final String FROM = "FROM mail id"; // Replace with your "From" address. This address must be
	// verified.
	static final String TO = "To Mail id"; // Replace with a "To" address. If you have not yet requested
	// production access, this address must be verified.
	static final String FROMNAME = "FROm Name";
	static final String SMTP_USERNAME = "SMTP username";

	// Replace smtp_password with your Amazon SES SMTP password.
	static final String SMTP_PASSWORD = "SMTP password";
	static final String HOST = "email-smtp.ap-south-1.amazonaws.com";
	static final int PORT = 465;

	static final String SUBJECT = "Amazon SES test (SMTP interface accessed using Java)";

	/*static final String BODY = String.join(System.getProperty("line.separator"), "<h1>Amazon SES SMTP Email Test</h1>",
			"<p>This email was sent with Amazon SES using the ",
			"<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
			" for <a href='https://www.java.com'>Java</a>.");*/
	static final String TEXTBODY = "This email was sent with Amazon SES using the AWS SDK for JAVA ";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String BODY ="File  uploaded to bucket";
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
				.withRegion(Regions.AP_SOUTH_1).build();
		SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(TO))
				.withMessage(new Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(BODY))
								.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		client.sendEmail(request);
		System.out.println("mail sent");
		//context.getLogger().log("email sent pls check mail");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
