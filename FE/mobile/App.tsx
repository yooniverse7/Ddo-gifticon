import React, {useEffect, useRef, useCallback} from 'react';
import {WebView, WebViewMessageEvent} from 'react-native-webview';

import {handleWebViewMessage} from './src/features/contactServices';
import SplashScreen from 'react-native-splash-screen';

function App() {
  const webViewRef = useRef<WebView>(null);

  useEffect(() => {
    const timer = setTimeout(() => {
      SplashScreen.hide();
    }, 1000);
    return () => clearTimeout(timer);
  }, []);
  const onMessage = useCallback((event: WebViewMessageEvent) => {
    handleWebViewMessage(
      event,
      message => {
        webViewRef.current?.postMessage(message);
      },
      () => {
        webViewRef.current?.reload();
      },
    );
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
