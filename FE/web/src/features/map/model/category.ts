export const CATEGORY_MAP = {
  STORES: 'FD6',
  GIFT: 'GIFT',
  MY_STORE: 'MY_STORE',
};

export type TCategory = (typeof CATEGORY_MAP)[keyof typeof CATEGORY_MAP] | null;
