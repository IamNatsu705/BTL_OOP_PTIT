import React from "react";
import { MapPin, Phone, Mail } from "lucide-react";

const Footer = () => (
  <footer className="bg-gray-800 text-gray-300 mt-16">
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
        <div>
          <h5 className="text-lg font-bold text-white mb-4">Về chúng tôi</h5>
          <p className="text-sm">
            Hệ thống sân cầu lông hiện đại, chất lượng cao, phục vụ đam mê thể
            thao của bạn.
          </p>
        </div>
        <div>
          <h5 className="text-lg font-bold text-white mb-4">Liên kết nhanh</h5>
          <ul className="space-y-2 text-sm">
            <li>
              <a href="#" className="hover:text-white">
                Trang chủ
              </a>
            </li>
            <li>
              <a href="#" className="hover:text-white">
                Đặt sân
              </a>
            </li>
            <li>
              <a href="#" className="hover:text-white">
                Khuyến mãi
              </a>
            </li>
            <li>
              <a href="#" className="hover:text-white">
                Liên hệ
              </a>
            </li>
          </ul>
        </div>
        <div>
          <h5 className="text-lg font-bold text-white mb-4">Liên hệ</h5>
          <ul className="space-y-2 text-sm">
            <li className="flex items-center">
              <MapPin className="w-4 h-4 mr-2" /> 123 Đường ABC, Quận 1, TP. HCM
            </li>
            <li className="flex items-center">
              <Phone className="w-4 h-4 mr-2" /> (028) 3812 3456
            </li>
            <li className="flex items-center">
              <Mail className="w-4 h-4 mr-2" /> support@sancaulong.com
            </li>
          </ul>
        </div>
        <div>
          <h5 className="text-lg font-bold text-white mb-4">
            Theo dõi chúng tôi
          </h5>
          <div className="flex space-x-4">
            {/* Thêm icons social media nếu muốn */}
          </div>
        </div>
      </div>
      <div className="mt-8 border-t border-gray-700 pt-8 text-center text-sm">
        <p>&copy; 2025 Badminton Court. All rights reserved.</p>
      </div>
    </div>
  </footer>
);

export default Footer;
