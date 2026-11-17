import type { Metadata } from 'next';
import './globals.css';
export const metadata: Metadata = { title: 'MOTO LAB ID', description: 'Simulator modifikasi motor Indonesia' };
export default function RootLayout({children}:{children:React.ReactNode}) { return <html lang="id"><body>{children}</body></html> }
