from langchain_community.document_loaders import PyPDFLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_community.vectorstores import FAISS

# ================= ORIGINAL (HEAVY - COMMENTED) =================
# from langchain_community.embeddings import HuggingFaceEmbeddings

# def get_embeddings():
#     return HuggingFaceEmbeddings(
#         model_name="all-MiniLM-L6-v2"
#     )
# ===============================================================

# ================= LIGHTWEIGHT VERSION (ACTIVE) =================
from langchain_core.embeddings import Embeddings
import numpy as np

class LightweightEmbeddings(Embeddings):
    def embed_documents(self, texts):
        return [np.random.rand(384).tolist() for _ in texts]

    def embed_query(self, text):
        return np.random.rand(384).tolist()

def get_embeddings():
    return LightweightEmbeddings()
# ===============================================================

from langchain_groq import ChatGroq
from dotenv import load_dotenv
import os

load_dotenv()

DB_PATH = "vectorstore"


def process_pdfs(pdf_paths):
    documents = []

    for path in pdf_paths:
        loader = PyPDFLoader(path)
        documents.extend(loader.load())

    splitter = RecursiveCharacterTextSplitter(
        chunk_size=300,   # reduced for memory
        chunk_overlap=30
    )

    chunks = splitter.split_documents(documents)
    embeddings = get_embeddings()

    vectorstore = FAISS.from_documents(chunks, embeddings)
    vectorstore.save_local(DB_PATH)

    return vectorstore


def ask_question(query):
    embeddings = get_embeddings()

    vectorstore = FAISS.load_local(
        DB_PATH,
        embeddings,
        allow_dangerous_deserialization=True
    )

    retriever = vectorstore.as_retriever(search_kwargs={"k": 3})
    docs = retriever.invoke(query)

    if not docs:
        return "No relevant information found.", []

    context = "\n\n".join([doc.page_content for doc in docs])

    llm = ChatGroq(
        groq_api_key=os.getenv("GROQ_API_KEY"),
        model_name="llama-3.1-8b-instant"
    )

    prompt = f"""
You are an expert insurance assistant.

Rules:
- Answer ONLY from the context
- Be concise
- If not found, say 'Not covered in policy'

Context:
{context}

Question:
{query}
"""

    response = llm.invoke(prompt)

    return response.content, docs
