import React from "react";
import { useTheme } from "../context/ThemeContext";

export default function SuggestedQuestions({ questions, onSelectQuestion }) {
  const { isDark } = useTheme();
  const icons = ["🛡️", "💼", "📋", "🔒"];

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      {questions.map((question, idx) => (
        <button
          key={idx}
          onClick={() => onSelectQuestion(question)}
          className={`group relative p-5 rounded-xl border transition-all duration-500 text-left overflow-hidden hover-lift animate-slideInUp ${
            isDark
              ? 'border-gray-700/50 bg-gradient-to-br from-gray-800/40 to-blue-900/10 hover:from-gray-800/60 hover:to-blue-900/20 hover:border-blue-500/50'
              : 'border-gray-300/50 bg-gradient-to-br from-gray-100/40 to-blue-100/10 hover:from-gray-100/60 hover:to-blue-100/30 hover:border-blue-400/50'
          }`}
          style={{ animationDelay: `${idx * 100}ms` }}
        >
          {/* Animated gradient background on hover */}
          <div className={`absolute inset-0 opacity-0 group-hover:opacity-100 transition-opacity duration-300 ${
            isDark
              ? 'bg-gradient-to-br from-blue-600/5 to-transparent'
              : 'bg-gradient-to-br from-blue-400/5 to-transparent'
          }`} />
          
          <div className="relative flex items-start gap-4">
            <div className={`flex-shrink-0 w-10 h-10 rounded-xl flex items-center justify-center transition-all duration-300 group-hover:scale-110 ${
              isDark
                ? 'bg-gradient-to-br from-blue-600/20 to-blue-500/10 group-hover:from-blue-600/40 group-hover:to-blue-500/20'
                : 'bg-gradient-to-br from-blue-500/20 to-blue-400/10 group-hover:from-blue-500/40 group-hover:to-blue-400/20'
            }`}>
              <span className="text-lg group-hover:scale-110 transition-transform duration-300">
                {icons[idx % icons.length]}
              </span>
            </div>
            
            <div className="flex-1">
              <p className={`text-sm font-semibold line-clamp-2 transition-colors duration-300 ${
                isDark
                  ? 'text-gray-100 group-hover:text-white'
                  : 'text-gray-900 group-hover:text-gray-950'
              }`}>
                {question}
              </p>
            </div>

            <div className={`flex-shrink-0 opacity-0 group-hover:opacity-100 transition-all duration-300 transform group-hover:translate-x-1 ${
              isDark ? 'text-blue-400' : 'text-blue-600'
            }`}>
              <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M13 7l5 5m0 0l-5 5m5-5H6" />
              </svg>
            </div>
          </div>
        </button>
      ))}
    </div>
  );
}
