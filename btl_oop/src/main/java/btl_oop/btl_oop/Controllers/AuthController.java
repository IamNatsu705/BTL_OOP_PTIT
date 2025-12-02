package btl_oop.btl_oop.Controllers;

import btl_oop.btl_oop.Models.User;
import btl_oop.btl_oop.Services.EmailService;
import btl_oop.btl_oop.Services.UserService;
import btl_oop.btl_oop.Utils.HashUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.Random;
@RequiredArgsConstructor
@Controller
public class AuthController {
    final UserService userService;
    final EmailService emailService;

    // Hiển thị form login
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // Xử lý login
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {

        User user = userService.findByUserName(username);

        if (user != null && user.getPassword().equals(HashUtil.hashPassword(password))) {
            // Kiểm tra trạng thái
            if (!"ACTIVE".equals(user.getStatus())) {
                // Tài khoản bị khóa
                return "redirect:/login?locked";
            }

            session.setAttribute("user", user);
            session.setAttribute("currentUser", user.getUserName());
            session.setAttribute("role", user.getRole());
            session.setAttribute("isAuthenticated", true);

            if ("ADMIN".equals(user.getRole())) return "redirect:/admin";
            else return "redirect:/";
        } else {
            return "redirect:/login?error";
        }
    }


    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam(required = false) String phone,
                           @RequestParam String email,
                           @RequestParam String fullName,
                           Model model) {

        try {
            String hashed = HashUtil.hashPassword(password);
            userService.register(username, hashed, phone, email, fullName);

            model.addAttribute("notificationMessage", "Đăng ký thành công!");
            model.addAttribute("notificationType", "success");
            return "login"; // chuyển sang trang login sau khi thành công
        } catch (RuntimeException e) {
            // Username/email trùng → show message trên form
            model.addAttribute("notificationMessage", e.getMessage());
            model.addAttribute("notificationType", "error");
            return "register"; // quay lại trang đăng ký
        }
    }



    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // Quên mật khẩu
    @GetMapping("/forgot-password")
    public String forgotPasswordForm(Model model) {
        model.addAttribute("step", "email");
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String sendOtp(@RequestParam String email, HttpSession session, Model model) {
        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("notificationMessage", "Email không tồn tại!");
            model.addAttribute("notificationType", "error");
            model.addAttribute("step", "email");
            return "forgot-password";
        }

        // Sinh OTP 6 số
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Lưu session 10 phút
        session.setAttribute("resetOtp", otp);
        session.setAttribute("resetEmail", email);
        session.setAttribute("otpGeneratedAt", LocalDateTime.now());

        // Gửi email OTP
        emailService.sendOtp(email, otp); // đúng với method hiện tại

        model.addAttribute("notificationMessage", "OTP đã gửi tới email!");
        model.addAttribute("notificationType", "success");
        model.addAttribute("step", "otp");
        return "forgot-password";
    }

    @PostMapping("/forgot-password/verify")
    public String verifyOtp(@RequestParam String otp,
                            @RequestParam String newPassword,
                            @RequestParam String confirmPassword,
                            HttpSession session,
                            Model model) {

        String sessionOtp = (String) session.getAttribute("resetOtp");
        LocalDateTime generatedAt = (LocalDateTime) session.getAttribute("otpGeneratedAt");
        String email = (String) session.getAttribute("resetEmail");

        if (sessionOtp == null || generatedAt == null || email == null ||
                generatedAt.isBefore(LocalDateTime.now().minusMinutes(10))) {
            model.addAttribute("notificationMessage", "OTP chưa gửi hoặc đã hết hạn!");
            model.addAttribute("notificationType", "error");
            model.addAttribute("step", "email");
            return "forgot-password";
        }

        if (!sessionOtp.equals(otp)) {
            model.addAttribute("notificationMessage", "OTP không đúng!");
            model.addAttribute("notificationType", "error");
            model.addAttribute("step", "otp");
            return "forgot-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("notificationMessage", "Mật khẩu nhập lại không khớp!");
            model.addAttribute("notificationType", "error");
            model.addAttribute("step", "otp");
            return "forgot-password";
        }

        // Hash pass trước khi lưu
        userService.updatePasswordByEmail(email, HashUtil.hashPassword(newPassword));

        // Xóa session OTP
        session.removeAttribute("resetOtp");
        session.removeAttribute("resetEmail");
        session.removeAttribute("otpGeneratedAt");

        model.addAttribute("notificationMessage", "Đổi mật khẩu thành công!");
        model.addAttribute("notificationType", "success");
        model.addAttribute("step", "email");
        return "forgot-password";
    }
}
