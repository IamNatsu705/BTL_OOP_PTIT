import React from "react";
import { useApp } from "../../context/AppContext";
import {
  BarChart2,
  Users,
  MessageSquare,
  Settings,
  LogOut,
  Tag,
  CreditCard,
  ShieldCheck,
} from "lucide-react";

const AdminSidebarLink = ({
  page,
  icon,
  text,
  isOpen,
  adminPage,
  setAdminPage,
}) => (
  <button
    onClick={() => setAdminPage(page)}
    className={`flex items-center w-full px-4 py-2 rounded-lg transition-colors ${
      adminPage === page
        ? "bg-blue-600 text-white"
        : "text-gray-300 hover:bg-gray-700 hover:text-white"
    } ${!isOpen && "justify-center"}`}
  >
    {icon}
    {isOpen && <span className="ml-3">{text}</span>}
  </button>
);

const AdminSidebar = ({ isOpen, adminPage, setAdminPage }) => {
  const { logout } = useApp();

  return (
    <aside
      className={`bg-gray-800 text-gray-300 ${
        isOpen ? "w-64" : "w-20"
      } transition-all duration-300 flex flex-col flex-shrink-0`}
    >
      <div className="flex items-center justify-center h-16 border-b border-gray-700">
        {isOpen ? (
          <span className="text-xl font-bold text-white">Quản Trị</span>
        ) : (
          <ShieldCheck className="w-6 h-6 text-white" />
        )}
      </div>
      <nav className="flex-1 px-4 py-6 space-y-2">
        <AdminSidebarLink
          page="overview"
          icon={<BarChart2 />}
          text="Tổng quan"
          isOpen={isOpen}
          adminPage={adminPage}
          setAdminPage={setAdminPage}
        />
        <AdminSidebarLink
          page="courts"
          icon={<Settings />}
          text="Quản lý Sân"
          isOpen={isOpen}
          adminPage={adminPage}
          setAdminPage={setAdminPage}
        />
        <AdminSidebarLink
          page="pricing"
          icon={<Tag />}
          text="Quản lý Giá"
          isOpen={isOpen}
          adminPage={adminPage}
          setAdminPage={setAdminPage}
        />
        <AdminSidebarLink
          page="revenue"
          icon={<CreditCard />}
          text="Doanh thu"
          isOpen={isOpen}
          adminPage={adminPage}
          setAdminPage={setAdminPage}
        />
        <AdminSidebarLink
          page="customers"
          icon={<Users />}
          text="Khách hàng"
          isOpen={isOpen}
          adminPage={adminPage}
          setAdminPage={setAdminPage}
        />
        <AdminSidebarLink
          page="comments"
          icon={<MessageSquare />}
          text="Bình luận"
          isOpen={isOpen}
          adminPage={adminPage}
          setAdminPage={setAdminPage}
        />
      </nav>
      <div className="px-4 py-4 border-t border-gray-700">
        <button
          onClick={logout}
          className={`flex items-center w-full px-4 py-2 rounded-lg text-gray-300 hover:bg-gray-700 hover:text-white ${
            !isOpen && "justify-center"
          }`}
        >
          <LogOut className="w-5 h-5" />
          {isOpen && <span className="ml-3">Đăng xuất</span>}
        </button>
      </div>
    </aside>
  );
};

export default AdminSidebar;
