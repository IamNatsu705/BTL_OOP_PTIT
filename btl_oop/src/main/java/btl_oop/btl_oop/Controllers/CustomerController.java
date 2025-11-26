package btl_oop.btl_oop.Controllers;

import btl_oop.btl_oop.Models.Bill;
import btl_oop.btl_oop.Models.User;
import btl_oop.btl_oop.Services.BookingService;
import btl_oop.btl_oop.Services.UserService;
import btl_oop.btl_oop.Utils.HashUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final BookingService bookingService;
    private final UserService userService;

    // --- TRANG LỊCH SỬ GIAO DỊCH ---
    @GetMapping("/history")
    public String historyPage(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Bill> historyBills = bookingService.getHistoryByUserId(currentUser.getUserId());
        // Write a service return bills{ bill.id, bill.totatalAmount, bill.createdAt, slotBookedList{court.name, bookingDate, slot.name, price}}
        model.addAttribute("bills", historyBills); 
        return "user_history";
    }

    // --- CÁC PHẦN PROFILE GIỮ NGUYÊN (Chỉ chỉnh lại import nếu cần) ---
    
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        return "user_profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(HttpSession session, @RequestParam String name, @RequestParam String email, @RequestParam String phone, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        
        user.setFullName(name);
        user.setEmail(email);
        user.setPhone(phone);

        userService.save(user);
        session.setAttribute("user", user); // Cập nhật lại session

        model.addAttribute("user", user);
        model.addAttribute("notificationMessage", "Cập nhật thông tin thành công!");
        model.addAttribute("notificationType", "success");

        return "user_profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(HttpSession session,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
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