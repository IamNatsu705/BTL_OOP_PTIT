package btl_oop.btl_oop.config;

import btl_oop.btl_oop.Models.Court;
import btl_oop.btl_oop.Models.Slot;
import btl_oop.btl_oop.Models.TypeSlots;
import btl_oop.btl_oop.Models.User;
import btl_oop.btl_oop.Repositories.CourtRepository;
import btl_oop.btl_oop.Repositories.SlotRepository;
import btl_oop.btl_oop.Repositories.TypeSlotRepository; // Bạn cần tạo Repo này
import btl_oop.btl_oop.Repositories.UserRepository;     // Bạn cần tạo Repo này
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(
            CourtRepository courtRepository,
            UserRepository userRepository,
            SlotRepository slotRepository,
            TypeSlotRepository typeSlotRepository // Cần inject thêm để tạo Slot
    ) {
        return args -> {
            // ==========================================
            // 1. TẠO SÂN (COURTS) - Code cũ giữ nguyên
            // ==========================================
            if (courtRepository.count() == 0) {
                System.out.println("Đang khởi tạo 6 sân...");
                List<Court> courts = new ArrayList<>();
                for (int i = 1; i <= 6; i++) {
                    Court court = new Court();
                    court.setName("Sân " + i);
                    if (i == 5) {
                        court.setStatus("ko tiếp khách");
                        court.setDescription("Sân đang bảo trì.");
                    } else {
                        court.setStatus("hoạt động");
                        court.setDescription("Sân tiêu chuẩn.");
                    }
                    courts.add(court);
                }
                courtRepository.saveAll(courts);
            }

            // ==========================================
            // 2. TẠO TÀI KHOẢN ADMIN
            // ==========================================
            // Kiểm tra nếu chưa có user nào tên là admin
            if (userRepository.findByUserName("admin") == null) {
                System.out.println("Đang khởi tạo tài khoản Admin...");
                User admin = new User();
                admin.setUserName("admin");
                admin.setPassword("123456"); // Lưu ý: Nên mã hóa BCrypt nếu dùng Spring Security
                admin.setPhone("0999999999");
                admin.setRole("admin"); // Role quan trọng để phân quyền
                admin.setStatus("active");
                
                userRepository.save(admin);
                System.out.println("Đã tạo user: admin / pass: 123456");
            }

            // ==========================================
            // 3. TẠO 24 SLOT (KHUNG GIỜ)
            // ==========================================
            if (slotRepository.count() == 0) {
                System.out.println("Đang khởi tạo 24 slot thời gian...");

                // BƯỚC QUAN TRỌNG: Phải có TypeSlots trước mới tạo được Slot
                // Kiểm tra xem có TypeSlot nào chưa, nếu chưa thì tạo 1 cái mặc định
                List<TypeSlots> types = typeSlotRepository.findAll();
                TypeSlots defaultType;
                
                if (types.isEmpty()) {
                    defaultType = new TypeSlots();
                    defaultType.setName("Giờ thường");
                    defaultType.setPrice(java.math.BigDecimal.valueOf(50000)); // Giá ví dụ
                    typeSlotRepository.save(defaultType);
                } else {
                    defaultType = types.get(0);
                }

                // Tạo 24 slot từ 00:00 đến 23:00
                List<Slot> slots = new ArrayList<>();
                for (int i = 0; i < 24; i++) {
                    Slot slot = new Slot();
                    
                    // Tính toán giờ bắt đầu và kết thúc
                    LocalTime startTime = LocalTime.of(i, 0); 
                    LocalTime endTime = startTime.plusMinutes(59); // Ví dụ: 00:00 -> 00:59

                    slot.setName(String.format("%02d:00 - %02d:59", i, i)); // Tên: "07:00 - 07:59"
                    slot.setTimeBegin(startTime);
                    slot.setTimeEnd(endTime);
                    
                    // Gán loại slot (Bắt buộc do nullable = false)
                    slot.setTypeSlots(defaultType);

                    slots.add(slot);
                }
                slotRepository.saveAll(slots);
                System.out.println("Đã tạo xong 24 slot!");
            }
        };
    }
}