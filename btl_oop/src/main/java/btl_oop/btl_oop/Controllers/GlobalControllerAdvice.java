package btl_oop.btl_oop.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpSession session) {
        
        // Đây chính là khối code lặp đi lặp lại
        Object isAuthenticated = session.getAttribute("isAuthenticated");
        
        if (isAuthenticated != null) {
            model.addAttribute("isAuthenticated", isAuthenticated);
            model.addAttribute("currentUser", session.getAttribute("currentUser"));
            model.addAttribute("role", session.getAttribute("role"));
        }
    }
}