'use client';

import { motion } from 'framer-motion';
import Link from 'next/link';
import { Shield } from 'lucide-react';

export default function Home() {
    return (
        <main className="min-h-screen bg-[#0a0a0f] flex flex-col items-center justify-center relative overflow-hidden px-6">

            <div className="absolute inset-0 bg-gradient-radial from-[#1a0a00]/40 via-transparent to-transparent" />
            <div className="absolute inset-0 bg-[url('/noise.svg')] opacity-5" />

            <div className="relative z-10 flex flex-col items-center gap-10 text-center max-w-2xl">

                {/* Logo */}
                <motion.div
                    initial={{ opacity: 0, y: -30 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.8 }}
                    className="flex flex-col items-center gap-3">
                    <Shield className="w-16 h-16 text-[#c9a84c]" strokeWidth={1.5} />
                    <h1 className="text-5xl md:text-7xl font-black text-[#c9a84c] tracking-widest uppercase"
                        style={{ fontFamily: 'var(--font-cinzel)', textShadow: '0 0 40px rgba(201,168,76,0.4)' }}>
                        KCD Formes
                    </h1>
                    <p className="text-[#8a7a5a] text-lg tracking-widest uppercase">Tower Defense Médiéval</p>
                </motion.div>

                {/* Séparateur */}
                <motion.div
                    initial={{ scaleX: 0 }} animate={{ scaleX: 1 }}
                    transition={{ duration: 0.6, delay: 0.4 }}
                    className="w-64 h-px bg-gradient-to-r from-transparent via-[#c9a84c] to-transparent"
                />

                {/* Pitch */}
                <motion.p
                    initial={{ opacity: 0 }} animate={{ opacity: 1 }}
                    transition={{ duration: 0.8, delay: 0.6 }}
                    className="text-gray-400 text-lg leading-relaxed"
                    style={{ fontFamily: 'var(--font-crimson)' }}>
                    Défendez votre citadelle contre des vagues d'envahisseurs.
                    Composez vos tourelles à partir de <span className="text-[#c9a84c]">formes géométriques</span>,
                    placez des <span className="text-[#8b6914]">murailles</span> sur le chemin
                    et repoussez les 5 assauts.
                </motion.p>

                {/* 3 icônes résumé */}
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, delay: 0.8 }}
                    className="flex gap-8">
                    {[
                        { emoji: '🏹', label: 'Construire' },
                        { emoji: '⚔️', label: 'Défendre' },
                        { emoji: '👑', label: 'Survivre' },
                    ].map((item, i) => (
                        <div key={i} className="flex flex-col items-center gap-2">
                            <span className="text-4xl">{item.emoji}</span>
                            <p className="text-gray-500 text-xs uppercase tracking-widest">{item.label}</p>
                        </div>
                    ))}
                </motion.div>

           {/* Boutons */}
                           <motion.div
                               initial={{ opacity: 0, scale: 0.9 }}
                               animate={{ opacity: 1, scale: 1 }}
                               transition={{ duration: 0.6, delay: 1 }}
                               className="flex flex-col items-center gap-4 w-full">
                               <Link href="/setup"
                                     className="inline-block w-full max-w-md bg-[#c9a84c] hover:bg-[#e8c96d] text-black font-black text-2xl px-16 py-5 rounded-lg tracking-widest uppercase transition-all duration-300 hover:shadow-[0_0_40px_rgba(201,168,76,0.6)] text-center"
                                     style={{ fontFamily: 'var(--font-cinzel)' }}>
                                   🛡️ Solo
                               </Link>
                               <Link href="/multi"
                                     className="inline-block w-full max-w-md bg-[#8b1a1a] hover:bg-[#a52020] text-white font-black text-2xl px-16 py-5 rounded-lg tracking-widest uppercase transition-all duration-300 hover:shadow-[0_0_40px_rgba(139,26,26,0.6)] text-center"
                                     style={{ fontFamily: 'var(--font-cinzel)' }}>
                                   ⚔️ Multijoueur
                               </Link>
                               <Link href="/guide"
                                     className="text-[#8a7a5a] hover:text-[#c9a84c] text-sm uppercase tracking-widest transition-colors"
                                     style={{ fontFamily: 'var(--font-cinzel)' }}>
                                   📜 Guide du jeu — Tourelles, Ennemis & Stratégie
                               </Link>
                           </motion.div>
            </div>
        </main>
    );
}