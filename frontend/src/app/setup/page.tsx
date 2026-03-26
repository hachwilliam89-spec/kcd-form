'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/api';
import { PixelShield, PixelBorder, PixelCoin, PixelTriangle, PixelCercle, PixelRectangle } from '@/components/PixelSprites';

const BUDGET_PAR_DIFFICULTE: Record<string, number> = {
    ECUYER: 1000,
    CHEVALIER: 700,
    SEIGNEUR: 400,
};

const DIFFICULTES = [
    {
        id: 'ECUYER',
        label: 'Écuyer',
        icon: <PixelTriangle size={28} />,
        desc: 'Pour les débutants. Budget généreux, ennemis faibles.',
        budget: 1000,
        borderSelected: '#5a8c28',
        borderHover: '#8cb414',
        badgeBg: 'rgba(90,140,40,0.2)',
        badgeColor: '#8cb414',
    },
    {
        id: 'CHEVALIER',
        label: 'Chevalier',
        icon: <PixelCercle size={28} />,
        desc: 'Équilibré. Budget standard, ennemis redoutables.',
        budget: 700,
        borderSelected: '#dcb464',
        borderHover: '#f0d070',
        badgeBg: 'rgba(220,180,100,0.2)',
        badgeColor: '#dcb464',
    },
    {
        id: 'SEIGNEUR',
        label: 'Seigneur',
        icon: <PixelRectangle size={28} />,
        desc: 'Pour les experts. Budget serré, ennemis impitoyables.',
        budget: 400,
        borderSelected: '#c44030',
        borderHover: '#d04838',
        badgeBg: 'rgba(196,64,48,0.2)',
        badgeColor: '#c44030',
    },
];

