import { RESULTS,checkNotifications, NotificationsResponse, requestNotifications } from 'react-native-permissions';

/**
 * 위치 권한을 확인하고 필요시 요청하는 함수
 * @returns 권한 처리 결과에 대한 Promise
 */
const checkNotificationPermission = async (): Promise<boolean> => {
  try {
    const result: NotificationsResponse = await checkNotifications();
    console.log('알림 권한 상태:', result.status);

    switch (result.status) {
      case RESULTS.UNAVAILABLE:
        console.log('이 기기에서는 알림 기능을 사용할 수 없습니다.');
        return false;

      case RESULTS.DENIED:
        console.log('알림 권한 요청 중...');
        const requestResult: NotificationsResponse = await requestNotifications();
        console.log('요청 결과:', requestResult);
        return requestResult.status === RESULTS.GRANTED;

      case RESULTS.GRANTED:
        console.log('알림 권한이 이미 허용되어 있습니다.');
        return true;

      default:
        return false;
    }
  } catch (error: unknown) {
    console.error('알림 권한 확인 중 오류 발생:', error);
    return false;
  }
};

export default checkNotificationPermission;
