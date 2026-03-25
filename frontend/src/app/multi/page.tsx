'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { motion } from 'framer-motion';
import { api } from '@/lib/api';

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

    return (
        <main className="min-h-screen bg-[#0a0a0f] text-white flex flex-col items-center justify-center p-4">
            <div className="w-full max-w-md space-y-6">
                <div className="text-center">
                    <h1 className="text-3xl font-black text-[#c9a84c] tracking-widest uppercase mb-2"
                        style={{ fontFamily: 'var(--font-cinzel)' }}>
                        ⚔️ Mode Multijoueur
                    </h1>
                    <p className="text-gray-400 text-sm">Défenseur vs Attaquant</p>
                </div>

                {mode === 'choix' && (
                    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-4">
                        <button onClick={() => setMode('creer')}
                                className="w-full bg-[#1a1a22] border-2 border-[#c9a84c] rounded-xl p-6 hover:bg-[#2a2a35] transition-all text-left">
                            <p className="text-xl mb-1">🛡️ Créer un lobby</p>
                            <p className="text-gray-400 text-sm">Défendez votre forteresse</p>
                        </button>
                        <button onClick={() => setMode('rejoindre')}
                                className="w-full bg-[#1a1a22] border-2 border-[#8b1a1a] rounded-xl p-6 hover:bg-[#2a2a35] transition-all text-left">
                            <p className="text-xl mb-1">⚔️ Rejoindre un lobby</p>
                            <p className="text-gray-400 text-sm">Attaquez la forteresse adverse</p>
                        </button>
                        <button onClick={() => router.push('/')}
                                className="w-full text-gray-500 hover:text-gray-300 text-sm transition-colors">
                            ← Retour à l'accueil
                        </button>
                    </motion.div>
                )}

                {mode === 'creer' && (
                    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-4 text-center">
                        <p className="text-gray-400">Un lobby sera créé avec une partie en difficulté Chevalier.</p>
                        <button onClick={creerLobby} disabled={loading}
                                className="w-full bg-[#c9a84c] hover:bg-[#e8c96d] text-black font-black px-6 py-3 rounded-lg uppercase tracking-widest transition-all disabled:opacity-50">
                            {loading ? 'Création...' : '🛡️ Créer le lobby'}
                        </button>
                        <button onClick={() => setMode('choix')}
                                className="text-gray-500 hover:text-gray-300 text-sm transition-colors">
                            ← Retour
                        </button>
                    </motion.div>
                )}

                {mode === 'rejoindre' && (
                    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-4 text-center">
                        <input
                            type="text"
                            value={code}
                            onChange={(e) => setCode(e.target.value.toUpperCase())}
                            placeholder="CODE DU LOBBY"
                            maxLength={6}
                            className="w-full bg-[#1a1a22] border-2 border-[#3a3a48] rounded-lg px-4 py-3 text-center text-2xl tracking-[0.5em] font-black text-[#c9a84c] placeholder-gray-600 focus:border-[#c9a84c] outline-none"
                        />
                        <button onClick={rejoindreLobby} disabled={loading || !code.trim()}
                                className="w-full bg-[#8b1a1a] hover:bg-[#a52020] text-white font-black px-6 py-3 rounded-lg uppercase tracking-widest transition-all disabled:opacity-50">
                            {loading ? 'Connexion...' : '⚔️ Rejoindre'}
                        </button>
                        <button onClick={() => setMode('choix')}
                                className="text-gray-500 hover:text-gray-300 text-sm transition-colors">
                            ← Retour
                        </button>
                    </motion.div>
                )}

                {erreur && (
                    <p className="text-red-400 text-sm text-center">{erreur}</p>
                )}
            </div>
        </main>
    );
}