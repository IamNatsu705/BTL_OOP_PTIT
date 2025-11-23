package btl_oop.btl_oop.Repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Thêm import này
import org.springframework.data.repository.query.Param; // Thêm import này
import org.springframework.stereotype.Repository;

import btl_oop.btl_oop.Models.SlotBooked;

@Repository
public interface SlotBookedRepository extends JpaRepository<SlotBooked, Long> {

    // 1. Tìm theo User ID (SỬA LỖI Ở ĐÂY)
    // Vì trong User bạn đặt là 'userId' nên phải query thủ công hoặc đổi tên hàm. 
    // Dùng @Query là an toàn nhất để không phải đổi tên hàm ở các Service khác.
    @Query("SELECT s FROM SlotBooked s WHERE s.user.userId = :uid")
    List<SlotBooked> findByUserId(@Param("uid") Long userId);

    // 2. Tìm theo Court ID + Date (Giữ nguyên tên hàm đã sửa ở bước trước)
    List<SlotBooked> findByCourtIdAndDate(Long courtId, LocalDate date);

    // 3. Tìm theo Court ID
    List<SlotBooked> findByCourtId(Long courtId);

    // 4. Tìm theo Date
    List<SlotBooked> findByDate(LocalDate date);
}