package btl_oop.btl_oop.Models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @Column(name = "court_name")
    private String courtName;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time_slot")
    private String timeSlot;

    @Column(name = "price")
    private BigDecimal price;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
