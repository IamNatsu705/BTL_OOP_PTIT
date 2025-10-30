import React from "react";
import { useApp } from "../context/AppContext";
import { ShieldCheck, Tag, Calendar } from "lucide-react";
import { mockCourts } from "../data/mockData";

const HomePage = () => {
  const { setCurrentPage } = useApp();

  return (
    <div className="animate-fade-in">
      {/* Hero Section */}
      <div className="relative h-[70vh] min-h-[500px] bg-gray-800">
        <img
          src="https://placehold.co/1600x900/334155/e2e8f0?text=Sân+Cầu+Lông+Chuyên+Nghiệp"
          alt="Sân cầu lông"
          className="absolute inset-0 w-full h-full object-cover opacity-50"
          onError={(e) => {
            e.target.src =
              "https://placehold.co/1600x900/334155/e2e8f0?text=Lỗi+Tải+Ảnh";
          }}
        />
        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-full flex flex-col justify-center items-center text-center">
          <h1 className="text-4xl md:text-6xl font-extrabold text-white text-shadow-lg">
            Trải Nghiệm Sân Cầu Lông
          </h1>
          <h2 className="text-5xl font-extrabold text-blue-300 text-shadow-lg mt-2">
            Đẳng Cấp
          </h2>
          <p className="mt-6 text-lg text-gray-200 max-w-2xl">
            Sàn thảm tiêu chuẩn quốc tế, ánh sáng chống lóa, không gian thoáng
            đãng.
          </p>
          <button
            onClick={() => setCurrentPage("booking")}
            className="mt-10 bg-blue-600 text-white px-8 py-3 rounded-full text-lg font-semibold hover:bg-blue-700 transition-transform hover:scale-105 shadow-lg"
          >
            Đặt Sân Ngay
          </button>
        </div>
      </div>

      {/* Features Section */}
      <div className="bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
          <h3 className="text-3xl font-bold text-center text-gray-800 mb-12">
            Tại sao chọn chúng tôi?
          </h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="bg-white p-8 rounded-lg shadow-md text-center">
              <ShieldCheck className="w-12 h-12 text-blue-600 mx-auto mb-4" />
              <h4 className="text-xl font-semibold mb-2">Chất Lượng Sân</h4>
              <p className="text-gray-600">
                6 sân đạt chuẩn, bảo trì thường xuyên, đảm bảo trải nghiệm tốt
                nhất.
              </p>
            </div>
            <div className="bg-white p-8 rounded-lg shadow-md text-center">
              <Tag className="w-12 h-12 text-blue-600 mx-auto mb-4" />
              <h4 className="text-xl font-semibold mb-2">Giá Cả Hợp Lý</h4>
              <p className="text-gray-600">
                Nhiều khung giờ với mức giá linh hoạt, phù hợp cho mọi đối
                tượng.
              </p>
            </div>
            <div className="bg-white p-8 rounded-lg shadow-md text-center">
              <Calendar className="w-12 h-12 text-blue-600 mx-auto mb-4" />
              <h4 className="text-xl font-semibold mb-2">Đặt Sân Dễ Dàng</h4>
              <p className="text-gray-600">
                Hệ thống đặt sân online nhanh chóng, tiện lợi, mọi lúc mọi nơi.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Our Courts Section */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <h3 className="text-3xl font-bold text-center text-gray-800 mb-12">
          Các Sân Của Chúng Tôi
        </h3>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {mockCourts.slice(0, 3).map(
            (
              court // Chỉ hiển thị 3 sân làm mẫu
            ) => (
              <div
                key={court.id}
                className="bg-white rounded-lg shadow-lg overflow-hidden transition-transform hover:scale-105"
              >
                <img
                  src={`https://placehold.co/600x400/5e60e6/ffffff?text=${encodeURIComponent(
                    court.name
                  )}`}
                  alt={court.name}
                  className="w-full h-48 object-cover"
                />
                <div className="p-6">
                  <h4 className="text-xl font-semibold mb-2">{court.name}</h4>
                  <p className="text-gray-600 text-sm mb-4">
                    {court.description}
                  </p>
                  <span
                    className={`inline-block px-3 py-1 rounded-full text-sm font-semibold ${
                      court.status === "available"
                        ? "bg-green-100 text-green-800"
                        : "bg-yellow-100 text-yellow-800"
                    }`}
                  >
                    {court.status === "available" ? "Sẵn sàng" : "Đang bảo trì"}
                  </span>
                </div>
              </div>
            )
          )}
        </div>
        <div className="text-center mt-12">
          <button
            onClick={() => setCurrentPage("booking")}
            className="bg-gray-700 text-white px-6 py-2 rounded-lg text-md font-semibold hover:bg-gray-800 transition-colors"
          >
            Xem tất cả 6 sân
          </button>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
