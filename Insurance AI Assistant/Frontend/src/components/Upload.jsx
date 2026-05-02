import React, { useState, useRef } from "react";
import axios from "axios";

export default function Upload() {
  const [isDragging, setIsDragging] = useState(false);
  const [isUploading, setIsUploading] = useState(false);
  const fileInputRef = useRef(null);
  const API_URL = import.meta.env.VITE_API_URL;

  const handleUpload = async (files) => {
    if (!files.length) return;
  
    setIsUploading(true);
    const formData = new FormData();
  
    for (let i = 0; i < files.length; i++) {
      formData.append("files", files[i]);
    }
  
    try {
      await axios.post(`${API_URL}/upload`, formData, {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      });
  
      alert("✅ Documents uploaded and processed successfully!");
    } catch (error) {
      console.error("Upload error:", error);
      alert("❌ Error uploading files. Please try again.");
    } finally {
      setIsUploading(false);
    }
  };

  const handleDragOver = (e) => {
    e.preventDefault();
    setIsDragging(true);
  };

  const handleDragLeave = () => {
    setIsDragging(false);
  };

  const handleDrop = (e) => {
    e.preventDefault();
    setIsDragging(false);
    handleUpload(e.dataTransfer.files);
  };

  const handleFileChange = (e) => {
    handleUpload(e.target.files);
  };

  return (
    <div
      onDragOver={handleDragOver}
      onDragLeave={handleDragLeave}
      onDrop={handleDrop}
      className={`relative px-4 py-2 rounded-lg border-2 border-dashed transition-all cursor-pointer ${
        isDragging
          ? "border-blue-500 bg-blue-500/10"
          : "border-gray-700 hover:border-gray-600 hover:bg-gray-800/50"
      } ${isUploading ? "opacity-50 cursor-not-allowed" : ""}`}
      onClick={() => fileInputRef.current?.click()}
    >
      <input
        ref={fileInputRef}
        type="file"
        multiple
        onChange={handleFileChange}
        disabled={isUploading}
        className="hidden"
        accept=".pdf,.doc,.docx,.txt,.xlsx,.xls"
      />
      
      <div className="flex items-center gap-2 text-sm">
        {isUploading ? (
          <>
            <svg className="w-4 h-4 animate-spin text-blue-500" fill="none" viewBox="0 0 24 24">
              <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
              <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" />
            </svg>
            <span className="text-gray-300">Uploading...</span>
          </>
        ) : (
          <>
            <svg className="w-4 h-4 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
            </svg>
            <span className="text-gray-300">Upload Documents</span>
          </>
        )}
      </div>
    </div>
  );
}
