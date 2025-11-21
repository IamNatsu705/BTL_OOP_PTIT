package btl_oop.btl_oop.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import btl_oop.btl_oop.Models.Booking;
import btl_oop.btl_oop.Models.User;

public interface BookingRepository extends JpaRepository<Booking, Long>{
    List<Booking> findByUser(User user);
}
