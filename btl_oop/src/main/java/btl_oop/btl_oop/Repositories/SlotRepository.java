package btl_oop.btl_oop.Repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import btl_oop.btl_oop.Models.Slot;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long>{
}
