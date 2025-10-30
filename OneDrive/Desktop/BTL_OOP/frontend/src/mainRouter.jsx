import React from "react";
import { useApp } from "./context/AppContext";

// Layout
import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";

// Pages
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import BookingPage from "./pages/BookingPage";
import AdminDashboard from "./pages/admin/AdminDashboard";

/**
 * MainRouter (Component chính điều hướng trang)
 */
const MainRouter = () => {
  const { currentPage } = useApp();

  const renderPage = () => {
    // Nếu đang ở trang admin, chỉ render AdminDashboard
    if (currentPage === "admin") {
      return <AdminDashboard />;
    }

    // Các trang khác render bình thường với Header và Footer
    switch (currentPage) {
      case "home":
        return <HomePage />;
      case "login":
        return <LoginPage />;
      case "register":
        return <RegisterPage />;
      case "booking":
        return <BookingPage />;
      // Bạn có thể thêm trang 'contact' ở đây
      default:
        return <HomePage />;
    }
  };

  // Không hiển thị Header/Footer cho trang Admin
  if (currentPage === "admin") {
    return renderPage();
  }

  return (
    <div className="flex flex-col min-h-screen w-full">
      <Header />
      <main className="flex-1 bg-gray-50">{renderPage()}</main>
      <Footer />
    </div>
  );
};

export default MainRouter;
