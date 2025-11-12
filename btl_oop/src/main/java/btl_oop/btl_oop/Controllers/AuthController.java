package btl_oop.btl_oop.Controllers;


import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
// import java.time.LocalDate;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session
    ) {
        if (username.equals("admin") && password.equals("123")) {
            session.setAttribute("currentUser", username);
            session.setAttribute("role", "admin");
            session.setAttribute("isAuthenticated", true);
            return "redirect:/admin";
        } 
        else if (username.equals("user") && password.equals("123")) {
            session.setAttribute("currentUser", username);
            session.setAttribute("role", "user");
            session.setAttribute("isAuthenticated", true);
            return "redirect:/";
        }
        else {
            return "redirect:/login?error";
        }
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    // @PostMapping("/register")
    // public String register(
    //         @RequestParam String fullName,
    //         @RequestParam String username,
    //         @RequestParam String email,
    //         @RequestParam String phone,
    //         @RequestParam String password
            
    // ){  
    //     return "redirect:/login";
    // }
            
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
