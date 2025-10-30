import React, { useState, createContext, useContext } from "react";
import { mockUsers } from "../data/mockData";
import Notification from "../components/ui/Notification";

const AppContext = createContext();

export const useApp = () => useContext(AppContext);

export const AppProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);
  const [currentPage, setCurrentPage] = useState("home"); // 'home', 'login', 'register', 'booking', 'admin'
  const [notification, setNotification] = useState({
    show: false,
    message: "",
    type: "success",
  });

  // Hiển thị thông báo
  const showNotification = (message, type = "success", duration = 3000) => {
    setNotification({ show: true, message, type });
    setTimeout(() => {
      setNotification({ show: false, message: "", type });
    }, duration);
  };

  // Hàm đăng nhập (giả lập)
  const login = (username, password) => {
    const user = mockUsers.find(
      (u) => u.username === username && u.password === password
    );
    if (user) {
      setIsAuthenticated(true);
      setCurrentUser(user);
      showNotification("Đăng nhập thành công!", "success");
      if (user.role === "admin") {
        setCurrentPage("admin");
      } else {
        setCurrentPage("home");
      }
      return true;
    }
    showNotification("Tên đăng nhập hoặc mật khẩu không đúng!", "error");
    return false;
  };

  // Hàm đăng xuất
  const logout = () => {
    setIsAuthenticated(false);
    setCurrentUser(null);
    setCurrentPage("home");
    showNotification("Đã đăng xuất.", "info");
  };

  // Hàm đăng ký (giả lập)
  const register = (fullName, username, email, phone, password) => {
    // Giả lập kiểm tra trùng lặp
    if (mockUsers.find((u) => u.username === username || u.email === email)) {
      showNotification("Tên đăng nhập hoặc email đã tồn tại!", "error");
      return false;
    }
    // Giả lập thêm user mới
    const newUser = {
      id: mockUsers.length + 1,
      username,
      password,
      fullName,
      email,
      phone,
      role: "customer",
    };
    mockUsers.push(newUser); // (Lưu ý: Chỉ là tạm thời, sẽ reset khi tải lại trang)
    showNotification("Đăng ký thành công! Vui lòng đăng nhập.", "success");
    setCurrentPage("login");
    return true;
  };

  return (
    <AppContext.Provider
      value={{
        isAuthenticated,
        currentUser,
        currentPage,
        login,
        logout,
        register,
        setCurrentPage,
        showNotification,
        notification, // Truyền notification xuống cho Component Notification
      }}
    >
      {children}
      {/* Component thông báo toàn cục */}
      <Notification />
    </AppContext.Provider>
  );
};
