package btl_oop.btl_oop.Models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Long id;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount; // Tổng tiền của cả hóa đơn

    @Column(name = "created_at")
    private LocalDateTime createdAt; // Ngày giờ thanh toán

    // --- Quan hệ ---

    // Hóa đơn của ai?
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Hóa đơn gồm những slot nào?
    // CascadeType.ALL: Lưu Bill là nó tự lưu luôn danh sách SlotBooked bên dưới
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<SlotBooked> slotBookedList;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now(); // Tự động lấy giờ hiện tại khi lưu
    }
}