import os
import torch
from uuid import uuid4
from diffusers import StableDiffusionPipeline
import boto3

# Stable Diffusion 모델 로드 (v1-5 모델 예시)
model_name = "runwayml/stable-diffusion-v1-5"

# CPU 사용 여부에 따라 torch_dtype 결정: GPU에서는 float16, CPU에서는 float32
device = "cuda" if torch.cuda.is_available() else "cpu"
dtype = torch.float16 if device == "cuda" else torch.float32

pipe = StableDiffusionPipeline.from_pretrained(
    model_name,
    torch_dtype=dtype
)
# 성능 향상을 위해 안전 검사기를 비활성화 (필요 시)
pipe.safety_checker = lambda images, **kwargs: (images, False)

pipe.to(device)

def generate_image(prompt: str) -> str:
    """Stable Diffusion 파이프라인으로 주어진 프롬프트의 이미지를 생성하고 로컬 경로에 저장, 파일 경로를 반환"""
    result = pipe(prompt, num_inference_steps=50, guidance_scale=7.5)
    image = result.images[0]
    filename = f"generated_{uuid4().hex}.png"
    image.save(filename)
    return filename

# AWS S3 설정 (환경변수에서 값 로드)
s3_bucket = os.getenv("AWS_S3_BUCKET_NAME")
aws_region = os.getenv("AWS_DEFAULT_REGION")
s3 = boto3.client('s3', region_name=aws_region)

def upload_to_s3(file_path: str) -> str:
    """주어진 파일을 S3 버킷에 업로드하고 공개 URL을 반환"""
    filename = os.path.basename(file_path)
    s3_key = f"generated_images/{filename}"
    s3.upload_file(file_path, s3_bucket, s3_key)
    url = f"https://{s3_bucket}.s3.{aws_region}.amazonaws.com/{s3_key}"
    return url
