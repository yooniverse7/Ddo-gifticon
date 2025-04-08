import React, {useEffect, useRef, useCallback} from 'react';
import {WebView, WebViewMessageEvent} from 'react-native-webview';

import {handleWebViewMessage} from './src/features/contactServices';
import SplashScreen from 'react-native-splash-screen';
import {NativeModules} from 'react-native';

const {DdopayNFC} = NativeModules;

function App() {
  const webViewRef = useRef<WebView>(null);

  useEffect(() => {
    const timer = setTimeout(() => {
      SplashScreen.hide();
    }, 1000);
    return () => clearTimeout(timer);
  }, []);

  const onMessage = useCallback((event: WebViewMessageEvent) => {
    const data = JSON.parse(event.nativeEvent.data);
    if (data.type === 'PAYMENT_REQUEST') {
      if (DdopayNFC) {
        DdopayNFC.startNfcService(data.token);
        console.log(data.token);
      }
      return;
    }
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
      source={{uri: 'https://j12e106.p.ssafy.io'}}
      style={{flex: 1}}
      javaScriptEnabled={true}
      onMessage={onMessage}
      ref={webViewRef}
    />
  );
}

export default App;
