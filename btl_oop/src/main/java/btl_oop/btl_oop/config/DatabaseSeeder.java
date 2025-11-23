package btl_oop.btl_oop.config;

import btl_oop.btl_oop.Models.Court;
import btl_oop.btl_oop.Models.Slot;
import btl_oop.btl_oop.Models.TypeSlots;
import btl_oop.btl_oop.Models.User;
import btl_oop.btl_oop.Repositories.CourtRepository;
import btl_oop.btl_oop.Repositories.SlotRepository;
import btl_oop.btl_oop.Repositories.TypeSlotRepository;
import btl_oop.btl_oop.Repositories.UserRepository;
import btl_oop.btl_oop.Utils.HashUtil;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(
            CourtRepository courtRepository,
            UserRepository userRepository,
            SlotRepository slotRepository,
            TypeSlotRepository typeSlotRepository
    ) {
        return args -> {
            // ==========================================
            // 1. TẠO SÂN (COURTS)
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
            // SỬA: Dùng .isEmpty() vì Repository trả về Optional
            if (userRepository.findByUserName("admin").isEmpty()) {
                System.out.println("Đang khởi tạo tài khoản Admin...");
                User admin = new User();
                admin.setUserName("admin");
                admin.setPassword(HashUtil.hashPassword("123456"));
                admin.setPhone("0999999999");
                admin.setRole("admin");
                admin.setStatus("active");
                
                userRepository.save(admin);
                System.out.println("Đã tạo user: admin / pass: 123456");
            }

            // ==========================================
            // 3. TẠO 24 SLOT (KHUNG GIỜ)
            // ==========================================
            if (slotRepository.count() == 0) {
                System.out.println("Đang khởi tạo 24 slot thời gian...");

                List<TypeSlots> types = typeSlotRepository.findAll();
                TypeSlots defaultType;
                
                if (types.isEmpty()) {
                    defaultType = new TypeSlots();
                    defaultType.setName("Giờ thường");
                    defaultType.setPrice(java.math.BigDecimal.valueOf(50000));
                    typeSlotRepository.save(defaultType);
                } else {
                    defaultType = types.get(0);
                }

                // Tạo 24 slot từ 0h đến 23h
                List<Slot> slots = new ArrayList<>();
                for (int i = 0; i < 24; i++) {
                    Slot slot = new Slot();
                    
                    // SỬA: Entity Slot dùng kiểu int, không phải LocalTime
                    int startHour = i;
                    int endHour = i + 1; // Ví dụ: 7h đến 8h

                    slot.setName(String.format("%02d:00 - %02d:00", startHour, endHour)); 
                    slot.setTimeBegin(startHour); // Lưu số nguyên (ví dụ: 7)
                    slot.setTimeEnd(endHour);     // Lưu số nguyên (ví dụ: 8)
                    
                    slot.setTypeSlots(defaultType);

                    slots.add(slot);
                }
                slotRepository.saveAll(slots);
                System.out.println("Đã tạo xong 24 slot!");
            }
        };
    }
}