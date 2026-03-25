'use client';

import { useState, useEffect, useRef } from 'react';
import { useRouter, useParams, useSearchParams } from 'next/navigation';
import { motion } from 'framer-motion';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { api, LobbyDTO, VagueConfigDTO, UniteConfig } from '@/lib/api';

const API_URL = process.env.NEXT_PUBLIC_API_URL;

const TYPES_UNITES = [
    { type: 'TRIANGLE', label: 'Cavaliers', emoji: '🐎', cout: 30, desc: 'Rapides' },
    { type: 'CERCLE', label: 'Infanterie', emoji: '🗡️', cout: 15, desc: 'Pas chers' },
    { type: 'RECTANGLE', label: 'Béliers', emoji: '🐏', cout: 50, desc: 'Tanks' },
];

export default function LobbyPage() {
    const router = useRouter();
    const params = useParams();
    const searchParams = useSearchParams();
    const lobbyId = params.lobbyId as string;
    const role = searchParams.get('role') || 'ATTAQUANT';
    const partieId = searchParams.get('partieId');

    const [lobby, setLobby] = useState<LobbyDTO | null>(null);
    const [vagues, setVagues] = useState<VagueConfigDTO[]>([
        { numero: 1, unites: [{ type: 'TRIANGLE', quantite: 0 }, { type: 'CERCLE', quantite: 0 }, { type: 'RECTANGLE', quantite: 0 }] },
        { numero: 2, unites: [{ type: 'TRIANGLE', quantite: 0 }, { type: 'CERCLE', quantite: 0 }, { type: 'RECTANGLE', quantite: 0 }] },
        { numero: 3, unites: [{ type: 'TRIANGLE', quantite: 0 }, { type: 'CERCLE', quantite: 0 }, { type: 'RECTANGLE', quantite: 0 }] },
    ]);
    const [loading, setLoading] = useState(false);
    const [erreur, setErreur] = useState('');
    const clientRef = useRef<Client | null>(null);

    useEffect(() => {
        chargerLobby();
        connecterWebSocket();
        if (role === 'DEFENSEUR') {
            api.rejoindreLobby(lobbyId, 'DEFENSEUR');
        }
        return () => { clientRef.current?.deactivate(); };
    }, []);

    const chargerLobby = async () => {
        try {
            const data = await api.getEtatLobby(lobbyId);
            setLobby(data);
        } catch (e: any) {
            setErreur(e.message);
        }
    };

    const connecterWebSocket = () => {
        const client = new Client({
            webSocketFactory: () => new SockJS(`${API_URL}/ws`),
            onConnect: () => {
                client.subscribe(`/topic/lobby/${lobbyId}`, (message) => {
                    const etat: LobbyDTO = JSON.parse(message.body);
                    setLobby(etat);

                    if (etat.etat === 'EN_COURS' && partieId) {
                        setTimeout(() => {
                            router.push(`/parties/${partieId}/combat`);
                        }, 1500);
                    }
                });
            },
        });
        client.activate();
        clientRef.current = client;
    };

    const calculerCoutTotal = () => {
        let total = 0;
        for (const v of vagues) {
            for (const u of v.unites) {
                const type = TYPES_UNITES.find(t => t.type === u.type);
                if (type) total += type.cout * u.quantite;
            }
        }
        return total;
    };

    const budgetMax = lobby?.budgetAttaquant ?? 500;
    const coutTotal = calculerCoutTotal();
    const budgetRestant = budgetMax - coutTotal;

    const modifierUnite = (vagueIdx: number, typeIdx: number, delta: number) => {
        const type = TYPES_UNITES[typeIdx];
        if (delta > 0 && budgetRestant < type.cout) return;

        setVagues(prev => {
            const next = prev.map(v => ({
                ...v,
                unites: v.unites.map(u => ({ ...u })),
            }));
            const unite = next[vagueIdx].unites[typeIdx];
            unite.quantite = Math.max(0, unite.quantite + delta);
            return next;
        });
    };

    const envoyerVagues = async () => {
        setLoading(true);
        setErreur('');
        try {
            const vaguesFiltrees = vagues.map(v => ({
                ...v,
                unites: v.unites.filter(u => u.quantite > 0),
            })).filter(v => v.unites.length > 0);

            await api.configurerVagues(lobbyId, vaguesFiltrees);
        } catch (e: any) {
            setErreur(e.message);
        }
        setLoading(false);
    };

    const marquerPret = async () => {
        setLoading(true);
        setErreur('');
        try {
            if (role === 'ATTAQUANT') {
                await envoyerVagues();
            }
            await api.marquerPret(lobbyId, role);
        } catch (e: any) {
            setErreur(e.message);
        }
        setLoading(false);
    };

    const estPret = role === 'DEFENSEUR' ? lobby?.defenseurPret : lobby?.attaquantPret;

    return (
        <main className="min-h-screen bg-[#0a0a0f] text-white flex flex-col items-center p-4">
            <div className="w-full max-w-2xl space-y-6 mt-8">

                {/* Header lobby */}
                <div className="text-center">
                    <h1 className="text-2xl font-black text-[#c9a84c] tracking-widest uppercase mb-1"
                        style={{ fontFamily: 'var(--font-cinzel)' }}>
                        Lobby {lobbyId}
                    </h1>
                    <p className="text-gray-400 text-sm">
                        {role === 'DEFENSEUR' ? '🛡️ Vous défendez' : '⚔️ Vous attaquez'}
                    </p>
                </div>

                {/* Statut joueurs */}
                <div className="flex gap-4">
                    <div className={`flex-1 rounded-xl p-4 border-2 text-center ${
                        lobby?.defenseurConnecte ? 'border-green-500 bg-green-900/20' : 'border-gray-700 bg-[#1a1a22]'
                    }`}>
                        <p className="text-xl mb-1">🛡️</p>
                        <p className="text-sm font-bold">Défenseur</p>
                        <p className="text-xs text-gray-400 mt-1">
                            {lobby?.defenseurPret ? '✅ Prêt' : lobby?.defenseurConnecte ? '🟡 Connecté' : '⏳ En attente'}
                        </p>
                    </div>
                    <div className={`flex-1 rounded-xl p-4 border-2 text-center ${
                        lobby?.attaquantConnecte ? 'border-red-500 bg-red-900/20' : 'border-gray-700 bg-[#1a1a22]'
                    }`}>
                        <p className="text-xl mb-1">⚔️</p>
                        <p className="text-sm font-bold">Attaquant</p>
                        <p className="text-xs text-gray-400 mt-1">
                            {lobby?.attaquantPret ? '✅ Prêt' : lobby?.attaquantConnecte ? '🟡 Connecté' : '⏳ En attente'}
                        </p>
                    </div>
                </div>

                {/* Défenseur — construction */}
                {role === 'DEFENSEUR' && partieId && (
                    <div className="bg-[#1a1a22] border border-[#3a3a48] rounded-xl p-4 text-center">
                        <p className="text-gray-400 mb-3">Préparez vos défenses avant le combat !</p>
                        <button onClick={() => router.push(`/parties/${partieId}/construction?from=lobby&lobbyId=${lobbyId}`)}
                                className="bg-[#c9a84c] hover:bg-[#e8c96d] text-black font-black px-6 py-2 rounded-lg uppercase tracking-widest transition-all text-sm">
                            🔨 Construire les défenses
                        </button>
                    </div>
                )}

                {/* Attaquant — composition des vagues */}
                {role === 'ATTAQUANT' && (
                    <div className="space-y-4">
                        <div className="flex justify-between items-center">
                            <h2 className="text-lg font-black text-[#c9a84c] uppercase tracking-widest"
                                style={{ fontFamily: 'var(--font-cinzel)' }}>
                                Composer vos vagues
                            </h2>
                            <p className={`text-sm font-bold ${budgetRestant < 0 ? 'text-red-400' : 'text-[#c9a84c]'}`}>
                                💰 {budgetRestant} / {budgetMax}
                            </p>
                        </div>

                        {vagues.map((vague, vi) => (
                            <div key={vi} className="bg-[#1a1a22] border border-[#3a3a48] rounded-xl p-4">
                                <p className="text-sm font-bold text-[#c9a84c] mb-3">Vague {vague.numero}</p>
                                <div className="space-y-2">
                                    {TYPES_UNITES.map((type, ti) => {
                                        const unite = vague.unites[ti];
                                        return (
                                            <div key={type.type} className="flex items-center justify-between">
                                                <div className="flex items-center gap-2">
                                                    <span className="text-lg">{type.emoji}</span>
                                                    <span className="text-sm">{type.label}</span>
                                                    <span className="text-xs text-gray-500">({type.cout}💰)</span>
                                                </div>
                                                <div className="flex items-center gap-2">
                                                    <button onClick={() => modifierUnite(vi, ti, -1)}
                                                            className="w-7 h-7 rounded bg-[#2a2a35] hover:bg-[#3a3a48] text-white text-sm font-bold">
                                                        -
                                                    </button>
                                                    <span className="w-8 text-center font-bold">{unite.quantite}</span>
                                                    <button onClick={() => modifierUnite(vi, ti, 1)}
                                                            disabled={budgetRestant < type.cout}
                                                            className="w-7 h-7 rounded bg-[#2a2a35] hover:bg-[#3a3a48] text-white text-sm font-bold disabled:opacity-30">
                                                        +
                                                    </button>
                                                </div>
                                            </div>
                                        );
                                    })}
                                </div>
                            </div>
                        ))}
                    </div>
                )}

                {/* Bouton Prêt */}
                {!estPret && (
                    <button onClick={marquerPret} disabled={loading || (role === 'ATTAQUANT' && coutTotal === 0)}
                            className="w-full bg-[#c9a84c] hover:bg-[#e8c96d] text-black font-black px-6 py-3 rounded-lg uppercase tracking-widest transition-all disabled:opacity-50 text-lg"
                            style={{ fontFamily: 'var(--font-cinzel)' }}>
                        {loading ? 'Envoi...' : '✅ Prêt !'}
                    </button>
                )}

                {estPret && (
                    <motion.div
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        className="text-center bg-green-900/30 border border-green-500 rounded-xl p-4">
                        <p className="text-green-400 font-bold">✅ Vous êtes prêt !</p>
                        <p className="text-gray-400 text-sm mt-1">En attente de l'autre joueur...</p>
                    </motion.div>
                )}

                {lobby?.etat === 'EN_COURS' && (
                    <motion.div
                        initial={{ scale: 0.8, opacity: 0 }}
                        animate={{ scale: 1, opacity: 1 }}
                        className="text-center bg-[#c9a84c]/20 border-2 border-[#c9a84c] rounded-xl p-6">
                        <p className="text-3xl mb-2">⚔️</p>
                        <p className="text-xl font-black text-[#c9a84c] uppercase tracking-widest"
                           style={{ fontFamily: 'var(--font-cinzel)' }}>
                            Combat lancé !
                        </p>
                        <p className="text-gray-400 text-sm mt-1">Redirection...</p>
                    </motion.div>
                )}

                {erreur && (
                    <p className="text-red-400 text-sm text-center">{erreur}</p>
                )}
            </div>
        </main>
    );
}