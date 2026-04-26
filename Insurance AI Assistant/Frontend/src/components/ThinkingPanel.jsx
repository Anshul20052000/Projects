import React from "react";
import { useTheme } from "../context/ThemeContext";

const THINKING_STEPS = [
  "Analyzing policy requirements",
  "Checking coverage rules",
  "Comparing premium options",
  "Validating claim eligibility",
  "Generating recommendations",
  "Finalizing response"
];

export default function ThinkingPanel({ isVisible, thinking }) {
  const { isDark } = useTheme();

  if (!isVisible) return null;

  const getStepProgress = (stepIndex) => {
    if (stepIndex < thinking.currentStep) return 100;
    if (stepIndex === thinking.currentStep) return 60;
    return 0;
  };

  return (
    <div className={`w-80 border-l flex flex-col transition-all duration-500 backdrop-blur-sm overflow-hidden ${
      isDark
        ? 'border-gray-800/50 bg-gradient-to-br from-gray-900 to-gray-900/50'
        : 'border-gray-200/50 bg-gradient-to-br from-gray-50 to-gray-100/50'
    } animate-slideInRight`}>
      {/* Header */}
      <div className={`p-4 border-b transition-all duration-500 ${
        isDark
          ? 'border-gray-800/30 bg-gray-900/50'
          : 'border-gray-200/30 bg-gray-100/30'
      }`}>
        <div className="flex items-center gap-2 mb-2">
          <div className="w-2 h-2 rounded-full bg-gradient-to-r from-blue-400 to-purple-400 animate-pulse"></div>
          <h3 className={`text-sm font-bold uppercase tracking-wide transition-colors duration-500 ${
            isDark ? 'text-gray-300' : 'text-gray-700'
          }`}>AI Thinking</h3>
        </div>
        <p className={`text-xs transition-colors duration-500 ${
          isDark ? 'text-gray-500' : 'text-gray-600'
        }`}>Live reasoning & analysis</p>
      </div>

      {/* Steps Container */}
      <div className="flex-1 overflow-y-auto p-4 space-y-3">
        {THINKING_STEPS.map((step, index) => {
          const progress = getStepProgress(index);
          const isActive = index === thinking.currentStep;
          const isCompleted = index < thinking.currentStep;

          return (
            <div
              key={index}
              className={`transition-all duration-300 ${isActive ? 'scale-100' : 'scale-95'}`}
            >
              {/* Step Header */}
              <div className="flex items-start gap-3 mb-2">
                <div className={`flex-shrink-0 w-6 h-6 rounded-full flex items-center justify-center transition-all duration-300 ${
                  isCompleted
                    ? 'bg-gradient-to-r from-green-500 to-green-600 shadow-lg shadow-green-500/30'
                    : isActive
                    ? 'bg-gradient-to-r from-blue-500 to-blue-600 shadow-lg shadow-blue-500/30 animate-pulse'
                    : isDark
                    ? 'bg-gray-700'
                    : 'bg-gray-300'
                }`}>
                  {isCompleted ? (
                    <svg className="w-4 h-4 text-white" fill="currentColor" viewBox="0 0 20 20">
                      <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                    </svg>
                  ) : isActive ? (
                    <div className="w-2 h-2 rounded-full bg-white animate-bounce"></div>
                  ) : (
                    <span className={`text-xs font-bold transition-colors duration-300 ${
                      isDark ? 'text-gray-500' : 'text-gray-600'
                    }`}>{index + 1}</span>
                  )}
                </div>
                <div className="flex-1">
                  <p className={`text-sm font-medium transition-colors duration-300 ${
                    isActive
                      ? isDark
                        ? 'text-blue-300'
                        : 'text-blue-600'
                      : isDark
                      ? isCompleted ? 'text-green-400' : 'text-gray-400'
                      : isCompleted ? 'text-green-600' : 'text-gray-500'
                  }`}>
                    {step}
                  </p>
                </div>
              </div>

              {/* Progress Bar */}
              <div className={`h-1.5 w-full rounded-full overflow-hidden transition-all duration-300 ${
                isDark
                  ? 'bg-gray-800/40'
                  : 'bg-gray-300/40'
              }`}>
                <div
                  className={`h-full rounded-full transition-all duration-500 ${
                    isCompleted
                      ? 'bg-gradient-to-r from-green-500 to-green-400 shadow-lg shadow-green-500/40'
                      : isActive
                      ? 'bg-gradient-to-r from-blue-500 to-purple-500 shadow-lg shadow-blue-500/40 animate-shimmer'
                      : 'bg-gray-600/30'
                  }`}
                  style={{ width: `${progress}%` }}
                ></div>
              </div>
            </div>
          );
        })}
      </div>

      {/* Footer - Confidence Score */}
      <div className={`p-4 border-t transition-all duration-500 ${
        isDark
          ? 'border-gray-800/30 bg-gray-900/50'
          : 'border-gray-200/30 bg-gray-100/30'
      }`}>
        <div className="flex items-center justify-between">
          <span className={`text-xs font-semibold uppercase tracking-wide transition-colors duration-500 ${
            isDark ? 'text-gray-500' : 'text-gray-600'
          }`}>Confidence</span>
          <div className="flex items-center gap-2">
            <div className={`h-1.5 w-12 rounded-full overflow-hidden transition-all duration-300 ${
              isDark
                ? 'bg-gray-800/40'
                : 'bg-gray-300/40'
            }`}>
              <div
                className="h-full bg-gradient-to-r from-blue-500 to-blue-400 transition-all duration-300 rounded-full"
                style={{ width: `${thinking.confidence}%` }}
              ></div>
            </div>
            <span className={`text-sm font-bold transition-colors duration-500 ${
              thinking.confidence >= 80
                ? isDark ? 'text-green-400' : 'text-green-600'
                : thinking.confidence >= 60
                ? isDark ? 'text-blue-400' : 'text-blue-600'
                : isDark ? 'text-yellow-400' : 'text-yellow-600'
            }`}>
              {thinking.confidence}%
            </span>
          </div>
        </div>
        <p className={`text-xs mt-2 transition-colors duration-500 ${
          isDark ? 'text-gray-600' : 'text-gray-500'
        }`}>
          {thinking.confidence < 40
            ? "Still analyzing..."
            : thinking.confidence < 70
            ? "Processing information..."
            : thinking.confidence < 90
            ? "Finalizing response..."
            : "Ready to respond"}
        </p>
      </div>
    </div>
  );
}
