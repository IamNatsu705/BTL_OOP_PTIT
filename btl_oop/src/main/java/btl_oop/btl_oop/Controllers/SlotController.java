package btl_oop.btl_oop.Controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import btl_oop.btl_oop.Services.*;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/getdata/slots")
public class SlotController {
    private final SlotService slotService;
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

    // API 2: Lấy các slot ĐÃ ĐẶT theo ngày và sân
    record slotBooked(int slotBooked_id,int court_id,int slot_id,LocalDate dateBooked){}
    @GetMapping("/booked")
    public ResponseEntity<List<slotBooked>> getBookedSlotIds(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        System.out.println("API /booked được gọi với ngày: " + date);

        // --- TẠO DỮ LIỆU GIẢ ---
        List<slotBooked> mockBookings = List.of(
            // Sân 1, Slot 8 (08:00)
            new slotBooked(101, 1, 8, date),
            
            // Sân 3, Slot 15 (15:00)
            new slotBooked(102, 3, 15, date),
            
            // Sân 3, Slot 16 (16:00) (Giờ liên tục để test)
            new slotBooked(103, 3, 16, date),

            // Sân 6, Slot 20 (20:00) - Sân cuối cùng
            new slotBooked(104, 6, 20, date)
        );
        
        // Trả về dữ liệu giả
        return ResponseEntity.ok(mockBookings);
    }
    //API 3: Lấy giá của từng slot , từng sân theo ngày
    record priceSlot(int court_id,int slot_id,Long price,LocalDate dateBooked){};
    @GetMapping("/price")
    public ResponseEntity<List<priceSlot>> getPriceSlots( @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        List<priceSlot> priceSlots = new ArrayList<>();
        int numberOfCourts = 6;
        int slotsPerDay = 24;

        for (int courtId = 1; courtId <= numberOfCourts; courtId++) {
            for (int slotId = 0; slotId < slotsPerDay; slotId++) {
                Long price;
                
                // Thiết lập giá giả định theo giờ:
                // 8h - 16h (slot 8-16): Giờ thấp điểm (50.000₫)
                // 17h - 21h (slot 17-21): Giờ cao điểm (120.000₫)
                // Các giờ còn lại: Giờ trung bình (80.000₫)
                if (slotId >= 8 && slotId <= 16) {
                    price = 50000L; // Giờ hành chính
                } else if (slotId >= 17 && slotId <= 21) {
                    price = 120000L; // Giờ cao điểm buổi tối
                } else {
                    price = 80000L; // Giờ đêm/sáng sớm
                }

                priceSlots.add(new priceSlot(courtId, slotId, price, date));
            }
        }

        return ResponseEntity.ok(priceSlots);
    }
}