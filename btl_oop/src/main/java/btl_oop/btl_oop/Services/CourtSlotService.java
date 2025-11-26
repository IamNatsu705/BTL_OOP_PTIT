package btl_oop.btl_oop.Services;

import btl_oop.btl_oop.Models.*;
import btl_oop.btl_oop.Repositories.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourtSlotService {

    private final CourtRepository courtRepo;
    private final SlotRepository slotRepo;
    private final SlotBookedRepository slotBookedRepo;

    public CourtSlotService(CourtRepository courtRepo, SlotRepository slotRepo, SlotBookedRepository slotBookedRepo) {
        this.courtRepo = courtRepo;
        this.slotRepo = slotRepo;
        this.slotBookedRepo = slotBookedRepo;
    }

    public List<Court> getAllCourts() {
        return courtRepo.findAll();
    }

    public List<Slot> getAllSlots() {
        return slotRepo.findAll();
    }

    // Hàm quan trọng: Tìm xem sân này ngày hôm nay CÒN TRỐNG những giờ nào?
    public List<Slot> getAvailableSlots(Long courtId, LocalDate date) {
        // 1. Lấy tất cả các slot gốc (Ví dụ: 7h-8h, 8h-9h...)
        List<Slot> allSlots = slotRepo.findAll();

        // 2. Lấy danh sách các slot ĐÃ BỊ ĐẶT (Booking) ngày hôm đó tại sân đó
        List<SlotBooked> bookedList = slotBookedRepo.findByBookingDateAndCourtId(date, courtId);
        
        // Lấy da danh sách ID của các slot đã bị đặt
        List<Long> bookedSlotIds = bookedList.stream()
                .map(sb -> sb.getSlot().getId()) // Giả sử SlotBooked có quan hệ getSlot()
                .collect(Collectors.toList());

        // 3. Lọc ra những slot chưa bị đặt
        return allSlots.stream()
                .filter(slot -> !bookedSlotIds.contains(slot.getId()))
                .collect(Collectors.toList());
    }
    // lấy giá tiền
    public record SlotPriceDTO(Long courtId, String courtName, Long slotId, String timeFrame, BigDecimal price) {}
    public List<SlotPriceDTO> getSlotPrices(LocalDate date) {
        List<Court> courts = courtRepo.findAll();
        List<Slot> slots = slotRepo.findAll();
        List<SlotPriceDTO> slotPrices = new ArrayList<>();
        for (Court court : courts) {
            for (Slot slot : slots) {
                SlotPriceDTO slotPrice = new SlotPriceDTO(court.getId(), court.getName(), slot.getId(), slot.getName(), slot.getTypeSlots().getPrice());
                slotPrices.add(slotPrice);
            }
        }
        return slotPrices;
    }
    // lấy slotcourt đã có người đặt trong ngày
    public record SlotBookedDTO(Long courId, Long slotId, BigDecimal price, String fullName, String phone){};

    public List<SlotBookedDTO> getSlotBooked(LocalDate date) {
        List<SlotBooked> slotBooked = slotBookedRepo.findByBookingDate(date);
        List<SlotBookedDTO> slotBookedDTO = new ArrayList<>();
        
        for (SlotBooked sb : slotBooked) {
            // Lấy User từ hóa đơn
            User user = sb.getBill().getUser();
            
            // 2. Map thêm số điện thoại vào DTO
            SlotBookedDTO dto = new SlotBookedDTO(
                sb.getCourt().getId(), 
                sb.getSlot().getId(), 
                sb.getPrice(), 
                user.getFullName(), // Lấy tên đầy đủ
                user.getPhone()     // Lấy số điện thoại
            );
            slotBookedDTO.add(dto);
        }
        return slotBookedDTO; 
    }
    
}