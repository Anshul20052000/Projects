import React, { useState } from "react";
import Chat from "./components/Chat";
import Sidebar from "./components/Sidebar";
import { ThemeProvider, useTheme } from "./context/ThemeContext";

function AppContent() {
  const [conversations, setConversations] = useState([]);
  const [activeConversation, setActiveConversation] = useState(null);
  const [uploadedDocuments, setUploadedDocuments] = useState([]);
  const { isDark } = useTheme();

  const createNewConversation = () => {
    const newConv = {
      id: Date.now(),
      title: "New Conversation",
      messages: [],
      createdAt: new Date()
    };
    setConversations([newConv, ...conversations]);
    setActiveConversation(newConv.id);
  };

  const deleteConversation = (id) => {
    setConversations(conversations.filter(c => c.id !== id));
    if (activeConversation === id) {
      setActiveConversation(conversations[0]?.id || null);
    }
  };

  // Initialize with one conversation
  React.useEffect(() => {
    if (conversations.length === 0) {
      createNewConversation();
    }
  }, []);

  const currentConversation = conversations.find(c => c.id === activeConversation);

  return (
    <div className={`flex h-screen transition-colors duration-500 ${
      isDark 
        ? 'bg-gradient-to-br from-gray-950 via-gray-950 to-blue-950/10' 
        : 'bg-gradient-to-br from-gray-50 via-white to-blue-50/30'
    }`}>
      <Sidebar 
        conversations={conversations}
        activeConversation={activeConversation}
        onSelectConversation={setActiveConversation}
        onNewConversation={createNewConversation}
        onDeleteConversation={deleteConversation}
      />
      
      <div className="flex-1 flex flex-col animate-fadeInRight">
        <Chat 
          conversation={currentConversation}
          conversations={conversations}
          setConversations={setConversations}
          uploadedDocuments={uploadedDocuments}
          setUploadedDocuments={setUploadedDocuments}
        />
      </div>
    </div>
  );
}

export default function App() {
  return (
    <ThemeProvider>
      <AppContent />
    </ThemeProvider>
  );
}