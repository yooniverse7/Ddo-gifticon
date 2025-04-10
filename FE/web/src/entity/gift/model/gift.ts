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

export type TSentGift = {
  id: number;
  title: string;
  image: string;
  amount: string;
  menu_name: string;
  phone_num: string;
  expiration_date: string;
  used_status: UsedStatus;
  res_id: number;
  position: {
    lat: number;
    lng: number;
  };
};
