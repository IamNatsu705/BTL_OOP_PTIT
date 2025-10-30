// --- MOCK DATA (Dữ liệu giả) ---
// Thay thế bằng API của bạn
// ---------------------------------

export const mockUsers = [
  {
    id: 1,
    username: "admin",
    password: "123",
    fullName: "Quản Trị Viên",
    email: "admin@sancaulong.com",
    phone: "0901234567",
    role: "admin",
  },
  {
    id: 2,
    username: "khachhang",
    password: "123",
    fullName: "Nguyễn Văn A",
    email: "khachhang@gmail.com",
    phone: "0987654321",
    role: "customer",
  },
  {
    id: 3,
    username: "vantrang",
    password: "123",
    fullName: "Trần Thị B",
    email: "vantrang@gmail.com",
    phone: "0912345678",
    role: "customer",
  },
];

export const mockCourts = [
  {
    id: 1,
    name: "Sân 1 (Sàn gỗ)",
    status: "available",
    description: "Sân tiêu chuẩn thi đấu, sàn gỗ nhập khẩu.",
  },
  {
    id: 2,
    name: "Sân 2 (Sàn thảm)",
    status: "available",
    description: "Sân thảm đỏ, êm ái, giảm chấn thương.",
  },
  {
    id: 3,
    name: "Sân 3 (Sàn thảm)",
    status: "maintenance",
    description: "Sân đang bảo trì, vui lòng quay lại sau.",
  },
  {
    id: 4,
    name: "Sân 4 (VIP)",
    status: "available",
    description: "Sân VIP, ánh sáng tốt nhất, có khu chờ riêng.",
  },
  {
    id: 5,
    name: "Sân 5 (Ngoài trời)",
    status: "available",
    description: "Sân ngoài trời, trải nghiệm không khí thoáng đãng.",
  },
  {
    id: 6,
    name: "Sân 6 (Sàn thảm)",
    status: "available",
    description: "Sân thảm xanh, mới đưa vào hoạt động.",
  },
];

export const mockTimeSlots = [
  {
    id: 1,
    slot_name: "Giờ thấp điểm (Sáng T2-T6)",
    start_time: "05:00:00",
    end_time: "16:00:00",
    price_per_hour: 100000,
    days_apply: "weekday_morning",
  },
  {
    id: 2,
    slot_name: "Giờ cao điểm (Tối T2-T6)",
    start_time: "16:00:00",
    end_time: "23:00:00",
    price_per_hour: 150000,
    days_apply: "weekday_evening",
  },
  {
    id: 3,
    slot_name: "Cuối tuần (T7-CN)",
    start_time: "05:00:00",
    end_time: "23:00:00",
    price_per_hour: 180000,
    days_apply: "weekend",
  },
];

export const mockBookings = [
  {
    id: 1,
    userId: 2,
    courtId: 1,
    startTime: "2025-11-10T17:00:00",
    endTime: "2025-11-10T18:00:00",
    totalPrice: 150000,
    status: "completed",
  },
  {
    id: 2,
    userId: 3,
    courtId: 4,
    startTime: "2025-11-10T18:00:00",
    endTime: "2025-11-10T20:00:00",
    totalPrice: 300000,
    status: "confirmed",
  },
  {
    id: 3,
    userId: 2,
    courtId: 2,
    startTime: "2025-11-11T08:00:00",
    endTime: "2025-11-11T09:00:00",
    totalPrice: 100000,
    status: "confirmed",
  },
];

export const mockComments = [
  {
    id: 1,
    bookingId: 1,
    userId: 2,
    userFullName: "Nguyễn Văn A",
    rating: 5,
    comment_text: "Sân 1 sàn gỗ chơi rất thích, ánh sáng tốt. Sẽ quay lại!",
    created_at: "2025-11-10T18:30:00",
  },
  {
    id: 2,
    bookingId: 2,
    userId: 3,
    userFullName: "Trần Thị B",
    rating: 4,
    comment_text: "Sân VIP xịn nhưng giá hơi cao. Phục vụ tốt.",
    created_at: "2025-11-10T20:05:00",
  },
];

export const mockRevenueData = [
  { name: "T6", "Doanh thu": 4000000 },
  { name: "T7", "Doanh thu": 7500000 },
  { name: "CN", "Doanh thu": 9000000 },
  { name: "T2", "Doanh thu": 3200000 },
  { name: "T3", "Doanh thu": 3800000 },
  { name: "T4", "Doanh thu": 4100000 },
  { name: "T5", "Doanh thu": 4500000 },
];
