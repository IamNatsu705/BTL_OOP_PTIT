import React from "react";
import { mockComments } from "../../../data/mockData";
import StarRating from "../../../components/ui/StarRating";

const AdminComments = () => (
  <div className="animate-fade-in">
    <h2 className="text-3xl font-semibold text-gray-800 mb-6">
      Bình luận từ Khách hàng
    </h2>
    <div className="space-y-6">
      {mockComments.map((comment) => (
        <div key={comment.id} className="bg-white p-6 rounded-lg shadow-md">
          <div className="flex justify-between items-start mb-2">
            <div>
              <p className="font-semibold text-gray-800">
                {comment.userFullName}
              </p>
              <p className="text-sm text-gray-500">
                Ngày: {new Date(comment.created_at).toLocaleString("vi-VN")}
              </p>
            </div>
            <StarRating rating={comment.rating} />
          </div>
          <p className="text-gray-700">{comment.comment_text}</p>
        </div>
      ))}
    </div>
  </div>
);

export default AdminComments;
