package btl_oop.btl_oop.Models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.Set;

@Data
@Entity
@Table(name = "slots")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long id;

    private String name; // Ví dụ: "Sáng sớm", "Tối"

    @Column(name = "time_begin")
    private LocalTime timeBegin;

    @Column(name = "time_end")
    private LocalTime timeEnd;

    // --- Relationships ---

    // Nhiều Slots có thể thuộc về một TypeSlots
    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false) // 'type_id' là tên cột khóa ngoại
    private TypeSlots typeSlots;

    // Một Slot có thể được đặt trong nhiều lượt (SlotBooked)
    @OneToMany(mappedBy = "slot")
    private Set<SlotBooked> bookings;
}
