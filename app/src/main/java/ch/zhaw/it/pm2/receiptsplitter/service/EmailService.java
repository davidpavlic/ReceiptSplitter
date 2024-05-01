package ch.zhaw.it.pm2.receiptsplitter.service;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class EmailService {
    private static final String EMAIL_SENDER = "noreplyreceiptsplitter@gmail.com";
    private static final String SMTP_SERVER = "smtp.sendgrid.net";
    private static final int SMTP_PORT = 25;
    private static final String USERNAME = System.getProperty("SMTP_USERNAME");
    private static final String PASSWORD = System.getProperty("SMTP_API_KEY");

    public EmailService() {}

    private Mailer getSmtpServer() {
        return MailerBuilder
                .withSMTPServer(SMTP_SERVER, SMTP_PORT, USERNAME, PASSWORD)
                .buildMailer();
    }

    public boolean sendEmail(String recipient, String subject, String body) throws Exception {
        if (!isValidMail(recipient)) {
            return false;
        }

        try (Mailer mailer = getSmtpServer()) {
            Email email = EmailBuilder.startingBlank()
                    .from(EMAIL_SENDER)
                    .to(recipient)
                    .withSubject(subject)
                    .withPlainText(body)
                    .buildEmail();
            mailer.sendMail(email);
        } catch (Exception exception) {
            throw new Exception("Failed to send email", exception);
        }

        return true;
    }

    public static boolean isValidMail(String email) {
        //TODO make this nonStatic and use one isValidMail method
        return email.matches("^(.+)@(.+)$");
    }
}