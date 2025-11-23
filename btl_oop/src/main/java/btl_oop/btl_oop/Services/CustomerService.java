package btl_oop.btl_oop.Services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull; // Import đúng của Spring

import btl_oop.btl_oop.Models.Booking;
import btl_oop.btl_oop.Models.User;
import btl_oop.btl_oop.Repositories.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomerService {
    private final UserRepository userRepo;
    private final CourtRepository courtRepo;
    private final BookingRepository bookingRepo;

    public Long getIdUser(String userName){
        // SỬA LỖI 1: Gọi đúng hàm getUserId() thay vì getId()
        return userRepo.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"))
                .getUserId();
    }

    public Map<String, String> getUserProfile(String userName){
        // Logic lấy ID user
        User user = userRepo.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // SỬA LỖI 3: Map.of không nhận null, phải check null trước
        String safeName = user.getFullName() != null ? user.getFullName() : user.getUserName();
        String safePhone = user.getPhone() != null ? user.getPhone() : "";
        String safeEmail = user.getEmail() != null ? user.getEmail() : "";
        String safeDate = user.getCreatedAt() != null ? user.getCreatedAt().format(fmt) : "N/A";

        return Map.of(
            "name", safeName,
            "phone", safePhone,
            "email", safeEmail,
            "registerDate", safeDate
        );
    }

    public void saveBooking(@NonNull Long userId,@NonNull Long courtId, LocalDate date, BigDecimal price, String slotBooked){
        // Nên dùng orElseThrow thay vì .get() để tránh lỗi NoSuchElementException
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String courtName = courtRepo.findById(courtId)
                .orElseThrow(() -> new RuntimeException("Court not found"))
                .getName();
        
        String[] bookedSlots = slotBooked.split(",");
        
        // Logic tính giờ
        String timeSlots = String.format("%02d:00 - %02d:00", 
                Integer.parseInt(bookedSlots[0]) - 1, 
                Integer.valueOf(bookedSlots[bookedSlots.length - 1])); 
        
        Booking booking = new Booking();
        booking.setCourtName(courtName);
        booking.setDate(date);
        booking.setPrice(price);
        booking.setTimeSlot(timeSlots);
        booking.setUser(user);
        bookingRepo.save(booking);
    }

    // SỬA LỖI 2: Dùng @NonNull đúng với import
    public List<Booking> getHistoryById(@NonNull Long userId){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepo.findByUser(user);
    }
}