package btl_oop.btl_oop.config;

import btl_oop.btl_oop.Models.*;
import btl_oop.btl_oop.Repositories.*;
import btl_oop.btl_oop.Utils.HashUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final CourtRepository courtRepo;
    private final SlotRepository slotRepo;
    private final TypeSlotRepository typeSlotRepo;
    private final BillRepository billRepo;
    private final SlotBookedRepository slotBookedRepo; // Dùng để check trùng khi seed

    public DatabaseSeeder(UserRepository userRepo, CourtRepository courtRepo, SlotRepository slotRepo, 
                          TypeSlotRepository typeSlotRepo, BillRepository billRepo, 
                          SlotBookedRepository slotBookedRepo) {
        this.userRepo = userRepo;
        this.courtRepo = courtRepo;
        this.slotRepo = slotRepo;
        this.typeSlotRepo = typeSlotRepo;
        this.billRepo = billRepo;
        this.slotBookedRepo = slotBookedRepo;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Nếu DB đã có user thì không tạo lại để tránh duplicate
        if (userRepo.count() > 0) {
            System.out.println("Dữ liệu đã tồn tại, bỏ qua Seeding.");
            return;
        }

        System.out.println("Đang khởi tạo dữ liệu mẫu...");

        seedTypeSlotsAndSlots();
        seedCourts();
        seedUsers();
        seedFakeBookings(); // Tạo hóa đơn giả

        System.out.println("Hoàn tất khởi tạo dữ liệu!");
    }

    // 1. Tạo Giá và Khung Giờ (00:00 -> 22:00)
    private void seedTypeSlotsAndSlots() {
        TypeSlots normal = new TypeSlots();
        normal.setName("Giờ Thường");
        normal.setPrice(new BigDecimal("50000"));
        typeSlotRepo.save(normal);

        TypeSlots golden = new TypeSlots();
        golden.setName("Giờ Vàng (17h-20h)");
        golden.setPrice(new BigDecimal("80000"));
        typeSlotRepo.save(golden);

        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            Slot slot = new Slot();
            slot.setName(i + ":00 - " + (i + 1) + ":00");
            slot.setTimeBegin(i);
            slot.setTimeEnd(i + 1);

            // Giờ vàng từ 17h đến 20h
            if (i >= 17 && i <= 20) {
                slot.setTypeSlots(golden);
            } else {
                slot.setTypeSlots(normal);
            }
            slots.add(slot);
        }
        slotRepo.saveAll(slots);
    }

    // 2. Tạo 6 Sân
    private void seedCourts() {
        List<Court> courts = new ArrayList<>();
        courts.add(createCourt("Sân 1 (VIP Gỗ)", "available", "Sàn gỗ nhập khẩu, ánh sáng chuẩn thi đấu."));
        courts.add(createCourt("Sân 2 (Thảm Xịn)", "available", "Thảm Yonex độ nảy tốt."));
        courts.add(createCourt("Sân 3 (Thường)", "available", "Sân tiêu chuẩn tập luyện."));
        courts.add(createCourt("Sân 4 (Thường)", "available", "Sân tiêu chuẩn tập luyện."));
        courts.add(createCourt("Sân 5 (Ngoài trời)", "available", "Đang sửa mái che."));
        courts.add(createCourt("Sân 6 (VIP 2)", "available", "Khu vực riêng tư, có điều hòa."));
        courtRepo.saveAll(courts);
    }

    private Court createCourt(String name, String status, String desc) {
        Court c = new Court();
        c.setName(name);
        c.setStatus(status);
        c.setDescription(desc);
        return c;
    }

    // 3. Tạo Admin và 20 User giả
    private void seedUsers() {
        // Tạo Admin
        User admin = new User();
        admin.setUserName("admin");
        admin.setPassword(HashUtil.hashPassword("123456"));
        admin.setFullName("Quản Trị Viên");
        admin.setRole("ADMIN");
        admin.setEmail("admin@badminton.com");
        admin.setStatus("ACTIVE");
        userRepo.save(admin);

        // Tạo 20 User ngẫu nhiên
        List<User> users = new ArrayList<>();
        String[] lastNames = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ", "Võ", "Đặng"};
        String[] firstNames = {"An", "Bình", "Cường", "Dũng", "Giang", "Hải", "Hùng", "Khánh", "Lan", "Minh"};

        Random rand = new Random();
        for (int i = 1; i <= 20; i++) {
            User u = new User();
            String lName = lastNames[rand.nextInt(lastNames.length)];
            String fName = firstNames[rand.nextInt(firstNames.length)];
            
            u.setUserName("user" + i);
            u.setPassword(HashUtil.hashPassword("123456")); // Mật khẩu chung
            u.setFullName(lName + " " + fName);
            u.setRole("USER");
            u.setEmail("user" + i + "@gmail.com");
            u.setPhone("09" + (10000000 + i));
            u.setStatus("ACTIVE");
            users.add(u);
        }
        userRepo.saveAll(users);
    }

    // 4. Tạo 50 Hóa đơn giả (Quan trọng để test thống kê)
    private void seedFakeBookings() {
        List<User> users = userRepo.findAll();
        List<Court> courts = courtRepo.findAll();
        List<Slot> allSlots = slotRepo.findAll();
        
        // Chỉ lấy user thường, bỏ admin ra khỏi danh sách đặt sân cho thực tế
        List<User> customers = users.stream().filter(u -> "USER".equals(u.getRole())).toList();

        Random rand = new Random();
        
        // Tạo 50 đơn hàng
        for (int i = 0; i < 50; i++) {
            // Random ngày: từ 7 ngày trước đến 3 ngày sau
            LocalDate date = LocalDate.now().minusDays(rand.nextInt(7)).plusDays(rand.nextInt(3));
            
            User randomUser = customers.get(rand.nextInt(customers.size()));
            Court randomCourt = courts.get(rand.nextInt(courts.size()));
            
            // Random số lượng slot (1 đến 3 tiếng)
            int numberOfSlots = rand.nextInt(3) + 1; 
            int startSlotIndex = rand.nextInt(allSlots.size() - numberOfSlots); 

            Bill bill = new Bill();
            bill.setUser(randomUser);
            
            List<SlotBooked> details = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (int j = 0; j < numberOfSlots; j++) {
                Slot slot = allSlots.get(startSlotIndex + j);

                // Check sơ bộ xem đã có ai đặt chưa (trong logic seed đơn giản này có thể bỏ qua, 
                // nhưng check để tránh lỗi constraint unique nếu có)
                boolean isBooked = !slotBookedRepo.findByBookingDateAndCourtId(date, randomCourt.getId()).isEmpty();
                // Nếu slot này giờ này đã full rồi thì bỏ qua lượt này (để đơn giản)
                // Tuy nhiên đây là seed nên mình cứ liều tạo, nếu trùng database sẽ báo lỗi dòng sau,
                // Để an toàn mình dùng try-catch cho từng bill
            }

            try {
                 // Logic tạo thật
                for (int j = 0; j < numberOfSlots; j++) {
                    Slot slot = allSlots.get(startSlotIndex + j);
                    
                    SlotBooked sb = new SlotBooked();
                    sb.setBill(bill);
                    sb.setCourt(randomCourt);
                    sb.setSlot(slot);
                    sb.setBookingDate(date);
                    sb.setPrice(slot.getTypeSlots().getPrice());
                    LocalDateTime createdDateTime = date.atTime(
                        rand.nextInt(24),           // Random giờ 0-23
                        rand.nextInt(60),           // Random phút 0-59
                        rand.nextInt(60)            // Random giây 0-59
                    );
                    bill.setCreatedAt(createdDateTime);
                    details.add(sb);
                    total = total.add(sb.getPrice());
                }

                bill.setTotalAmount(total);
                bill.setSlotBookedList(details);
                
                // Lưu bill (Cascade sẽ lưu luôn slotBooked)
                billRepo.save(bill);
                
            } catch (Exception e) {
                // Nếu random trúng slot đã đặt thì bỏ qua, không sao cả
                System.out.println("Skip trùng slot: " + e.getMessage());
            }
        }
    }
}