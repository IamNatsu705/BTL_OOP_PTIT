package btl_oop.btl_oop.Controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/getdata/slots")
public class SlotController {

    // Tạo một record (hoặc class) đơn giản để chứa dữ liệu slot
    record Slot(long id, double pricing, String status, String time_start, String time_end) {}

    /**
     * API 1: Lấy 24 slot mẫu (cấu hình, giá, trạng thái...)
     */
    @GetMapping("/template")
    public ResponseEntity<List<Slot>> getTemplateSlots() {
        List<Slot> templateSlots = new ArrayList<>();
        
        // --- Tạo 24 slot giả ---
        for (int i = 0; i <= 23; i++) {
            long id = i + 1; // Slot ID (từ 1 đến 24)
            String timeStart = String.format("%02d:00", i);
            String timeEnd = String.format("%02d:00", i + 1);
            
            double price = 50000; // Giá thường
            String status = "giờ thường";

            if (i >= 17 && i <= 20) {
                price = 80000; // Giờ vàng
                status = "giờ vàng";
            } else if (i < 5) {
                price = 30000; // Giờ sáng sớm
            }

            if (i == 13) { // Giờ nghỉ trưa
                status = "ko tiếp khách";
            }

            templateSlots.add(new Slot(id, price, status, timeStart, timeEnd));
        }
        
        return ResponseEntity.ok(templateSlots);
    }

    /**
     * API 2: Lấy các slot ĐÃ ĐẶT theo ngày và sân
     */
    @GetMapping("/booked")
    public ResponseEntity<Set<Long>> getBookedSlotIds(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("courtId") Long courtId) {
        
        // --- Logic dữ liệu giả ---
        // Backend sẽ truy vấn DB dựa trên date và courtId
        // Ở đây, chúng ta trả về dữ liệu giả
        
        Set<Long> bookedIds;

        if (courtId == 1) {
            // Sân 1 có slot 17, 18, 19 (tức 16:00, 17:00, 18:00) đã đặt
            bookedIds = Set.of(17L, 18L, 19L); 
        } else if (courtId == 2) {
             // Sân 2 có slot 8, 9, 10 (tức 07:00, 08:00, 09:00) đã đặt
            bookedIds = Set.of(8L, 9L, 10L);
        } else {
            bookedIds = Set.of(); // Sân khác rỗng
        }

        // Chỉ trả về 1 Set các ID: [17, 18, 19]
        return ResponseEntity.ok(bookedIds);
    }
   
}