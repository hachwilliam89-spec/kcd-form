'use client';

import { motion } from 'framer-motion';
import Link from 'next/link';
import { PixelShield, PixelBanner, PixelBorder, PixelTriangle, PixelCercle, PixelRectangle, PixelTourelle, PixelCoin } from '@/components/PixelSprites';
import PixelTitle from '@/components/PixelTitle';

export default function Home() {

    /* ── Style helper ── */
    const pixelFont = { fontFamily: 'var(--font-pixel)' };

    return (
        <main className="min-h-screen bg-medieval-hero flex flex-col items-center justify-end relative overflow-hidden px-6 pb-12 pt-[35vh]">

            {/* Bannières */}
            <div className="absolute top-[28%] left-20 hidden lg:block">
                <PixelBanner size={48} color="#c44030" className="opacity-50" />
            </div>
            <div className="absolute top-[28%] right-20 hidden lg:block">
                <PixelBanner size={48} color="#dc8c3c" className="opacity-50" />
            </div>

            {/* Contenu principal */}
            <div className="relative z-10 flex flex-col items-center gap-6 text-center max-w-2xl w-full">

                {/* Blason pixel art */}
                <motion.div
                    initial={{ opacity: 0, y: -20, scale: 0.8 }}
                    animate={{ opacity: 1, y: 0, scale: 1 }}
                    transition={{ duration: 0.6 }}
                    className="animate-float">
                    <PixelShield size={72} />
                </motion.div>

                {/* Titre pixel art */}
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.8, delay: 0.2 }}
                    className="flex flex-col items-center gap-3">
                    <PixelTitle size={72} />
                    <p style={{
                        ...pixelFont,
                        fontSize: '0.5rem',
                        letterSpacing: '0.3em',
                        textTransform: 'uppercase' as const,
                        color: '#dcb464',
                        textShadow: '0 2px 4px rgba(0,0,0,0.9), 0 0 12px rgba(0,0,0,0.6)',
                        lineHeight: '1.8',
                    }}>
                        Tower Defense Médiéval
                    </p>
                </motion.div>

                {/* Séparateur pixel */}
                <motion.div
                    initial={{ scaleX: 0 }} animate={{ scaleX: 1 }}
                    transition={{ duration: 0.5, delay: 0.4 }}
                    className="w-80">
                    <PixelBorder />
                </motion.div>

                {/* Pitch — style pixel art panel */}
                <motion.div
                    initial={{ opacity: 0 }} animate={{ opacity: 1 }}
                    transition={{ duration: 0.8, delay: 0.6 }}
                    className="max-w-lg w-full relative"
                    style={{
                        background: 'linear-gradient(180deg, rgba(26,20,32,0.92) 0%, rgba(20,14,24,0.95) 100%)',
                        outline: '3px solid #1a0a00',
                        boxShadow: 'inset 0 3px 0 rgba(220,180,100,0.15), inset 0 -3px 0 rgba(0,0,0,0.3), 0 4px 0 #1a0a00, 0 6px 16px rgba(0,0,0,0.5)',
                        padding: '20px 24px',
                    }}>
                    <PixelBorder className="absolute top-0 left-0 right-0" />
                    <PixelBorder className="absolute bottom-0 left-0 right-0 rotate-180" />
                    <p className="py-2"
                       style={{
                           ...pixelFont,
                           fontSize: '0.42rem',
                           color: '#d4c8a0',
                           lineHeight: '2.4',
                       }}>
                        Défendez votre citadelle contre des vagues d&apos;envahisseurs.
                        Composez vos tourelles à partir de{' '}
                        <span style={{ color: '#dcb464' }}>formes géométriques</span>,
                        placez des <span style={{ color: '#dc8c3c' }}>murailles</span> sur le chemin
                        et repoussez les 5 assauts.
                    </p>
                </motion.div>

                {/* 3 piliers pixel art */}
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, delay: 0.8 }}
                    className="flex gap-6 w-full max-w-lg justify-center">
                    {[
                        { icon: <PixelTriangle size={40} />, label: 'Construire', sub: 'Tourelles' },
                        { icon: <PixelCercle size={40} />, label: 'Défendre', sub: 'Forteresse' },
                        { icon: <PixelRectangle size={40} />, label: 'Survivre', sub: '5 vagues' },
                    ].map((item, i) => (
                        <motion.div
                            key={i}
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ delay: 0.9 + i * 0.15 }}
                            className="flex-1 flex flex-col items-center gap-2 py-4 px-2"
                            style={{
                                background: 'rgba(26,20,32,0.85)',
                                outline: '2px solid #1a0a00',
                                boxShadow: 'inset 0 2px 0 rgba(220,180,100,0.1), inset 0 -2px 0 rgba(0,0,0,0.3), 0 3px 0 #1a0a00',
                            }}>
                            <div className="animate-float" style={{ animationDelay: `${i * 0.5}s` }}>
                                {item.icon}
                            </div>
                            <p style={{
                                ...pixelFont,
                                fontSize: '0.45rem',
                                color: '#dcb464',
                                letterSpacing: '0.1em',
                                textTransform: 'uppercase' as const,
                                lineHeight: '1.6',
                            }}>
                                {item.label}
                            </p>
                            <p style={{
                                ...pixelFont,
                                fontSize: '0.35rem',
                                color: '#6a6050',
                                letterSpacing: '0.1em',
                                textTransform: 'uppercase' as const,
                            }}>
                                {item.sub}
                            </p>
                        </motion.div>
                    ))}
                </motion.div>

                {/* Boutons */}
                <motion.div
                    initial={{ opacity: 0, scale: 0.9 }}
                    animate={{ opacity: 1, scale: 1 }}
                    transition={{ duration: 0.6, delay: 1.1 }}
                    className="flex flex-col items-center gap-5 w-full max-w-sm mt-2">

                    <Link href="/setup" className="w-full">
                        <button className="btn-gold w-full py-4 flex items-center justify-center gap-3"
                                style={{ ...pixelFont, fontSize: '0.65rem' }}>
                            <PixelTourelle size={24} />
                            Bataille Solo
                        </button>
                    </Link>

                    <Link href="/multi" className="w-full">
                        <button className="btn-blood w-full py-4 flex items-center justify-center gap-3"
                                style={{ ...pixelFont, fontSize: '0.65rem' }}>
                            <PixelBanner size={24} color="#ffe0d0" />
                            Multijoueur
                        </button>
                    </Link>

                    {/* Guide — style pixel art lien */}
                    <Link href="/guide" className="w-full">
                        <div className="flex items-center justify-center gap-2 py-3 px-6 group cursor-pointer transition-all"
                             style={{
                                 background: 'rgba(26,20,32,0.7)',
                                 outline: '2px solid rgba(138,122,90,0.3)',
                                 boxShadow: '0 2px 0 rgba(0,0,0,0.3)',
                             }}>
                            <span className="group-hover:text-[#dcb464] transition-colors"
                                  style={{
                                      ...pixelFont,
                                      fontSize: '0.38rem',
                                      color: '#8a7a5a',
                                      letterSpacing: '0.1em',
                                      lineHeight: '2',
                                  }}>
                                📜 Guide du jeu — Tourelles, Ennemis &amp; Stratégie
                            </span>
                        </div>
                    </Link>
                </motion.div>

                {/* Footer pixel art */}
                <motion.div
                    initial={{ opacity: 0 }} animate={{ opacity: 1 }}
                    transition={{ delay: 1.4 }}
                    className="flex flex-col items-center gap-3 mt-6">
                    <div className="flex items-center gap-2">
                        <PixelCoin size={12} />
                        <div className="w-32"><PixelBorder /></div>
                        <PixelShield size={16} />
                        <div className="w-32"><PixelBorder /></div>
                        <PixelCoin size={12} />
                    </div>
                    <p style={{
                        ...pixelFont,
                        fontSize: '0.32rem',
                        color: '#4a4a5a',
                        letterSpacing: '0.15em',
                        textTransform: 'uppercase' as const,
                    }}>
                        Kingdom Come Defenses — Licence Pro UHA 4.0
                    </p>
                </motion.div>
            </div>
        </main>
    );
}