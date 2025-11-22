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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    // API đặt sân
    record BookingSlot(int courtId,int slotId,Long price){};
    record BookingRequest(LocalDate bookingDate,Long totalAmount,List<BookingSlot> selectedSlots) {}
    @PostMapping("/booking")
    public String createBooking(
        @RequestBody BookingRequest request,
        RedirectAttributes redirectAttributes
    ) {
        return "redirect:/booking";
    }
}