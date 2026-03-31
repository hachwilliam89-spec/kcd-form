'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { motion } from 'framer-motion';
import { api } from '@/lib/api';
import { PixelBorder, PixelShield, PixelBanner } from '@/components/PixelSprites';

export default function MultiPage() {
    const router = useRouter();
    const [mode, setMode] = useState<'choix' | 'creer' | 'rejoindre'>('choix');
    const [code, setCode] = useState('');
    const [loading, setLoading] = useState(false);
    const [erreur, setErreur] = useState('');

    const creerLobby = async () => {
        setLoading(true);
        setErreur('');
        try {
            const joueur = await api.creerJoueur({ nom: 'Défenseur', budget: 700, vies: 3 });
            const partie = await api.creerPartie({ joueurId: joueur.id, difficulte: 'CHEVALIER' });
            const result = await api.creerLobby(partie.id, 3);
            router.push(`/multi/${result.lobbyId}?role=DEFENSEUR&partieId=${partie.id}`);
        } catch (e: any) {
            setErreur(e.message);
        }
        setLoading(false);
    };

    const rejoindreLobby = async () => {
        if (!code.trim()) return;
        setLoading(true);
        setErreur('');
        try {
            const lobby = await api.rejoindreLobby(code.toUpperCase(), 'ATTAQUANT');
            router.push(`/multi/${code.toUpperCase()}?role=ATTAQUANT&partieId=${lobby.partieId}`);
        } catch (e: any) {
            setErreur(e.message);
        }
        setLoading(false);
    };

    /* ── Style helpers ── */
    const pixelFont = { fontFamily: 'var(--font-pixel)' };
    const cinzelFont = { fontFamily: 'var(--font-cinzel)' };
    const crimsonFont = { fontFamily: 'var(--font-crimson)' };

    return (
        <main className="min-h-screen bg-medieval-multi text-white flex flex-col items-center justify-center p-4">
            <div className="w-full max-w-md flex flex-col gap-6 items-center">

                {/* ════════ Header ════════ */}
                <div className="text-center flex flex-col items-center gap-3">
                    <div className="flex items-center gap-3">
                        <PixelShield size={36} />
                        <span className="text-3xl" style={{ textShadow: '0 2px 4px rgba(0,0,0,0.5)' }}>⚔️</span>
                        <PixelBanner size={36} color="#c44030" />
                    </div>
                    <h1 className="text-xl tracking-widest uppercase"
                        style={{
                            ...pixelFont,
                            color: '#dcb464',
                            textShadow: '0 2px 4px rgba(0,0,0,0.6), 0 0 20px rgba(220,180,100,0.2)',
                            lineHeight: '1.6',
                        }}>
                        Mode Multijoueur
                    </h1>
                    <p style={{
                        ...pixelFont,
                        fontSize: '0.65rem',
                        color: 'rgba(212,200,160,0.6)',
                        textShadow: '0 1px 2px rgba(0,0,0,0.5)',
                        letterSpacing: '0.15em',
                    }}>
                        Défenseur vs Attaquant
                    </p>
                    <PixelBorder className="w-56" />
                </div>

                {/* ════════ Choix ════════ */}
                {mode === 'choix' && (
                    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="flex flex-col gap-4 w-full">

                        {/* Créer — défenseur */}
                        <button onClick={() => setMode('creer')}
                                className="w-full text-left p-5 transition-all hover:translate-y-[-2px] active:translate-y-[1px]"
                                style={{
                                    background: 'rgba(26,20,32,0.85)',
                                    outline: '3px solid #1a0a00',
                                    boxShadow: 'inset 0 3px 0 rgba(220,180,100,0.1), inset 0 -3px 0 rgba(0,0,0,0.3), 0 4px 0 #1a0a00',
                                }}>
                            <div className="flex items-center gap-4">
                                <PixelShield size={40} />
                                <div>
                                    <p className="font-bold"
                                       style={{
                                           ...pixelFont,
                                           fontSize: '0.7rem',
                                           color: '#dcb464',
                                           lineHeight: '1.8',
                                       }}>
                                        Créer un lobby
                                    </p>
                                    <p className="mt-1"
                                       style={{
                                           ...pixelFont,
                                           fontSize: '0.55rem',
                                           color: 'rgba(212,200,160,0.5)',
                                           lineHeight: '1.6',
                                       }}>
                                        Défendez votre forteresse
                                    </p>
                                </div>
                            </div>
                        </button>

                        {/* Rejoindre — attaquant */}
                        <button onClick={() => setMode('rejoindre')}
                                className="w-full text-left p-5 transition-all hover:translate-y-[-2px] active:translate-y-[1px]"
                                style={{
                                    background: 'rgba(60,20,20,0.7)',
                                    outline: '3px solid #1a0a00',
                                    boxShadow: 'inset 0 3px 0 rgba(196,64,48,0.15), inset 0 -3px 0 rgba(0,0,0,0.3), 0 4px 0 #1a0a00',
                                }}>
                            <div className="flex items-center gap-4">
                                <PixelBanner size={40} color="#c44030" />
                                <div>
                                    <p className="font-bold"
                                       style={{
                                           ...pixelFont,
                                           fontSize: '0.7rem',
                                           color: '#c44030',
                                           lineHeight: '1.8',
                                       }}>
                                        Rejoindre un lobby
                                    </p>
                                    <p className="mt-1"
                                       style={{
                                           ...pixelFont,
                                           fontSize: '0.55rem',
                                           color: 'rgba(212,200,160,0.5)',
                                           lineHeight: '1.6',
                                       }}>
                                        Attaquez la forteresse adverse
                                    </p>
                                </div>
                            </div>
                        </button>

                        <button onClick={() => router.push('/')}
                                className="btn-stone w-full py-3 mt-2"
                                style={{ ...pixelFont, fontSize: '0.6rem' }}>
                            ← Retour à l&apos;accueil
                        </button>
                    </motion.div>
                )}

                {/* ════════ Créer ════════ */}
                {mode === 'creer' && (
                    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }}
                                className="flex flex-col gap-4 w-full items-center">
                        <div className="text-center p-5 w-full relative"
                             style={{
                                 background: 'rgba(26,20,32,0.85)',
                                 outline: '3px solid #1a0a00',
                                 boxShadow: 'inset 0 3px 0 rgba(220,180,100,0.08), inset 0 -3px 0 rgba(0,0,0,0.3), 0 3px 0 #1a0a00',
                             }}>
                            <PixelBorder className="absolute top-0 left-0 right-0" />
                            <PixelShield size={32} className="mx-auto mb-3 mt-2" />
                            <p style={{
                                ...pixelFont,
                                fontSize: '0.55rem',
                                color: 'rgba(212,200,160,0.7)',
                                lineHeight: '2',
                            }}>
                                Un lobby sera créé avec une partie en difficulté Chevalier.
                            </p>
                        </div>
                        <button onClick={creerLobby} disabled={loading}
                                className={`btn-gold w-full py-4 flex items-center justify-center gap-2 ${loading ? 'opacity-50' : ''}`}
                                style={{ ...pixelFont, fontSize: '0.65rem' }}>
                            <PixelShield size={18} />
                            {loading ? 'Création...' : 'Créer le lobby'}
                        </button>
                        <button onClick={() => setMode('choix')}
                                className="btn-stone w-full py-3"
                                style={{ ...pixelFont, fontSize: '0.6rem' }}>
                            ← Retour
                        </button>
                    </motion.div>
                )}

                {/* ════════ Rejoindre ════════ */}
                {mode === 'rejoindre' && (
                    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }}
                                className="flex flex-col gap-4 w-full items-center">
                        <div className="w-full p-5 relative"
                             style={{
                                 background: 'rgba(60,20,20,0.6)',
                                 outline: '3px solid #1a0a00',
                                 boxShadow: 'inset 0 3px 0 rgba(196,64,48,0.1), inset 0 -3px 0 rgba(0,0,0,0.3), 0 3px 0 #1a0a00',
                             }}>
                            <PixelBorder className="absolute top-0 left-0 right-0" />
                            <PixelBanner size={32} color="#c44030" className="mx-auto mb-3 mt-2" />
                            <label style={{
                                ...pixelFont,
                                fontSize: '0.55rem',
                                color: 'rgba(212,200,160,0.6)',
                                display: 'block',
                                marginBottom: '8px',
                                textAlign: 'center',
                            }}>
                                Code du lobby
                            </label>
                            <input
                                type="text"
                                value={code}
                                onChange={(e) => setCode(e.target.value.toUpperCase())}
                                placeholder="CODE"
                                maxLength={6}
                                className="w-full px-4 py-3 text-center text-lg tracking-[0.5em] placeholder-gray-700"
                                style={{
                                    ...pixelFont,
                                    background: 'rgba(10,10,15,0.6)',
                                    color: '#dcb464',
                                    border: 'none',
                                    outline: '3px solid #1a0a00',
                                    boxShadow: 'inset 0 3px 0 rgba(0,0,0,0.3), 0 2px 0 #1a0a00',
                                }}
                            />
                        </div>
                        <button onClick={rejoindreLobby} disabled={loading || !code.trim()}
                                className={`btn-blood w-full py-4 flex items-center justify-center gap-2 ${loading || !code.trim() ? 'opacity-50' : ''}`}
                                style={{ ...pixelFont, fontSize: '0.65rem' }}>
                            <PixelBanner size={18} color="#ffe0d0" />
                            {loading ? 'Connexion...' : 'Rejoindre'}
                        </button>
                        <button onClick={() => setMode('choix')}
                                className="btn-stone w-full py-3"
                                style={{ ...pixelFont, fontSize: '0.6rem' }}>
                            ← Retour
                        </button>
                    </motion.div>
                )}

                {/* ════════ Erreur ════════ */}
                {erreur && (
                    <div className="text-center py-2 px-4 w-full"
                         style={{
                             background: 'rgba(196,64,48,0.15)',
                             outline: '2px solid #c44030',
                             boxShadow: '0 2px 0 #1a0a00',
                         }}>
                        <p style={{ ...pixelFont, fontSize: '0.55rem', color: '#c44030', lineHeight: '1.8' }}>
                            {erreur}
                        </p>
                    </div>
                )}
            </div>
        </main>
    );
}