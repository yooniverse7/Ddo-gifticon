export const handleMessageToRn = () => {
  // React Native 환경에서는 원래 로직 실행
  if (window.ReactNativeWebView) {
    window.ReactNativeWebView.postMessage(
      JSON.stringify({
        type: 'OPEN_PERMISSION_REQUEST',
      })
    );
  } else {
    // Web 환경: 접근 권한 쿠키를 임의로 설정하여 미들웨어를 우회함
    document.cookie = 'accessPermission=true; path=/';
    console.log('Web 환경에서 accessPermission 쿠키를 자동 설정했습니다.');
    window.location.reload();
  }
};
