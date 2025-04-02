import React, {useEffect, useRef, useCallback} from 'react';
import {WebView, WebViewMessageEvent} from 'react-native-webview';
import {
  checkCameraAndAudioPermissions,
  checkContactsPermission,
  checkLocationPermission,
  checkNotificationPermission,
  checkStoragePermission,
} from './src/shared/utils/';
import {handleWebViewMessage} from './src/features/contactServices';

function App() {
  const webViewRef = useRef<WebView>(null);

  useEffect(() => {
    const authorize = async () => {
      await checkLocationPermission();
      await checkCameraAndAudioPermissions();
      await checkContactsPermission();
      await checkNotificationPermission();
      await checkStoragePermission();
    };
    authorize();
  }, []);
  const onMessage = useCallback((event: WebViewMessageEvent) => {
    handleWebViewMessage(event, message => {
      webViewRef.current?.postMessage(message);
    });
  }, []);

  return (
    <WebView
      source={{uri: 'http://localhost:3000'}}
      style={{flex: 1}}
      javaScriptEnabled={true}
      onMessage={onMessage}
      ref={webViewRef}
    />
  );
}

export default App;
