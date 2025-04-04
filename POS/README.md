npm install 으로 다음 라이브러리 설치
pyscard
requests

main_screen.py 실행하기

order_data.py에서 메뉴 데이터 수정

- main_screen.py : 실행 시 제일 처음 나오는 화면
- table_screen.py : 메인화면에서 테이블 버튼 누르면 나오는 화면. 오른쪽의 메뉴를 눌러 주문 내역을 추가하고, 주문 내역을 눌러 취소 가능. 닫기 버튼을 누르면 화면이 닫히는데 주문내역이 없어지지는 않습니다. 결제 버튼을 누르면 결제 시스템이 활성화 됩니다.

- payment_screen.py : 결제 로그 화면과 결제 로직이 있는 파일
- NfcReader.py : nfc 활성화 / 인식 로직이 있는 파일
- order_data.py : 메뉴와 주문내역을 관리할 수 있는 파일 
