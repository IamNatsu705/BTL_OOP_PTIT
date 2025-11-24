package btl_oop.btl_oop.Controllers;

import btl_oop.btl_oop.Services.CourtSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final CourtSlotService courtSlotService;

    // --- TRANG CHỦ ---
    @GetMapping("/")
    public String home(Model model) {
        // Lấy danh sách sân thật từ DB hiển thị lên trang chủ
        model.addAttribute("courts", courtSlotService.getAllCourts());
        return "home";
    }

    // --- TRANG ĐẶT SÂN ---
    @GetMapping("/booking")
    public String bookingPage(Model model) {
        // Lấy danh sách sân đổ vào dropdown chọn sân
        model.addAttribute("courts", courtSlotService.getAllCourts());
        
        // Mặc định ngày chọn là hôm nay
        model.addAttribute("selectedDate", LocalDate.now().toString());
        
        return "booking";
    }
}