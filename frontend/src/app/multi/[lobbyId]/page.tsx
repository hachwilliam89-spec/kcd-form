'use client';

import { useState, useEffect, useRef } from 'react';
import { useRouter, useParams, useSearchParams } from 'next/navigation';
import { motion, AnimatePresence } from 'framer-motion';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { api, LobbyDTO, VagueConfigDTO, UniteConfig } from '@/lib/api';

const API_URL = process.env.NEXT_PUBLIC_API_URL;

const TYPES_UNITES = [
    { type: 'TRIANGLE', label: 'Cavaliers', emoji: '🐎', cout: 30, desc: 'Rapides, contournent les murailles', color: 'var(--flag-red)' },
    { type: 'CERCLE', label: 'Infanterie', emoji: '🗡️', cout: 15, desc: 'Peu chers, nombreux', color: 'var(--sky)' },
    { type: 'RECTANGLE', label: 'Béliers', emoji: '🐏', cout: 50, desc: 'Tanks, détruisent les murs', color: 'var(--wood)' },
];

/* ── Pixel art ornements SVG ── */
const SwordDivider = () => (
    <div className="flex items-center gap-3 my-4 opacity-60">
        <div className="flex-1 separator-pixel" />
        <svg width="20" height="20" viewBox="0 0 16 16" className="pixel-render" style={{ imageRendering: 'pixelated' }}>
            <rect x="7" y="0" width="2" height="12" fill="var(--stone-light)" />
            <rect x="4" y="10" width="8" height="2" fill="var(--gold)" />
            <rect x="6" y="12" width="4" height="3" fill="var(--wood-dark)" />
            <rect x="7" y="15" width="2" height="1" fill="var(--gold-dark)" />
        </svg>
        <div className="flex-1 separator-pixel" />
    </div>
);

const ShieldIcon = () => (
    <svg width="48" height="48" viewBox="0 0 16 16" className="pixel-render" style={{ imageRendering: 'pixelated' }}>
        <rect x="3" y="1" width="10" height="2" fill="var(--stone)" />
        <rect x="2" y="3" width="12" height="2" fill="var(--sky)" />
        <rect x="2" y="5" width="12" height="2" fill="var(--sky)" />
        <rect x="3" y="7" width="10" height="2" fill="var(--sky)" />
        <rect x="4" y="9" width="8" height="2" fill="var(--sky)" />
        <rect x="5" y="11" width="6" height="2" fill="var(--sky)" />
        <rect x="6" y="13" width="4" height="1" fill="var(--sky)" />
        <rect x="7" y="14" width="2" height="1" fill="var(--sky)" />
        <rect x="7" y="3" width="2" height="9" fill="var(--gold)" />
        <rect x="4" y="5" width="8" height="2" fill="var(--gold)" />
    </svg>
);

const SwordIcon = () => (
    <svg width="48" height="48" viewBox="0 0 16 16" className="pixel-render" style={{ imageRendering: 'pixelated' }}>
        <rect x="12" y="1" width="2" height="2" fill="var(--flag-red)" />
        <rect x="10" y="3" width="2" height="2" fill="var(--stone-light)" />
        <rect x="8" y="5" width="2" height="2" fill="var(--stone-light)" />
        <rect x="6" y="7" width="2" height="2" fill="var(--stone-light)" />
        <rect x="4" y="9" width="2" height="2" fill="var(--stone-light)" />
        <rect x="2" y="11" width="4" height="2" fill="var(--gold)" />
        <rect x="1" y="12" width="2" height="2" fill="var(--wood-dark)" />
        <rect x="0" y="14" width="2" height="2" fill="var(--gold-dark)" />
        <rect x="13" y="2" width="1" height="1" fill="#fff" opacity="0.4" />
        <rect x="11" y="4" width="1" height="1" fill="#fff" opacity="0.3" />
    </svg>
);

