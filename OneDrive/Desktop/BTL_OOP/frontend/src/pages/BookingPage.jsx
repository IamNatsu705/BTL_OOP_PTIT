import React, { useState } from "react";
import { useApp } from "../context/AppContext";
import { mockCourts, mockBookings, mockTimeSlots } from "../data/mockData";

const BookingPage = () => {
  const { isAuthenticated, setCurrentPage, showNotification } = useApp();
  const [selectedDate, setSelectedDate] = useState(
    new Date().toISOString().split("T")[0]
  );
  const [selectedCourtId, setSelectedCourtId] = useState(mockCourts[0].id);
  const [selectedTimeSlots, setSelectedTimeSlots] = useState([]); // mảng các giờ bắt đầu, vd: [8, 9, 17]

  // Lấy danh sách giờ hoạt động (5h - 23h)
  const operatingHours = Array.from({ length: 23 - 5 }, (_, i) => i + 5);

  // Giả lập kiểm tra lịch đã đặt
  const getBookedSlots = (date, courtId) => {
    const booked = mockBookings
      .filter(
        (booking) =>
          booking.courtId === courtId && booking.startTime.startsWith(date)
      )
      .map((booking) => {
        const startHour = new Date(booking.startTime).getHours();
        const endHour = new Date(booking.endTime).getHours();
        const hours = [];
        for (let h = startHour; h < endHour; h++) {
          hours.push(h);
        }
        return hours;
      })
      .flat();

    return booked;
  };

  const bookedSlots = getBookedSlots(selectedDate, selectedCourtId);

  const getPriceForHour = (hour) => {
    const date = new Date(selectedDate);
    const dayOfWeek = date.getDay(); // 0 = CN, 1 = T2, ..., 6 = T7

    let priceSlot;
    if (dayOfWeek === 0 || dayOfWeek === 6) {
      // T7, CN
      priceSlot = mockTimeSlots.find((s) => s.days_apply === "weekend");
    } else {
      // T2-T6
      if (hour >= 5 && hour < 16) {
        priceSlot = mockTimeSlots.find(
          (s) => s.days_apply === "weekday_morning"
        );
      } else {
        priceSlot = mockTimeSlots.find(
          (s) => s.days_apply === "weekday_evening"
        );
      }
    }
    return priceSlot ? priceSlot.price_per_hour : 0;
  };

  const handleTimeSlotClick = (hour) => {
    if (bookedSlots.includes(hour)) return; // Không cho chọn giờ đã đặt

    setSelectedTimeSlots((prev) => {
      if (prev.includes(hour)) {
        return prev.filter((h) => h !== hour); // Bỏ chọn
      } else {
        return [...prev, hour].sort((a, b) => a - b); // Thêm vào
      }
    });
  };

  // Kiểm tra các slot có liên tục không
  const isContinuous = (slots) => {
    for (let i = 0; i < slots.length - 1; i++) {
      if (slots[i + 1] - slots[i] !== 1) return false;
    }
    return true;
  };

  const calculateTotal = () => {
    if (!isContinuous(selectedTimeSlots))
      return { total: 0, error: "Vui lòng chọn các khung giờ liên tục." };

    let total = 0;
    selectedTimeSlots.forEach((hour) => {
      total += getPriceForHour(hour);
    });

    return { total, error: null };
  };

  const { total, error } = calculateTotal();

  const handleBooking = () => {
    if (!isAuthenticated) {
      showNotification("Vui lòng đăng nhập để đặt sân!", "error");
      setCurrentPage("login");
      return;
    }

    if (selectedTimeSlots.length === 0) {
      showNotification("Vui lòng chọn ít nhất 1 khung giờ!", "error");
      return;
    }

    if (error) {
      showNotification(error, "error");
      return;
    }

    // Giả lập đặt sân
    console.log({
      date: selectedDate,
      courtId: selectedCourtId,
      slots: selectedTimeSlots,
      total: total,
    });

    showNotification(
      `Đặt sân thành công! Tổng tiền: ${total.toLocaleString("vi-VN")} VNĐ`,
      "success"
    );
    setSelectedTimeSlots([]);
    // (Trong ứng dụng thật, bạn sẽ thêm vào mockBookings và gọi API)
  };

  const selectedCourt = mockCourts.find((c) => c.id === selectedCourtId);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 animate-fade-in">
      <h1 className="text-4xl font-bold text-gray-800 mb-10 text-center">
        Đặt Sân Cầu Lông
      </h1>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-10">
        {/* Cột chọn sân và ngày */}
        <div className="lg:col-span-1 space-y-6">
          <div className="bg-white p-6 rounded-lg shadow-lg">
            <h3 className="text-xl font-semibold mb-4 text-gray-700">
              1. Chọn ngày
            </h3>
            <input
              type="date"
              value={selectedDate}
              onChange={(e) => setSelectedDate(e.target.value)}
              min={new Date().toISOString().split("T")[0]}
              className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="bg-white p-6 rounded-lg shadow-lg">
            <h3 className="text-xl font-semibold mb-4 text-gray-700">
              2. Chọn sân
            </h3>
            <div className="space-y-3 max-h-96 overflow-y-auto pr-2">
              {mockCourts.map((court) => (
                <button
                  key={court.id}
                  onClick={() => {
                    if (court.status !== "maintenance") {
                      setSelectedCourtId(court.id);
                    }
                  }}
                  disabled={court.status === "maintenance"}
                  className={`w-full text-left p-4 rounded-lg border-2 transition-all ${
                    selectedCourtId === court.id
                      ? "border-blue-500 bg-blue-50 ring-2 ring-blue-300"
                      : "border-gray-200 hover:border-blue-400"
                  } ${
                    court.status === "maintenance"
                      ? "bg-gray-100 cursor-not-allowed opacity-60"
                      : "cursor-pointer"
                  }`}
                >
                  <div className="flex justify-between items-center">
                    <span className="font-semibold">{court.name}</span>
                    {court.status === "maintenance" && (
                      <span className="text-xs font-medium text-red-600">
                        Bảo trì
                      </span>
                    )}
                  </div>
                  <p className="text-sm text-gray-500 mt-1">
                    {court.description}
                  </p>
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* Cột chọn giờ */}
        <div className="lg:col-span-2 bg-white p-8 rounded-lg shadow-lg">
          <h3 className="text-xl font-semibold mb-4 text-gray-700">
            3. Chọn giờ (Sân: {selectedCourt.name})
          </h3>
          <div className="mb-4 flex flex-wrap gap-4 text-sm">
            <span className="flex items-center">
              <span className="w-4 h-4 bg-gray-200 border rounded mr-2"></span>
              Trống
            </span>
            <span className="flex items-center">
              <span className="w-4 h-4 bg-blue-500 border border-blue-600 rounded mr-2"></span>
              Đang chọn
            </span>
            <span className="flex items-center">
              <span className="w-4 h-4 bg-red-300 border border-red-400 rounded line-through mr-2"></span>
              Đã đặt
            </span>
            <span className="flex items-center">
              <span className="w-4 h-4 bg-gray-100 border rounded mr-2"></span>
              Ngoài giờ
            </span>
          </div>
          <div className="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-5 lg:grid-cols-6 gap-3">
            {operatingHours.map((hour) => {
              const isBooked = bookedSlots.includes(hour);
              const isSelected = selectedTimeSlots.includes(hour);
              const price = getPriceForHour(hour);

              return (
                <button
                  key={hour}
                  disabled={isBooked}
                  onClick={() => handleTimeSlotClick(hour)}
                  className={`p-3 rounded-lg border text-center transition-all ${
                    isBooked
                      ? "bg-red-300 border-red-400 text-gray-500 line-through cursor-not-allowed"
                      : isSelected
                      ? "bg-blue-500 border-blue-600 text-white font-semibold ring-2 ring-blue-400"
                      : "bg-gray-50 border-gray-200 hover:bg-blue-50 hover:border-blue-400"
                  }`}
                >
                  <div className="font-semibold">{`${hour}:00`}</div>
                  <div className="text-xs mt-1">
                    {price.toLocaleString("vi-VN")}đ
                  </div>
                </button>
              );
            })}
            <div className="p-3 rounded-lg text-center bg-gray-100 text-gray-400">
              <div className="font-semibold">23:00</div>
              <div className="text-xs mt-1">Đóng cửa</div>
            </div>
          </div>

          {/* Cột thanh toán */}
          <div className="mt-8 border-t pt-6">
            <h3 className="text-xl font-semibold mb-4 text-gray-700">
              4. Xác nhận
            </h3>
            {/* UI Tweak: Bọc phần xác nhận trong box nền xám */}
            <div className="bg-gray-50 p-6 rounded-lg">
              {selectedTimeSlots.length > 0 ? (
                <>
                  <p className="text-gray-600">
                    Sân:{" "}
                    <span className="font-semibold">{selectedCourt.name}</span>
                  </p>
                  <p className="text-gray-600">
                    Ngày:{" "}
                    <span className="font-semibold">
                      {new Date(selectedDate).toLocaleDateString("vi-VN")}
                    </span>
                  </p>
                  <p className="text-gray-600">
                    Giờ:{" "}
                    <span className="font-semibold">
                      {selectedTimeSlots.map((h) => `${h}:00`).join(", ")}
                    </span>
                  </p>
                  {error && <p className="text-red-500 mt-2">{error}</p>}
                  <p className="text-2xl font-bold text-gray-800 mt-4">
                    Tổng cộng:{" "}
                    <span className="text-blue-600">
                      {total.toLocaleString("vi-VN")} VNĐ
                    </span>
                  </p>
                </>
              ) : (
                <p className="text-gray-500">
                  Vui lòng chọn khung giờ để xem tổng tiền.
                </p>
              )}
              <button
                onClick={handleBooking}
                disabled={selectedTimeSlots.length === 0 || !!error}
                className="w-full mt-6 bg-green-600 text-white py-3 px-4 rounded-lg text-lg font-semibold hover:bg-green-700 transition-colors shadow-md disabled:bg-gray-400 disabled:cursor-not-allowed"
              >
                Xác Nhận Đặt Sân
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BookingPage;
