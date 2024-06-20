package com.pwm.aws.crud.lambda.api.Controller;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.pwm.aws.crud.lambda.api.model.Product;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

public class ProductLambdaHandler implements RequestStreamHandler {

    private String DYNAMO_TABLE = "Products";
    private static final Logger logger = Logger.getLogger(ProductLambdaHandler.class.getName());

    @SuppressWarnings("unchecked")
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        JSONParser parser = new JSONParser();
        JSONObject responseObject = new JSONObject();
        JSONObject responseBody = new JSONObject();

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDB dynamoDB = new DynamoDB(client);

        try {
            JSONObject reqObject = (JSONObject) parser.parse(reader);
            String httpMethod = (String) reqObject.get("httpMethod");
            if (httpMethod == null) {
                logger.warning("Received request with null or missing HTTP method. Request object: " + reqObject.toString());
                responseObject.put("statusCode", 400);
                responseBody.put("message", "Invalid or missing HTTP method");
            } else if ("OPTIONS".equals(httpMethod)) {
                // Handle CORS preflight (OPTIONS) request
                responseObject.put("statusCode", 200);
                logger.info("CORS preflight (OPTIONS) request handled successfully");
                responseObject.put("headers", getCorsHeaders());
            } else if ("POST".equals(httpMethod) || "PUT".equals(httpMethod)) {
                // Handle POST or PUT requests
                if (reqObject.get("body") != null) {
                    try {
                        // Parse the "body" as a JSON object
                        JSONObject requestBodyObject = (JSONObject) parser.parse((String) reqObject.get("body"));

                        // Now you can retrieve individual values from requestBodyObject
                        String name = (String) requestBodyObject.get("name");
                        String email = (String) requestBodyObject.get("email");
                        String phone = (String) requestBodyObject.get("phone");
                        String message = (String) requestBodyObject.get("message");

                        // Create a Product instance with the retrieved values
                        Product product = new Product(name, email, phone, message);

                        // Store the product in DynamoDB
                        dynamoDB.getTable(DYNAMO_TABLE)
                                .putItem(new PutItemSpec().withItem(new Item()
                                        .withString("name", product.getName())
                                        .withString("email", product.getEmail())
                                        .withString("phone", product.getPhone())
                                        .withString("message", product.getMessage())));
                        responseBody.put("message", "New Contact Registered");
                        logger.info("New Contact Registered successfully");
                        responseObject.put("statusCode", 200);
                        responseObject.put("body", responseBody.toString());
                        // Log the request payload
                        logger.warning("Request body: " + reqObject.get("body").toString());
                        sendEmailWithAttachment(product);
                    } catch (ParseException e) {
                        // Handle parsing exception
                        logger.warning("Error parsing JSON: " + e.getMessage());
                        responseObject.put("statusCode", 400);
                        responseBody.put("error", "Invalid JSON format");
                    }
                }
            } else {
                responseObject.put("statusCode", 405);
                responseBody.put("error", "Unsupported HTTP Method");
                logger.warning("Unsupported HTTP Method: " + httpMethod);
            }

        } catch (Exception e) {
            responseObject.put("statusCode", 500);
            responseBody.put("error", "Internal Server Error");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.severe("Internal Server Error: " + sw.toString());
        }

        // Set CORS headers for OPTIONS requests and response headers
        responseObject.put("headers", getCorsHeaders());
        responseObject.put("body", responseBody.toString());
        writer.write(responseObject.toString());

        reader.close();
        writer.close();
    }

    private JSONObject getCorsHeaders() {
        JSONObject headers = new JSONObject();
        headers.put("Access-Control-Allow-Origin", "http://localhost:4200");
     //   headers.put("Access-Control-Allow-Origin", "https://selltoflorida.com");
        headers.put("Access-Control-Allow-Methods", "OPTIONS, POST, PUT");
        headers.put("Access-Control-Allow-Headers", "Content-Type");
        return headers;
    }

    private void sendEmailWithAttachment(Product product) {
        final String fromEmail = "selltoflorida@selltoflorida.com"; // requires valid email id
        final String PwEmail = "chriscosta@live.com";
        final String password = "(**replace**)"; // correct password for email id
        final String toEmail = "costachristopher90@gmail.com";
        System.out.println("Sending email to: " + toEmail);

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.office365.com"); // SMTP Host
        props.put("mail.smtp.port", "587"); // TLS Port
        props.put("mail.smtp.auth", "true"); // enable authentication
        props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println("Attempting to authenticate with username: " + PwEmail);
                return new PasswordAuthentication(PwEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);
        session.setDebug(true);

        System.out.println("DEBUG: JavaMail debugging enabled.");

        String subject = "New Contact in DB!!!";
        String body = "New contact details:\n" +
                "Name: " + product.getName() + "\n" +
                "Email: " + product.getEmail() + "\n" +
                "Phone: " + product.getPhone() + "\n" +
                "Message: " + product.getMessage();

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail, "SelltoFlorida"));
            msg.setReplyTo(InternetAddress.parse(fromEmail, false));
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Create the text part of the message
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);

            // Add the text part to the multipart message
            multipart.addBodyPart(textPart);

            // Set the content of the message to the multipart message
            msg.setContent(multipart);

            // Send the email
            Transport.send(msg);
            logger.info("Email Sent Successfully to " + toEmail);
            System.out.println("Email Sent Successfully to " + toEmail);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.severe("Error sending email: " + e.getMessage());
            e.printStackTrace();
            System.err.println("Error sending email");
        }
    }
}
























