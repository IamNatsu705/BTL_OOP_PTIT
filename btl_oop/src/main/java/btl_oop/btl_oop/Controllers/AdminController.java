package btl_oop.btl_oop.Controllers;

import btl_oop.btl_oop.Services.CourtSlotService;
import btl_oop.btl_oop.Models.*;
import btl_oop.btl_oop.Repositories.BillRepository;
import btl_oop.btl_oop.Repositories.SlotRepository;
import btl_oop.btl_oop.Repositories.UserRepository;
import btl_oop.btl_oop.Repositories.TypeSlotRepository;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final CourtSlotService courtSlotService;
    private final BillRepository billRepo;
    private final UserRepository userRepo;
    private final TypeSlotRepository typeSlotRepo;
    private final SlotRepository slotRepo;
    @GetMapping("/admin")
    public String adminOverview(Model model) {
        // 1. Thống kê KHÁCH HÀNG
        long totalCustomers = userRepo.count();
        
        // 2. Thống kê ĐƠN HÀNG & DOANH THU
        List<Bill> allBills = billRepo.findAll();
        
        // Tính doanh thu tổng (hoặc tháng này nếu muốn query kỹ hơn)
        BigDecimal totalRevenue = allBills.stream()
                .map(Bill::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalBookings = allBills.size();

        // 3. Thống kê HÔM NAY (Giả định đơn giản)
        // Để chính xác cần viết Query trong Repository: findByDate...
        // Ở đây mình lấy ví dụ số liệu mẫu hoặc tính sơ bộ
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end   = LocalDate.now().atTime(23, 59, 59);
        long todayBookings = billRepo.findByCreatedAtBetween(start, end).size(); // Ví dụ: Cần query DB đếm số bill created_at hôm nay
        List<BigDecimal> todayRevenues = billRepo.findByCreatedAtBetween(start, end).stream().map(Bill::getTotalAmount).collect(Collectors.toList()); 
        BigDecimal todayRevenue = new BigDecimal("0");
        for (BigDecimal x: todayRevenues) todayRevenue = todayRevenue.add(x);
        // 4. Trạng thái SÂN NGAY LÚC NÀY (Real-time)
        // Logic: Lấy giờ hiện tại -> Check xem bao nhiêu sân đang có khách
        // (Tạm thời hardcode hoặc gọi service check)
        long courtsOccupied = courtSlotService.getCourtAvailable(); 
        long totalCourts = courtSlotService.getAllCourts().size();
        List<Court> courts = courtSlotService.getAllCourts();
        // Gửi sang View
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("totalRevenue", totalRevenue);
        
        model.addAttribute("todayBookings", todayBookings);
        model.addAttribute("todayRevenue", todayRevenue);
        
        model.addAttribute("courts", courts);


        return "admin-overview";
    }

    @GetMapping("/admin/courts")
    public String adminCourts(Model model) {
        // Lấy danh sách sân thật
        model.addAttribute("courts", courtSlotService.getAllCourts());
        return "admin-courts";
    }

    // Các trang admin con khác (Pricing, Customers...) bạn cứ giữ nguyên mockup tạm thời
    // hoặc update dần sau.
    @GetMapping("/admin/pricing")
    public String adminPricing(Model model) {
        // 1. Lấy 3 mức giá
        model.addAttribute("typeSlots", typeSlotRepo.findAll());
        
        // 2. Lấy danh sách 24 Slot (Sắp xếp theo giờ bắt đầu để hiển thị từ 0h -> 23h)
        model.addAttribute("slots", slotRepo.findAll(Sort.by(Sort.Direction.ASC, "timeBegin")));
        
        return "admin-pricing";
    }

    // --- 1. CẬP NHẬT GIÁ TIỀN (Giữ nguyên) ---
    @PostMapping("/admin/pricing/update")
    public String updatePrice(@RequestParam Long id, @RequestParam BigDecimal price) {
        TypeSlots typeSlot = typeSlotRepo.findById(id).orElse(null);
        if (typeSlot != null) {
            typeSlot.setPrice(price);
            typeSlotRepo.save(typeSlot);
        }
        return "redirect:/admin/pricing";
    }

    // --- 2. CẤU HÌNH LOẠI GIÁ CHO TỪNG SLOT (MỚI) ---
    // Chúng ta dùng Map<String, String> để hứng tất cả các ô select gửi lên
    // Key sẽ là "slot_1", "slot_2"... Value là ID của TypeSlot (ví dụ "1", "2")
    @PostMapping("/admin/pricing/configure")
    public String configureSchedule(@RequestParam Map<String, String> allParams) {
        
        // Duyệt qua tất cả tham số gửi lên
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            String key = entry.getKey();   // Ví dụ: "slot_5" (5 là id của slot)
            String value = entry.getValue(); // Ví dụ: "2" (2 là id của TypeSlot)

            if (key.startsWith("slot_")) {
                try {
                    Long slotId = Long.parseLong(key.substring(5)); // Lấy số 5 ra
                    Long typeId = Long.parseLong(value);

                    Slot slot = slotRepo.findById(slotId).orElse(null);
                    TypeSlots type = typeSlotRepo.findById(typeId).orElse(null);

                    if (slot != null && type != null) {
                        slot.setTypeSlots(type);
                        slotRepo.save(slot);
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua lỗi convert
                }
            }
        }
        return "redirect:/admin/pricing?success=saved";
    }

    @GetMapping("/admin/customers")
    public String adminCustomers(Model model) {
        model.addAttribute("customers", userRepo.findAll());
        return "admin-customers";
    }
}