export type FavoriteStores = {
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
};
