export type FavoriteStores = {
  id: number;
  place_name: string;
  restaurant_image: string;
  address_name: string;
  position: {
    lat: number;
    lng: number;
  };
  visited_count: number;
};
