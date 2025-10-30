import React, { useState } from "react";
import { useApp } from "../../context/AppContext";
import { Menu, X } from "lucide-react";

// Layout
import AdminSidebar from "../../components/layout/AdminSideBar";

// Views
import AdminOverview from "./view/AdminOverview";
import AdminCourts from "./view/AdminCourts";
import AdminPricing from "./view/AdminPricing";
import AdminCustomers from "./view/AdminCustomers";
import AdminComments from "./view/AdminComments";

const AdminDashboard = () => {
  const { currentUser } = useApp();
  const [adminPage, setAdminPage] = useState("overview"); // overview, courts, pricing, revenue, customers, comments
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);

  // Kiểm tra quyền
  if (!currentUser || currentUser.role !== "admin") {
    return (
      <div className="text-center py-20">
        <h1 className="text-3xl font-bold text-red-600">Truy cập bị từ chối</h1>
        <p className="mt-4">Bạn không có quyền truy cập trang này.</p>
      </div>
    );
  }

  const renderAdminPage = () => {
    switch (adminPage) {
      case "overview":
        return <AdminOverview />;
      case "courts":
        return <AdminCourts />;
      case "pricing":
        return <AdminPricing />;
      case "revenue":
        // Bạn có thể tạo component AdminRevenue tương tự
        return <AdminOverview />; // Đang dùng tạm AdminOverview
      case "customers":
        return <AdminCustomers />;
      case "comments":
        return <AdminComments />;
      default:
        return <AdminOverview />;
    }
  };

  return (
    <div className="flex h-screen bg-gray-100">
      <AdminSidebar
        isOpen={isSidebarOpen}
        adminPage={adminPage}
        setAdminPage={setAdminPage}
      />
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Top Header */}
        <header className="bg-white shadow-sm border-b border-gray-200">
          <div className="flex items-center justify-between h-16 px-6">
            <button
              onClick={() => setIsSidebarOpen(!isSidebarOpen)}
              className="text-gray-600"
            >
              {isSidebarOpen ? (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                >
                  <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
                  <line x1="9" y1="3" x2="9" y2="21"></line>
                </svg>
              ) : (
                <Menu />
              )}
            </button>
            <div className="flex items-center space-x-4">
              <span className="text-gray-700">
                Chào, {currentUser.fullName}
              </span>
            </div>
          </div>
        </header>

        {/* Main Content */}
        <main className="flex-1 overflow-x-hidden overflow-y-auto bg-gray-100">
          {/* UI Tweak: Thêm max-w-7xl để giữ nội dung không quá rộng trên laptop */}
          <div className="max-w-7xl mx-auto p-6">{renderAdminPage()}</div>
        </main>
      </div>
    </div>
  );
};

export default AdminDashboard;
