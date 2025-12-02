package btl_oop.btl_oop.Controllers;

import btl_oop.btl_oop.Models.Court;
import btl_oop.btl_oop.Models.Slot;
import btl_oop.btl_oop.Models.User;
import btl_oop.btl_oop.Services.BookingService;
import btl_oop.btl_oop.Services.CourtSlotService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final CourtSlotService courtSlotService;
    private final BookingService bookingService;    
    @GetMapping("/slots/price")
    public ResponseEntity<List<CourtSlotService.SlotPriceDTO>> getPriceSlots(
           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
            return ResponseEntity.ok(courtSlotService.getSlotPrices(date));
    }

    // 2. API Lấy danh sách Slot ĐÃ ĐẶT (Sửa lại logic chỗ này)
    @GetMapping("/slots/booked")
    public ResponseEntity<List<CourtSlotService.SlotBookedDTO>> getBookedSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpSession session) {
        
        String currentRole = (String) session.getAttribute("role");
        boolean isAdmin = "admin".equalsIgnoreCase(currentRole);
        List<CourtSlotService.SlotBookedDTO> res=courtSlotService.getSlotBooked(date,isAdmin);
        return ResponseEntity.ok(res);
    }

    // 3. API Tạo đơn đặt sân
    @PostMapping("/booking/create")
    public String createBooking(
            @RequestParam Long courtId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam List<Long> slotIds, 
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Trả về chuỗi đặc biệt để JS nhận biết cần login
            return "REDIRECT_LOGIN"; 
        }

        try {
            bookingService.createBooking(user.getUserId(), courtId, date, slotIds);
            return "SUCCESS"; // Báo thành công cho JS
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }
}