/* ── Barre de budget pixel ── */
const BudgetBar = ({ spent, max }: { spent: number; max: number }) => {
    const pct = Math.min((spent / max) * 100, 100);
    const isOver = spent > max;
    return (
        <div className="panel-stone p-4">
            <div className="flex justify-between items-center mb-2">
                <span className="label-pixel" style={{ color: 'var(--parchment-darker)' }}>
                    Tresor de guerre
                </span>
                <span className="label-pixel-lg" style={{
                    color: isOver ? 'var(--flag-red)' : 'var(--gold-bright)',
                }}>
                    {max - spent} / {max} or
                </span>
            </div>
            <div className="w-full h-5 relative" style={{
                background: 'var(--bg-deep)',
                outline: '3px solid var(--stone-dark)',
                imageRendering: 'pixelated',
            }}>
                <motion.div
                    className="h-full"
                    initial={{ width: 0 }}
                    animate={{ width: `${pct}%` }}
                    transition={{ type: 'spring', stiffness: 120, damping: 20 }}
                    style={{
                        background: isOver
                            ? 'linear-gradient(90deg, var(--flag-red), #ff6040)'
                            : pct > 80
                                ? 'linear-gradient(90deg, var(--wood-dark), var(--flag-orange))'
                                : 'linear-gradient(90deg, var(--gold-dark), var(--gold-bright))',
                        boxShadow: 'inset 0 2px 0 rgba(255,255,255,0.2), inset 0 -2px 0 rgba(0,0,0,0.3)',
                        imageRendering: 'pixelated',
                    }}
                />
                {[25, 50, 75].map(p => (
                    <div key={p} className="absolute top-0 bottom-0 w-px opacity-30"
                         style={{ left: `${p}%`, background: 'var(--stone-light)' }} />
                ))}
            </div>
        </div>
    );
};

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
    const [vagueOuverte, setVagueOuverte] = useState(0);
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
                            router.push(`/parties/${partieId}/combat?role=${role}`);
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

    const calculerCoutVague = (vagueIdx: number) => {
        let total = 0;
        for (const u of vagues[vagueIdx].unites) {
            const type = TYPES_UNITES.find(t => t.type === u.type);
            if (type) total += type.cout * u.quantite;
        }
        return total;
    };

    const totalUnitesVague = (vagueIdx: number) => {
        return vagues[vagueIdx].unites.reduce((sum, u) => sum + u.quantite, 0);
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

    /* ── Statut joueur card ── */
    const JoueurCard = ({ joueur, connecte, pret }: { joueur: 'DEFENSEUR' | 'ATTAQUANT'; connecte?: boolean; pret?: boolean }) => {
        const estDefenseur = joueur === 'DEFENSEUR';
        const estMoi = joueur === role;
        return (
            <motion.div
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: estDefenseur ? 0.2 : 0.3 }}
                className={`flex-1 relative overflow-hidden ${estDefenseur ? 'card-medieval' : 'card-blood'}`}
                style={{
                    padding: '20px',
                    outline: estMoi ? '3px solid var(--gold)' : 'none',
                    outlineOffset: '-1px',
                }}
            >
                {estMoi && (
                    <div className="absolute top-0 right-0 px-2 py-0.5 label-pixel"
                         style={{
                             background: 'var(--gold)',
                             color: 'var(--bg-deep)',
                             fontSize: '0.45rem',
                         }}>
                        Vous
                    </div>
                )}

                <div className="flex flex-col items-center gap-3">
                    {estDefenseur ? <ShieldIcon /> : <SwordIcon />}

                    <div className="text-center">
                        <p className="label-pixel-lg"
                           style={{ color: estDefenseur ? 'var(--sky-light)' : 'var(--flag-red)' }}>
                            {estDefenseur ? 'Defenseur' : 'Attaquant'}
                        </p>
                        <p className="text-xs mt-1.5" style={{ color: 'var(--parchment-darker)', fontFamily: 'var(--font-crimson)' }}>
                            {estDefenseur ? 'Place les tourelles' : 'Compose les vagues'}
                        </p>
                    </div>

                    {/* Statut */}
                    <div className="w-full text-center py-2 mt-1" style={{
                        background: pret
                            ? 'rgba(90,140,40,0.2)'
                            : connecte
                                ? 'rgba(220,180,100,0.1)'
                                : 'rgba(100,100,120,0.1)',
                        border: `2px solid ${pret ? 'var(--grass)' : connecte ? 'var(--gold-dark)' : 'var(--night-light)'}`,
                        imageRendering: 'pixelated',
                    }}>
                        <span className="label-pixel" style={{
                            color: pret ? 'var(--grass-light)' : connecte ? 'var(--gold)' : 'var(--night-light)',
                            fontSize: '0.5rem',
                        }}>
                            {pret ? 'Pret !' : connecte ? 'Connecte' : 'Attente...'}
                        </span>
                    </div>
                </div>
            </motion.div>
        );
    };

    return (
        <main className="min-h-screen bg-medieval-multi text-white flex flex-col items-center px-4 py-6">
            <div className="w-full max-w-2xl space-y-5 mt-4">

                {/* ═══ Header ═══ */}
                <motion.div
                    initial={{ opacity: 0, y: -20 }}
                    animate={{ opacity: 1, y: 0 }}
                    className="text-center"
                >
                    <p className="label-pixel mb-3" style={{ color: 'var(--parchment-darker)', fontSize: '0.5rem' }}>
                        Salle de guerre
                    </p>
                    <h1 className="text-3xl font-black tracking-widest uppercase text-gold-glow"
                        style={{ fontFamily: 'var(--font-cinzel)' }}>
                        Lobby #{lobbyId.slice(0, 6)}
                    </h1>
                    <div className="flex items-center justify-center gap-2 mt-3">
                        <div className="w-2 h-2 rounded-full animate-pixel-pulse"
                             style={{ background: lobby?.etat === 'EN_COURS' ? 'var(--grass-light)' : 'var(--gold)' }} />
                        <span className="label-pixel" style={{ color: 'var(--parchment-darker)', fontSize: '0.45rem' }}>
                            {lobby?.etat === 'ATTENTE' ? 'En preparation' : lobby?.etat === 'EN_COURS' ? 'Combat !' : lobby?.etat ?? '...'}
                        </span>
                    </div>
                </motion.div>

                <SwordDivider />

                {/* ═══ Statut joueurs ═══ */}
                <div className="flex gap-4">
                    <JoueurCard joueur="DEFENSEUR" connecte={lobby?.defenseurConnecte} pret={lobby?.defenseurPret} />
                    <JoueurCard joueur="ATTAQUANT" connecte={lobby?.attaquantConnecte} pret={lobby?.attaquantPret} />
                </div>

                {/* ═══ Défenseur — accès construction ═══ */}
                {role === 'DEFENSEUR' && partieId && (
                    <motion.div
                        initial={{ opacity: 0, scale: 0.95 }}
                        animate={{ opacity: 1, scale: 1 }}
                        transition={{ delay: 0.4 }}
                        className="card-medieval p-6 text-center"
                    >
                        <p className="text-sm mb-4" style={{ color: 'var(--parchment)', fontFamily: 'var(--font-crimson)' }}>
                            Érigez vos tourelles et murailles avant que l'ennemi ne frappe !
                        </p>
                        <button
                            onClick={() => router.push(`/parties/${partieId}/construction?from=lobby&lobbyId=${lobbyId}`)}
                            className="btn-gold"
                        >
                            Construire
                        </button>
                    </motion.div>
                )}

                {/* ═══ Attaquant — composition des vagues ═══ */}
                {role === 'ATTAQUANT' && (
                    <motion.div
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        transition={{ delay: 0.4 }}
                        className="space-y-4"
                    >
                        <BudgetBar spent={coutTotal} max={budgetMax} />

                        {vagues.map((vague, vi) => {
                            const isOpen = vagueOuverte === vi;
                            const coutVague = calculerCoutVague(vi);
                            const totalUnites = totalUnitesVague(vi);

                            return (
                                <motion.div
                                    key={vi}
                                    initial={{ opacity: 0, y: 15 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    transition={{ delay: 0.5 + vi * 0.1 }}
                                    className="card-medieval overflow-hidden"
                                >
                                    <button
                                        onClick={() => setVagueOuverte(isOpen ? -1 : vi)}
                                        className="w-full flex items-center justify-between p-4 transition-colors"
                                        style={{ background: isOpen ? 'rgba(220,180,100,0.05)' : 'transparent' }}
                                    >
                                        <div className="flex items-center gap-3">
                                            <div className="w-8 h-8 flex items-center justify-center label-pixel"
                                                 style={{
                                                     fontSize: '0.6rem',
                                                     background: coutVague > 0 ? 'var(--gold-dark)' : 'var(--night)',
                                                     color: coutVague > 0 ? 'var(--bg-deep)' : 'var(--night-light)',
                                                     outline: `2px solid ${coutVague > 0 ? 'var(--gold)' : 'var(--night-light)'}`,
                                                     imageRendering: 'pixelated',
                                                 }}>
                                                {vague.numero}
                                            </div>
                                            <span className="label-pixel-lg"
                                                  style={{ color: 'var(--parchment)' }}>
                                                Vague {vague.numero}
                                            </span>
                                        </div>
                                        <div className="flex items-center gap-4">
                                            {totalUnites > 0 && (
                                                <span className="label-pixel" style={{ color: 'var(--parchment-darker)', fontSize: '0.5rem' }}>
                                                    {totalUnites} unite{totalUnites > 1 ? 's' : ''} - {coutVague} or
                                                </span>
                                            )}
                                            <motion.span
                                                animate={{ rotate: isOpen ? 180 : 0 }}
                                                transition={{ duration: 0.2 }}
                                                style={{ color: 'var(--gold-dark)' }}
                                            >
                                                ▾
                                            </motion.span>
                                        </div>
                                    </button>

                                    <AnimatePresence>
                                        {isOpen && (
                                            <motion.div
                                                initial={{ height: 0, opacity: 0 }}
                                                animate={{ height: 'auto', opacity: 1 }}
                                                exit={{ height: 0, opacity: 0 }}
                                                transition={{ duration: 0.25 }}
                                                className="overflow-hidden"
                                            >
                                                <div className="separator-medieval" />
                                                <div className="p-4 space-y-3">
                                                    {TYPES_UNITES.map((type, ti) => {
                                                        const unite = vague.unites[ti];
                                                        const canAdd = budgetRestant >= type.cout;
                                                        return (
                                                            <div key={type.type} className="flex items-center justify-between panel-stone p-3">
                                                                <div className="flex items-center gap-3">
                                                                    <span className="text-2xl">{type.emoji}</span>
                                                                    <div>
                                                                        <p className="label-pixel" style={{ color: type.color, fontSize: '0.55rem' }}>
                                                                            {type.label}
                                                                        </p>
                                                                        <p className="text-[10px] mt-0.5" style={{ color: 'var(--parchment-darker)', fontFamily: 'var(--font-crimson)' }}>
                                                                            {type.desc} · {type.cout} or
                                                                        </p>
                                                                    </div>
                                                                </div>
                                                                <div className="flex items-center gap-2">
                                                                    <button
                                                                        onClick={() => modifierUnite(vi, ti, -1)}
                                                                        disabled={unite.quantite === 0}
                                                                        className="btn-stone !p-0 disabled:opacity-20"
                                                                        style={{ width: 30, height: 30, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.7rem' }}
                                                                    >
                                                                        -
                                                                    </button>
                                                                    <motion.span
                                                                        key={unite.quantite}
                                                                        initial={{ scale: 1.4 }}
                                                                        animate={{ scale: 1 }}
                                                                        className="w-8 text-center label-pixel-lg"
                                                                        style={{
                                                                            color: unite.quantite > 0 ? 'var(--gold-bright)' : 'var(--night-light)',
                                                                        }}
                                                                    >
                                                                        {unite.quantite}
                                                                    </motion.span>
                                                                    <button
                                                                        onClick={() => modifierUnite(vi, ti, 1)}
                                                                        disabled={!canAdd}
                                                                        className="btn-stone !p-0 disabled:opacity-20"
                                                                        style={{ width: 30, height: 30, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.7rem' }}
                                                                    >
                                                                        +
                                                                    </button>
                                                                </div>
                                                            </div>
                                                        );
                                                    })}
                                                </div>
                                            </motion.div>
                                        )}
                                    </AnimatePresence>
                                </motion.div>
                            );
                        })}
                    </motion.div>
                )}

                {/* ═══ Bouton Prêt ═══ */}
                <AnimatePresence mode="wait">
                    {!estPret && lobby?.etat !== 'EN_COURS' && (
                        <motion.div
                            key="pret"
                            initial={{ opacity: 0, y: 10 }}
                            animate={{ opacity: 1, y: 0 }}
                            exit={{ opacity: 0, y: -10 }}
                        >
                            <button
                                onClick={marquerPret}
                                disabled={loading || (role === 'ATTAQUANT' && coutTotal === 0)}
                                className="btn-blood w-full"
                                style={{ padding: '16px 32px' }}
                            >
                                {loading ? 'Envoi...' : 'Pret au combat !'}
                            </button>
                        </motion.div>
                    )}

                    {estPret && lobby?.etat !== 'EN_COURS' && (
                        <motion.div
                            key="attente"
                            initial={{ opacity: 0, scale: 0.95 }}
                            animate={{ opacity: 1, scale: 1 }}
                            exit={{ opacity: 0 }}
                            className="card-grass p-5 text-center"
                            style={{ border: '2px solid var(--grass)' }}
                        >
                            <p className="label-pixel-lg mb-2" style={{ color: 'var(--grass-light)' }}>
                                Pret au combat
                            </p>
                            <motion.p
                                animate={{ opacity: [0.4, 1, 0.4] }}
                                transition={{ duration: 2, repeat: Infinity }}
                                className="text-xs"
                                style={{ color: 'var(--parchment-darker)', fontFamily: 'var(--font-crimson)' }}
                            >
                                En attente de l'adversaire...
                            </motion.p>
                        </motion.div>
                    )}

                    {lobby?.etat === 'EN_COURS' && (
                        <motion.div
                            key="combat"
                            initial={{ scale: 0.8, opacity: 0 }}
                            animate={{ scale: 1, opacity: 1 }}
                            transition={{ type: 'spring', stiffness: 200, damping: 15 }}
                            className="text-center p-8 relative overflow-hidden"
                            style={{
                                background: 'linear-gradient(160deg, rgba(220,180,100,0.15) 0%, rgba(196,64,48,0.1) 100%)',
                                border: '3px solid var(--gold)',
                                imageRendering: 'pixelated',
                            }}
                        >
                            <motion.div
                                className="absolute inset-0"
                                animate={{ opacity: [0.05, 0.15, 0.05] }}
                                transition={{ duration: 1.5, repeat: Infinity }}
                                style={{ background: 'radial-gradient(circle, var(--gold) 0%, transparent 70%)' }}
                            />
                            <div className="relative z-10">
                                <motion.p
                                    className="text-4xl mb-3"
                                    animate={{ rotate: [0, -5, 5, 0] }}
                                    transition={{ duration: 0.6, repeat: Infinity, repeatDelay: 1 }}
                                >
                                    ⚔️
                                </motion.p>
                                <p className="label-pixel-xl text-gold-glow mb-2">
                                    Aux armes !
                                </p>
                                <p className="label-pixel" style={{ color: 'var(--parchment-darker)', fontSize: '0.45rem' }}>
                                    Redirection...
                                </p>
                            </div>
                        </motion.div>
                    )}
                </AnimatePresence>

                {/* ═══ Erreur ═══ */}
                <AnimatePresence>
                    {erreur && (
                        <motion.p
                            initial={{ opacity: 0, x: -10 }}
                            animate={{ opacity: 1, x: 0 }}
                            exit={{ opacity: 0 }}
                            className="label-pixel text-center py-3 px-4"
                            style={{
                                color: 'var(--flag-red)',
                                background: 'rgba(196,64,48,0.1)',
                                border: '1px solid rgba(196,64,48,0.3)',
                                fontSize: '0.5rem',
                            }}
                        >
                            {erreur}
                        </motion.p>
                    )}
                </AnimatePresence>
            </div>
        </main>
    );
}