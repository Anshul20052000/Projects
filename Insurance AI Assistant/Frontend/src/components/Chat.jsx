import React, { useState, useRef, useEffect } from "react";
import axios from "axios";
import Message from "./Message";
import SuggestedQuestions from "./SuggestedQuestions";
import UploadedDocuments from "./UploadedDocuments";
import ThinkingPanel from "./ThinkingPanel";
import { useTheme } from "../context/ThemeContext";

const DEFAULT_SUGGESTIONS = [
  "What are the top insurance policies available?",
  "How do I file a claim?",
  "What is the coverage limit for my policy?",
  "How can I renew my insurance?"
];

export default function Chat({ conversation, conversations, setConversations, uploadedDocuments, setUploadedDocuments }) {
  const { isDark } = useTheme();
  const [input, setInput] = useState("");
  const [isStreaming, setIsStreaming] = useState(false);
  const [isDragging, setIsDragging] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [isUploading, setIsUploading] = useState(false);
  const [showThinkingPanel, setShowThinkingPanel] = useState(false);
  const [thinkingState, setThinkingState] = useState({
    currentStep: 0,
    confidence: 0
  });
  const messagesEndRef = useRef(null);
  const inputRef = useRef(null);
  const fileInputRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [conversation?.messages]);

  const handleUpload = async (files) => {
    if (!files.length) return;

    const newDocs = Array.from(files).map(file => ({
      id: Math.random(),
      name: file.name,
      size: file.size,
      type: file.type,
      uploadedAt: new Date()
    }));

    const formData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append("files", files[i]);
    }

    setIsUploading(true);
    setUploadProgress(0);

    try {
      const response = await axios.post("http://localhost:8000/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
        onUploadProgress: (progressEvent) => {
          const progress = Math.round((progressEvent.loaded / progressEvent.total) * 100);
          setUploadProgress(progress);
        }
      });
      
      setUploadProgress(100);
      setTimeout(() => {
        setUploadedDocuments([...uploadedDocuments, ...newDocs]);
        setIsUploading(false);
        setUploadProgress(0);
      }, 500);
    } catch (error) {
      console.error("Upload error:", error);
      alert("Error uploading files. Please try again.");
      setIsUploading(false);
      setUploadProgress(0);
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

  const simulateThinking = async () => {
    const thinkingSteps = [
      { step: 0, confidence: 15 },
      { step: 1, confidence: 35 },
      { step: 2, confidence: 55 },
      { step: 3, confidence: 70 },
      { step: 4, confidence: 80 },
      { step: 5, confidence: 87 }
    ];

    for (const { step, confidence } of thinkingSteps) {
      await new Promise(resolve => setTimeout(resolve, 600));
      setThinkingState({ currentStep: step, confidence });
    }
  };

  const sendMessage = async (text = input) => {
    if (!text.trim() || !conversation) return;

    const userMsg = { role: "user", text, sources: null };
    
    const updatedConversations = conversations.map(c =>
      c.id === conversation.id
        ? {
            ...c,
            messages: [...c.messages, userMsg],
            title: c.messages.length === 0 ? text.substring(0, 30) : c.title
          }
        : c
    );
    setConversations(updatedConversations);

    setInput("");
    setIsStreaming(true);
    setShowThinkingPanel(true);
    setThinkingState({ currentStep: 0, confidence: 0 });

    simulateThinking();

    try {
      const res = await axios.post("http://localhost:8000/ask", null, {
        params: { query: text }
      });

      const botMsg = {
        role: "bot",
        text: res.data.answer || "I couldn't generate a response. Please try again.",
        sources: res.data.sources || []
      };

      const finalConversations = updatedConversations.map(c =>
        c.id === conversation.id
          ? { ...c, messages: [...c.messages, botMsg] }
          : c
      );
      setConversations(finalConversations);
    } catch (error) {
      console.error("Error sending message:", error);
      const errorMsg = {
        role: "bot",
        text: "Sorry, I encountered an error. Please try again.",
        sources: []
      };
      const finalConversations = updatedConversations.map(c =>
        c.id === conversation.id
          ? { ...c, messages: [...c.messages, errorMsg] }
          : c
      );
      setConversations(finalConversations);
    } finally {
      setIsStreaming(false);
      setTimeout(() => setShowThinkingPanel(false), 1000);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      sendMessage();
    }
  };

  const messages = conversation?.messages || [];
  const showSuggestions = messages.length === 0;

  return (
    <div className={`flex h-full transition-all duration-500 ${
      isDark
        ? 'bg-gradient-to-br from-gray-950 via-gray-950 to-blue-950/5'
        : 'bg-gradient-to-br from-white via-gray-50 to-blue-50/30'
    }`} onDragOver={handleDragOver} onDragLeave={handleDragLeave} onDrop={handleDrop}>
      
      <div className="flex-1 flex flex-col">
        {/* Header */}
        <div className={`border-b transition-all duration-500 px-8 py-5 backdrop-blur-sm animate-slideInDown ${
          isDark
            ? 'border-gray-800/50 bg-gradient-to-r from-gray-900/50 to-blue-900/20'
            : 'border-gray-200/50 bg-gradient-to-r from-white/50 to-blue-50/30'
        }`}>
          <div>
            <h1 className={`text-3xl font-bold bg-clip-text text-transparent transition-all duration-500 ${
              isDark
                ? 'bg-gradient-to-r from-blue-400 to-blue-600'
                : 'bg-gradient-to-r from-blue-600 to-blue-800'
            }`}>
              🧠 Insurance AI Assistant
            </h1>
            <p className={`text-sm mt-1 transition-colors duration-500 ${
              isDark ? 'text-gray-400' : 'text-gray-600'
            }`}>Intelligent policy guidance powered by advanced AI</p>
          </div>
        </div>

        {/* Messages Area */}
        <div className="flex-1 overflow-y-auto">
          {showSuggestions ? (
            <div className="h-full flex flex-col items-center justify-center px-6 pb-12 animate-fadeInUp">
              <div className="max-w-3xl w-full">
                <div className="text-center mb-16">
                  <h2 className={`text-5xl font-bold mb-4 transition-colors duration-500 ${
                    isDark ? 'text-white' : 'text-gray-900'
                  }`}>What can I help you with?</h2>
                  <p className={`text-lg transition-colors duration-500 ${
                    isDark ? 'text-gray-400' : 'text-gray-600'
                  }`}>Get instant answers about insurance policies, claims, and coverage</p>
                </div>

                <SuggestedQuestions 
                  questions={DEFAULT_SUGGESTIONS}
                  onSelectQuestion={sendMessage}
                />

                {uploadedDocuments.length > 0 && (
                  <div className="mt-16 animate-fadeInUp" style={{ animationDelay: '200ms' }}>
                    <UploadedDocuments documents={uploadedDocuments} />
                  </div>
                )}
              </div>
            </div>
          ) : (
            <div className={`px-8 py-8 space-y-6 max-w-4xl mx-auto w-full transition-colors duration-500`}>
              {messages.map((msg, i) => (
                <Message key={i} msg={msg} />
              ))}
              {isStreaming && (
                <div className="flex justify-start animate-fadeInUp">
                  <div className={`px-5 py-4 rounded-2xl rounded-bl-none transition-all duration-500 ${
                    isDark
                      ? 'bg-gradient-to-r from-gray-800 to-gray-700 text-gray-200'
                      : 'bg-gradient-to-r from-gray-200 to-gray-100 text-gray-900'
                  }`}>
                    <div className="flex items-center gap-3">
                      <div className="flex gap-1">
                        <div className={`w-2 h-2 rounded-full animate-bounce ${isDark ? 'bg-blue-400' : 'bg-blue-600'}`} style={{animationDelay: "0s"}}></div>
                        <div className={`w-2 h-2 rounded-full animate-bounce ${isDark ? 'bg-blue-400' : 'bg-blue-600'}`} style={{animationDelay: "0.1s"}}></div>
                        <div className={`w-2 h-2 rounded-full animate-bounce ${isDark ? 'bg-blue-400' : 'bg-blue-600'}`} style={{animationDelay: "0.2s"}}></div>
                      </div>
                      <p className="text-sm font-medium">Analyzing your query...</p>
                    </div>
                  </div>
                </div>
              )}
              <div ref={messagesEndRef} />
            </div>
          )}
        </div>

        {/* Uploaded Documents Preview */}
        {uploadedDocuments.length > 0 && !showSuggestions && (
          <div className={`border-t transition-all duration-500 backdrop-blur-sm px-8 py-4 animate-slideInUp ${
            isDark ? 'border-gray-800/50 bg-gray-900/30' : 'border-gray-200/50 bg-gray-100/30'
          }`}>
            <div className="max-w-4xl mx-auto">
              <p className={`text-xs font-semibold uppercase tracking-wide mb-3 ${isDark ? 'text-gray-400' : 'text-gray-600'}`}>Uploaded Documents</p>
              <div className="flex flex-wrap gap-2 overflow-x-auto pb-2">
                {uploadedDocuments.map((doc, idx) => (
                  <div key={doc.id} className={`flex items-center gap-2 px-3 py-2 rounded-lg flex-shrink-0 animate-slideInLeft ${
                    isDark ? 'bg-blue-900/20 border border-blue-800/40' : 'bg-blue-100/50 border border-blue-300/40'
                  }`} style={{ animationDelay: `${idx * 50}ms` }}>
                    <span className="text-sm font-medium truncate max-w-xs">{doc.name}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* Upload Progress */}
        {isUploading && (
          <div className={`border-t transition-all duration-500 px-8 py-3 ${isDark ? 'border-gray-800/50 bg-gray-900/30' : 'border-gray-200/50 bg-gray-100/30'}`}>
            <div className="max-w-4xl mx-auto">
              <div className="h-2 w-full rounded-full overflow-hidden bg-gray-200/50">
                <div className="h-full bg-blue-500 transition-all duration-300" style={{ width: `${uploadProgress}%` }}></div>
              </div>
            </div>
          </div>
        )}

        {/* Input Area */}
        <div className={`border-t transition-all duration-500 px-8 py-6 ${
          isDark ? 'border-gray-800/50 bg-gray-900/50' : 'border-gray-200/50 bg-gray-50/50'
        }`}>
          <div className="max-w-4xl mx-auto">
            <div className="relative flex items-end gap-3">
              <button
                onClick={() => fileInputRef.current?.click()}
                disabled={isStreaming || isUploading}
                className="px-4 py-3 rounded-lg bg-gray-200 dark:bg-gray-800 text-gray-700 dark:text-gray-300"
              >
                Upload
              </button>

              <textarea
                ref={inputRef}
                value={input}
                onChange={(e) => setInput(e.target.value)}
                onKeyPress={handleKeyPress}
                placeholder="Ask me about insurance..."
                className={`flex-1 border rounded-xl px-5 py-3 resize-none focus:outline-none min-h-[52px] ${
                  isDark ? 'bg-gray-800/50 border-gray-700 text-white' : 'bg-gray-100/50 border-gray-300 text-gray-900'
                }`}
                rows="1"
              />

              <button
                onClick={() => sendMessage()}
                disabled={!input.trim() || isStreaming || isUploading}
                className="px-5 py-3 rounded-lg bg-blue-600 text-white disabled:opacity-50"
              >
                Send
              </button>
            </div>
          </div>

          <input
            ref={fileInputRef}
            type="file"
            multiple
            onChange={(e) => handleUpload(e.target.files)}
            disabled={isStreaming || isUploading}
            className="hidden"
            accept=".pdf,.doc,.docx,.txt,.xlsx,.xls"
          />
        </div>
      </div>

      <ThinkingPanel isVisible={showThinkingPanel} thinking={thinkingState} />
    </div>
  );
}