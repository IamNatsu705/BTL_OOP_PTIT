package btl_oop.btl_oop.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Entity
@Table(name = "type_slots")
public class TypeSlots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Long id;

    // (giờ vàng, giờ thường, giờ không tiếp khách)
    @Column(nullable = false)
    private String name;

    @Column(precision = 10, scale = 2) // Dùng BigDecimal cho tiền tệ
    private BigDecimal price;

    // --- Relationships ---

    // Một TypeSlots có thể áp dụng cho nhiều Slots
    @OneToMany(mappedBy = "typeSlots")
    private Set<Slot> slots;
}