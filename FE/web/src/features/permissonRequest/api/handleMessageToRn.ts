export const handleMessageToRn = () => {
  // React Native 환경에서는 원래 로직 실행
  if (window.ReactNativeWebView) {
    window.ReactNativeWebView.postMessage(
      JSON.stringify({
        type: 'OPEN_PERMISSION_REQUEST',
      })
    );
  }
};
