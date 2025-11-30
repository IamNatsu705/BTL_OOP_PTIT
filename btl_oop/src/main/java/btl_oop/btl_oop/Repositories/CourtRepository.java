package btl_oop.btl_oop.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import btl_oop.btl_oop.Models.Court;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long>{
    Long countByStatus(String status);
}
