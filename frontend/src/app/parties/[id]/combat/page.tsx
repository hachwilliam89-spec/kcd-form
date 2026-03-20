'use client';

import { useState, useEffect, useRef, useCallback } from 'react';
import { motion } from 'framer-motion';
import { useRouter, useParams } from 'next/navigation';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { api, Partie, Tourelle } from '@/lib/api';
import CarteCombat from './CarteCombat';
import BarresEtat from './BarresEtat';
import PanneauTourelles from './PanneauTourelles';
import { CombatEtat } from '@/lib/types';

const API_URL = process.env.NEXT_PUBLIC_API_URL;

const CHEMIN_POSITIONS: { col: number; row: number }[] = [
    { col: 1, row: 2 }, { col: 2, row: 2 }, { col: 3, row: 2 },
    { col: 4, row: 2 }, { col: 5, row: 2 },
    { col: 5, row: 3 }, { col: 5, row: 4 },
    { col: 4, row: 4 }, { col: 3, row: 4 }, { col: 2, row: 4 }, { col: 1, row: 4 },
    { col: 1, row: 5 }, { col: 1, row: 6 },
    { col: 2, row: 6 }, { col: 3, row: 6 }, { col: 4, row: 6 },
    { col: 5, row: 6 }, { col: 6, row: 6 }, { col: 7, row: 6 },
    { col: 8, row: 6 }, { col: 9, row: 6 },
];

