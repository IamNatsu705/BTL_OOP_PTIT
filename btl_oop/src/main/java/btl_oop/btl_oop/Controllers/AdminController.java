package btl_oop.btl_oop.Controllers;

import btl_oop.btl_oop.Services.CourtSlotService;
import btl_oop.btl_oop.Models.TypeSlots;
import btl_oop.btl_oop.Repositories.BillRepository;
import btl_oop.btl_oop.Repositories.UserRepository;
import btl_oop.btl_oop.Repositories.TypeSlotRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

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
    @GetMapping("/admin")
    public String adminOverview(Model model) {
        // Lấy số liệu thật từ DB
        long totalCustomers = userRepo.count();
        long totalBills = billRepo.count();

        model.addAttribute("monthlyRevenue", 0); // Tạm thời để 0 (cần viết query tính tổng tiền sau)
        model.addAttribute("todayBookings", totalBills); // Tổng số đơn đã đặt
        model.addAttribute("totalCustomers", totalCustomers);
        
        // Tạm bỏ biểu đồ doanh thu mock, sau này làm query group by date sau
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
        // Lấy danh sách TypeSlots thật từ Database
        model.addAttribute("typeSlots", typeSlotRepo.findAll());
        return "admin-pricing";
    }
    @PostMapping("/admin/pricing/save")
    public String savePricing(@RequestParam(required = false) Long id,
                              @RequestParam String name,
                              @RequestParam BigDecimal price) {
        TypeSlots typeSlot;
        if (id != null) {
            // Cập nhật
            typeSlot = typeSlotRepo.findById(id).orElse(new TypeSlots());
        } else {
            // Thêm mới
            typeSlot = new TypeSlots();
        }
        typeSlot.setName(name);
        typeSlot.setPrice(price);
        typeSlotRepo.save(typeSlot);
        
        return "redirect:/admin/pricing";
    }

    // API Xóa
    @PostMapping("/admin/pricing/delete")
    public String deletePricing(@RequestParam Long id) {
        try {
            typeSlotRepo.deleteById(id);
        } catch (Exception e) {
            // Có thể lỗi nếu TypeSlot này đang được dùng bởi các Slot khác (Foreign Key)
            // Trong thực tế nên dùng Soft Delete hoặc báo lỗi ra giao diện
            System.out.println("Không thể xóa do ràng buộc dữ liệu: " + e.getMessage());
        }
        return "redirect:/admin/pricing";
    }

    @GetMapping("/admin/customers")
    public String adminCustomers(Model model) {
        model.addAttribute("customers", userRepo.findAll());
        return "admin-customers";
    }
}