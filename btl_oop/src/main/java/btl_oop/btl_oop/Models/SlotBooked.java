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

    // Ngày đá/chơi cầu lông (Ví dụ: ngày mai)
    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    // Giá tiền CỦA RIÊNG SLOT NÀY tại thời điểm đặt
    // Cần lưu lại để sau này nếu bảng giá gốc tăng thì lịch sử giá này ko bị đổi
    @Column(precision = 10, scale = 2)
    private BigDecimal price; 

    // --- Quan hệ ---

    // Slot này thuộc về Hóa đơn nào?
    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false) 
    @JsonIgnore
    private Bill bill;
    

    // Slot này đặt cho Sân nào?
    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    @JsonIgnore
    private Court court;

    // Slot này là khung giờ nào? (Ví dụ: Ca 1, Ca 2...)
    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    @JsonIgnore
    private Slot slot;
}