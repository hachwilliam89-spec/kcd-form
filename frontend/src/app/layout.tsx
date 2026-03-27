import type { Metadata } from 'next';
import './globals.css';
import { Cinzel, Crimson_Text, Press_Start_2P } from 'next/font/google';

const cinzel = Cinzel({ subsets: ['latin'], variable: '--font-cinzel', weight: ['400', '700', '900'] });
const crimson = Crimson_Text({ subsets: ['latin'], variable: '--font-crimson', weight: ['400', '600', '700'] });
const pressStart = Press_Start_2P({ weight: '400', subsets: ['latin'], variable: '--font-pixel' });

export const metadata: Metadata = {
    title: 'KCD Formes — Tower Defense',
    description: 'Défendez votre forteresse avec des formes géométriques',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
    return (
        <html lang="fr" className={`${cinzel.variable} ${crimson.variable} ${pressStart.variable}`}>
        <body className="bg-[#0a0a0f] text-white antialiased">
        {children}
        </body>
        </html>
    );
}