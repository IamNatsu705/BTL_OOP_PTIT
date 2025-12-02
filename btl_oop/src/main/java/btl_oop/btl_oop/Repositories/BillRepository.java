package btl_oop.btl_oop.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import btl_oop.btl_oop.Models.Bill;
import java.util.List;
import java.time.LocalDateTime;


@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByUserUserId(Long userId);
    List<Bill> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}