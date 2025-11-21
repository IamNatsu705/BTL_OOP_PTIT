package btl_oop.btl_oop.Repo;

import btl_oop.btl_oop.Models.TypeSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeSlotRepo extends JpaRepository<TypeSlots, Long> {
}
