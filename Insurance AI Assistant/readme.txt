# 🧠 Insurance AI Assistant

An AI-powered web application that allows users to upload insurance policy documents (PDFs) and ask natural language questions to get accurate, context-aware answers.

---

## 🚀 Overview

Insurance documents are often lengthy, complex, and difficult for customers to understand. This project solves that problem by building a **Retrieval-Augmented Generation (RAG)** system that can:

* 📄 Process multiple policy PDFs
* 🔍 Extract and index relevant information
* 💬 Answer user queries in natural language
* 🎯 Provide precise, policy-specific responses

---

## 🧩 Key Features

* 📂 **Multi-PDF Upload**
  Upload one or more insurance policy documents directly from the UI.

* 🤖 **AI-Powered Q&A**
  Ask questions like:

  * "What is covered under this policy?"
  * "What are the claim conditions?"
  * "Is maternity covered?"

* 🧠 **Context-Aware Responses**
  Uses RAG (Retrieval-Augmented Generation) to ensure answers are grounded in the uploaded documents.

* 📑 **Source Transparency**
  Displays the relevant document snippets used to generate answers.

* ⚡ **Fast Vector Search**
  Uses FAISS for efficient similarity search across document chunks.

---

## 🏗️ Tech Stack

### 🔹 Frontend

* React (Vite)
* Tailwind CSS
* Axios

### 🔹 Backend

* FastAPI
* LangChain

### 🔹 AI / ML

* Groq LLM (LLaMA 3.1)
* HuggingFace Embeddings
* FAISS Vector Database

---

## ⚙️ How It Works

1. 📄 User uploads PDF(s)
2. ✂️ Documents are split into chunks
3. 🔢 Each chunk is converted into embeddings
4. 🗂️ Stored in FAISS vector database
5. ❓ User asks a question
6. 🔍 Relevant chunks are retrieved
7. 🤖 LLM generates answer based on context

---

## 📦 Project Structure

```bash
backend/
  ├── app/
  │   ├── api.py
  │   ├── rag.py
  ├── vectorstore/
  ├── uploads/

frontend/
  ├── src/
  │   ├── components/
  │   ├── App.jsx
```

---

## ▶️ Getting Started

### Backend

```bash
cd backend
pip install -r requirements.txt
uvicorn main:app --reload
```

---

### Frontend

```bash
cd frontend
npm install
npm run dev
```

---

## 🎯 Use Cases

* Insurance customer support automation
* Policy understanding assistant
* Claim eligibility queries
* Internal agent support tool

---

## 🔮 Future Improvements

* 📊 Policy comparison feature
* 🧾 Highlight answers directly in PDF
* 🧠 Chat history memory
* 🌐 Multi-language support
* ☁️ Cloud deployment

---

## 🏆 Conclusion

This project demonstrates how modern AI techniques like RAG and LLMs can transform static documents into interactive, intelligent assistants—enhancing user experience and reducing operational effort.

---
