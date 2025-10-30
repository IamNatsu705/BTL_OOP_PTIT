import React, { useState } from "react";
import { useApp } from "../context/AppContext";
import { User, Lock } from "lucide-react";
import InputWithIcon from "../components/ui/InputWithIcon";

const LoginPage = () => {
  const { login, setCurrentPage } = useApp();
  const [username, setUsername] = useState("admin"); // Thêm giá trị mặc định để test
  const [password, setPassword] = useState("123"); // Thêm giá trị mặc định để test
  const [error, setError] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");
    if (!login(username, password)) {
      setError("Tên đăng nhập hoặc mật khẩu không đúng.");
    }
  };

  return (
    <div className="flex justify-center items-center py-20 md:py-32 bg-gray-50">
      {/* Tăng max-w và padding */}
      <div className="w-full max-w-lg bg-white rounded-lg shadow-xl p-10 animate-fade-in-up">
        <h2 className="text-3xl font-bold text-center text-gray-800 mb-8">
          Đăng Nhập
        </h2>
        {error && <p className="text-red-500 text-center mb-4">{error}</p>}
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Tên đăng nhập
            </label>
            {/* Sử dụng component InputWithIcon */}
            <InputWithIcon
              icon={User}
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              placeholder="vd: admin"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Mật khẩu
            </label>
            {/* Sử dụng component InputWithIcon */}
            <InputWithIcon
              icon={Lock}
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              placeholder="vd: 123"
            />
          </div>
          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-3 px-4 rounded-lg font-semibold hover:bg-blue-700 transition-colors shadow-md"
          >
            Đăng Nhập
          </button>
        </form>
        <p className="text-center text-sm text-gray-600 mt-6">
          Chưa có tài khoản?{" "}
          <button
            onClick={() => setCurrentPage("register")}
            className="text-blue-600 hover:underline font-medium"
          >
            Đăng ký ngay
          </button>
        </p>
      </div>
    </div>
  );
};

export default LoginPage;
