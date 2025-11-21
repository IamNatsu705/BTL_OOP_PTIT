package btl_oop.btl_oop.Repo;

import btl_oop.btl_oop.Models.Court;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourtRepo extends JpaRepository<Court, Long> {
}
