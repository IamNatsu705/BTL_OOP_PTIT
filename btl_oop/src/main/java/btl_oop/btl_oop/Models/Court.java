package btl_oop.btl_oop.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "courts")
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "court_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String status;

    private String description;
    @OneToMany(mappedBy = "court")
    private Set<SlotBooked> bookings;
}