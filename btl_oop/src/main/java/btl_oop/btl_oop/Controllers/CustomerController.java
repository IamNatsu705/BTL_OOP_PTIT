package btl_oop.btl_oop.Controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import btl_oop.btl_oop.Models.Booking;
import btl_oop.btl_oop.Models.User;
import btl_oop.btl_oop.Services.CustomerService;
import btl_oop.btl_oop.Services.UserService;
import btl_oop.btl_oop.Services.ViewService;
import btl_oop.btl_oop.Utils.HashUtil;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CustomerController {
    
    private final CustomerService customerService; 
    private final ViewService viewService; 
    private final UserService userService; // <--- ĐÃ THÊM DÒNG NÀY (Fix lỗi chính)

    /*
     * KHÔNG CẦN addGlobalAttributes ở đây.
     * GlobalControllerAdvice sẽ tự động thêm (isAuthenticated, currentUser, role)
     */

    @GetMapping("/history")
    public String historyPage(Model model, HttpSession session) {
        // SỬA LẠI: Lấy User object từ session "user" để đồng bộ với các hàm dưới
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Lấy lịch sử dựa trên ID của user trong session
        List<Booking> historyBookings = customerService.getHistoryById(currentUser.getUserId());
        model.addAttribute("bookings", historyBookings);
        return "user_history";
    }
    
    /**
     * Trang Thông tin cá nhân
     */
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "user_profile"; 
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

        userService.save(user); // Bây giờ dòng này sẽ hoạt động vì đã khai báo userService
        session.setAttribute("user", user); // Cập nhật lại session

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

        // Lưu ý: HashUtil phải có trong project của bạn
        String hashedOld = HashUtil.hashPassword(oldPassword);
        if (!user.getPassword().equals(hashedOld)) {
            model.addAttribute("user", user);
            model.addAttribute("notificationMessage", "Mật khẩu cũ không chính xác!");
            model.addAttribute("notificationType", "error");
            return "user_profile";
        }

        user.setPassword(HashUtil.hashPassword(newPassword));
        userService.save(user); // Đã fix
        session.setAttribute("user", user);

        model.addAttribute("user", user);
        model.addAttribute("notificationMessage", "Đổi mật khẩu thành công!");
        model.addAttribute("notificationType", "success");

        return "user_profile";
    }
}