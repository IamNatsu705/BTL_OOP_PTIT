package btl_oop.btl_oop.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "slot_booked")
public class SlotBooked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sb_id")
    private Long id;

    @Column(precision = 10, scale = 2)
    private BigDecimal price; // Giá tại thời điểm đặt

    private LocalDate date; // Ngày đặt

    // --- Relationships ---

    // Nhiều lượt đặt có thể thuộc về một User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Nhiều lượt đặt có thể thuộc về một Court
    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    // Nhiều lượt đặt có thể thuộc về một Slot
    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;
}