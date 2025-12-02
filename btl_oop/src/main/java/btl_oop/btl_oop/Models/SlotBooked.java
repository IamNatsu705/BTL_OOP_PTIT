package btl_oop.btl_oop.Models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "slot_booked")
public class SlotBooked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sb_id")
    private Long id;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal price; 

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false) 
    @JsonIgnore
    private Bill bill;
    
    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    @JsonIgnore
    private Court court;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    @JsonIgnore
    private Slot slot;
}