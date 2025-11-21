package btl_oop.btl_oop.Repositories;

import java.time.LocalDate;
import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import btl_oop.btl_oop.Models.SlotBooked;

@Repository
public interface SlotBookedRepository extends JpaRepository<SlotBooked, Long> {

    // lấy lịch đã đặt theo sân + ngày
    List<SlotBooked> findByCourt_IdAndDate(Long courtId, LocalDate date);

    // lấy lịch theo user
    List<SlotBooked> findByUser_Id(Long userId);

    // lấy lịch theo sân
    List<SlotBooked> findByCourt_Id(Long courtId);

    // lấy lịch theo ngày
    List<SlotBooked> findByDate(Date date);
}

