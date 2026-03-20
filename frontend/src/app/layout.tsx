import type { Metadata } from 'next';
import { Cinzel, Crimson_Text } from 'next/font/google';
import './globals.css';

const cinzel = Cinzel({
    subsets: ['latin'],
    variable: '--font-cinzel',
    weight: ['400', '700', '900'],
});

const crimson = Crimson_Text({
    subsets: ['latin'],
    variable: '--font-crimson',
    weight: ['400', '600'],
});

export const metadata: Metadata = {
    title: 'KCD Formes — Tower Defense',
    description: 'Défendez votre forteresse avec des formes géométriques',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
    return (
        <html lang="fr" className={`${cinzel.variable} ${crimson.variable}`}>
        <body className="bg-[#0a0a0f] text-white antialiased">
        {children}
        </body>
        </html>
    );
}