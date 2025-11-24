package btl_oop.btl_oop.Services;

import btl_oop.btl_oop.Models.*;
import btl_oop.btl_oop.Repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Quan trọng để rollback nếu lỗi

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final BillRepository billRepo;
    private final SlotBookedRepository slotBookedRepo;
    private final UserRepository userRepo;
    private final SlotRepository slotRepo;
    private final CourtRepository courtRepo;

    public BookingService(BillRepository billRepo, SlotBookedRepository slotBookedRepo, UserRepository userRepo, SlotRepository slotRepo, CourtRepository courtRepo) {
        this.billRepo = billRepo;
        this.slotBookedRepo = slotBookedRepo;
        this.userRepo = userRepo;
        this.slotRepo = slotRepo;
        this.courtRepo = courtRepo;
    }

    // Hàm này được gọi khi khách bấm nút "THANH TOÁN"
    // @Transactional: Nếu có lỗi xảy ra ở giữa chừng, nó sẽ hủy toàn bộ thao tác (không lưu Bill rác)
    @Transactional
    public Bill createBooking(Long userId, Long courtId, LocalDate bookingDate, List<Long> slotIds) {
        
        // 1. Tìm User
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // 2. Tìm Sân
        Court court = courtRepo.findById(courtId)
                .orElseThrow(() -> new RuntimeException("Sân không tồn tại"));

        // 3. Tạo Hóa Đơn (Bill)
        Bill bill = new Bill();
        bill.setUser(user);
        // setStatus đã có mặc định trong @PrePersist hoặc bạn set tay ở đây nếu dùng Enum
        // bill.setStatus(BillStatus.PAID); 

        // 4. Tạo danh sách các SlotBooked chi tiết
        List<SlotBooked> details = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Long slotId : slotIds) {
            Slot slot = slotRepo.findById(slotId)
                    .orElseThrow(() -> new RuntimeException("Slot id " + slotId + " không tồn tại"));

            // Check trùng: Kiểm tra xem slot này ngày hôm đó đã có ai đặt chưa?
            // (Bạn nên viết thêm hàm check này trong Repo nếu muốn kỹ hơn)
            
            // Tạo chi tiết
            SlotBooked detail = new SlotBooked();
            detail.setBill(bill); // Gắn vào hóa đơn cha
            detail.setCourt(court);
            detail.setSlot(slot);
            detail.setBookingDate(bookingDate);
            
            // Lấy giá tiền hiện tại của khung giờ (TypeSlot)
            BigDecimal currentPrice = slot.getTypeSlots().getPrice();
            detail.setPrice(currentPrice);

            // Cộng dồn tổng tiền
            totalAmount = totalAmount.add(currentPrice);

            details.add(detail);
        }

        // 5. Hoàn thiện Bill
        bill.setTotalAmount(totalAmount);
        bill.setSlotBookedList(details); // Gắn danh sách con vào cha

        // 6. Lưu xuống DB (Chỉ cần lưu Bill, nhờ Cascade.ALL nó sẽ tự lưu SlotBooked)
        return billRepo.save(bill);
    }
    
    // Hàm xem lịch sử đặt sân
    public List<Bill> getHistoryByUserId(Long userId) {
        return billRepo.findByUserUserId(userId);
    }
}