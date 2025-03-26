import { Alert, Linking } from 'react-native';
import { check, PERMISSIONS, request, RESULTS, PermissionStatus } from 'react-native-permissions';

/**
 * 위치 권한을 확인하고 필요시 요청하는 함수
 * @returns 권한 처리 결과에 대한 Promise
 */
const checkLocationPermission = async (): Promise<boolean> => {
  try {
    const result: PermissionStatus = await check(PERMISSIONS.ANDROID.ACCESS_FINE_LOCATION);
    console.log('위치 권한 상태:', result);

    switch (result) {
      case RESULTS.UNAVAILABLE:
        console.log('이 기기에서는 위치 기능을 사용할 수 없습니다.');
        return false;

      case RESULTS.DENIED:
        console.log('위치 권한 요청 중...');
        const requestResult: PermissionStatus = await request(PERMISSIONS.ANDROID.ACCESS_FINE_LOCATION);
        console.log('요청 결과:', requestResult);
        return requestResult === RESULTS.GRANTED;

      case RESULTS.BLOCKED:
        // 권한이 영구적으로 차단된 경우 설정으로 이동하도록 안내
        Alert.alert(
          '위치 권한 필요',
          '앱에서 위치 정보를 사용하기 위해 권한이 필요합니다. 설정 화면에서 권한을 허용해주세요.',
          [
            {
              text: '설정으로 이동',
              onPress: () => Linking.openSettings(),
            },
            {
              text: '취소',
              style: 'cancel',
            },
          ]
        );
        return false;

      case RESULTS.GRANTED:
        console.log('위치 권한이 이미 허용되어 있습니다.');
        return true;

      default:
        return false;
    }
  } catch (error: unknown) {
    console.error('위치 권한 확인 중 오류 발생:', error);
    return false;
  }
};

export default checkLocationPermission;
