package btl_oop.btl_oop.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
// import org.springframework.web.bind.annotation.RequestParam;

// import jakarta.servlet.http.HttpSession;


// =================================================================================
// 1. DATA MODELS (Records)
// =================================================================================
record User(int id, String username, String password, String fullName, String email, String phone, String role) {}
record Court(int id, String name, String status, String description) {}
record TimeSlot(int id, String slot_name, String start_time, String end_time, double price_per_hour, String days_apply) {}
record Booking(int id, int userId, int courtId, String startTime, String endTime, double totalPrice, String status) {}
record Comment(int id, int bookingId, int userId, String userFullName, int rating, String comment_text, String created_at) {}
// DTO rỗng để binding form đăng ký
record UserDto(String fullName, String username, String email, String phone, String password) {}

// =================================================================================
// 2. MOCK DATA PROVIDER
// =================================================================================
class MockData {
    private static final List<User> users = List.of(
            new User(1, "admin", "123", "Quản Trị Viên", "admin@sancaulong.com", "0901234567", "admin"),
            new User(2, "khachhang", "123", "Nguyễn Văn A", "khachhang@gmail.com", "0987654321", "customer"),
            new User(3, "vantrang", "123", "Trần Thị B", "vantrang@gmail.com", "0912345678", "customer")
    );

    private static final List<Court> courts = List.of(
            new Court(1, "Sân 1 (Sàn gỗ)", "available", "Sân tiêu chuẩn thi đấu, sàn gỗ nhập khẩu."),
            new Court(2, "Sân 2 (Sàn thảm)", "available", "Sân thảm đỏ, êm ái, giảm chấn thương."),
            new Court(3, "Sân 3 (Sàn thảm)", "maintenance", "Sân đang bảo trì, vui lòng quay lại sau."),
            new Court(4, "Sân 4 (VIP)", "available", "Sân VIP, ánh sáng tốt nhất, có khu chờ riêng."),
            new Court(5, "Sân 5 (Ngoài trời)", "available", "Sân ngoài trời, trải nghiệm không khí thoáng đãng."),
            new Court(6, "Sân 6 (Sàn thảm)", "available", "Sân thảm xanh, mới đưa vào hoạt động.")
    );

    private static final List<TimeSlot> timeSlots = List.of(
            new TimeSlot(1, "Giờ thấp điểm (Sáng T2-T6)", "05:00:00", "16:00:00", 100000, "weekday_morning"),
            new TimeSlot(2, "Giờ cao điểm (Tối T2-T6)", "16:00:00", "23:00:00", 150000, "weekday_evening"),
            new TimeSlot(3, "Cuối tuần (T7-CN)", "05:00:00", "23:00:00", 180000, "weekend")
    );

    private static final List<Booking> bookings = List.of(
            new Booking(1, 2, 1, "2025-11-10T17:00:00", "2025-11-10T18:00:00", 150000, "completed"),
            new Booking(2, 3, 4, "2025-11-10T18:00:00", "2025-11-10T20:00:00", 300000, "confirmed"),
            new Booking(3, 2, 2, "2025-11-11T08:00:00", "2025-11-11T09:00:00", 100000, "confirmed")
    );

    private static final List<Comment> comments = List.of(
            new Comment(1, 1, 2, "Nguyễn Văn A", 5, "Sân 1 sàn gỗ chơi rất thích, ánh sáng tốt. Sẽ quay lại!", "2025-11-10T18:30:00"),
            new Comment(2, 2, 3, "Trần Thị B", 4, "Sân VIP xịn nhưng giá hơi cao. Phục vụ tốt.", "2025-11-10T20:05:00")
    );

