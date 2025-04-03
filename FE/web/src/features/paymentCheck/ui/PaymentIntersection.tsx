interface PaymentIntersectionProps {
  listShowState: 'all' | 'in' | 'out';
  setListShowState: (state: 'all' | 'in' | 'out') => void;
}

export const PaymentIntersection = ({
  listShowState,
  setListShowState,
}: PaymentIntersectionProps) => {
  return (
    <div className='flex border-b'>
      <button
        className={`flex-1 py-3 text-center ${
          listShowState === 'all'
            ? 'text-gray-900 border-b-2 border-gray-900'
            : 'text-gray-500'
        }`}
        onClick={() => setListShowState('all')}
      >
        전체
      </button>
      <button
        className={`flex-1 py-3 text-center ${
          listShowState === 'in'
            ? 'text-gray-900 border-b-2 border-gray-900'
            : 'text-gray-500'
        }`}
        onClick={() => setListShowState('in')}
      >
        입금
      </button>
      <button
        className={`flex-1 py-3 text-center ${
          listShowState === 'out'
            ? 'text-gray-900 border-b-2 border-gray-900'
            : 'text-gray-500'
        }`}
        onClick={() => setListShowState('out')}
      >
        출금
      </button>
    </div>
  );
};
