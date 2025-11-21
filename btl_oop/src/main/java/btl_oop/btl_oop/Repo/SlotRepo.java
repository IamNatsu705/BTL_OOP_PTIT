package btl_oop.btl_oop.Repo;

import btl_oop.btl_oop.Models.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotRepo extends JpaRepository<Slot, Long> {
}
