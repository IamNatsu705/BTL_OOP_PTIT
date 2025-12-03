package btl_oop.btl_oop.Controllers;

import btl_oop.btl_oop.Services.CourtSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final CourtSlotService courtSlotService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("courts", courtSlotService.getAllCourts());
        return "home";
    }

    @GetMapping("/booking")
    public String bookingPage(Model model) {
        model.addAttribute("courts", courtSlotService.getAllCourts());
        model.addAttribute("selectedDate", LocalDate.now().toString());
        return "booking";
    }
     @GetMapping("/contact")
     public String contactPage(){
        return "redirect:/";
     }
}