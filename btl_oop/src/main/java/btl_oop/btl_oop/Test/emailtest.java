package btl_oop.btl_oop.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class emailtest implements CommandLineRunner {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void run(String... args) throws Exception {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("minhlim2005@gmail.com"); // gửi cho chính bạn thử
            message.setFrom("minhlim2005@gmail.com");
            message.setSubject("Test Email Spring Boot");
            message.setText("Nếu nhận được mail này là ok!");

            mailSender.send(message);
            System.out.println("✅ Email gửi thành công!");
        } catch (Exception e) {
            System.out.println("❌ Email gửi thất bại!");
            e.printStackTrace();
        }
    }
}
