package lk.ijse.gdse.mail;

import com.email.durgesh.Email;
import javafx.concurrent.Task;


import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;


public class Mail extends Task<Boolean> {
    String mail;
    String text;
    String subject;
    File file;

    public Mail(String mail, String text, String subject, File file) {
        this.mail = mail;
        this.text = text;
        this.subject = subject;
        this.file = file;
    }

    public Mail(String mail, String subject , String text) {
        this.mail = mail;
        this.text = text;
        this.subject=subject;

    }

    @Override
    protected Boolean call() throws Exception {
        try {
            Email mail = new Email("moodssalon16@gmail.com", "fvbioyzckrwscqby");
            mail.setFrom("moodssalon16@gmail.com", "Moods Salon");
            mail.setSubject(subject);
            mail.setContent(text, "text/html");

            if(file!=null) {
                MimeBodyPart mbp1 = new MimeBodyPart();
                mbp1.setContent(text, "text/html");
                //mbp1.setText(text);
                MimeBodyPart mbp2 = new MimeBodyPart();
                mbp2.attachFile(file);
                mbp2.setFileName("Payment Receipt.pdf");
                Multipart mp = new MimeMultipart();
                mp.addBodyPart(mbp1);
                mp.addBodyPart(mbp2);
                mail.addAttatchment(mp);
            }

            //mail.addAttatchment();
            mail.addRecipient(this.mail);
            updateProgress(50,100);
            mail.send();
            updateProgress(99,100);
            updateProgress(100,100);
            Thread.sleep(250);
            return true;
        } catch (Exception e) {
            updateMessage("Connection Error - Sending User Details Error");

        }
        return false;
    }
}
