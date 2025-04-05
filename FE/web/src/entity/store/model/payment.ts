export type TPayment = {
  id: number;
  title: string;
  time: string;
  in_out_amount: number;
  type: string;
};

export type TPaymentResponse = {
  status: {
    code: number;
    message: string;
  };
  content: TPayment[];
};
