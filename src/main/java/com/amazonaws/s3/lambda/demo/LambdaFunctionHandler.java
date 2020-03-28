package com.amazonaws.s3.lambda.demo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

public class LambdaFunctionHandler implements RequestHandler<S3EventNotification, String> {

	private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();

	public LambdaFunctionHandler() {
	}

	// Test purpose only.
	LambdaFunctionHandler(AmazonS3 s3) {
		this.s3 = s3;
	}

	static final String FROM = "FROm MAIL ID"; // Replace with your "From" address. This address must be
															// verified.
	static final String TO = "tO mAIL ID"; // Replace with a "To" address. If you have not yet requested
	// production access, this address must be verified.
	static final String FROMNAME = "USER NAME ";
	static final String SMTP_USERNAME = "smtp USER NAME ";

	// Replace smtp_password with your Amazon SES SMTP password.
	static final String SMTP_PASSWORD = "smtp APSSWORD";
	static final String HOST = "email-smtp.ap-south-1.amazonaws.com";
	static final int PORT = 465;

	static final String SUBJECT = "File uploaded";

	/*static final String BODY = String.join(System.getProperty("line.separator"), "<h1>Amazon SES SMTP Email Test</h1>",
			"<p>This email was sent with Amazon SES using the ",
			"<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
			" for <a href='https://www.java.com'>Java</a>.");*/
	static final String TEXTBODY = "This email was sent with Amazon SES using the AWS SDK for JAVA ";

	@Override
	public String handleRequest(S3EventNotification event, Context context) {
		context.getLogger().log("Received event: " + event.toJson());

		// Get the object from the event and show its content type
		String bucket = event.getRecords().get(0).getS3().getBucket().getName();
		String key = event.getRecords().get(0).getS3().getObject().getKey();
		try {
			S3Object response = s3.getObject(new GetObjectRequest(bucket, key));
			String contentType = response.getObjectMetadata().getContentType();
			context.getLogger().log("CONTENT TYPE: " + contentType);
			context.getLogger().log("uploaded to S3 bucket name:: " + bucket);

			/*
			 * Properties props = System.getProperties();
			 * props.put("mail.transport.protocol", "smtp"); props.put("mail.smtp.port",
			 * PORT); props.put("mail.smtp.starttls.enable", "true");
			 * props.put("mail.smtp.auth", "true"); Session session =
			 * Session.getDefaultInstance(props);
			 */

			// Create a message with the specified information.
			/*
			 * MimeMessage msg = new MimeMessage(session); msg.setFrom(new
			 * InternetAddress(FROM,FROMNAME)); msg.setRecipient(Message.RecipientType.TO,
			 * new InternetAddress(TO)); msg.setSubject(SUBJECT);
			 * msg.setContent(BODY,"text/html"); Transport transport =
			 * session.getTransport(); try { System.out.println("Sending...");
			 * 
			 * // Connect to Amazon SES using the SMTP username and password you specified
			 * above. transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
			 * 
			 * // Send the email. transport.sendMessage(msg, msg.getAllRecipients());
			 * System.out.println("Email sent!"); } catch (Exception ex) {
			 * System.out.println("The email was not sent.");
			 * context.getLogger().log("exception: " + ex.getMessage()); } finally { //
			 * Close and terminate the connection. transport.close(); }
			 */
			String BODY ="File "+key+" uploaded to bucket"+bucket;
			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
					.withRegion(Regions.AP_SOUTH_1).build();
			SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(TO))
					.withMessage(new Message()
							.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(BODY))
									.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
							.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
					.withSource(FROM);
			client.sendEmail(request);
			context.getLogger().log("email sent pls check mail");

			return contentType;
		} catch (Exception e) {
			e.printStackTrace();
			context.getLogger().log(String.format("Error getting object %s from bucket %s. Make sure they exist and"
					+ " your bucket is in the same region as this function.", key, bucket));
			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return key;
	}
}
