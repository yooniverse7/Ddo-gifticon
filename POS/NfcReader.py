import smartcard
from smartcard.System import readers
from smartcard.util import toHexString
import time

def hex2str(li: list) -> str:
    return ''.join(chr(int(i, 16)) for i in li)

def read_card_data(timeout=10) -> str:
    # AID 정의 (F222222233)
    aid = [0xF2, 0x22, 0x22, 0x22, 0x33]  # AID = F222222233
    
    # 연결된 NFC 리더기 찾기
    r = readers()
    if len(r) == 0:
        raise RuntimeError("NFC 리더기가 연결되지 않았습니다.")
    reader = r[0]
    # print(f"리더기: {reader}")


    # 카드가 접촉되면 데이터를 읽기
    print("카드를 리더기에 올려주세요...")
    start_time = time.time()
    while True:
        # 5초 이내에 카드가 접촉되지 않으면 종료
        if time.time() - start_time > timeout:
            print(f"카드가 {timeout}초 이내에 접촉되지 않았습니다.")
            break
        
        try:
            # 리더기와 연결 시도 -> 에러 발생 가능
            connection = reader.createConnection()
            connection.connect()

            # 카드에서 IsoDep 프로토콜을 통해 APDU 명령어 보내기 (일반적으로 카드 식별을 위한 명령)
            command = [0x00, 0xA4, 0x04, 0x00] + [len(aid)] + aid  # 예: SELECT 명령
            response, sw1, sw2 = connection.transmit(command)

            # 응답 출력
            print(f"응답: {toHexString(response)}")
            print(f"상태 워드: {sw1:02X} {sw2:02X}")
            
            # 카드가 유효하면 데이터 처리 (예: 카드 정보를 읽음)
            if sw1 == 0x90 and sw2 == 0x00:
                print("카드 데이터를 읽는데 성공했습니다.")
                card_data = hex2str(toHexString(response).split(" "))
                print(f"카드 데이터: {card_data}")

                connection.disconnect()
                return card_data
            else:
                print("카드 데이터 읽기 실패")
                connection.disconnect()
                return ""

        except Exception as e:
            # print(f"오류 발생: {e}")
            time.sleep(0.3)

    # 연결 종료
    connection.disconnect()
    return ""

