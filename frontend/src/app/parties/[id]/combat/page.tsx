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
import { PixelBorder, PixelShield, PixelBanner, PixelTourelle } from '@/components/PixelSprites';

const API_URL = process.env.NEXT_PUBLIC_API_URL;
const px = { fontFamily: 'var(--font-pixel)' };

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

    useEffect(() => { charger(); return () => { clientRef.current?.deactivate(); }; }, []);

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
        const cellEl = grilleRef.current.querySelector(`[data-col="${cell.col}"][data-row="${cell.row}"]`) as HTMLElement;
        if (!cellEl) return { x: 0, y: 0 };
        const grilleRect = grilleRef.current.getBoundingClientRect();
        const cellRect = cellEl.getBoundingClientRect();
        return { x: cellRect.left - grilleRect.left + cellRect.width / 2 - 16, y: cellRect.top - grilleRect.top + cellRect.height / 2 - 20 };
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
                        setTimeout(() => { router.push(`/parties/${partieId}/resultats`); }, 2000);
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

    const lancerCombat = async () => { connecterWebSocket(); await api.demarrerCombat(partieId); setCombatLance(true); };
    const reprendreDirectement = async () => { setEntreVagues(false); await api.reprendreCombat(partieId); };
    const retourConstruction = () => { clientRef.current?.deactivate(); router.push(`/parties/${partieId}/construction`); };

    const timerPct = combatEtat ? (combatEtat.tempsEcoule / combatEtat.dureeSecondes) * 100 : 0;

    return (
            <main className="h-screen bg-medieval-combat text-white flex flex-col overflow-hidden p-4 gap-3">

                {/* ════════ Header ════════ */}
                <div className="flex justify-between items-center pb-2 flex-shrink-0 relative">
                    <div className="flex items-center gap-3">
                        <PixelBanner size={28} color="#c44030" />
                        <div>
                            <h1 style={{ ...px, fontSize: '0.6rem', color: '#dcb464', textShadow: '0 2px 4px rgba(0,0,0,0.6)', lineHeight: '1.8', letterSpacing: '0.1em', textTransform: 'uppercase' }}>
                                Phase de Combat
                            </h1>
                            <p style={{ ...px, fontSize: '0.32rem', color: 'rgba(212,200,160,0.7)', lineHeight: '1.8' }}>
                                {partie?.joueurNom} — {partie?.difficulte}
                                {connecte && <span style={{ color: '#8cb414', marginLeft: '8px' }}>● Connecté</span>}
                            </p>
                        </div>
                    </div>
                    <div className="flex items-center gap-4">
                        {combatEtat && (
                            <>
                                <div className="flex flex-col items-center px-3 py-1"
                                     style={{ background: 'rgba(26,20,32,0.8)', outline: '2px solid #1a0a00', boxShadow: 'inset 0 2px 0 rgba(220,180,100,0.1), 0 2px 0 #1a0a00' }}>
                                    <p style={{ ...px, fontSize: '0.25rem', color: 'rgba(212,200,160,0.5)', letterSpacing: '0.1em', textTransform: 'uppercase' }}>Score</p>
                                    <p style={{ ...px, fontSize: '0.6rem', color: '#dcb464' }}>{combatEtat.score}</p>
                                </div>
                                <div className="flex flex-col items-center px-3 py-1"
                                     style={{ background: 'rgba(26,20,32,0.8)', outline: '2px solid #1a0a00', boxShadow: 'inset 0 2px 0 rgba(220,180,100,0.1), 0 2px 0 #1a0a00' }}>
                                    <p style={{ ...px, fontSize: '0.25rem', color: 'rgba(212,200,160,0.5)', letterSpacing: '0.1em', textTransform: 'uppercase' }}>Vague</p>
                                    <p style={{ ...px, fontSize: '0.6rem', color: '#dcb464' }}>{combatEtat.vagueNumero} / 5</p>
                                </div>
                                {!combatEtat.derniereVague && (
                                    <div className="flex flex-col items-center px-3 py-1"
                                         style={{
                                             background: 'rgba(26,20,32,0.8)',
                                             outline: `2px solid ${timerPct > 80 ? '#c44030' : '#1a0a00'}`,
                                             boxShadow: timerPct > 80
                                                 ? 'inset 0 2px 0 rgba(196,64,48,0.2), 0 2px 0 #1a0a00, 0 0 12px rgba(196,64,48,0.2)'
                                                 : 'inset 0 2px 0 rgba(220,180,100,0.1), 0 2px 0 #1a0a00',
                                         }}>
                                        <p style={{ ...px, fontSize: '0.25rem', color: 'rgba(212,200,160,0.5)', letterSpacing: '0.1em', textTransform: 'uppercase' }}>Timer</p>
                                        <p className={timerPct > 80 ? 'animate-pulse' : ''}
                                           style={{ ...px, fontSize: '0.5rem', color: timerPct > 80 ? '#c44030' : '#dcb464' }}>
                                            {combatEtat.tempsEcoule}s / {combatEtat.dureeSecondes}s
                                        </p>
                                    </div>
                                )}
                            </>
                        )}
                        {!combatLance && (
                            <button onClick={lancerCombat} className="btn-blood py-3 px-5 flex items-center gap-2"
                                    style={{ ...px, fontSize: '0.45rem' }}>
                                <PixelBanner size={16} color="#ffe0d0" />
                                Lancer le combat
                            </button>
                        )}
                    </div>
                    <PixelBorder className="absolute bottom-0 left-0 right-0" />
                </div>

                <div className="flex gap-4 flex-1 min-h-0">
                    <div className="flex-1 flex flex-col gap-3 min-w-0">
                        {combatEtat && (
                            <BarresEtat forteressePvActuels={combatEtat.forteressePvActuels} forteressePvMax={combatEtat.forteressePvMax}
                                ennemisVivants={combatEtat.ennemisVivants} ennemisTotal={combatEtat.ennemisTotal}
                                tempsEcoule={combatEtat.tempsEcoule} dureeSecondes={combatEtat.dureeSecondes} derniereVague={combatEtat.derniereVague} />
                        )}

                        <CarteCombat ref={grilleRef} tourelles={tourelles} ennemis={combatEtat?.ennemis ?? []}
                            murailles={combatEtat?.murailles ?? []} forteressePvActuels={combatEtat?.forteressePvActuels ?? 0}
                            forteressePvMax={combatEtat?.forteressePvMax ?? 1} getPixelForPos={getPixelForPos} />

                        {/* Entre vagues */}
                        {entreVagues && combatEtat && (
                            <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }}
                                className="flex-shrink-0 relative p-6"
                                style={{ background: 'rgba(26,20,32,0.92)', outline: '3px solid #dcb464',
                                    boxShadow: 'inset 0 3px 0 rgba(220,180,100,0.15), inset 0 -3px 0 rgba(0,0,0,0.3), 0 4px 0 #1a0a00, 0 0 24px rgba(220,180,100,0.15)' }}>
                                <PixelBorder className="absolute top-0 left-0 right-0" />
                                <PixelBorder className="absolute bottom-0 left-0 right-0 rotate-180" />
                                <div className="text-center mb-4 pt-2">
                                    <PixelShield size={40} className="mx-auto mb-2" />
                                    <h2 style={{ ...px, fontSize: '0.6rem', color: '#dcb464', lineHeight: '1.8', letterSpacing: '0.1em', textTransform: 'uppercase', marginBottom: '8px' }}>
                                        Vague {combatEtat.vagueNumero} repoussée !
                                    </h2>
                                    <p style={{ ...px, fontSize: '0.35rem', color: 'rgba(212,200,160,0.7)', lineHeight: '2' }}>
                                        {combatEtat.ennemisVivants > 0
                                            ? `️ ${combatEtat.ennemisVivants} survivant${combatEtat.ennemisVivants > 1 ? 's' : ''} reporté${combatEtat.ennemisVivants > 1 ? 's' : ''}`
                                            : ' Tous les ennemis éliminés !'}
                                    </p>
                                </div>
                                <div className="flex gap-4 justify-center">
                                    <button onClick={retourConstruction} className="btn-stone py-3 px-5 flex items-center gap-2"
                                            style={{ ...px, fontSize: '0.4rem' }}>
                                        <PixelTourelle size={16} /> Fortifier
                                    </button>
                                    <button onClick={reprendreDirectement} className="btn-gold py-3 px-5 flex items-center gap-2"
                                            style={{ ...px, fontSize: '0.4rem' }}>
                                        Vague {combatEtat.vagueNumero + 1} ⚔️
                                    </button>
                                </div>
                            </motion.div>
                        )}

                        {/* Fin de partie */}
                        {combatEtat && !entreVagues && (combatEtat.etat === 'GAGNE' || combatEtat.etat === 'PERDU') && (
                            <motion.div initial={{ opacity: 0, scale: 0.8 }} animate={{ opacity: 1, scale: 1 }}
                                className="text-center py-5 flex-shrink-0 relative"
                                style={{
                                    background: combatEtat.etat === 'GAGNE' ? 'rgba(90,140,40,0.2)' : 'rgba(196,64,48,0.2)',
                                    outline: `3px solid ${combatEtat.etat === 'GAGNE' ? '#5a8c28' : '#c44030'}`,
                                    boxShadow: `0 4px 0 #1a0a00, 0 0 20px ${combatEtat.etat === 'GAGNE' ? 'rgba(90,140,40,0.2)' : 'rgba(196,64,48,0.2)'}`,
                                }}>
                                <p style={{ ...px, fontSize: '0.7rem', color: combatEtat.etat === 'GAGNE' ? '#8cb414' : '#c44030',
                                    textShadow: '0 2px 4px rgba(0,0,0,0.5)', letterSpacing: '0.1em', textTransform: 'uppercase' }}>
                                    {combatEtat.etat === 'GAGNE' ? '👑 Victoire !' : '💀 Défaite...'}
                                </p>
                            </motion.div>
                        )}
                    </div>

                    <PanneauTourelles tourelles={tourelles} />
                </div>
            </main>
        );
    }