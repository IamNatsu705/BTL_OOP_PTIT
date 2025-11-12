package btl_oop.btl_oop.Models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String phone;

    private String role;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private String status;

    // --- Relationships ---

    // Một User có thể có nhiều lượt đặt (SlotBooked)
    // 'mappedBy = "user"' trỏ đến tên trường 'user' trong class SlotBooked
    @OneToMany(mappedBy = "user")
    private Set<SlotBooked> bookings;
}