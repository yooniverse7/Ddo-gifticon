export type TPayment = {
  status: {
    code: number;
    message: string;
  };
  content: {
    title: string;
    time: string;
    in_out_amount: number;
    type: string;
  }[];
};
