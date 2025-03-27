import { create } from 'zustand';

interface MapState {
  map: kakao.maps.Map | null;
  setMap: (map: any) => void;
}

export const useMapStore = create<MapState>((set) => ({
  map: null,
  setMap: (map) => set({ map }),
}));
