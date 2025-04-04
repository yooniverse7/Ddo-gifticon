export type TMarketResponse = {
  id: number;
  place_name: string;
  address_name: string;
  main_image_url: string;
  position: {
    lat: number;
    lng: number;
  };
  place_id: number | null;
  restaurant_id: number | null;
  star_rating: number | null;
  user_intro: string | null;
  visited_count: number;
  content?: {
    menu: TMenu[];
    custom_menu: TCustomMenu[];
  };
};

export type TMenu = {
  menu_id: number;
  menu_name: string;
  menu_desc: string;
  menu_price: string;
  menu_image: string;
};

export type TCustomMenu = {
  custom_menu_id: number;
  custom_menu_name: string;
  custom_menu_price: string;
  custom_menu_image: string;
};
