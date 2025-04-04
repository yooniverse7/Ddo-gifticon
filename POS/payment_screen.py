import tkinter as tk
import threading
import requests
import time
from NfcReader import read_card_data  # 카드 데이터를 읽어오는 메소드 임포트
from order_data import order_data  # 테이블 주문 데이터
from queue import Queue

log_text = ""

# 로그 메세지를 업데이트하는 함수
def append_log(message: str):
    global log_text
    log_text += message + "\n"

# 실제로 log_box를 업데이트하는 함수 (메인 스레드에서만 실행됨)
def update_log_box(log_box):
    global log_text
    log_box.config(state=tk.NORMAL)
    log_box.delete(1.0, tk.END)  # 기존 텍스트 지우기
    log_box.insert(tk.END, log_text)  # 새로운 로그 삽입
    log_box.config(state=tk.DISABLED)
    log_box.yview(tk.END)  # 자동으로 최신 로그로 스크롤

# NFC 태깅 메소드
def read_nfc_card(result_queue: Queue) -> str:
    append_log("카드 태깅 대기 중...")
    card_data = read_card_data()
    result_queue.put(card_data)  # 결과를 큐에 넣어 메인 스레드로 전달

# http 통신 보내기
def send_payment_request(card_data, total_price) -> bool:
    url = "https://j12e106.p.ssafy.io/api/pay/pos"

    headers = {
        'Content-Type': 'application/json',
        'xx-auth': 'acc-tkn'
    }

    data = {
        "storeAccount": "9994113480025743",  # 가게 계좌 - 하드코딩
        "paymentToken": card_data,  # 기프티콘 토큰
        "paymentAmount": total_price  # 총 금액
    }

    append_log("서버에 결제 요청 보내는 중...")
    try:
        # POST 요청 보내기
        response = requests.post(url, headers=headers, json=data)
        if response.status_code == 200:
            append_log(f"서버 요청 성공")
            return True
        else:
            append_log(f"결제 서버 오류 (HTTP {response.status_code})")
            return False
    except Exception as e:
        append_log(f"결제 요청 중 오류 발생: {str(e)}")
        return False

# 스레드 메소드 : nfc -> http통신 -> 결과 반환
def payment_thread(window, total_price: int, result_queue: Queue):
    # nfc 통신으로 정보 받기
    read_nfc_card(result_queue)

    card_data = result_queue.get()  # 메인 스레드로부터 카드 데이터 받기

    if card_data == "":
        append_log("카드 읽기 실패. 다시 시도해 주세요.")
        result_queue.put(False)
    else:  # 카드 데이터가 정상적으로 읽혔을 경우
        append_log(f"카드 데이터 읽기 성공: {card_data}")
        # http 통신
        result = send_payment_request(card_data, total_price)
        # result = True
        # 결과 리턴
        append_log(f"결제 결과: {'성공' if result else '실패'}") 
        result_queue.put(result)
    time.sleep(1)
    window.quit()  # GUI 종료

# 메인
def payment_logic(total_price) -> bool:
    global log_text
    # 초기화
    window = tk.Tk()
    window.title("결제 처리 중...")
    window.geometry("400x300")

    # 로그 출력 라벨
    log_label = tk.Label(window, text="태그 대기중", font=("Arial", 14), pady=20)
    log_label.pack()
    log_box = tk.Text(window, wrap="word", height=10, width=40, font=("Arial", 12))
    log_box.pack(pady=10)
    log_box.config(state=tk.DISABLED)

    result_queue = Queue()

    payment_thread_instance = threading.Thread(target=payment_thread, args=(window,total_price, result_queue))
    payment_thread_instance.daemon = True  # 윈도우 종료 시 스레드도 종료되도록 설정
    payment_thread_instance.start()

    # GUI를 계속 업데이트하여 응답 유지
    # 예외 처리 추가
    def keep_gui_alive():
        try:
            if payment_thread_instance.is_alive():
                window.after(100, keep_gui_alive)  # 100ms마다 호출하여 계속 갱신
            update_log_box(log_box)  # 메인 스레드에서 로그 박스를 갱신
        except Exception as e:
            print(f"Error in keep_gui_alive: {str(e)}")
    # GUI가 응답을 계속 유지하도록 함
    window.after(0, keep_gui_alive)  

    window.mainloop()
    
    # 결과값을 확인하고 UI 종료 후 반환
    result = result_queue.get()
    window.destroy()
    log_text = ""
    return result
