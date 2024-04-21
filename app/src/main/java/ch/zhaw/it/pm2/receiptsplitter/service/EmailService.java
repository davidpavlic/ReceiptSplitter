package ch.zhaw.it.pm2.receiptsplitter.service;

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
    //private Mailer getSmtpServer() {
     //   return new Mailer(smtpServer, smtpPort, username, password);
    //}

    public boolean sendEmail(String recipient, String subject, String body) {
        //Mailer mailer = getSmtpServer();
        //mailer.sendMail(emailSender, recipient, subject, body);
        return false;
    }

    public boolean isValidMail(String email) {
        return email.matches("^(.+)@(.+)$");
    }
}