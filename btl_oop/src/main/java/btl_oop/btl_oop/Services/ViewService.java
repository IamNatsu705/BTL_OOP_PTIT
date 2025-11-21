package btl_oop.btl_oop.Services;
import java.math.BigDecimal;
import java.time.LocalDate;

import btl_oop.btl_oop.Models.Court;
import btl_oop.btl_oop.Models.SlotBooked;
import btl_oop.btl_oop.Repositories.SlotBookedRepository;

import java.util.*;

import org.springframework.stereotype.Service;

import btl_oop.btl_oop.Models.Slot;
import btl_oop.btl_oop.Models.User;
import btl_oop.btl_oop.Repositories.CourtRepository;
import btl_oop.btl_oop.Repositories.SlotRepository;
import btl_oop.btl_oop.Repositories.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewService {

    private final SlotBookedRepository bookedRepo;
    private final UserRepository userRepo;
    private final SlotRepository slotRepo;
    private final CourtRepository courtRepo;

    public List<Court> getAllCourts(){
        return courtRepo.findAll();
    }

    public void saveBookSlot(@NonNull Long userId,@NonNull  Long courtId,@NonNull  Long slotId, LocalDate date) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Court court = courtRepo.findById(courtId)
                .orElseThrow(() -> new RuntimeException("Court not found"));
        Slot slot = slotRepo.findById(slotId).orElseThrow(() -> new RuntimeException("Court not found"));
        
        BigDecimal price = slot.getTypeSlots().getPrice();

        SlotBooked booking = new SlotBooked();
        booking.setUser(user);
        booking.setCourt(court);
        booking.setSlot(slot);
        booking.setDate(date);
        booking.setPrice(price);

        bookedRepo.save(booking);
    }

}
