package btl_oop.btl_oop.Services;

import btl_oop.btl_oop.Models.User;
import btl_oop.btl_oop.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(String userName, String password, String phone, String email,String fullName) {

        if (userRepository.findByUserName(userName).isPresent()) {
            throw new RuntimeException("UserName đã tồn tại!");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        user.setFullName(fullName);
        user.setRole("USER");
        user.setStatus("ACTIVE");

        return userRepository.save(user);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public void updatePasswordByEmail(String email, String newPassword) {
        User user = findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword);
            userRepository.save(user);
        }
    }
    public User save(User user) {
        return userRepository.save(user);
    }

}
