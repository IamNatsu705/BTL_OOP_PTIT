import React from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";
import { mockRevenueData, mockUsers } from "../../../data/mockData";

const AdminOverview = () => (
  <div className="animate-fade-in">
    <h2 className="text-3xl font-semibold text-gray-800 mb-6">Tổng quan</h2>
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h4 className="text-gray-500">Doanh thu (Tháng)</h4>
        <p className="text-3xl font-bold text-green-600">125.000.000 VNĐ</p>
      </div>
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h4 className="text-gray-500">Lượt đặt mới (Hôm nay)</h4>
        <p className="text-3xl font-bold text-blue-600">32</p>
      </div>
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h4 className="text-gray-500">Tổng khách hàng</h4>
        <p className="text-3xl font-bold text-indigo-600">
          {mockUsers.filter((u) => u.role === "customer").length}
        </p>
      </div>
    </div>
    <div className="bg-white p-6 rounded-lg shadow-md">
      <h4 className="text-xl font-semibold text-gray-700 mb-4">
        Doanh thu 7 ngày qua
      </h4>
      <div style={{ width: "100%", height: 300 }}>
        <ResponsiveContainer>
          <BarChart data={mockRevenueData}>
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip
              formatter={(value) => `${value.toLocaleString("vi-VN")} VNĐ`}
            />
            <Legend />
            <Bar dataKey="Doanh thu" fill="#3b82f6" />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  </div>
);

export default AdminOverview;
