import tkinter as tk
from table_screen import create_table_screen  # 테이블 화면 함수 임포트

# 전역 변수로 화면 크기 설정
global_wid = 800  # 화면 가로 크기 (수정 가능)
global_he = 600  # 화면 세로 크기 (수정 가능)

# 메인 화면을 설정하는 함수
def create_main_screen():
    # 기본 윈도우 설정
    window = tk.Tk()
    window.title("Main Screen")  # 윈도우 제목 설정
    window.geometry(f"{global_wid}x{global_he}")  # 전역변수로 화면 크기 설정
    
    # 테이블 버튼
    table_button1 = tk.Button(window, text="테이블 1", width=30, height=15, relief="solid", borderwidth=3, command=lambda: create_table_screen(1))
    table_button2 = tk.Button(window, text="테이블 2", width=30, height=15, relief="solid", borderwidth=3, command=lambda: create_table_screen(2))
    # 아래쪽 버튼. 그냥 만들어 놨음
    bottom_button1 = tk.Button(window, text="긴 버튼 1", width=50, height=2, relief="solid", borderwidth=3)
    bottom_button2 = tk.Button(window, text="긴 버튼 2", width=50, height=2, relief="solid", borderwidth=3)
    
    table_button1.place(relx=0.3, rely=0.4, anchor="center")  # 좌측에 배치
    table_button2.place(relx=0.7, rely=0.4, anchor="center")  # 우측에 배치
    
    bottom_button1.place(relx=0.25, rely=0.9, anchor="center")  # 왼쪽 버튼 배치
    bottom_button2.place(relx=0.75, rely=0.9, anchor="center")  # 오른쪽 버튼 배치

    # 윈도우 실행
    window.mainloop()

# 프로그램 실행
if __name__ == "__main__":
    create_main_screen()
