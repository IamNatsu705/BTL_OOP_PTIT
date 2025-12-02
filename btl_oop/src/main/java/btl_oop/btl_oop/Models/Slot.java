package btl_oop.btl_oop.Models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "slots")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long id;

    private String name;

    @Column(name = "time_begin")
    private int timeBegin;

    @Column(name = "time_end")
    private int timeEnd;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private TypeSlots typeSlots;

    @OneToMany(mappedBy = "slot")
    private Set<SlotBooked> bookings;
}
