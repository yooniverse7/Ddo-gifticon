import { Alert, Linking } from 'react-native';
import { check, PERMISSIONS, request, RESULTS, PermissionStatus } from 'react-native-permissions';

/**
 * 저장소(사진첩) 접근 권한을 확인하고 필요시 요청하는 함수
 * @returns 권한 처리 결과에 대한 Promise
 */
const checkStoragePermission = async (): Promise<boolean> => {
  try {
    // 이미지 권한 상태 확인 (기준 권한으로 사용)
    const result: PermissionStatus = await check(PERMISSIONS.ANDROID.READ_MEDIA_IMAGES);
    console.log('저장소 권한 상태:', result);

    switch (result) {
      case RESULTS.UNAVAILABLE:
        console.log('이 기기에서는 저장소 접근 기능을 사용할 수 없습니다.');
        return false;

      case RESULTS.DENIED:
        console.log('저장소 권한 요청 중...');
        // 모든 미디어 권한 요청
        const imageResult = await request(PERMISSIONS.ANDROID.READ_MEDIA_IMAGES);
        const videoResult = await request(PERMISSIONS.ANDROID.READ_MEDIA_VIDEO);
        const audioResult = await request(PERMISSIONS.ANDROID.READ_MEDIA_AUDIO);

        console.log('미디어 권한 요청 결과:', { imageResult, videoResult, audioResult });

        // 모든 권한이 허용되었는지 확인
        return (
          imageResult === RESULTS.GRANTED &&
          videoResult === RESULTS.GRANTED &&
          audioResult === RESULTS.GRANTED
        );

      case RESULTS.BLOCKED:
        // 권한이 영구적으로 차단된 경우 설정으로 이동하도록 안내
        Alert.alert(
          '사진 접근 권한 필요',
          '앱에서 사진첩/갤러리에 접근하기 위해 권한이 필요합니다. 설정 화면에서 권한을 허용해주세요.',
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
        console.log('이미지 권한이 이미 허용되어 있습니다. 나머지 권한 확인 중...');
        // 다른 권한들도 이미 허용되어 있는지 확인
        const videoCheck = await check(PERMISSIONS.ANDROID.READ_MEDIA_VIDEO);
        const audioCheck = await check(PERMISSIONS.ANDROID.READ_MEDIA_AUDIO);

        // 모든 권한이 이미 허용되어 있다면 true 반환
        if (videoCheck === RESULTS.GRANTED && audioCheck === RESULTS.GRANTED) {
          console.log('모든 미디어 권한이 이미 허용되어 있습니다.');
          return true;
        }

        // 일부 권한이 없다면 요청
        console.log('일부 미디어 권한이 없습니다. 권한 요청 중...');
        const missingVideoResult = videoCheck !== RESULTS.GRANTED ?
          await request(PERMISSIONS.ANDROID.READ_MEDIA_VIDEO) : RESULTS.GRANTED;
        const missingAudioResult = audioCheck !== RESULTS.GRANTED ?
          await request(PERMISSIONS.ANDROID.READ_MEDIA_AUDIO) : RESULTS.GRANTED;

        return (
          missingVideoResult === RESULTS.GRANTED &&
          missingAudioResult === RESULTS.GRANTED
        );

      default:
        return false;
    }
  } catch (error: unknown) {
    console.error('저장소 권한 확인 중 오류 발생:', error);
    return false;
  }
};

export default checkStoragePermission;