    // Dùng Map để khớp với key "Doanh thu" trong biểu đồ
    private static final List<Map<String, Object>> revenueData = List.of(
            Map.of("name", "T6", "Doanh thu", 4000000.0),
            Map.of("name", "T7", "Doanh thu", 7500000.0),
            Map.of("name", "CN", "Doanh thu", 9000000.0),
            Map.of("name", "T2", "Doanh thu", 3200000.0),
            Map.of("name", "T3", "Doanh thu", 3800000.0),
            Map.of("name", "T4", "Doanh thu", 4100000.0),
            Map.of("name", "T5", "Doanh thu", 4500000.0)
    );

    // Methods
    public static List<User> getUsers() { return users; }
    public static User getAdminUser() { return users.stream().filter(u -> u.role().equals("admin")).findFirst().orElse(null); }
    public static List<User> getCustomers() { return users.stream().filter(u -> u.role().equals("customer")).collect(Collectors.toList()); }
    public static List<Court> getCourts() { return courts; }
    public static List<TimeSlot> getTimeSlots() { return timeSlots; }
    public static List<Booking> getBookings() { return bookings; }
    public static List<Comment> getComments() { return comments; }
    public static List<Map<String, Object>> getRevenueData() { return revenueData; }
}


// =================================================================================
// 3. MAIN VIEW CONTROLLER
// (Xử lý request và trả về file HTML với dữ liệu)
// =================================================================================
@Controller
public class ViewController {

    // --- TRANG CHÍNH ---

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("courts", MockData.getCourts().stream().limit(3).collect(Collectors.toList()));
        return "home";
    }

    
    // @GetMapping("/register")
    // public String register(Model model) {
    //     model.addAttribute("userDto", new UserDto("", "", "", "", "")); // Gửi DTO rỗng để binding
    //     return "register"; // Trả về file register.html
    // }

    @GetMapping("/booking")
    public String bookingPage(Model model) {

        // --- Dữ liệu giả cho danh sách sân ---
        var court1 = Map.of(
                "id", 1L,
                "name", "Sân 1 (Thảm dày)",
                "description", "Sân tiêu chuẩn thi đấu",
                "status", "available"
        );
        var court2 = Map.of(
                "id", 2L,
                "name", "Sân 2 (Ngoài trời)",
                "description", "View đẹp, thoáng mát",
                "status", "available"
        );
        var court3 = Map.of(
                "id", 3L,
                "name", "Sân 3 (Bảo trì)",
                "description", "Đang sửa chữa",
                "status", "maintenance"
        );
        model.addAttribute("courts", List.of(court1, court2, court3));

        // Gửi ngày hôm nay để làm giá trị mặc định
        model.addAttribute("selectedDate", LocalDate.now().toString());

        // KHÔNG CẦN gửi "allTemplateSlots" hoặc "allBookedSlots"
        
        return "booking"; // Tên của file booking.html
    }
    
    // --- TRANG ADMIN ---

    @GetMapping("/admin")
    public String adminOverview(Model model) {
        model.addAttribute("monthlyRevenue", 125000000);
        model.addAttribute("todayBookings", 32);
        model.addAttribute("totalCustomers", MockData.getCustomers().size());
        model.addAttribute("revenueData", MockData.getRevenueData());
        return "admin-overview"; // Trả về file admin-overview.html
    }

    @GetMapping("/admin/courts")
    public String adminCourts(Model model) {
        model.addAttribute("courts", MockData.getCourts());
        return "admin-courts"; // Trả về file admin-courts.html
    }

    @GetMapping("/admin/pricing")
    public String adminPricing(Model model) {
        model.addAttribute("timeSlots", MockData.getTimeSlots());
        return "admin-pricing"; // Trả về file admin-pricing.html
    }

    @GetMapping("/admin/customers")
    public String adminCustomers(Model model) {
        model.addAttribute("customers", MockData.getCustomers());
        return "admin-customers"; // Trả về file admin-customers.html
    }

    @GetMapping("/admin/comments")
    public String adminComments(Model model) {
        model.addAttribute("comments", MockData.getComments());
        return "admin-comments"; // Trả về file admin-comments.html
    }
}