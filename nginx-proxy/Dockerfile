# nginx-proxy/Dockerfile

# 1. Nginx 최신 버전 베이스 이미지 사용
FROM nginx:latest

# 2. 커스텀 nginx.conf 파일을 Nginx 설정 위치에 복사
COPY nginx.conf /etc/nginx/nginx.conf

# 3. (선택) health check, 로그 설정 등도 나중에 추가 가능
