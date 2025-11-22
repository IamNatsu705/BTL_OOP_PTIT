package btl_oop.btl_oop.Controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import btl_oop.btl_oop.Utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import btl_oop.btl_oop.Models.User;
import btl_oop.btl_oop.Services.UserService;



import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import btl_oop.btl_oop.Models.Booking;
import btl_oop.btl_oop.Services.CustomerService;
import btl_oop.btl_oop.Services.ViewService;

// @RequiredArgsConstructor
@Controller
public class CustomerController {
    @Autowired
    private UserService userService;
    /*
     * KHÔNG CẦN addGlobalAttributes ở đây.
     * GlobalControllerAdvice sẽ tự động thêm (isAuthenticated, currentUser, role)
     */

    @GetMapping("/history")
    public String historyPage(
            Model model,
            HttpSession session) {

        String userName = String.valueOf(session.getAttribute("currentUser"));
        Long userId = customerService.getIdUser(userName);
        List<Booking> historyBookings = customerService.getHistoryById(userId);
        model.addAttribute("bookings", historyBookings);
        return "user_history";
    }
    
    /**
     * Trang Thông tin cá nhân (Dùng Map)
     */

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "user_profile"; // Template user_profile.html
    }

    // -------------------- UPDATE PROFILE --------------------
    @PostMapping("/profile/update")
    public String updateProfile(HttpSession session,
                                @RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String phone,
                                Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        user.setFullName(name);
        user.setEmail(email);
        user.setPhone(phone);

        userService.save(user); // Lưu database
        session.setAttribute("user", user);

        model.addAttribute("user", user);
        model.addAttribute("notificationMessage", "Cập nhật thông tin thành công!");
        model.addAttribute("notificationType", "success");

        return "user_profile";
    }

    // -------------------- CHANGE PASSWORD --------------------
    @PostMapping("/profile/change-password")
    public String changePassword(HttpSession session,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam(required = false) String confirmPassword,
                                 Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("user", user);
            model.addAttribute("notificationMessage", "Xác nhận mật khẩu không khớp!");
            model.addAttribute("notificationType", "error");
            return "user_profile";
        }

        String hashedOld = HashUtil.hashPassword(oldPassword);
        if (!user.getPassword().equals(hashedOld)) {
            model.addAttribute("user", user);
            model.addAttribute("notificationMessage", "Mật khẩu cũ không chính xác!");
            model.addAttribute("notificationType", "error");
            return "user_profile";
        }

        user.setPassword(HashUtil.hashPassword(newPassword));
        userService.save(user);
        session.setAttribute("user", user);

        model.addAttribute("user", user);
        model.addAttribute("notificationMessage", "Đổi mật khẩu thành công!");
        model.addAttribute("notificationType", "success");

        return "user_profile";
    }
}