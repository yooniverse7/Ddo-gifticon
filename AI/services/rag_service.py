import os
from langchain.embeddings import OpenAIEmbeddings
from langchain.vectorstores import FAISS
from langchain.docstore.document import Document

# 음식 설명 더미 데이터 정의 (실제로는 파일에서 읽어오도록 구성 가능)
food_texts = {
    "Hamburger": "A hamburger is a sandwich consisting of a cooked patty of ground meat, usually beef, placed inside a sliced bread roll or bun. It often includes lettuce, tomato, cheese, and condiments.",
    "Pizza": "Pizza is an Italian dish consisting of a round, flattened base of dough topped with tomatoes, cheese, and various other ingredients and baked at a high temperature.",
    "French Fries": "French fries are potatoes cut into strips and deep-fried until crispy and golden. Commonly served salted, often as a side dish."
    # 필요에 따라 다른 음식도 추가
}

# Document 객체 리스트로 변환 (LangChain의 Document 사용)
documents = [Document(page_content=text, metadata={"food": name}) for name, text in food_texts.items()]

# OpenAI 임베딩 모델 초기화 (환경변수에서 API 키 로드)
openai_api_key = os.getenv("OPENAI_API_KEY")
embeddings = OpenAIEmbeddings(openai_api_key=openai_api_key)

# FAISS 벡터스토어 생성 (documents를 임베딩하여 인덱스 구축)
vector_store = FAISS.from_documents(documents, embedding=embeddings)

# 검색 함수: 음식 이름을 넣으면 가장 유사한 설명 텍스트 반환
def search_food_description(query: str) -> str:
    """음식 이름(query)에 대한 가장 관련 높은 설명 텍스트 반환"""
    if not query:
        return ""
    # 벡터 스토어에서 유사도 검색
    results = vector_store.similarity_search(query, k=1)
    if results:
        return results[0].page_content  # 가장 관련 있는 설명 텍스트
    else:
        return ""
