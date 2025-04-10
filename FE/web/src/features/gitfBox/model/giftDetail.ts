export type TGiftDetail = {
  id: number;
  title: string;
  amount: number;
  phone_num: string;
  image: string;
  expiration_date: string;
  used_status: string;
  send_user_name: string;
  message: string;
  restaurant_id: number;
  restraunt_name: string;
  position: { lat: number; lng: number };
};
