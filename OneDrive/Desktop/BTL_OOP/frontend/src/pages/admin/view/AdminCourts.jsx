import React, { useState } from "react";
import { mockCourts } from "../../../data/mockData";

const AdminCourts = () => {
  const [courts, setCourts] = useState(mockCourts);

  // Giả lập cập nhật trạng thái
  const toggleStatus = (id) => {
    setCourts((prevCourts) =>
      prevCourts.map((court) =>
        court.id === id
          ? {
              ...court,
              status:
                court.status === "available" ? "maintenance" : "available",
            }
          : court
      )
    );
  };

  return (
    <div className="animate-fade-in">
      <h2 className="text-3xl font-semibold text-gray-800 mb-6">
        Quản lý Sân ({courts.length} sân)
      </h2>
      <div className="bg-white rounded-lg shadow-md overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                Tên Sân
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                Mô tả
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                Trạng thái
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                Hành động
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {courts.map((court) => (
              <tr key={court.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  {court.name}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                  {court.description}
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span
                    className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${
                      court.status === "available"
                        ? "bg-green-100 text-green-800"
                        : "bg-yellow-100 text-yellow-800"
                    }`}
                  >
                    {court.status === "available" ? "Sẵn sàng" : "Bảo trì"}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <button
                    onClick={() => toggleStatus(court.id)}
                    className="text-blue-600 hover:text-blue-900 mr-4"
                  >
                    Đổi trạng thái
                  </button>
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
};

export default AdminCourts;
