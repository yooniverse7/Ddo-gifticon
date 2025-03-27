'use client';

import { Button } from '@/components/ui/button';
import { GiftForm } from '@/features/giftForm';
import { X } from 'lucide-react';
import { motion } from 'motion/react';
import { useRouter } from 'next/navigation';

export default function page() {
  const router = useRouter();

  return (
    <motion.div
      className="flex flex-col h-full items-center"
      initial={{ transform: 'translateY(100%)' }}
      animate={{ transform: 'translateY(0)' }}
    >
      <Button className="absolute top-4 left-4" onClick={() => router.back()}>
        <X />
      </Button>
      <GiftForm />
    </motion.div>
  );
}
