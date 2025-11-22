package btl_oop.btl_oop.Repositories;

import btl_oop.btl_oop.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName); // tìm user theo username
    Optional<User> findByEmail(String email);       // tìm user theo email
}
