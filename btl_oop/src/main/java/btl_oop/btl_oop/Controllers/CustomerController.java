package btl_oop.btl_oop.Controllers;


import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerController {

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
    public String profilePage(
            Model model,
            HttpSession session) {

        // --- Dữ liệu giả cho Thông tin cá nhân (dùng Map) ---
        Map<String, String> fakeUser = new HashMap<>();
        fakeUser.put("name", "Dương Xuân Quỳnh");
        fakeUser.put("phone", "0987654321");
        fakeUser.put("email", "quynh.duong@email.com");
        
        // Dùng String cho ngày đăng ký là đã chính xác
        fakeUser.put("registerDate", "20/10/2025"); 


        // --- Thêm đối tượng user (dưới dạng Map) vào model ---
        model.addAttribute("user", fakeUser);

        return "user_profile"; // Tên file HTML
    }

    @PostMapping("/booking")
    public String createBooking(
            @RequestParam("selectedCourtId") Long courtId,
            @RequestParam("selectedDate") String selectedDateStr,
            @RequestParam("selectedTimeSlots") String selectedTimeSlots, // mang json
            @RequestParam("totalPrice") double totalPrice,
            HttpSession session) {

        return "redirect:/booking";
    }
    @GetMapping("/testPostman")
    public String Test(){
        return "Ok ae";
    }
}