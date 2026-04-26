import React from "react";
import { useTheme } from "../context/ThemeContext";

export default function Message({ msg }) {
  const { isDark } = useTheme();
  const isUser = msg.role === "user";

  const formatText = (text) => {
    return text.split("\n").map((line, i) => (
      <p key={i} className="mb-2 last:mb-0">
        {line || "\u00A0"}
      </p>
    ));
  };

  return (
    <div className={`flex ${isUser ? "justify-end" : "justify-start"} animate-fadeInUp`}>
      <div className={`max-w-2xl ${isUser ? "w-fit" : "w-full"}`}>
        <div
          className={`px-5 py-4 rounded-2xl shadow-lg transition-all duration-500 ${
            isUser
              ? isDark
                ? 'bg-gradient-to-br from-blue-600 to-blue-700 text-white rounded-br-none shadow-blue-500/30'
                : 'bg-gradient-to-br from-blue-500 to-blue-600 text-white rounded-br-none shadow-blue-400/30'
              : isDark
                ? 'bg-gradient-to-br from-gray-800 to-gray-700 text-gray-100 rounded-bl-none shadow-black/30'
                : 'bg-gradient-to-br from-gray-200 to-gray-100 text-gray-900 rounded-bl-none shadow-gray-300/40'
          }`}
        >
          <div className="text-base leading-relaxed whitespace-pre-wrap break-words font-medium">
            {formatText(msg.text)}
          </div>

          {msg.sources && msg.sources.length > 0 && (
            <div className={`mt-4 pt-4 transition-all duration-500 ${
              isUser ? 'border-white/20' : isDark ? 'border-gray-600/40' : 'border-gray-400/40'
            } border-t`}>
              <details className="cursor-pointer group">
                <summary className={`text-sm font-semibold flex items-center gap-2 transition-colors duration-500 ${
                  isUser
                    ? "text-blue-100 group-hover:text-white"
                    : isDark
                      ? "text-gray-300 group-hover:text-gray-200"
                      : "text-gray-700 group-hover:text-gray-900"
                }`}>
                  <svg className="w-4 h-4 group-open:rotate-180 transition-transform" fill="currentColor" viewBox="0 0 20 20">
                    <path fillRule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clipRule="evenodd" />
                  </svg>
                  📄 Sources ({msg.sources.length})
                </summary>
                <div className="mt-3 space-y-2 pl-6">
                  {msg.sources.map((source, i) => (
                    <div key={i} className={`text-sm transition-colors duration-500 ${
                      isUser
                        ? "text-blue-100 hover:text-white"
                        : isDark
                          ? "text-gray-400 hover:text-gray-300"
                          : "text-gray-700 hover:text-gray-900"
                    } truncate flex items-start gap-2`}>
                      <span className="flex-shrink-0 mt-0.5">•</span>
                      <span className="truncate">{source}</span>
                    </div>
                  ))}
                </div>
              </details>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}