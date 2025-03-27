'use client';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { PropsWithChildren, useEffect, useState } from 'react';

const CustomQueryClientProvider: React.FC<PropsWithChildren> = ({ children }) => {
  const [queryClient] = useState(() => {
    return new QueryClient({});
  });

  useEffect(() => {
    return () => {
      queryClient.clear();
    };
  }, [queryClient]);

  return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
};

export default CustomQueryClientProvider;
