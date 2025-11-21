package btl_oop.btl_oop.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String FROM_EMAIL = "minhlim2005@gmail.com"; // phải trùng Gmail của bạn

    public void sendOtp(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_EMAIL); // thêm dòng này
        message.setTo(to);
        message.setSubject("OTP Quên Mật Khẩu");
        message.setText("Mã OTP của bạn là: " + otp + " (hết hạn sau 10 phút)");
        mailSender.send(message);
    }
}
