import React, { useState } from "react";
import { useTheme } from "../context/ThemeContext";

export default function Sidebar({
  conversations,
  activeConversation,
  onSelectConversation,
  onNewConversation,
  onDeleteConversation
}) {
  const { isDark, toggleTheme } = useTheme();
  const [showSettings, setShowSettings] = useState(false);

  return (
    <div className={`w-64 flex flex-col h-full shadow-xl transition-all duration-500 border-r animate-slideInLeft ${
      isDark 
        ? 'bg-gradient-to-b from-gray-900 to-gray-950 border-gray-800/50' 
        : 'bg-gradient-to-b from-gray-100 to-gray-50 border-gray-200/50'
    }`}>
      {/* Logo and Title */}
      <div className={`p-5 border-b transition-all duration-500 backdrop-blur-sm ${
        isDark 
          ? 'border-gray-800/50 bg-gradient-to-r from-gray-800/50 to-transparent' 
          : 'border-gray-200/50 bg-gradient-to-r from-gray-100/50 to-transparent'
      }`}>
        <button
          onClick={onNewConversation}
          className={`w-full flex items-center justify-center gap-2 px-4 py-3 rounded-lg text-white transition-all duration-300 font-medium shadow-lg active:scale-95 animate-fadeInDown ${
            isDark
              ? 'bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800 hover:shadow-blue-500/50'
              : 'bg-gradient-to-r from-blue-500 to-blue-600 hover:from-blue-600 hover:to-blue-700 hover:shadow-blue-400/40'
          }`}
        >
          <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M12 4v16m8-8H4" />
          </svg>
          New Chat
        </button>
      </div>

      {/* Conversations List */}
      <div className={`flex-1 overflow-y-auto py-4 px-3 space-y-2 scrollbar-thin transition-all duration-500 ${
        isDark ? 'scrollbar-dark' : 'scrollbar-light'
      }`}>
        {conversations.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-12 px-4 animate-fadeInUp">
            <svg className={`w-12 h-12 mb-3 transition-colors duration-500 ${
              isDark ? 'text-gray-700' : 'text-gray-400'
            }`} fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M8 12h.01M12 12h.01M16 12h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <p className={`text-sm text-center font-medium transition-colors duration-500 ${
              isDark ? 'text-gray-500' : 'text-gray-600'
            }`}>No conversations yet</p>
          </div>
        ) : (
          conversations.map((conv, idx) => (
            <div
              key={conv.id}
              className={`group relative p-3 rounded-lg cursor-pointer transition-all duration-300 animate-slideInLeft border ${
                activeConversation === conv.id
                  ? isDark
                    ? 'bg-gradient-to-r from-blue-600/30 to-blue-500/10 text-white border-blue-500/30'
                    : 'bg-gradient-to-r from-blue-100/60 to-blue-50/30 text-blue-900 border-blue-300/50'
                  : isDark
                    ? 'text-gray-400 hover:bg-gray-800/50 hover:text-gray-200 border-transparent hover:border-gray-700'
                    : 'text-gray-600 hover:bg-gray-200/50 hover:text-gray-900 border-transparent hover:border-gray-300'
              }`}
              onClick={() => onSelectConversation(conv.id)}
              style={{ animationDelay: `${idx * 50}ms` }}
            >
              <p className="text-sm font-semibold truncate">{conv.title}</p>
              <p className={`text-xs mt-1 transition-colors duration-500 ${
                isDark ? 'text-gray-500' : 'text-gray-500'
              }`}>
                {new Date(conv.createdAt).toLocaleDateString()}
              </p>

              {/* Delete Button */}
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  if (window.confirm("Delete this conversation?")) {
                    onDeleteConversation(conv.id);
                  }
                }}
                className={`absolute right-3 top-3 opacity-0 group-hover:opacity-100 transition-opacity p-1.5 rounded-lg ${
                  isDark
                    ? 'hover:bg-red-600/20'
                    : 'hover:bg-red-100/50'
                }`}
              >
                <svg className={`w-4 h-4 transition-colors duration-300 ${
                  isDark
                    ? 'text-gray-500 hover:text-red-400'
                    : 'text-gray-500 hover:text-red-500'
                }`} fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
              </button>
            </div>
          ))
        )}
      </div>

      {/* Footer */}
      <div className={`border-t transition-all duration-500 bg-gradient-to-t backdrop-blur-sm p-4 space-y-3 ${
        isDark
          ? 'border-gray-800/50 from-gray-900 to-transparent'
          : 'border-gray-200/50 from-gray-100 to-transparent'
      }`}>
        <div className="space-y-2 animate-fadeInUp">
          <p className={`text-xs font-semibold uppercase tracking-wider transition-colors duration-500 ${
            isDark ? 'text-gray-500' : 'text-gray-600'
          }`}>Insurance AI Assistant</p>
          <p className={`text-xs transition-colors duration-500 ${
            isDark ? 'text-gray-600' : 'text-gray-500'
          }`}>v1.0 • Powered by AI</p>
        </div>
        
        <div className="flex gap-3 pt-3 border-t animate-fadeInUp" style={{ animationDelay: '100ms' }} onClick={() => console.log('border')}>
          <button className={`relative p-2 rounded-lg transition-all duration-300 hover-lift ${
            isDark
              ? 'hover:bg-gray-800 text-gray-500 hover:text-blue-400'
              : 'hover:bg-gray-200 text-gray-600 hover:text-blue-500'
          }`}>
            <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </button>

          <button 
            onClick={() => setShowSettings(!showSettings)}
            className={`relative p-2 rounded-lg transition-all duration-300 hover-lift ${
              isDark
                ? 'hover:bg-gray-800 text-gray-500 hover:text-blue-400'
                : 'hover:bg-gray-200 text-gray-600 hover:text-blue-500'
            }`}
          >
            <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
          </button>
        </div>

        {/* Settings Dropdown */}
        {showSettings && (
          <div className={`absolute bottom-24 left-4 right-4 rounded-lg shadow-xl animate-slideInUp p-4 border ${
            isDark
              ? 'bg-gray-800 border-gray-700'
              : 'bg-gray-100 border-gray-300'
          }`}>
            <div className="flex items-center justify-between">
              <span className={`text-sm font-semibold ${isDark ? 'text-white' : 'text-gray-900'}`}>
                {isDark ? '🌙 Dark Mode' : '☀️ Light Mode'}
              </span>
              <button
                onClick={toggleTheme}
                className={`relative inline-flex h-7 w-12 items-center rounded-full transition-colors duration-500 ${
                  isDark
                    ? 'bg-blue-600 hover:bg-blue-700'
                    : 'bg-gray-300 hover:bg-gray-400'
                }`}
              >
                <span
                  className={`inline-block h-6 w-6 transform rounded-full bg-white transition-transform duration-300 ${
                    isDark ? 'translate-x-6' : 'translate-x-0.5'
                  }`}
                />
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
