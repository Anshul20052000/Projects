from fastapi import FastAPI, UploadFile, File, Query
from fastapi.middleware.cors import CORSMiddleware
import shutil
import os

from app.rag import process_pdfs, ask_question

app = FastAPI()

# ✅ CORS (restrict in production)
app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "https://your-frontend-url.vercel.app"  # 👈 replace this
    ],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

UPLOAD_DIR = "uploads"
os.makedirs(UPLOAD_DIR, exist_ok=True)


# ✅ Root route (fix 404 confusion)
@app.get("/")
def home():
    return {"message": "Insurance AI Backend is running 🚀"}


# ✅ Upload API
@app.post("/upload")
async def upload(files: list[UploadFile] = File(...)):
    paths = []

    for file in files:
        path = os.path.join(UPLOAD_DIR, file.filename)

        with open(path, "wb") as buffer:
            shutil.copyfileobj(file.file, buffer)

        paths.append(path)

    process_pdfs(paths)

    return {"message": "Processed successfully"}


# ✅ Ask API (fix: explicit query param)
@app.post("/ask")
async def ask(query: str = Query(...)):
    try:
        answer, docs = ask_question(query)

        return {
            "answer": answer,
            "sources": [d.page_content[:300] for d in docs]
        }

    except Exception as e:
        return {
            "error": str(e)
        }