export default function CombatPage() {
    const router = useRouter();
    const params = useParams();
    const partieId = Number(params.id);

    const [partie, setPartie] = useState<Partie | null>(null);
    const [tourelles, setTourelles] = useState<Tourelle[]>([]);
    const [combatEtat, setCombatEtat] = useState<CombatEtat | null>(null);
    const [combatLance, setCombatLance] = useState(false);
    const [connecte, setConnecte] = useState(false);
    const [entreVagues, setEntreVagues] = useState(false);
    const clientRef = useRef<Client | null>(null);
    const grilleRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        charger();
        return () => { clientRef.current?.deactivate(); };
    }, []);

    const charger = async () => {
        const p = await api.getPartie(partieId);
        setPartie(p);
        const t = await api.getTourelles(partieId);
        setTourelles(t);
    };

    const getPixelForPos = useCallback((pos: number) => {
        const idx = Math.max(0, Math.min(pos, CHEMIN_POSITIONS.length - 1));
        const cell = CHEMIN_POSITIONS[idx];
        if (!grilleRef.current) return { x: 0, y: 0 };
        const cellEl = grilleRef.current.querySelector(
            `[data-col="${cell.col}"][data-row="${cell.row}"]`
        ) as HTMLElement;
        if (!cellEl) return { x: 0, y: 0 };
        const grilleRect = grilleRef.current.getBoundingClientRect();
        const cellRect = cellEl.getBoundingClientRect();
        return {
            x: cellRect.left - grilleRect.left + cellRect.width / 2 - 16,
            y: cellRect.top - grilleRect.top + cellRect.height / 2 - 20,
        };
    }, []);

    const connecterWebSocket = () => {
        const client = new Client({
            webSocketFactory: () => new SockJS(`${API_URL}/ws`),
            onConnect: () => {
                setConnecte(true);
                client.subscribe(`/topic/combat/${partieId}`, (message) => {
                    const etat: CombatEtat = JSON.parse(message.body);
                    setCombatEtat(etat);

                    if (etat.etat === 'GAGNE' || etat.etat === 'PERDU') {
                        setTimeout(() => {
                            router.push(`/parties/${partieId}/resultats`);
                        }, 2000);
                    } else if (etat.etat === 'ENTRE_VAGUES') {
                        setEntreVagues(true);
                    }
                });
            },
            onDisconnect: () => setConnecte(false),
        });
        client.activate();
        clientRef.current = client;
    };

    const lancerCombat = async () => {
        connecterWebSocket();
        await api.demarrerCombat(partieId);
        setCombatLance(true);
    };

    const reprendreDirectement = async () => {
        setEntreVagues(false);
        await api.reprendreCombat(partieId);
    };

    const retourConstruction = () => {
        clientRef.current?.deactivate();
        router.push(`/parties/${partieId}/construction`);
    };

    const timerPct = combatEtat
        ? (combatEtat.tempsEcoule / combatEtat.dureeSecondes) * 100
        : 0;

    return (
        <main className="h-screen bg-[#0a0a0f] text-white flex flex-col overflow-hidden p-4 gap-4">

            {/* Header */}
            <div className="flex justify-between items-center border-b border-[#c9a84c]/20 pb-3 flex-shrink-0">
                <div>
                    <h1 className="text-xl font-black text-[#c9a84c] tracking-widest uppercase"
                        style={{ fontFamily: 'var(--font-cinzel)' }}>
                        ⚔️ Phase de Combat
                    </h1>
                    <p className="text-gray-400 text-sm">
                        {partie?.joueurNom} — {partie?.difficulte}
                        {connecte && <span className="text-green-400 ml-2">● Connecté</span>}
                    </p>
                </div>
                <div className="flex items-center gap-6">
                    {combatEtat && (
                        <>
                            <div className="text-right">
                                <p className="text-xs text-gray-400 uppercase tracking-widest">Score</p>
                                <p className="text-2xl font-black text-[#c9a84c]">{combatEtat.score}</p>
                            </div>
                            <div className="text-right">
                                <p className="text-xs text-gray-400 uppercase tracking-widest">Vague</p>
                                <p className="text-2xl font-black text-[#c9a84c]">
                                    {combatEtat.vagueNumero} / 5
                                </p>
                            </div>
                            {!combatEtat.derniereVague && (
                                <div className="text-right">
                                    <p className="text-xs text-gray-400 uppercase tracking-widest">Timer</p>
                                    <p className={`text-2xl font-black ${
                                        timerPct > 80 ? 'text-red-400 animate-pulse' : 'text-[#c9a84c]'
                                    }`}>
                                        {combatEtat.tempsEcoule}s / {combatEtat.dureeSecondes}s
                                    </p>
                                </div>
                            )}
                        </>
                    )}
                    {!combatLance && (
                        <button onClick={lancerCombat}
                                className="bg-[#c9a84c] hover:bg-[#e8c96d] text-black font-black px-5 py-3 rounded-lg uppercase tracking-widest transition-all text-sm"
                                style={{ fontFamily: 'var(--font-cinzel)' }}>
                            Lancer le combat 👾
                        </button>
                    )}
                </div>
            </div>

            <div className="flex gap-4 flex-1 min-h-0">

                {/* Zone carte */}
                <div className="flex-1 flex flex-col gap-3 min-w-0">

                    {combatEtat && (
                        <BarresEtat
                            forteressePvActuels={combatEtat.forteressePvActuels}
                            forteressePvMax={combatEtat.forteressePvMax}
                            ennemisVivants={combatEtat.ennemisVivants}
                            ennemisTotal={combatEtat.ennemisTotal}
                            tempsEcoule={combatEtat.tempsEcoule}
                            dureeSecondes={combatEtat.dureeSecondes}
                            derniereVague={combatEtat.derniereVague}
                        />
                    )}

                    <CarteCombat
                        ref={grilleRef}
                        tourelles={tourelles}
                        ennemis={combatEtat?.ennemis ?? []}
                        murailles={combatEtat?.murailles ?? []}
                        forteressePvActuels={combatEtat?.forteressePvActuels ?? 0}
                        forteressePvMax={combatEtat?.forteressePvMax ?? 1}
                        getPixelForPos={getPixelForPos}
                    />

                    {/* Entre vagues */}
                    {entreVagues && combatEtat && (
                        <motion.div
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            className="bg-[#1a1a22] border-2 border-[#c9a84c] rounded-xl p-6 flex-shrink-0">
                            <div className="text-center mb-4">
                                <p className="text-3xl mb-2">🛡️</p>
                                <h2 className="text-xl font-black text-[#c9a84c] uppercase tracking-widest mb-2"
                                    style={{ fontFamily: 'var(--font-cinzel)' }}>
                                    Vague {combatEtat.vagueNumero} repoussée !
                                </h2>
                                <p className="text-gray-400">
                                    {combatEtat.ennemisVivants > 0
                                        ? `⚠️ ${combatEtat.ennemisVivants} survivant${combatEtat.ennemisVivants > 1 ? 's' : ''} reporté${combatEtat.ennemisVivants > 1 ? 's' : ''} à la prochaine vague`
                                        : '✅ Tous les ennemis éliminés !'}
                                </p>
                            </div>
                            <div className="flex gap-3 justify-center">
                                <button onClick={retourConstruction}
                                        className="bg-[#2a2a35] hover:bg-[#3a3a48] text-white font-black px-6 py-3 rounded-lg uppercase tracking-widest transition-all text-sm"
                                        style={{ fontFamily: 'var(--font-cinzel)' }}>
                                    🔨 Fortifier
                                </button>
                                <button onClick={reprendreDirectement}
                                        className="bg-[#c9a84c] hover:bg-[#e8c96d] text-black font-black px-6 py-3 rounded-lg uppercase tracking-widest transition-all text-sm"
                                        style={{ fontFamily: 'var(--font-cinzel)' }}>
                                    Vague {combatEtat.vagueNumero + 1} ⚔️
                                </button>
                            </div>
                        </motion.div>
                    )}

                    {/* Fin de partie */}
                    {combatEtat && !entreVagues && (combatEtat.etat === 'GAGNE' || combatEtat.etat === 'PERDU') && (
                        <motion.div
                            initial={{ opacity: 0, scale: 0.8 }}
                            animate={{ opacity: 1, scale: 1 }}
                            className={`text-center py-4 rounded-xl font-black text-2xl uppercase tracking-widest flex-shrink-0 ${
                                combatEtat.etat === 'GAGNE'
                                    ? 'bg-green-900/50 border border-green-500 text-green-400'
                                    : 'bg-red-900/50 border border-red-500 text-red-400'
                            }`}
                            style={{ fontFamily: 'var(--font-cinzel)' }}>
                            {combatEtat.etat === 'GAGNE' ? '👑 Victoire !' : '💀 Défaite...'}
                        </motion.div>
                    )}
                </div>

                <PanneauTourelles tourelles={tourelles} />
            </div>
        </main>
    );
}