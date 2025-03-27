import { BottomBar } from '@/widgets/bottomBar';

export default function BarLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <main className="h-[calc(100vh-80px)]">
      {children}
      <BottomBar />
    </main>
  );
}
