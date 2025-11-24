package btl_oop.btl_oop.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import btl_oop.btl_oop.Models.Bill;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    
    // Tìm tất cả hóa đơn của một User (Để xem lịch sử giao dịch)
    // Spring Data JPA tự động hiểu: tìm field 'user', bên trong user tìm field 'userId'
    List<Bill> findByUserUserId(Long userId);
}