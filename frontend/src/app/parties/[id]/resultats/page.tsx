'use client';

import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { useRouter, useParams } from 'next/navigation';
import { api, Partie, Tourelle, Muraille } from '@/lib/api';

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

    return (
        <main className="min-h-screen bg-[#0a0a0f] text-white flex flex-col items-center justify-center p-8 gap-8">

            {/* Résultat principal */}
            <motion.div
                initial={{ opacity: 0, scale: 0.5 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ duration: 0.6, ease: 'backOut' }}
                className="text-center">
                <div className="text-8xl mb-4">
                    {victoire ? '👑' : '💀'}
                </div>
                <h1
                    className={`text-5xl font-black uppercase tracking-widest mb-2 ${
                        victoire ? 'text-[#c9a84c]' : 'text-red-500'
                    }`}
                    style={{ fontFamily: 'var(--font-cinzel)' }}>
                    {victoire ? 'Victoire !' : 'Défaite...'}
                </h1>
                <p className="text-gray-400 text-lg">
                    {partie.joueurNom} — {partie.difficulte}
                </p>
            </motion.div>

            {/* Étoiles */}
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.3, duration: 0.5 }}
                className="text-center">
                <p className="text-5xl tracking-widest">{etoilesAffichage}</p>
                <p className="text-gray-400 text-sm mt-2 uppercase tracking-widest">
                    {partie.etoiles === 3 ? 'Perfection !' :
                        partie.etoiles === 2 ? 'Bien joué !' :
                            partie.etoiles === 1 ? 'De justesse...' : 'Défaite'}
                </p>
            </motion.div>

            {/* Score global */}
            <motion.div
                initial={{ opacity: 0, scale: 0.8 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ delay: 0.5, duration: 0.5 }}
                className="bg-[#1a1a22] border-2 border-[#c9a84c] rounded-2xl p-6 text-center">
                <p className={`text-6xl font-black ${
                    partie.scoreFinal >= 80 ? 'text-[#c9a84c]' :
                        partie.scoreFinal >= 50 ? 'text-yellow-400' :
                            partie.scoreFinal > 0 ? 'text-orange-400' : 'text-red-500'
                }`}>
                    {partie.scoreFinal}
                </p>
                <p className="text-xs text-gray-400 uppercase tracking-widest mt-1">Score / 100</p>
            </motion.div>

            {/* Détail des 3 critères */}
            <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.6, duration: 0.5 }}
                className="grid grid-cols-3 gap-4 w-full max-w-2xl">

                <div className="bg-[#1a1a22] border border-[#3a3a48] rounded-xl p-4 text-center">
                    <p className="text-2xl font-black text-red-400">{Math.round(scoreRemparts)}</p>
                    <p className="text-xs text-gray-500 mt-1">/ 30 pts</p>
                    <div className="w-full bg-[#2a2a35] rounded-full h-1.5 mt-2">
                        <div className="h-1.5 rounded-full bg-red-500" style={{ width: `${(scoreRemparts / 30) * 100}%` }} />
                    </div>
                    <p className="text-xs text-gray-400 uppercase tracking-widest mt-2">🏰 Citadelle</p>
                    <p className="text-[10px] text-gray-500">{partie.forteressePvRestants} / {partie.forteressePvMax} PV</p>
                </div>

                <div className="bg-[#1a1a22] border border-[#3a3a48] rounded-xl p-4 text-center">
                    <p className="text-2xl font-black text-purple-400">{Math.round(scoreEnnemis)}</p>
                    <p className="text-xs text-gray-500 mt-1">/ 40 pts</p>
                    <div className="w-full bg-[#2a2a35] rounded-full h-1.5 mt-2">
                        <div className="h-1.5 rounded-full bg-purple-500" style={{ width: `${(scoreEnnemis / 40) * 100}%` }} />
                    </div>
                    <p className="text-xs text-gray-400 uppercase tracking-widest mt-2">💀 Ennemis</p>
                    <p className="text-[10px] text-gray-500">{partie.ennemisElimines} / {partie.ennemisTotal} éliminés</p>
                </div>

                <div className="bg-[#1a1a22] border border-[#3a3a48] rounded-xl p-4 text-center">
                    <p className="text-2xl font-black text-[#c9a84c]">{Math.round(scoreBudget)}</p>
                    <p className="text-xs text-gray-500 mt-1">/ 30 pts</p>
                    <div className="w-full bg-[#2a2a35] rounded-full h-1.5 mt-2">
                        <div className="h-1.5 rounded-full bg-[#c9a84c]" style={{ width: `${(scoreBudget / 30) * 100}%` }} />
                    </div>
                    <p className="text-xs text-gray-400 uppercase tracking-widest mt-2">💰 Efficacité</p>
                    <p className="text-[10px] text-gray-500">{partie.orDepense} / {budgetInitial} or dépensé</p>
                </div>
            </motion.div>

            {/* Défenses utilisées */}
            <motion.div
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.8, duration: 0.5 }}
                className="w-full max-w-2xl">
                <h2 className="text-[#c9a84c] text-xs uppercase tracking-widest mb-3"
                    style={{ fontFamily: 'var(--font-cinzel)' }}>
                    Composition des défenses
                </h2>
                <div className="flex flex-col gap-2">
                    {tourelles.map((t, i) => (
                        <motion.div
                            key={t.id}
                            initial={{ opacity: 0, x: -20 }}
                            animate={{ opacity: 1, x: 0 }}
                            transition={{ delay: 0.9 + i * 0.1 }}
                            className="bg-[#1a1a22] border border-[#3a3a48] rounded-lg p-3 flex items-center gap-4">
                            <span className="text-2xl">{t.aoe ? '🪨' : '🏹'}</span>
                            <div className="flex-1">
                                <p className="font-bold text-sm">{t.nom}</p>
                                <p className="text-xs text-gray-500">Position {t.position}</p>
                            </div>
                            <div className="flex gap-4 text-center">
                                <div>
                                    <p className="text-[#c9a84c] font-black text-sm">{t.dps.toFixed(1)}</p>
                                    <p className="text-[9px] text-gray-500">DPS</p>
                                </div>
                                <div>
                                    <p className="text-gray-300 font-black text-sm">{t.cout}</p>
                                    <p className="text-[9px] text-gray-500">Coût</p>
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
                            className="bg-[#3a2a10] border border-[#8b6914]/30 rounded-lg p-3 flex items-center gap-4">
                            <span className="text-2xl">🧱</span>
                            <div className="flex-1">
                                <p className="font-bold text-sm">Muraille</p>
                                <p className="text-xs text-gray-500">Chemin pos.{m.position}</p>
                            </div>
                            <div className="flex gap-4 text-center">
                                <div>
                                    <p className="text-blue-400 font-black text-sm">{m.pvMax}</p>
                                    <p className="text-[9px] text-gray-500">PV</p>
                                </div>
                                <div>
                                    <p className="text-gray-300 font-black text-sm">{m.cout}</p>
                                    <p className="text-[9px] text-gray-500">Coût</p>
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
                className="flex gap-4">
                <button
                    onClick={() => router.push('/')}
                    className="bg-[#2a2a35] hover:bg-[#3a3a48] text-white font-black px-6 py-3 rounded-lg uppercase tracking-widest transition-all text-sm"
                    style={{ fontFamily: 'var(--font-cinzel)' }}>
                    ← Accueil
                </button>
                <button
                    onClick={() => router.push('/setup')}
                    className="bg-[#c9a84c] hover:bg-[#e8c96d] text-black font-black px-6 py-3 rounded-lg uppercase tracking-widest transition-all text-sm"
                    style={{ fontFamily: 'var(--font-cinzel)' }}>
                    Rejouer ⚔️
                </button>
            </motion.div>
        </main>
    );
}