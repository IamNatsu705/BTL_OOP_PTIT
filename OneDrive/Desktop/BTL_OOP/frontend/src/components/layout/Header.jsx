import React, { useState } from "react";
import { useApp } from "../../context/AppContext";
import { Menu, X, ChevronDown } from "lucide-react";

const Header = () => {
  const { isAuthenticated, currentUser, logout, setCurrentPage } = useApp();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const NavLink = ({ page, children }) => (
    <button
      onClick={() => {
        setCurrentPage(page);
        setIsMobileMenuOpen(false);
      }}
      className="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium transition-colors"
    >
      {children}
    </button>
  );

  const MobileNavLink = ({ page, children }) => (
    <button
      onClick={() => {
        setCurrentPage(page);
        setIsMobileMenuOpen(false);
      }}
      className="text-gray-600 hover:bg-gray-100 block px-3 py-2 rounded-md text-base font-medium transition-colors"
    >
      {children}
    </button>
  );

  return (
    <nav className="bg-white shadow-md sticky top-0 z-40">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo/Brand */}
          <div className="flex-shrink-0">
            <button
              onClick={() => setCurrentPage("home")}
              className="flex items-center space-x-2"
            >
              <img
                className="h-8 w-auto"
                src="https://img.icons8.com/color/48/badminton-2.png"
                alt="Logo Sân cầu lông"
              />
              <span className="text-xl font-bold text-gray-800">
                Badminton Court
              </span>
            </button>
          </div>

          {/* Desktop Nav Links */}
          <div className="hidden md:flex md:items-center md:space-x-4">
            <NavLink page="home">Trang chủ</NavLink>
            <NavLink page="booking">Đặt sân</NavLink>
            <NavLink page="contact">Liên hệ</NavLink>

            {isAuthenticated ? (
              <div className="relative group">
                <button className="flex items-center text-gray-600 hover:text-blue-600 px-3 py-2">
                  Chào, {currentUser.fullName.split(" ")[0]}{" "}
                  <ChevronDown className="w-4 h-4 ml-1" />
                </button>
                {/* Dropdown (Sửa lại để hiển thị khi hover) */}
                <div className="opacity-0 group-hover:opacity-100 invisible group-hover:visible absolute right-0 mt-0 w-48 bg-white rounded-md shadow-lg py-1 z-50 transition-all duration-200">
                  {currentUser.role === "admin" && (
                    <button
                      onClick={() => setCurrentPage("admin")}
                      className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                    >
                      Trang Quản Trị
                    </button>
                  )}
                  <button
                    onClick={() => {
                      /* Xử lý trang cá nhân */
                    }}
                    className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                  >
                    Thông tin cá nhân
                  </button>
                  <button
                    onClick={logout}
                    className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                  >
                    Đăng xuất
                  </button>
                </div>
              </div>
            ) : (
              <>
                <button
                  onClick={() => setCurrentPage("login")}
                  className="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium transition-colors"
                >
                  Đăng nhập
                </button>
                <button
                  onClick={() => setCurrentPage("register")}
                  className="bg-blue-600 text-white px-4 py-2 rounded-md text-sm font-medium hover:bg-blue-700 transition-colors"
                >
                  Đăng ký
                </button>
              </>
            )}
          </div>

          {/* Mobile Menu Button */}
          <div className="md:hidden flex items-center">
            <button
              onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
              className="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-blue-500"
            >
              {isMobileMenuOpen ? (
                <X className="h-6 w-6" />
              ) : (
                <Menu className="h-6 w-6" />
              )}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile Menu (Dropdown) */}
      {isMobileMenuOpen && (
        <div className="md:hidden border-t border-gray-200">
          <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
            <MobileNavLink page="home">Trang chủ</MobileNavLink>
            <MobileNavLink page="booking">Đặt sân</MobileNavLink>
            <MobileNavLink page="contact">Liên hệ</MobileNavLink>

            {isAuthenticated ? (
              <div className="border-t border-gray-100 pt-4 pb-3">
                <div className="flex items-center px-5">
                  <div className="ml-3">
                    <div className="text-base font-medium text-gray-800">
                      {currentUser.fullName}
                    </div>
                    <div className="text-sm font-medium text-gray-500">
                      {currentUser.email}
                    </div>
                  </div>
                </div>
                <div className="mt-3 px-2 space-y-1">
                  {currentUser.role === "admin" && (
                    <MobileNavLink page="admin">Trang Quản Trị</MobileNavLink>
                  )}
                  <button
                    onClick={() => {
                      /* Trang cá nhân */ setIsMobileMenuOpen(false);
                    }}
                    className="block px-3 py-2 rounded-md text-base font-medium text-gray-500 hover:text-gray-800 hover:bg-gray-100"
                  >
                    Thông tin cá nhân
                  </button>
                  <button
                    onClick={logout}
                    className="block px-3 py-2 rounded-md text-base font-medium text-gray-500 hover:text-gray-800 hover:bg-gray-100"
                  >
                    Đăng xuất
                  </button>
                </div>
              </div>
            ) : (
              <div className="border-t border-gray-100 pt-3 space-y-2 px-2">
                <button
                  onClick={() => {
                    setCurrentPage("login");
                    setIsMobileMenuOpen(false);
                  }}
                  className="w-full text-left block px-3 py-2 rounded-md text-base font-medium text-gray-600 hover:bg-gray-100"
                >
                  Đăng nhập
                </button>
                <button
                  onClick={() => {
                    setCurrentPage("register");
                    setIsMobileMenuOpen(false);
                  }}
                  className="w-full bg-blue-600 text-white block px-3 py-2 rounded-md text-base font-medium hover:bg-blue-700"
                >
                  Đăng ký
                </button>
              </div>
            )}
          </div>
        </div>
      )}
    </nav>
  );
};

export default Header;
