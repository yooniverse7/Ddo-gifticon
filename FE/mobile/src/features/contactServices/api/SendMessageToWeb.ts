import {WebViewMessageEvent} from 'react-native-webview';
import ContactService from './GetContacts';
import {requestAllPermissionsAndSetCookie} from '../../../shared/utils/CheckPermission';
export const handleWebViewMessage = async (
  event: WebViewMessageEvent,
  postMessage: (message: string) => void,
  reload: () => void,
) => {
  try {
    const data = JSON.parse(event.nativeEvent.data);
    if (data.type === 'OPEN_CONTACTS') {
      try {
        const contacts = await ContactService.getContacts();
        console.log(contacts);
        postMessage(
          JSON.stringify({
            type: 'CONTACTS_RESPONSE',
            success: true,
            contacts,
          }),
        );
      } catch (error) {
        if (error instanceof Error) {
          postMessage(
            JSON.stringify({
              type: 'CONTACTS_ERROR',
              success: false,
              message: error.message,
            }),
          );
        }
      }
    } else if (data.type === 'OPEN_PERMISSION_REQUEST') {
      try {
        const granted = await requestAllPermissionsAndSetCookie();
        if (granted) {
          setTimeout(() => {
            console.log('WebView reload 실행');
            reload();
          }, 100);
        }
        postMessage(
          JSON.stringify({
            type: 'OPEN_PERMISSION_REQUEST',
            success: true,
          }),
        );
      } catch (error) {
        postMessage(
          JSON.stringify({
            type: 'OPEN_PERMISSION_REQUEST',
            success: false,
          }),
        );
      }
    }
  } catch (e) {
    console.error('메시지 파싱 오류', e);
  }
};
