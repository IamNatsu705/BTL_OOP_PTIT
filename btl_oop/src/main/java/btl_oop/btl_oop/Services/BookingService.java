package btl_oop.btl_oop.Services;

import btl_oop.btl_oop.Models.*;
import btl_oop.btl_oop.Repositories.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Quan trọng để rollback nếu lỗi

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Transactional
    public Bill createBooking(Long userId, Long courtId, LocalDate bookingDate, List<Long> slotIds) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Court court = courtRepo.findById(courtId)
                .orElseThrow(() -> new RuntimeException("Sân không tồn tại"));

        Bill bill = new Bill();
        bill.setUser(user);

        List<SlotBooked> details = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Long slotId : slotIds) {
            Slot slot = slotRepo.findById(slotId)
                    .orElseThrow(() -> new RuntimeException("Slot id " + slotId + " không tồn tại"));
            if(slotBookedRepo.findByCourtIdAndSlotIdAndBookingDate(courtId,slotId,bookingDate).isPresent()){
                throw new RuntimeException("Đã bị trùng vui lòng chọn lại");
            }
            SlotBooked detail = new SlotBooked();
            detail.setBill(bill);
            detail.setCourt(court);
            detail.setSlot(slot);
            detail.setBookingDate(bookingDate);
            
            BigDecimal currentPrice = slot.getTypeSlots().getPrice();
            detail.setPrice(currentPrice);

            totalAmount = totalAmount.add(currentPrice);

            details.add(detail);
        }

        bill.setTotalAmount(totalAmount);
        bill.setSlotBookedList(details);

        return billRepo.save(bill);
    }
    
    public List<Bill> getHistoryByUserId(Long userId) {
        return billRepo.findByUserUserId(userId);
    }

}