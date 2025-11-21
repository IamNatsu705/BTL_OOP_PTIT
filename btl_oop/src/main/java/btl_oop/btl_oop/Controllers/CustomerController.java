package btl_oop.btl_oop.Controllers;


import java.util.ArrayList;
import java.util.HashMap;

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
        
        List<Map<String, Object>> fakeBookings = new ArrayList<>();

        // 1. SÂN CHƯA CHƠI (UPCOMING)
        Map<String, Object> booking1 = new HashMap<>();
        booking1.put("id", 12345L);
        booking1.put("courtName", "Sân 2 (Ngoài trời)");
        
        // --- THAY ĐỔI ---
        booking1.put("status", "UPCOMING"); // Dùng cho logic tô màu
        booking1.put("statusText", "Đã đặt - Chưa chơi"); // Hiển thị
        // -----------------
        
        booking1.put("date", "15-11-2025"); 
        booking1.put("timeSlot", "18:00 - 19:00");
        booking1.put("price", 80000.0);
        fakeBookings.add(booking1);

        // 2. SÂN ĐÃ CHƠI (COMPLETED)
        Map<String, Object> booking2 = new HashMap<>();
        booking2.put("id", 12344L);
        booking2.put("courtName", "Sân 1 (Thảm dày)");
        
        // --- THAY ĐỔI ---
        booking2.put("status", "COMPLETED"); // Dùng cho logic tô màu
        booking2.put("statusText", "Đã đặt - Đã chơi"); // Hiển thị
        // -----------------

        booking2.put("date", "10-11-2025");
        booking2.put("timeSlot", "17:00 - 18:00");
        booking2.put("price", 80000.0);
        fakeBookings.add(booking2);

        // 3. Đã XÓA lịch "Đã hủy" (booking3)
        // Vì bạn chỉ quan tâm 2 trạng thái trên.

        model.addAttribute("bookings", fakeBookings);
        
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