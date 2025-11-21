package btl_oop.btl_oop.Repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import btl_oop.btl_oop.Models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUserName(String userName);
}
