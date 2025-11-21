package btl_oop.btl_oop.Controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class AdminController {
    //record
    record bookedSlot(int id_bookedSlot,int id_user, int court,int slot, String dateBooked, Long price){};
    
    //api
    @GetMapping("/admin")
    public String adminOverview(Model model) {
        model.addAttribute("monthlyRevenue", 125000000);
        model.addAttribute("todayBookings", 32);
        model.addAttribute("totalCustomers", MockData.getCustomers().size());
        model.addAttribute("revenueData", MockData.getRevenueData());
        return "admin-overview"; // Trả về file admin-overview.html
    }

    @GetMapping("/admin/courts")
    public String adminCourts(Model model) {
        model.addAttribute("courts", MockData.getCourts());
        return "admin-courts"; // Trả về file admin-courts.html
    }

    @GetMapping("/admin/pricing")
    public String adminPricing(Model model) {
        model.addAttribute("timeSlots", MockData.getTimeSlots());
        return "admin-pricing"; // Trả về file admin-pricing.html
    }

    @GetMapping("/admin/customers")
    public String adminCustomers(Model model) {
        model.addAttribute("customers", MockData.getCustomers());
        return "admin-customers"; // Trả về file admin-customers.html
    }

    @GetMapping("/admin/comments")
    public String adminComments(Model model) {
        model.addAttribute("comments", MockData.getComments());
        return "admin-comments"; // Trả về file admin-comments.html
    }
}
