import { Alert, Linking } from 'react-native';
import { check, PERMISSIONS, request, RESULTS, PermissionStatus } from 'react-native-permissions';

// 설정으로 이동하는 Alert 표시 함수
const showSettingsAlert = (permissionType: string): void => {
  Alert.alert(
    `${permissionType} 권한 필요`,
    `앱에서 ${permissionType}를 사용하기 위해 권한이 필요합니다. 설정 화면에서 권한을 허용해주세요.`,
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
};

// 카메라 및 오디오 권한 함께 요청 함수
export const checkCameraAndAudioPermissions = async (): Promise<boolean> => {
  try {
    // 카메라 권한 확인
    const cameraResult: PermissionStatus = await check(PERMISSIONS.ANDROID.CAMERA);
    // 오디오 권한 확인
    const audioResult: PermissionStatus = await check(PERMISSIONS.ANDROID.RECORD_AUDIO);

    console.log('카메라 권한 상태:', cameraResult);
    console.log('오디오 권한 상태:', audioResult);

    // 카메라 권한이 필요한 경우
    if (cameraResult === RESULTS.DENIED) {
      console.log('카메라 권한 요청 중...');
      const cameraRequestResult: PermissionStatus = await request(PERMISSIONS.ANDROID.CAMERA);
      console.log('카메라 요청 결과:', cameraRequestResult);
    } else if (cameraResult === RESULTS.BLOCKED) {
      showSettingsAlert('카메라');
      return false; // 설정으로 이동하는 얼럿이 표시된 경우 함수 종료
    }

    // 오디오 권한이 필요한 경우
    if (audioResult === RESULTS.DENIED) {
      console.log('오디오 권한 요청 중...');
      const audioRequestResult: PermissionStatus = await request(PERMISSIONS.ANDROID.RECORD_AUDIO);
      console.log('오디오 요청 결과:', audioRequestResult);
    } else if (audioResult === RESULTS.BLOCKED) {
      showSettingsAlert('오디오');
      return false;
    }

    // 최종 권한 상태 확인
    const finalCameraResult: PermissionStatus = await check(PERMISSIONS.ANDROID.CAMERA);
    const finalAudioResult: PermissionStatus = await check(PERMISSIONS.ANDROID.RECORD_AUDIO);

    if (finalCameraResult === RESULTS.GRANTED && finalAudioResult === RESULTS.GRANTED) {
      console.log('모든 권한이 허용되어 있습니다. 카메라 기능을 사용할 수 있습니다.');
      return true;
    } else {
      console.log('일부 권한이 거부되어 있습니다.');
      return false;
    }
  } catch (error) {
    console.error('권한 확인 중 오류 발생:', error);
    return false;
  }
};


