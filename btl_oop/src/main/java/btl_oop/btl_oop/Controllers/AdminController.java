package btl_oop.btl_oop.Controllers;

import btl_oop.btl_oop.Services.CourtSlotService;
import btl_oop.btl_oop.Models.*;
import btl_oop.btl_oop.Repositories.BillRepository;
import btl_oop.btl_oop.Repositories.SlotRepository;
import btl_oop.btl_oop.Repositories.UserRepository;
import btl_oop.btl_oop.Repositories.TypeSlotRepository;
import btl_oop.btl_oop.Utils.HashUtil;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final CourtSlotService courtSlotService;
    private final BillRepository billRepo;
    private final UserRepository userRepo;
    private final TypeSlotRepository typeSlotRepo;
    private final SlotRepository slotRepo;
    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "admin".equalsIgnoreCase(role);
    }
    @GetMapping("/admin")
    public String adminOverview(Model model,HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/"; 
        }
        // 1. Thống kê KHÁCH HÀNG
        long totalCustomers = userRepo.count();

        List<Bill> allBills = billRepo.findAll();
        
        // Tính doanh thu tổng (hoặc tháng này nếu muốn query kỹ hơn)
        BigDecimal totalRevenue = allBills.stream()
                .map(Bill::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalBookings = allBills.size();


        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end   = LocalDate.now().atTime(23, 59, 59);
        long todayBookings = billRepo.findByCreatedAtBetween(start, end).size(); 
        List<BigDecimal> todayRevenues = billRepo.findByCreatedAtBetween(start, end).stream().map(Bill::getTotalAmount).collect(Collectors.toList()); 
        BigDecimal todayRevenue = new BigDecimal("0");
        for (BigDecimal x: todayRevenues) todayRevenue = todayRevenue.add(x);

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
    public String adminCourts(Model model,HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/"; 
        }
        return "admin-courts";
    }

    // Các trang admin con khác (Pricing, Customers...) bạn cứ giữ nguyên mockup tạm thời
    // hoặc update dần sau.
    @GetMapping("/admin/pricing")
    public String adminPricing(Model model,HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/"; 
        }
        model.addAttribute("typeSlots", typeSlotRepo.findAll());
        model.addAttribute("slots", slotRepo.findAll(Sort.by(Sort.Direction.ASC, "timeBegin")));
        
        return "admin-pricing";
    }

    // --- 1. CẬP NHẬT GIÁ TIỀN (Giữ nguyên) ---
    @PostMapping("/admin/pricing/update")
    public String updatePrice(@RequestParam Long id, @RequestParam BigDecimal price,HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/"; 
        }
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
    public String configureSchedule(@RequestParam Map<String, String> allParams,HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/"; 
        }
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
    public String adminCustomers(Model model,HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/"; 
        }
        model.addAttribute("customers", userRepo.findAll());
        return "admin-customers";
    }
    @PostMapping("/admin/customers/delete")
    public String deleteCustomer(@RequestParam Long id, RedirectAttributes redirectAttrs,HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/"; 
        }
        // Kiểm tra khách hàng tồn tại
        if(userRepo.existsById(id)) {
            userRepo.deleteById(id);
            redirectAttrs.addFlashAttribute("notificationMessage", "Xóa khách hàng thành công!");
            redirectAttrs.addFlashAttribute("notificationType", "success");
        } else {
            redirectAttrs.addFlashAttribute("notificationMessage", "Khách hàng không tồn tại!");
            redirectAttrs.addFlashAttribute("notificationType", "error");
        }
        return "redirect:/admin/customers";
    }
    @PostMapping("/admin/customers/save")
    public String saveCustomer(@RequestParam(required = false) Long userId,
                               @RequestParam String fullName,
                               @RequestParam String userName,
                               @RequestParam String email,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) String password,
                               RedirectAttributes redirectAttrs,
                               HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/"; 
        }
        if (userId == null) {
            // Thêm mới
            User newUser = new User();
            newUser.setFullName(fullName);
            newUser.setUserName(userName);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            if (password == null || password.isEmpty()) {
                newUser.setPassword(HashUtil.hashPassword("123456")); // mặc định 123456
            } else {
                newUser.setPassword(HashUtil.hashPassword(password));
            }
            newUser.setStatus("ACTIVE");
            newUser.setRole("USER");
            userRepo.save(newUser);

            redirectAttrs.addFlashAttribute("notificationMessage", "Thêm khách hàng thành công!");
            redirectAttrs.addFlashAttribute("notificationType", "success");
        } else {
            // Cập nhật
            Optional<User> optUser = userRepo.findById(userId);
            if (optUser.isPresent()) {
                User user = optUser.get();
                user.setFullName(fullName);
                user.setEmail(email);
                user.setPhone(phone);
                if (password != null && !password.isEmpty()) {
                    user.setPassword(HashUtil.hashPassword(password));
                }
                userRepo.save(user);

                redirectAttrs.addFlashAttribute("notificationMessage", "Cập nhật khách hàng thành công!");
                redirectAttrs.addFlashAttribute("notificationType", "success");
            } else {
                redirectAttrs.addFlashAttribute("notificationMessage", "Không tìm thấy khách hàng!");
                redirectAttrs.addFlashAttribute("notificationType", "error");
            }
        }

        return "redirect:/admin/customers";
    }
    @PostMapping("/admin/customers/lock")
    public String lockOrUnlockCustomer(@RequestParam Long id, RedirectAttributes redirectAttrs,HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/"; 
        }
        Optional<User> optUser = userRepo.findById(id);
        if (optUser.isPresent()) {
            User user = optUser.get();
            if ("ACTIVE".equals(user.getStatus())) {
                user.setStatus("LOCKED");
                redirectAttrs.addFlashAttribute("notificationMessage", "Đã khóa tài khoản: " + user.getFullName());
                redirectAttrs.addFlashAttribute("notificationType", "success");
            } else {
                user.setStatus("ACTIVE");
                redirectAttrs.addFlashAttribute("notificationMessage", "Đã mở khóa tài khoản: " + user.getFullName());
                redirectAttrs.addFlashAttribute("notificationType", "success");
            }
            userRepo.save(user);
        } else {
            redirectAttrs.addFlashAttribute("notificationMessage", "Không tìm thấy khách hàng!");
            redirectAttrs.addFlashAttribute("notificationType", "error");
        }

        return "redirect:/admin/customers";
    }


}