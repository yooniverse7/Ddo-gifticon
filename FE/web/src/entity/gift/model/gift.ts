export type UsedStatus = 'BEFORE_USE' | 'AFTER_USE' | 'EXPIRED' | 'CANCLE';

export type TGift = {
  id: number;
  title: string;
  send_user_name: string;
  expiration_date: string;
  image: string;
  used_status: UsedStatus;
  position: {
    lat: number;
    lng: number;
  };
};
export type TGiftResponse = {
  status: {
    code: string;
    message: string;
  };
  content: TGift[];
};
