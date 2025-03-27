export type Coordinates = {
  lat: number;
  lng: number;
};

export type Marker = {
  id: string | number;
  position: Coordinates;
  place_name: string;
  address_name: string;
  place_url: string;
  visited_count?: number;
};
