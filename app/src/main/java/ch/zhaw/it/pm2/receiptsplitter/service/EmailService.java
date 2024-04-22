package ch.zhaw.it.pm2.receiptsplitter.service;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class EmailService {
    private static EmailService instance;
    private final String emailSender;
    private final String smtpServer;
    private final int smtpPort;
    private final String username;
    private final String password;

    private EmailService(String emailSender, String smtpServer, int smtpPort, String username, String password) {
        this.emailSender = emailSender;
        this.smtpServer = smtpServer;
        this.smtpPort = smtpPort;
        this.username = username;
        this.password = password;
    }

    public static EmailService getInstance(String emailSender, String smtpServer, int smtpPort, String username, String password) {
        if (instance == null) {
            instance = new EmailService(emailSender, smtpServer, smtpPort, username, password);
        }
        return instance;
    }

    // TODO Implement Mailer dependency
    private Mailer getSmtpServer() {
        return MailerBuilder
                .withSMTPServer(smtpServer, smtpPort, username, password)
                .buildMailer();
    }

    public boolean sendEmail(String recipient, String subject, String body) {
        Mailer mailer = getSmtpServer();
        Email email = EmailBuilder.startingBlank()
                .from(emailSender)
                .to(recipient)
                .withSubject(subject)
                .withPlainText(body)
                .buildEmail();
        mailer.sendMail(email);
        return true;
    }

    public boolean isValidMail(String email) {
        return email.matches("^(.+)@(.+)$");
    }
}