import os
from langchain.chat_models import ChatOpenAI
from langchain.prompts import PromptTemplate
from langchain.chains import LLMChain

# OpenAI GPT-4 모델 초기화 (Chat 모델)
openai_api_key = os.getenv("OPENAI_API_KEY")
llm = ChatOpenAI(model_name="gpt-4", temperature=0.7, openai_api_key=openai_api_key)

# 프롬프트 템플릿 정의
prompt_template = PromptTemplate(
    input_variables=["food_name", "food_info"],
    template=(
        "You are a world-class food photographer and stylist. "
        "Create a vivid, detailed English prompt for a realistic photograph of a {food_name}. "
        "Incorporate the following description details: {food_info}. "
        "The prompt should focus on the visual richness and presentation of the dish."
    )
)

# LLMChain 생성
prompt_chain = LLMChain(llm=llm, prompt=prompt_template)

def generate_image_prompt(food_name: str, food_info: str) -> str:
    """음식 이름과 음식 설명을 넣어 Stable Diffusion용 이미지 프롬프트 문장을 생성"""
    # 프롬프트 생성 실행
    result = prompt_chain.run({"food_name": food_name, "food_info": food_info})
    return result.strip()
