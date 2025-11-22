package btl_oop.btl_oop.Repo;

import btl_oop.btl_oop.Models.SlotBooked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SlotBookedRepo extends JpaRepository<SlotBooked, Long> {

    List<SlotBooked> findByDate(LocalDate date);

    List<SlotBooked> findByCourtId(Long courtId);

    List<SlotBooked> findByCourtIdAndDate(Long courtId, LocalDate date);
}
