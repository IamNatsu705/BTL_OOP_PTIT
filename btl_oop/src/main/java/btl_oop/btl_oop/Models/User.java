package btl_oop.btl_oop.Models;

import jakarta.persistence.*;
import lombok.Data; // Thêm Lombok
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role")
    private String role;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Quan hệ
    @OneToMany(mappedBy = "user") 
    @JsonIgnore
    private List<Bill> bills; 

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}