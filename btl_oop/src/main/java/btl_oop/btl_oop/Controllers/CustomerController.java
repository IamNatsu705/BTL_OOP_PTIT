package btl_oop.btl_oop.Controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import btl_oop.btl_oop.Models.Booking;
import btl_oop.btl_oop.Services.CustomerService;
import btl_oop.btl_oop.Services.ViewService;

@RequiredArgsConstructor
@Controller
public class CustomerController {
    private final CustomerService customerService;
    private final ViewService viewService;

    @GetMapping("/history")
    public String historyPage(
            Model model,
            HttpSession session) {

        String userName = String.valueOf(session.getAttribute("currentUser"));
        Long userId = customerService.getIdUser(userName);
        List<Booking> historyBookings = customerService.getHistoryById(userId);
        model.addAttribute("bookings", historyBookings);
        return "user_history";
    }

    @GetMapping("/profile")
    public String profilePage(
            Model model,
            HttpSession session) {
        String userName = String.valueOf(session.getAttribute("currentUser"));
        Map<String, String> userProfile = customerService.getUserProfile(userName);
        model.addAttribute("user", userProfile);
        return "user_profile";
    }

    @PostMapping("/booking")
    public String createBooking(
            @RequestParam("selectedCourtId") Long courtId,
            @RequestParam("selectedDate") String selectedDateStr,
            @RequestParam("selectedTimeSlots") String selectedTimeSlots,
            @RequestParam("totalPrice") double totalPrice,
            HttpSession session) {
        Object userName = session.getAttribute("currentUser");
        Long idUser = customerService.getIdUser(String.valueOf(userName));
        List<Long> bookedSlots = new ArrayList<>(List.of(selectedTimeSlots.split(","))).stream().map(a -> Long.valueOf(a)).collect(Collectors.toList());
        LocalDate date = LocalDate.parse(selectedDateStr);
        for(Long slot: bookedSlots){
            viewService.saveBookSlot(idUser, courtId, slot, date);
        }
        
        customerService.saveBooking(idUser, courtId, date, BigDecimal.valueOf(totalPrice), selectedTimeSlots);
        return "redirect:/booking";
    }
}