import React from "react";
import { AppProvider } from "./context/AppContext";
import MainRouter from "./mainRouter";

/**
 * App (Component gốc, export default)
 */
export default function App() {
  return (
    <AppProvider>
      <MainRouter />
    </AppProvider>
  );
}

// Thêm CSS tùy chỉnh và Google Font (vì không dùng file index.css)
const style = document.createElement("style");
style.innerHTML = `
  @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap');
  
  body {
    font-family: 'Inter', sans-serif;
  }

  .text-shadow-lg {
    text-shadow: 0 4px 6px rgba(0, 0, 0, 0.3);
  }

  @keyframes fade-in {
    from { opacity: 0; }
    to { opacity: 1; }
  }

  @keyframes fade-in-up {
    from { opacity: 0; transform: translateY(20px); }
    to { opacity: 1; transform: translateY(0); }
  }

  .animate-fade-in {
    animation: fade-in 0.5s ease-out;
  }
  
  .animate-fade-in-up {
    animation: fade-in-up 0.5s ease-out;
  }
`;
document.head.appendChild(style);
