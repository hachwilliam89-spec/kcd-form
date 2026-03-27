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

    /* ── Style helpers ── */
    const pixelFont = { fontFamily: 'var(--font-pixel)' };

    return (
        <main className="min-h-screen bg-medieval-setup flex flex-col items-center justify-center px-6 py-12">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                className="w-full max-w-xl flex flex-col gap-6"
            >
                {/* ════════ Header ════════ */}
                <div className="text-center flex flex-col items-center gap-3">
                    <PixelShield size={48} />
                    <h1 className="text-xl tracking-widest uppercase"
                        style={{
                            ...pixelFont,
                            color: '#dcb464',
                            textShadow: '0 2px 4px rgba(0,0,0,0.5)',
                            lineHeight: '1.6',
                        }}>
                        Nouvelle Partie
                    </h1>
                    <PixelBorder className="w-48" />
                </div>

                {/* ════════ Nom du joueur ════════ */}
                <div className="flex flex-col gap-2">
                    <label style={{
                        ...pixelFont,
                        fontSize: '0.5rem',
                        color: '#dcb464',
                        letterSpacing: '0.15em',
                        textTransform: 'uppercase' as const,
                    }}>
                        Ton nom de seigneur
                    </label>
                    <input
                        className="px-4 py-3 text-white outline-none transition-all"
                        style={{
                            ...pixelFont,
                            fontSize: '0.7rem',
                            background: 'rgba(26,20,32,0.9)',
                            border: 'none',
                            outline: '3px solid #1a0a00',
                            boxShadow: 'inset 0 3px 0 rgba(0,0,0,0.3), inset 0 -3px 0 rgba(220,180,100,0.05), 0 3px 0 #1a0a00',
                        }}
                        placeholder="Ex: Kim, CHU..."
                        value={nom}
                        onChange={e => { setNom(e.target.value); setErreur(''); }}
                    />
                </div>

                {/* ════════ Choix difficulté ════════ */}
                <div className="flex flex-col gap-3">
                    <label style={{
                        ...pixelFont,
                        fontSize: '0.5rem',
                        color: '#dcb464',
                        letterSpacing: '0.15em',
                        textTransform: 'uppercase' as const,
                    }}>
                        Difficulté
                    </label>
                    {DIFFICULTES.map(d => {
                        const selected = difficulte === d.id;
                        return (
                            <button
                                key={d.id}
                                onClick={() => setDifficulte(d.id as 'ECUYER' | 'CHEVALIER' | 'SEIGNEUR')}
                                className="text-left transition-all duration-150 active:translate-y-[1px]"
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
                                        <div className="flex items-center gap-2 flex-wrap">
                                            <span style={{
                                                ...pixelFont,
                                                fontSize: '0.6rem',
                                                color: '#fff',
                                                lineHeight: '1.8',
                                            }}>
                                                {d.label}
                                            </span>
                                            <span className="flex items-center gap-1"
                                                  style={{
                                                      ...pixelFont,
                                                      fontSize: '0.4rem',
                                                      padding: '2px 6px',
                                                      background: d.badgeBg,
                                                      color: d.badgeColor,
                                                      outline: `1px solid ${d.badgeColor}40`,
                                                  }}>
                                                <PixelCoin size={10} className="inline-block" />
                                                {d.budget} or
                                            </span>
                                        </div>
                                        <p style={{
                                            ...pixelFont,
                                            fontSize: '0.38rem',
                                            color: 'rgba(180,170,150,0.6)',
                                            lineHeight: '2',
                                            marginTop: '4px',
                                        }}>
                                            {d.desc}
                                        </p>
                                    </div>
                                    {selected && (
                                        <div style={{
                                            ...pixelFont,
                                            fontSize: '0.8rem',
                                            color: '#dcb464',
                                        }}>▶</div>
                                    )}
                                </div>
                            </button>
                        );
                    })}
                </div>

                {/* ════════ Erreur ════════ */}
                {erreur && (
                    <div className="text-center py-2 px-4"
                         style={{
                             background: 'rgba(196,64,48,0.15)',
                             outline: '2px solid #c44030',
                             boxShadow: '0 2px 0 #1a0a00',
                         }}>
                        <p style={{ ...pixelFont, fontSize: '0.45rem', color: '#c44030', lineHeight: '1.8' }}>
                            {erreur}
                        </p>
                    </div>
                )}

                {/* ════════ Résumé ════════ */}
                <div className="relative py-4 px-5"
                     style={{
                         background: 'rgba(26,20,32,0.9)',
                         outline: '3px solid #1a0a00',
                         boxShadow: 'inset 0 3px 0 rgba(220,180,100,0.08), inset 0 -3px 0 rgba(0,0,0,0.3), 0 4px 0 #1a0a00',
                     }}>
                    <PixelBorder className="absolute top-0 left-0 right-0" />
                    <div className="flex flex-col gap-3 pt-4">
                        <p className="flex items-center gap-2"
                           style={{ ...pixelFont, fontSize: '0.4rem', color: 'rgba(180,170,150,0.6)', lineHeight: '2' }}>
                            ⚔️ <span style={{ color: '#fff' }}>5 vagues</span> d&apos;envahisseurs à repousser
                        </p>
                        <p className="flex items-center gap-2"
                           style={{ ...pixelFont, fontSize: '0.4rem', color: 'rgba(180,170,150,0.6)', lineHeight: '2' }}>
                            <PixelCoin size={14} /> Budget de départ : <span style={{ color: '#dcb464' }}>{diff.budget} or</span>
                        </p>
                        <p className="flex items-center gap-2"
                           style={{ ...pixelFont, fontSize: '0.4rem', color: 'rgba(180,170,150,0.6)', lineHeight: '2' }}>
                            🏰 Défendez la Citadelle jusqu&apos;au dernier assaut
                        </p>
                    </div>
                </div>

                {/* ════════ Bouton lancer ════════ */}
                <button
                    onClick={lancer}
                    disabled={loading}
                    className={`btn-gold w-full py-4 flex items-center justify-center gap-3 ${loading ? 'opacity-50 cursor-not-allowed' : ''}`}
                    style={{ ...pixelFont, fontSize: '0.65rem' }}
                >
                    {loading ? 'Chargement...' : (
                        <>
                            <PixelShield size={20} />
                            Lancer la bataille
                        </>
                    )}
                </button>

                {/* ════════ Retour ════════ */}
                <button
                    onClick={() => router.push('/')}
                    className="btn-stone w-full py-3 flex items-center justify-center gap-2"
                    style={{ ...pixelFont, fontSize: '0.55rem' }}
                >
                    ← Retour à l&apos;accueil
                </button>
            </motion.div>
        </main>
    );
}