import React from "react";
import { useApp } from "../../context/AppContext";

const Notification = () => {
  const { notification } = useApp();

  if (!notification.show) return null;

  return (
    <div
      className={`fixed bottom-5 right-5 p-4 rounded-lg shadow-lg text-white z-50 ${
        notification.type === "success"
          ? "bg-green-500"
          : notification.type === "error"
          ? "bg-red-500"
          : "bg-blue-500"
      }`}
    >
      {notification.message}
    </div>
  );
};

export default Notification;
