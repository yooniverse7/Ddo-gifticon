import tkinter as tk
from order_data import order_data, menu_items  # 주문 데이터와 메뉴 정보를 임포트
from payment_screen import payment_logic

total_price_int = 0

# 테이블 화면을 구성하는 함수
def create_table_screen(table_number):
    # 화면 크기 설정
    window = tk.Toplevel()
    window.title(f"Table {table_number}")  # 테이블 번호로 제목 설정
    window.geometry("800x600")  # 기본 크기 설정

    # 1번 화면 (주문 내역 화면) - 주문내역 버튼들이 세로로 정렬
    frame1 = tk.Frame(window, bd=1, relief="solid")
    frame1.place(relx=0.0, rely=0.0, relwidth=0.6, relheight=0.8)
    
    # 2번 화면 (메뉴 화면) - 메뉴 버튼들이 세로로 정렬
    frame2 = tk.Frame(window, bd=1, relief="solid")
    frame2.place(relx=0.6, rely=0.0, relwidth=0.4, relheight=0.8)
    
    # 3번 화면 (가격 총합 화면) - 총합 가격을 표시
    total_price_label = tk.Label(window, text="Total: 0", font=("Arial", 14))
    total_price_label.place(relx=0.0, rely=0.8, relwidth=0.6, relheight=0.2)

    # 4번 화면 (4개의 버튼)
    frame4 = tk.Frame(window, bd=1, relief="solid")
    frame4.place(relx=0.6, rely=0.8, relwidth=0.4, relheight=0.2)

    # 1번 화면에 주문 내역을 표시하는 함수
    def update_order_display():
        global total_price_int
        # 주문 내역을 화면에 표시
        for widget in frame1.winfo_children():
            widget.destroy()  # 기존에 있던 주문 내역 버튼을 삭제
        
        total_price = 0
        for idx, (item, (quantity, price)) in enumerate(order_data.get(table_number, {}).items()):
            if quantity > 0:
                total_price += price * quantity
                button = tk.Button(frame1, text=f"{item} - {quantity}개 - {price * quantity}원", command=lambda item=item: decrease_quantity(item), font=("Arial", 14), height=2)
                button.pack(fill="x")
        
        total_price_label.config(text=f"Total: {total_price}원")
        total_price_int = total_price

    # 2번 화면에서 메뉴 버튼 클릭 시 호출되는 함수
    def add_to_order(item_name):
        if table_number not in order_data:
            order_data[table_number] = {}

        # 메뉴가 이미 존재하면 수량을 증가시키고, 없으면 새로 추가
        if item_name in order_data[table_number]:
            order_data[table_number][item_name] = (order_data[table_number][item_name][0] + 1, menu_items[item_name])
        else:
            order_data[table_number][item_name] = (1, menu_items[item_name])
        
        update_order_display()  # 주문 내역 갱신

    # 1번 화면의 버튼 클릭 시 수량 감소 함수
    def decrease_quantity(item_name):
        if table_number in order_data and item_name in order_data[table_number]:
            current_quantity, price = order_data[table_number][item_name]
            if current_quantity > 1:
                order_data[table_number][item_name] = (current_quantity - 1, price)
            else:
                del order_data[table_number][item_name]
            update_order_display()

    # 결제 버튼 클릭 시 호출되는 함수
    def payment_button_click():
        global total_price_int
        if total_price_int == 0:
            print("주문이 없습니다.")
            return

        result = payment_logic(total_price_int)
        if result:
            print("진짜 미쳤다. 다 했다")
            order_data[table_number] = {}
            window.destroy()
        else:
            print("you died guys")

    # 2번 화면의 메뉴 버튼 생성
    for item_name in menu_items:
        button = tk.Button(frame2, text=f"{item_name} - {menu_items[item_name]}원", command=lambda item_name=item_name: add_to_order(item_name), font=("Arial", 14), height=2)
        button.pack(fill="x")

    # 초기 주문 내역 갱신
    update_order_display()

    # 4번 화면에 4개의 버튼 생성
    button1 = tk.Button(frame4, text="결제", font=("Arial", 16), command=lambda:payment_button_click())
    button1.grid(row=0, column=0, sticky="nsew")

    button2 = tk.Button(frame4, text="버튼2", font=("Arial", 16))
    button2.grid(row=0, column=1, sticky="nsew")

    button3 = tk.Button(frame4, text="버튼3", font=("Arial", 16))
    button3.grid(row=1, column=0, sticky="nsew")

    button4 = tk.Button(frame4, text="닫기", font=("Arial", 16), command=window.destroy)  # "닫기" 버튼 클릭 시 창 닫기
    button4.grid(row=1, column=1, sticky="nsew")


    # 4개의 버튼 크기 동일하게 만들기
    for i in range(2):
        frame4.grid_columnconfigure(i, weight=1)
        frame4.grid_rowconfigure(i, weight=1)
