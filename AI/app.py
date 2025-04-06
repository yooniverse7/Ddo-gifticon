# app.py의 최상단에 추가
from dotenv import load_dotenv
import os

load_dotenv()  # .env 파일의 환경변수를 로드
print("OPENAI_API_KEY =", os.getenv("OPENAI_API_KEY"))  # 디버그 출력

from fastapi import FastAPI
from pydantic import BaseModel
from dotenv import load_dotenv
# 서비스 모듈 import
from services.rag_service import search_food_description
from services.prompt_service import generate_image_prompt
from services.image_service import generate_image, upload_to_s3

app = FastAPI()


# 요청 바디 모델 정의
class GenerateRequest(BaseModel):
    food_name: str

# 이미지 생성 엔드포인트
@app.post("/generate")
def generate_endpoint(req: GenerateRequest):
    # 1. 음식 이름 추출
    food_name = req.food_name
    
    # 2. 음식 설명 검색 (RAG)
    food_info = search_food_description(food_name)
    
    # 3. GPT-4로 이미지 프롬프트 생성
    prompt = generate_image_prompt(food_name, food_info)
    
    # 4. Stable Diffusion으로 이미지 생성
    image_path = generate_image(prompt)
    
    # 5. S3에 이미지 업로드 및 URL 획득
    image_url = upload_to_s3(image_path)
    
    # 6. 결과 반환 (생성 프롬프트와 이미지 URL)
    return {"food_name": food_name, "prompt": prompt, "image_url": image_url}
