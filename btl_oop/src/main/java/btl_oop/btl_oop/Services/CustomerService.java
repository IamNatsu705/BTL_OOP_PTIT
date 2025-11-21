package btl_oop.btl_oop.Services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import btl_oop.btl_oop.Models.Booking;
import btl_oop.btl_oop.Models.User;
import btl_oop.btl_oop.Repositories.*;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class CustomerService {
    private final UserRepository userRepo;
    private final CourtRepository courtRepo;
    private final BookingRepository bookingRepo;
    public Long getIdUser(String userName){
        long idUser = userRepo.findByUserName(userName).orElseThrow(() -> new RuntimeException("User không tồn tại")).getId();
        return idUser;
    }
    public Map<String, String> getUserProfile(String userName){
        Long idUser = getIdUser(userName);
        User user = userRepo.findById(idUser).get();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return Map.of("name", user.getUserName(), "phone", user.getPhone(), "email", "abc@gmail.com", "registerDate", String.valueOf(user.getCreatedAt().format(fmt)));
    }
    public void saveBooking(Long userId, Long courtId, LocalDate date, BigDecimal price, String slotBooked){
        User user = userRepo.findById(userId).get();
        String courtName = courtRepo.findById(courtId).get().getName();
        String bookedSlots[] = slotBooked.split(",");
        String timeSlots = String.format("%02d:00 - %02d:00", Integer.parseInt(bookedSlots[0])-1, Integer.valueOf(bookedSlots[bookedSlots.length-1])); 
        Booking booking = new Booking();
        booking.setCourtName(courtName);
        booking.setDate(date);
        booking.setPrice(price);
        booking.setTimeSlot(timeSlots);
        booking.setUser(user);
        bookingRepo.save(booking);
    }
    public List<Booking> getHistoryById(@Nonnull Long userId){
        User user = userRepo.findById(userId).get();
        List<Booking> history = bookingRepo.findByUser(user);
        return history;
    }
}
