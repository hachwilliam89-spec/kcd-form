'use client';

import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { useRouter, useParams } from 'next/navigation';
import { api, Partie, Tourelle, Muraille } from '@/lib/api';
import { PixelBorder, PixelShield, PixelCoin, PixelTriangle, PixelCercle, PixelRectangle, PixelTourelle } from '@/components/PixelSprites';

const BUDGET_INITIAL: Record<string, number> = {
    ECUYER: 1000,
    CHEVALIER: 700,
    SEIGNEUR: 400,
};

export default function ResultatsPage() {
    const router = useRouter();
    const params = useParams();
    const partieId = Number(params.id);

    const [partie, setPartie] = useState<Partie | null>(null);
    const [tourelles, setTourelles] = useState<Tourelle[]>([]);
    const [murailles, setMurailles] = useState<Muraille[]>([]);

    useEffect(() => { charger(); }, []);

    const charger = async () => {
        const p = await api.getPartie(partieId);
        setPartie(p);
        const t = await api.getTourelles(partieId);
        setTourelles(t);
        const m = await api.getMurailles(partieId);
        setMurailles(m);
    };

    if (!partie) return null;

    const victoire = partie.etat === 'GAGNE';
    const budgetInitial = BUDGET_INITIAL[partie.difficulte] ?? 400;

    const scoreRemparts = partie.forteressePvMax > 0
        ? (partie.forteressePvRestants / partie.forteressePvMax) * 30 : 0;
    const scoreEnnemis = partie.ennemisTotal > 0
        ? (partie.ennemisElimines / partie.ennemisTotal) * 40 : 0;
    const scoreBudget = budgetInitial > 0
        ? (1.0 - partie.orDepense / budgetInitial) * 30 : 0;

    const etoilesAffichage = '⭐'.repeat(partie.etoiles) + '☆'.repeat(3 - partie.etoiles);

    const scoreColor = partie.scoreFinal >= 80 ? '#dcb464'
        : partie.scoreFinal >= 50 ? '#dcb464'
            : partie.scoreFinal > 0 ? '#dc8c3c' : '#c44030';

    return (
        <main className="min-h-screen bg-medieval-resultats text-white flex flex-col items-center justify-center p-8 gap-6">

            {/* Résultat principal */}
            <motion.div
                initial={{ opacity: 0, scale: 0.5 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ duration: 0.6, ease: 'backOut' }}
                className="text-center flex flex-col items-center">
                <div className="text-8xl mb-4">
                    {victoire ? '👑' : '💀'}
                </div>
                <h1 className="text-5xl font-black uppercase tracking-widest mb-2"
                    style={{
                        fontFamily: 'var(--font-cinzel)',
                        color: victoire ? '#dcb464' : '#c44030',
                        textShadow: '0 2px 4px rgba(0,0,0,0.6), 0 0 20px ' + (victoire ? 'rgba(220,180,100,0.3)' : 'rgba(196,64,48,0.3)'),
                    }}>
                    {victoire ? 'Victoire !' : 'Défaite...'}
                </h1>
                <p style={{ fontFamily: 'var(--font-crimson)', color: 'rgba(212,200,160,0.7)', textShadow: '0 1px 2px rgba(0,0,0,0.5)' }}
                   className="text-lg">
                    {partie.joueurNom} — {partie.difficulte}
                </p>
                <PixelBorder className="w-48 mt-4" />
            </motion.div>

            {/* Étoiles */}
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.3, duration: 0.5 }}
                className="text-center">
                <p className="text-5xl tracking-widest"
                   style={{ textShadow: '0 2px 8px rgba(0,0,0,0.5)' }}>
                    {etoilesAffichage}
                </p>
                <p className="text-sm mt-2 uppercase tracking-widest"
                   style={{ fontFamily: 'var(--font-cinzel)', color: 'rgba(212,200,160,0.6)' }}>
                    {partie.etoiles === 3 ? 'Perfection !' :
                        partie.etoiles === 2 ? 'Bien joué !' :
                            partie.etoiles === 1 ? 'De justesse...' : 'Défaite'}
                </p>
            </motion.div>

            {/* Score global — pixel art panel */}
            <motion.div
                initial={{ opacity: 0, scale: 0.8 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ delay: 0.5, duration: 0.5 }}
                className="text-center relative py-6 px-10"
                style={{
                    background: 'rgba(26,20,32,0.9)',
                    outline: `3px solid ${victoire ? '#dcb464' : '#c44030'}`,
                    boxShadow: `inset 0 3px 0 rgba(255,255,255,0.05), inset 0 -3px 0 rgba(0,0,0,0.3), 0 4px 0 #1a0a00, 0 0 20px ${victoire ? 'rgba(220,180,100,0.15)' : 'rgba(196,64,48,0.15)'}`,
                }}>
                <PixelBorder className="absolute top-0 left-0 right-0" />
                <PixelBorder className="absolute bottom-0 left-0 right-0 rotate-180" />
                <p className="text-6xl font-black pt-1"
                   style={{ color: scoreColor, fontFamily: 'var(--font-cinzel)' }}>
                    {partie.scoreFinal}
                </p>
                <p className="text-xs uppercase tracking-widest mt-1"
                   style={{ fontFamily: 'var(--font-cinzel)', color: 'rgba(212,200,160,0.5)' }}>
                    Score / 100
                </p>
            </motion.div>

            {/* Détail des 3 critères — pixel art panels */}
            <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.6, duration: 0.5 }}
                className="grid grid-cols-3 gap-4 w-full max-w-2xl">

                {[
                    { score: scoreRemparts, max: 30, color: '#c44030', label: 'Citadelle', emoji: '🏰', detail: `${partie.forteressePvRestants} / ${partie.forteressePvMax} PV` },
                    { score: scoreEnnemis, max: 40, color: '#8c64dc', label: 'Ennemis', emoji: '💀', detail: `${partie.ennemisElimines} / ${partie.ennemisTotal} éliminés` },
                    { score: scoreBudget, max: 30, color: '#dcb464', label: 'Efficacité', emoji: null, detail: `${partie.orDepense} / ${budgetInitial} or dépensé` },
                ].map((critere, i) => (
                    <motion.div
                        key={i}
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ delay: 0.7 + i * 0.1 }}
                        className="text-center py-4 px-3"
                        style={{
                            background: 'rgba(26,20,32,0.85)',
                            outline: '3px solid #1a0a00',
                            boxShadow: 'inset 0 3px 0 rgba(255,255,255,0.04), inset 0 -3px 0 rgba(0,0,0,0.3), 0 3px 0 #0a0508',
                        }}>
                        <p className="text-2xl font-black" style={{ color: critere.color, fontFamily: 'var(--font-cinzel)' }}>
                            {Math.round(critere.score)}
                        </p>
                        <p className="text-[10px] mt-0.5" style={{ color: 'rgba(212,200,160,0.4)', fontFamily: 'var(--font-cinzel)' }}>
                            / {critere.max} pts
                        </p>
                        {/* Barre pixel art */}
                        <div className="w-full h-2 mt-2 mx-auto" style={{ background: 'rgba(0,0,0,0.5)', outline: '1px solid rgba(0,0,0,0.3)' }}>
                            <div className="h-2" style={{ width: `${(critere.score / critere.max) * 100}%`, background: critere.color }} />
                        </div>
                        <div className="flex items-center justify-center gap-1 mt-2">
                            {critere.emoji ? (
                                <span className="text-xs">{critere.emoji}</span>
                            ) : (
                                <PixelCoin size={12} />
                            )}
                            <p className="text-[10px] uppercase tracking-widest"
                               style={{ fontFamily: 'var(--font-cinzel)', color: 'rgba(212,200,160,0.6)' }}>
                                {critere.label}
                            </p>
                        </div>
                        <p className="text-[9px] mt-0.5" style={{ color: 'rgba(212,200,160,0.35)' }}>
                            {critere.detail}
                        </p>
                    </motion.div>
                ))}
            </motion.div>

            {/* Défenses utilisées */}
            <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.8, duration: 0.5 }}
                className="w-full max-w-2xl">
                <div className="flex items-center gap-2 mb-3">
                    <PixelTourelle size={16} />
                    <h2 className="text-xs uppercase tracking-widest"
                        style={{ fontFamily: 'var(--font-cinzel)', color: '#dcb464' }}>
                        Composition des défenses
                    </h2>
                </div>
                <div className="flex flex-col gap-2">
                    {tourelles.map((t, i) => (
                        <motion.div
                            key={t.id}
                            initial={{ opacity: 0, x: -20 }}
                            animate={{ opacity: 1, x: 0 }}
                            transition={{ delay: 0.9 + i * 0.1 }}
                            className="flex items-center gap-4 p-3"
                            style={{
                                background: 'rgba(26,20,32,0.8)',
                                outline: '2px solid #1a0a00',
                                boxShadow: 'inset 0 2px 0 rgba(220,180,100,0.06), inset 0 -2px 0 rgba(0,0,0,0.2), 0 2px 0 #0a0508',
                            }}>
                            <span className="text-2xl">{t.aoe ? '🪨' : '🏹'}</span>
                            <div className="flex-1">
                                <p className="font-bold text-sm" style={{ fontFamily: 'var(--font-cinzel)', color: '#d4c8a0' }}>
                                    {t.nom}
                                </p>
                                <p className="text-[10px]" style={{ color: 'rgba(212,200,160,0.4)' }}>
                                    Position {t.position}
                                </p>
                            </div>
                            <div className="flex gap-4 text-center">
                                <div>
                                    <p className="font-black text-sm" style={{ color: '#dcb464', fontFamily: 'var(--font-cinzel)' }}>
                                        {t.dps.toFixed(1)}
                                    </p>
                                    <p className="text-[8px] uppercase" style={{ color: 'rgba(212,200,160,0.4)', fontFamily: 'var(--font-cinzel)' }}>DPS</p>
                                </div>
                                <div className="flex items-center gap-1">
                                    <PixelCoin size={10} />
                                    <div>
                                        <p className="font-black text-sm" style={{ color: '#d4c8a0', fontFamily: 'var(--font-cinzel)' }}>
                                            {t.cout}
                                        </p>
                                        <p className="text-[8px] uppercase" style={{ color: 'rgba(212,200,160,0.4)', fontFamily: 'var(--font-cinzel)' }}>Coût</p>
                                    </div>
                                </div>
                            </div>
                        </motion.div>
                    ))}
                    {murailles.map((m, i) => (
                        <motion.div
                            key={`m-${m.id}`}
                            initial={{ opacity: 0, x: -20 }}
                            animate={{ opacity: 1, x: 0 }}
                            transition={{ delay: 0.9 + (tourelles.length + i) * 0.1 }}
                            className="flex items-center gap-4 p-3"
                            style={{
                                background: 'rgba(60,40,20,0.6)',
                                outline: '2px solid #1a0a00',
                                boxShadow: 'inset 0 2px 0 rgba(180,140,80,0.06), inset 0 -2px 0 rgba(0,0,0,0.2), 0 2px 0 #0a0508',
                            }}>
                            <PixelRectangle size={28} />
                            <div className="flex-1">
                                <p className="font-bold text-sm" style={{ fontFamily: 'var(--font-cinzel)', color: '#d4c8a0' }}>
                                    Muraille
                                </p>
                                <p className="text-[10px]" style={{ color: 'rgba(212,200,160,0.4)' }}>
                                    Chemin pos.{m.position}
                                </p>
                            </div>
                            <div className="flex gap-4 text-center">
                                <div>
                                    <p className="font-black text-sm" style={{ color: '#3c8cdc', fontFamily: 'var(--font-cinzel)' }}>
                                        {m.pvMax}
                                    </p>
                                    <p className="text-[8px] uppercase" style={{ color: 'rgba(212,200,160,0.4)', fontFamily: 'var(--font-cinzel)' }}>PV</p>
                                </div>
                                <div className="flex items-center gap-1">
                                    <PixelCoin size={10} />
                                    <div>
                                        <p className="font-black text-sm" style={{ color: '#d4c8a0', fontFamily: 'var(--font-cinzel)' }}>
                                            {m.cout}
                                        </p>
                                        <p className="text-[8px] uppercase" style={{ color: 'rgba(212,200,160,0.4)', fontFamily: 'var(--font-cinzel)' }}>Coût</p>
                                    </div>
                                </div>
                            </div>
                        </motion.div>
                    ))}
                </div>
            </motion.div>

            {/* Boutons */}
            <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ delay: 1.1 }}
                className="flex gap-4 mt-2">
                <button
                    onClick={() => router.push('/')}
                    className="btn-stone text-sm py-3 px-6 flex items-center gap-2">
                    ← Accueil
                </button>
                <button
                    onClick={() => router.push('/setup')}
                    className="btn-gold text-sm py-3 px-6 flex items-center gap-2">
                    <PixelShield size={16} />
                    Rejouer
                </button>
            </motion.div>
        </main>
    );
}