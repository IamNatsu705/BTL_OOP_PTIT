package btl_oop.btl_oop.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import btl_oop.btl_oop.Models.TypeSlots;

@Repository
public interface TypeSlotRepository extends JpaRepository<TypeSlots, Long>{

}
