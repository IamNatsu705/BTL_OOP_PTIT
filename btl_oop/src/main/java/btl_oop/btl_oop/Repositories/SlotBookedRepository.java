package btl_oop.btl_oop.Repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import btl_oop.btl_oop.Models.SlotBooked;

@Repository
public interface SlotBookedRepository extends JpaRepository<SlotBooked, Long> {


    List<SlotBooked> findByBookingDateAndCourtId(LocalDate bookingDate, Long courtId);

    List<SlotBooked> findByBillId(Long billId);

    List<SlotBooked> findByBookingDate(LocalDate bookingDate);


    @Query("SELECT s FROM SlotBooked s WHERE s.bill.user.userId = :uid")
    List<SlotBooked> findAllByUserId(@Param("uid") Long userId);
}