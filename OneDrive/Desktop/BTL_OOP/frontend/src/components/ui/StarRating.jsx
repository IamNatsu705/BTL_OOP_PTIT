import React from "react";
import { Star } from "lucide-react";

const StarRating = ({ rating }) => (
  <div className="flex">
    {[...Array(5)].map((_, index) => (
      <Star
        key={index}
        className={`w-5 h-5 ${
          index < rating ? "text-yellow-400" : "text-gray-300"
        }`}
        fill={index < rating ? "currentColor" : "none"}
      />
    ))}
  </div>
);

export default StarRating;