export default function SetupPage() {
    const router = useRouter();
    const [nom, setNom] = useState('');
    const [difficulte, setDifficulte] = useState<'ECUYER' | 'CHEVALIER' | 'SEIGNEUR'>('CHEVALIER');
    const [loading, setLoading] = useState(false);
    const [erreur, setErreur] = useState('');

    const lancer = async () => {
        if (!nom.trim()) { setErreur('Entre ton nom de seigneur !'); return; }
        setLoading(true);
        try {
            const budgetInitial = BUDGET_PAR_DIFFICULTE[difficulte] ?? 700;
            const joueur = await api.creerJoueur({ nom, budget: budgetInitial, vies: 3 });
            const partie = await api.creerPartie({ joueurId: joueur.id, difficulte });
            router.push(`/parties/${partie.id}/construction`);
        } catch {
            setErreur('Erreur serveur, vérifie que Spring Boot tourne.');
            setLoading(false);
        }
    };

    const diff = DIFFICULTES.find(d => d.id === difficulte)!;

    return (
        <main className="min-h-screen bg-medieval-setup flex flex-col items-center justify-center px-6 py-12">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                className="w-full max-w-xl flex flex-col gap-6"
            >
                {/* Header */}
                <div className="text-center flex flex-col items-center gap-3">
                    <PixelShield size={48} />
                    <h1 className="text-3xl font-black tracking-widest uppercase"
                        style={{
                            fontFamily: 'var(--font-cinzel)',
                            color: '#dcb464',
                            textShadow: '0 2px 4px rgba(0,0,0,0.5)',
                        }}>
                        Nouvelle Partie
                    </h1>
                    <PixelBorder className="w-48" />
                </div>

                {/* Nom du joueur — pixel art input */}
                <div className="flex flex-col gap-2">
                    <label className="text-[#dcb464] text-xs tracking-widest uppercase"
                           style={{ fontFamily: 'var(--font-cinzel)' }}>
                        Ton nom de seigneur
                    </label>
                    <input
                        className="px-4 py-3 text-white text-lg outline-none transition-all"
                        style={{
                            background: 'rgba(26,20,32,0.9)',
                            border: 'none',
                            outline: '3px solid #1a0a00',
                            boxShadow: 'inset 0 3px 0 rgba(0,0,0,0.3), inset 0 -3px 0 rgba(220,180,100,0.05), 0 3px 0 #1a0a00',
                            fontFamily: 'var(--font-crimson)',
                        }}
                        placeholder="Ex: Kim, CHU..."
                        value={nom}
                        onChange={e => { setNom(e.target.value); setErreur(''); }}
                    />
                </div>

                {/* Choix difficulté */}
                <div className="flex flex-col gap-3">
                    <label className="text-[#dcb464] text-xs tracking-widest uppercase"
                           style={{ fontFamily: 'var(--font-cinzel)' }}>
                        Difficulté
                    </label>
                    {DIFFICULTES.map(d => {
                        const selected = difficulte === d.id;
                        return (
                            <button
                                key={d.id}
                                onClick={() => setDifficulte(d.id as 'ECUYER' | 'CHEVALIER' | 'SEIGNEUR')}
                                className="text-left transition-all duration-150"
                                style={{
                                    background: selected
                                        ? 'rgba(26,20,32,0.95)'
                                        : 'rgba(20,14,24,0.8)',
                                    border: 'none',
                                    outline: selected
                                        ? `3px solid ${d.borderSelected}`
                                        : '3px solid #1a0a00',
                                    boxShadow: selected
                                        ? `inset 0 3px 0 rgba(255,255,255,0.05), inset 0 -3px 0 rgba(0,0,0,0.3), 0 4px 0 #1a0a00, 0 0 16px ${d.borderSelected}30`
                                        : 'inset 0 2px 0 rgba(255,255,255,0.02), inset 0 -2px 0 rgba(0,0,0,0.2), 0 3px 0 #0a0a10',
                                    padding: '14px 16px',
                                    transform: selected ? 'translateY(-1px)' : 'none',
                                }}
                            >
                                <div className="flex items-center gap-3">
                                    <div className="flex-shrink-0">
                                        {d.icon}
                                    </div>
                                    <div className="flex-1">
                                        <div className="flex items-center gap-2">
                                            <span className="font-bold text-white text-sm"
                                                  style={{ fontFamily: 'var(--font-cinzel)' }}>
                                                {d.label}
                                            </span>
                                            <span className="text-[10px] px-2 py-0.5 flex items-center gap-1"
                                                  style={{
                                                      background: d.badgeBg,
                                                      color: d.badgeColor,
                                                      outline: `1px solid ${d.badgeColor}40`,
                                                      fontFamily: 'var(--font-cinzel)',
                                                      fontWeight: 700,
                                                  }}>
                                                <PixelCoin size={10} className="inline-block" />
                                                {d.budget} or
                                            </span>
                                        </div>
                                        <p className="text-gray-400 text-xs mt-1"
                                           style={{ fontFamily: 'var(--font-crimson)' }}>
                                            {d.desc}
                                        </p>
                                    </div>
                                    {selected && (
                                        <div className="text-[#dcb464] text-lg">▶</div>
                                    )}
                                </div>
                            </button>
                        );
                    })}
                </div>

                {/* Erreur */}
                {erreur && (
                    <div className="text-center py-2 px-4"
                         style={{
                             background: 'rgba(196,64,48,0.15)',
                             outline: '2px solid #c44030',
                             boxShadow: '0 2px 0 #1a0a00',
                         }}>
                        <p className="text-[#c44030] text-sm">{erreur}</p>
                    </div>
                )}

                {/* Résumé — pixel art panel */}
                <div className="relative py-4 px-5"
                     style={{
                         background: 'rgba(26,20,32,0.9)',
                         outline: '3px solid #1a0a00',
                         boxShadow: 'inset 0 3px 0 rgba(220,180,100,0.08), inset 0 -3px 0 rgba(0,0,0,0.3), 0 4px 0 #1a0a00',
                     }}>
                    <PixelBorder className="absolute top-0 left-0 right-0" />
                    <div className="flex flex-col gap-2 text-sm pt-1"
                         style={{ fontFamily: 'var(--font-crimson)' }}>
                        <p className="text-gray-400 flex items-center gap-2">
                            ⚔️ <span className="text-white font-semibold">5 vagues</span> d&apos;envahisseurs à repousser
                        </p>
                        <p className="text-gray-400 flex items-center gap-2">
                            <PixelCoin size={14} /> Budget de départ : <span className="text-[#dcb464] font-semibold">{diff.budget} or</span>
                        </p>
                        <p className="text-gray-400 flex items-center gap-2">
                            🏰 Défendez la Citadelle jusqu&apos;au dernier assaut
                        </p>
                    </div>
                </div>

                {/* Bouton lancer */}
                <button
                    onClick={lancer}
                    disabled={loading}
                    className={`btn-gold w-full text-lg py-4 flex items-center justify-center gap-3 ${loading ? 'opacity-50 cursor-not-allowed' : ''}`}
                >
                    {loading ? 'Chargement...' : (
                        <>
                            <PixelShield size={20} />
                            Lancer la bataille
                        </>
                    )}
                </button>

                {/* Retour */}
                <button
                    onClick={() => router.push('/')}
                    className="btn-stone w-full text-sm py-3 flex items-center justify-center gap-2"
                >
                    ← Retour à l&apos;accueil
                </button>
            </motion.div>
        </main>
    );
}