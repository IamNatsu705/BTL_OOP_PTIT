import React from "react";
import { mockTimeSlots } from "../../../data/mockData";

const AdminPricing = () => (
  <div className="animate-fade-in">
    <h2 className="text-3xl font-semibold text-gray-800 mb-6">
      Quản lý Giá (Theo khung giờ)
    </h2>
    <div className="bg-white rounded-lg shadow-md overflow-x-auto">
      <table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-50">
          <tr>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
              Tên khung giờ
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
              Bắt đầu
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
              Kết thúc
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
              Áp dụng
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
              Giá (VNĐ/giờ)
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
              Hành động
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {mockTimeSlots.map((slot) => (
            <tr key={slot.id}>
              <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                {slot.slot_name}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                {slot.start_time}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                {slot.end_time}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                {slot.days_apply}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm font-semibold text-gray-900">
                {slot.price_per_hour.toLocaleString("vi-VN")}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                <button className="text-indigo-600 hover:text-indigo-900">
                  Sửa
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </div>
);

export default AdminPricing;
