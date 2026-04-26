import React from "react";
import { useTheme } from "../context/ThemeContext";

export default function UploadedDocuments({ documents }) {
  const { isDark } = useTheme();

  const getFileIcon = (fileName) => {
    const ext = fileName.split('.').pop().toLowerCase();
    switch (ext) {
      case 'pdf':
        return '📄';
      case 'doc':
      case 'docx':
        return '📝';
      case 'xlsx':
      case 'xls':
        return '📊';
      case 'txt':
        return '📃';
      default:
        return '📎';
    }
  };

  return (
    <div className={`rounded-xl border backdrop-blur-sm p-6 transition-all duration-500 animate-fadeInUp ${
      isDark
        ? 'border-gray-800/50 bg-gradient-to-br from-gray-800/30 to-blue-900/10'
        : 'border-gray-300/50 bg-gradient-to-br from-gray-100/30 to-blue-100/10'
    }`}>
      <div className="flex items-center gap-3 mb-4 animate-slideInDown">
        <div className={`p-2 rounded-lg transition-all duration-500 ${
          isDark
            ? 'bg-blue-600/20'
            : 'bg-blue-500/20'
        }`}>
          <svg className={`w-5 h-5 transition-colors duration-500 ${
            isDark ? 'text-blue-400' : 'text-blue-600'
          }`} fill="currentColor" viewBox="0 0 20 20">
            <path d="M9 2a1 1 0 000 2h2a1 1 0 100-2H9z" />
            <path fillRule="evenodd" d="M4 5a2 2 0 012-2 1 1 0 000 2H6a6 6 0 100 12H4a1 1 0 100 2 2 2 0 01-2-2V5zm15 0a1 1 0 010 2H14v6h5a1 1 0 110 2h-5.22l1.565 5.243a1 1 0 01-.97 1.286H9.89a1 1 0 01-.935-.735l-2.4-7.558H5a1 1 0 110-2h1V7H5a1 1 0 010-2h4.08L7.589 2.745a1 1 0 01.935-.735h8.22z" />
          </svg>
        </div>
        <div>
          <h3 className={`font-semibold transition-colors duration-500 ${
            isDark ? 'text-white' : 'text-gray-900'
          }`}>Uploaded Documents</h3>
          <p className={`text-sm transition-colors duration-500 ${
            isDark ? 'text-gray-400' : 'text-gray-600'
          }`}>{documents.length} file{documents.length !== 1 ? 's' : ''}</p>
        </div>
      </div>

      <div className="space-y-2">
        {documents.map((doc, idx) => (
          <div
            key={doc.id}
            className={`flex items-center justify-between p-3 rounded-lg transition-all duration-500 group hover-lift animate-slideInLeft ${
              isDark
                ? 'bg-gray-800/40 hover:bg-gray-800/60'
                : 'bg-gray-200/40 hover:bg-gray-200/60'
            }`}
            style={{ animationDelay: `${idx * 50}ms` }}
          >
            <div className="flex items-center gap-3 flex-1 min-w-0">
              <span className="text-xl flex-shrink-0">{getFileIcon(doc.name)}</span>
              <div className="min-w-0 flex-1">
                <p className={`text-sm font-medium truncate transition-colors duration-500 ${
                  isDark ? 'text-gray-200' : 'text-gray-800'
                }`}>{doc.name}</p>
                <p className={`text-xs transition-colors duration-500 ${
                  isDark ? 'text-gray-500' : 'text-gray-600'
                }`}>
                  {(doc.size / 1024).toFixed(1)} KB • {new Date(doc.uploadedAt).toLocaleDateString()}
                </p>
              </div>
            </div>
            <button
              onClick={() => {
                // This could be replaced with actual delete functionality
              }}
              className={`ml-2 p-2 rounded-lg opacity-0 group-hover:opacity-100 hover:scale-110 transition-all ${
                isDark
                  ? 'text-gray-400 hover:text-red-400 hover:bg-red-600/20'
                  : 'text-gray-600 hover:text-red-600 hover:bg-red-100/50'
              }`}
            >
              <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
