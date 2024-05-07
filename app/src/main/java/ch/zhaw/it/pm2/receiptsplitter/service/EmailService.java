package ch.zhaw.it.pm2.receiptsplitter.service;

import ch.zhaw.it.pm2.receiptsplitter.enums.EnvConstants;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import java.util.logging.Logger;

/**
 * This class provides functionality to send emails.
 * It uses the Simple Java Mail library to create and send emails.
 * The SMTP server details and credentials are retrieved from the system properties.
 *
 * @version 1.0
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 */
public class EmailService {
    private static final String EMAIL_SENDER = "noreplyreceiptsplitter@gmail.com";
    private static final String SMTP_SERVER = "smtp.sendgrid.net";
    private static final int SMTP_PORT = 25;
    private static final String USERNAME = System.getProperty(EnvConstants.SMTP_USERNAME.getKey());
    private static final String PASSWORD = System.getProperty(EnvConstants.SMTP_API_KEY.getKey());
    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    /**
     * Constructs a new EmailService instance.
     */
    public EmailService() {
    }

    /**
     * Sends an email to the specified recipient with the given subject and body.
     * The email is sent using the SMTP server details and credentials retrieved from the system properties.
     *
     * @param recipient the recipient of the email
     * @param subject   the subject of the email
     * @param body      the body of the email
     * @return true if the email was sent successfully, false otherwise
     * @throws Exception if an error occurs while sending the email
     */
    public boolean sendEmail(String recipient, String subject, String body) throws Exception {
        if (!isValidMail(recipient)) {
            logger.warning("The recipient email is not valid");
            return false;
        }

        try (Mailer mailer = getSmtpServer()) {
            Email email = EmailBuilder.startingBlank().from(EMAIL_SENDER).to(recipient).withSubject(subject).withHTMLText(body).buildEmail();

            logger.info("Sending Email to " + recipient);
            mailer.sendMail(email);
        } catch (Exception exception) {
            logger.fine("There was an issue when trying to send out the Email: " + exception.getMessage());
            throw new Exception("There was an issue when trying to send out the Email", exception);
        }
        return true;
    }


    /**
     * Checks if the given email address is valid.
     * A valid email address matches the regular expression "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$".
     *
     * @param email the email address to check
     * @return true if the email address is valid, false otherwise
     */
    public static boolean isValidMail(String email) {
        return email.trim().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }



    /**
     * Returns a Mailer instance configured with the SMTP server details and credentials retrieved from the system properties.
     *
     * @return a Mailer instance
     */
    private Mailer getSmtpServer() {
        return MailerBuilder.withSMTPServer(SMTP_SERVER, SMTP_PORT, USERNAME, PASSWORD).withSessionTimeout(20 * 1000).buildMailer();
    }
}