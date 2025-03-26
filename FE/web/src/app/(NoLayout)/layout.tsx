import { BottomBar } from '@/widgets/bottomBar';

export default function NoLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return <main className="h-dvh">{children}</main>;
}
