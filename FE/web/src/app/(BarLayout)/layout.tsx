import { BottomBar } from '@/widgets/bottomBar';

export default function BarLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
      {children}
      <BottomBar />
    </>
  );
}
