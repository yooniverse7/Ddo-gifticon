import { API_URL, BASE_URL } from '@/shared/constants/url';
import { http, HttpResponse } from 'msw';
import { favoriteStores } from '../data/store';

export const storeHandlers = [
  http.get(`${BASE_URL}${API_URL.favoriteStores}`, () => {
    console.log('최애 식당');
    return HttpResponse.json(favoriteStores);
  }),
];
