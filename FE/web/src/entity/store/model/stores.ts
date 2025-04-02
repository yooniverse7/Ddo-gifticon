export type TFavoriteStores = {
  id: number;
  place_name: string;
  main_image_url: string;
  address_name: string;
  position: {
    lat: number;
    lng: number;
  };
  user_intro: string;
  star_rating: number;
  visited_count: number;
  menus?: [
    {
      id: number;
      menu_name: string;
      menu_desc: string;
      menu_price: string;
      menu_image: string;
    }
  ];
  custom_menu?: [
    {
      id: number;
      custom_menu_name: string;
      custom_menu_price: string;
      custom_menu_image: string;
    }
  ];
};
