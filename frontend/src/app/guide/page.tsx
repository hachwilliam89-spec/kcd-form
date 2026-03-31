'use client';

import { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import Link from 'next/link';
import { PixelShield, PixelBorder, PixelTourelle, PixelBanner, PixelCoin, PixelTriangle, PixelCercle, PixelRectangle } from '@/components/PixelSprites';

/* ── Style helpers ── */
const px = { fontFamily: 'var(--font-pixel)' };

/* ── Data ── */
const ETAPES = [
    {
        id: 'construire',
        titre: 'Construire',
        icon: '🔨',
        couleur: '#dcb464',
        contenu: (
            <div className="flex flex-col gap-5">
                <p style={{ ...px, fontSize: '0.55rem', color: '#d4c8a0', lineHeight: '2.2' }}>
                    Utilisez votre or pour bâtir des tourelles et des murailles.
                    Chaque forme géométrique donne un pouvoir différent.
                </p>
                <div className="flex flex-col gap-3">
                    {[
                        { icon: <PixelTriangle size={24} />, nom: '🏹 Archer', forme: 'Triangle', desc: 'Tir mono-cible, cadence rapide', couleur: '#e74c3c' },
                        { icon: <PixelCercle size={24} />, nom: '🪨 Catapulte', forme: 'Cercle', desc: 'Dégâts de zone (AoE)', couleur: '#3498db' },
                        { icon: <PixelRectangle size={24} />, nom: '🧱 Muraille', forme: 'Rectangle', desc: 'Bloque les ennemis sur le chemin', couleur: '#8b6914' },
                    ].map((t, i) => (
                        <div key={i} className="flex items-center gap-3 p-3"
                             style={{
                                 background: 'rgba(26,20,32,0.8)',
                                 outline: '2px solid #1a0a00',
                                 boxShadow: 'inset 0 2px 0 rgba(255,255,255,0.03), 0 2px 0 #1a0a00',
                             }}>
                            <div className="flex-shrink-0">{t.icon}</div>
                            <div className="flex-1">
                                <p style={{ ...px, fontSize: '0.6rem', color: t.couleur, lineHeight: '1.8' }}>{t.nom}</p>
                                <p style={{ ...px, fontSize: '0.45rem', color: 'rgba(180,170,150,0.6)', lineHeight: '1.8' }}>{t.desc}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        ),
    },
    {
        id: 'combattre',
        titre: 'Combattre',
        icon: '⚔️',
        couleur: '#c44030',
        contenu: (
            <div className="flex flex-col gap-5">
                <p style={{ ...px, fontSize: '0.55rem', color: '#d4c8a0', lineHeight: '2.2' }}>
                    Les ennemis avancent sur le chemin. Vos tourelles tirent automatiquement.
                    Survivez aux 5 vagues !
                </p>
                <div className="flex flex-col gap-3">
                    {[
                        { emoji: '⚔️', nom: 'Cavalerie', desc: 'Rapide et fragile — fonce vers votre base', couleur: '#e74c3c', contre: 'Catapulte + Muraille' },
                        { emoji: '🛡️', nom: 'Infanterie', desc: 'Lent mais très résistant — un mur de boucliers', couleur: '#3498db', contre: 'Archer' },
                        { emoji: '🐏', nom: 'Bélier', desc: 'Très solide — ×2 dégâts contre les murailles', couleur: '#8b6914', contre: 'Catapulte (×1.25)' },
                    ].map((e, i) => (
                        <div key={i} className="flex items-center gap-3 p-3"
                             style={{
                                 background: 'rgba(60,20,20,0.4)',
                                 outline: '2px solid #1a0a00',
                                 boxShadow: 'inset 0 2px 0 rgba(255,255,255,0.03), 0 2px 0 #1a0a00',
                             }}>
                            <span className="text-2xl flex-shrink-0">{e.emoji}</span>
                            <div className="flex-1">
                                <p style={{ ...px, fontSize: '0.6rem', color: e.couleur, lineHeight: '1.8' }}>{e.nom}</p>
                                <p style={{ ...px, fontSize: '0.45rem', color: 'rgba(180,170,150,0.6)', lineHeight: '1.8' }}>{e.desc}</p>
                                <p style={{ ...px, fontSize: '0.4rem', color: '#5a8c28', lineHeight: '1.8', marginTop: '2px' }}>
                                    🎯 Contré par : {e.contre}
                                </p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        ),
    },
    {
        id: 'strategie',
        titre: 'Stratégie',
        icon: '🧠',
        couleur: '#3498db',
        contenu: (
            <div className="flex flex-col gap-5">
                <p style={{ ...px, fontSize: '0.55rem', color: '#d4c8a0', lineHeight: '2.2' }}>
                    Aucune tourelle ne couvre tout. Diversifiez vos défenses !
                </p>

                {/* Matrice simplifiée */}
                <div style={{
                    outline: '2px solid #1a0a00',
                    boxShadow: '0 3px 0 #1a0a00',
                    overflow: 'hidden',
                }}>
                    {/* Header */}
                    <div className="grid grid-cols-4"
                         style={{ background: 'rgba(26,20,32,0.95)' }}>
                        <div className="p-3" />
                        {['🏹', '🪨', '🧱'].map((e, i) => (
                            <div key={i} className="p-3 text-center"
                                 style={{ borderLeft: '1px solid rgba(26,10,0,0.5)' }}>
                                <span className="text-lg">{e}</span>
                            </div>
                        ))}
                    </div>
                    {/* Rows */}
                    {[
                        { ennemi: '⚔️', vals: ['❌', '✅', '✅'] },
                        { ennemi: '🛡️', vals: ['✅', '⚠️', '⚠️'] },
                        { ennemi: '🐏', vals: ['⚠️', '✅', '💀'] },
                    ].map((row, i) => (
                        <div key={i} className="grid grid-cols-4"
                             style={{
                                 background: i % 2 === 0 ? 'rgba(20,14,24,0.8)' : 'rgba(26,20,32,0.6)',
                                 borderTop: '1px solid rgba(26,10,0,0.5)',
                             }}>
                            <div className="p-3 flex items-center justify-center">
                                <span className="text-lg">{row.ennemi}</span>
                            </div>
                            {row.vals.map((v, j) => (
                                <div key={j} className="p-3 text-center flex items-center justify-center"
                                     style={{ borderLeft: '1px solid rgba(26,10,0,0.5)' }}>
                                    <span className="text-lg">{v}</span>
                                </div>
                            ))}
                        </div>
                    ))}
                </div>

                <div className="flex flex-col gap-2">
                    <p style={{ ...px, fontSize: '0.45rem', color: 'rgba(180,170,150,0.6)', lineHeight: '2' }}>
                        ✅ Efficace &nbsp; ⚠️ Moyen &nbsp; ❌ Faible &nbsp; 💀 Dangereux
                    </p>
                </div>

                {/* Tips */}
                <div className="flex flex-col gap-2 p-3"
                     style={{
                         background: 'rgba(90,140,40,0.1)',
                         outline: '2px solid rgba(90,140,40,0.3)',
                         boxShadow: '0 2px 0 #1a0a00',
                     }}>
                    <p style={{ ...px, fontSize: '0.5rem', color: '#5a8c28', lineHeight: '1.8' }}>💡 Astuces</p>
                    <p style={{ ...px, fontSize: '0.45rem', color: 'rgba(180,170,150,0.6)', lineHeight: '2.2' }}>
                        • Mélangez Archers et Catapultes pour couvrir tous les types
                    </p>
                    <p style={{ ...px, fontSize: '0.45rem', color: 'rgba(180,170,150,0.6)', lineHeight: '2.2' }}>
                        • Les Murailles ralentissent tout sauf le Bélier
                    </p>
                    <p style={{ ...px, fontSize: '0.45rem', color: 'rgba(180,170,150,0.6)', lineHeight: '2.2' }}>
                        • Gardez de l&apos;or entre les vagues pour adapter
                    </p>
                </div>
            </div>
        ),
    },
    {
        id: 'score',
        titre: 'Score',
        icon: '⭐',
        couleur: '#dcb464',
        contenu: (
            <div className="flex flex-col gap-5">
                <p style={{ ...px, fontSize: '0.55rem', color: '#d4c8a0', lineHeight: '2.2' }}>
                    Votre score est calculé sur 100 points à la fin des 5 vagues.
                </p>
                <div className="flex flex-col gap-3">
                    {[
                        { pts: '30', label: 'Citadelle restante', desc: 'Vies restantes de votre base', couleur: '#dcb464' },
                        { pts: '40', label: 'Ennemis éliminés', desc: 'Pourcentage d\'ennemis tués', couleur: '#c44030' },
                        { pts: '30', label: 'Efficacité budget', desc: 'Or bien utilisé = plus de points', couleur: '#5a8c28' },
                    ].map((s, i) => (
                        <div key={i} className="flex items-center gap-3 p-3"
                             style={{
                                 background: 'rgba(26,20,32,0.8)',
                                 outline: '2px solid #1a0a00',
                                 boxShadow: 'inset 0 2px 0 rgba(255,255,255,0.03), 0 2px 0 #1a0a00',
                             }}>
                            <div className="flex-shrink-0 w-10 h-10 flex items-center justify-center"
                                 style={{
                                     ...px,
                                     fontSize: '0.65rem',
                                     color: s.couleur,
                                     background: 'rgba(0,0,0,0.3)',
                                     outline: '2px solid #1a0a00',
                                 }}>
                                {s.pts}
                            </div>
                            <div className="flex-1">
                                <p style={{ ...px, fontSize: '0.55rem', color: s.couleur, lineHeight: '1.8' }}>{s.label}</p>
                                <p style={{ ...px, fontSize: '0.4rem', color: 'rgba(180,170,150,0.6)', lineHeight: '1.8' }}>{s.desc}</p>
                            </div>
                        </div>
                    ))}
                </div>
                <div className="text-center p-3"
                     style={{
                         background: 'rgba(220,180,100,0.1)',
                         outline: '2px solid rgba(220,180,100,0.3)',
                         boxShadow: '0 2px 0 #1a0a00',
                     }}>
                    <p style={{ ...px, fontSize: '0.5rem', color: '#dcb464', lineHeight: '2' }}>
                        ⭐⭐⭐ = Score ≥ 80
                    </p>
                </div>
            </div>
        ),
    },
];

export default function GuidePage() {
    const [etapeActive, setEtapeActive] = useState(0);

    return (
        <main className="min-h-screen bg-medieval-guide flex flex-col items-center px-6 py-8">
            <div className="w-full max-w-lg flex flex-col gap-6">

                {/* ════════ Header ════════ */}
                <div className="text-center flex flex-col items-center gap-3">
                    <div className="text-3xl">📜</div>
                    <h1 style={{
                        ...px,
                        fontSize: '1rem',
                        color: '#dcb464',
                        textShadow: '0 2px 4px rgba(0,0,0,0.5)',
                        lineHeight: '1.6',
                        letterSpacing: '0.1em',
                    }}>
                        Guide du jeu
                    </h1>
                    <PixelBorder className="w-48" />
                </div>

                {/* ════════ Onglets ════════ */}
                <div className="flex gap-2 w-full">
                    {ETAPES.map((e, i) => {
                        const active = etapeActive === i;
                        return (
                            <button
                                key={e.id}
                                onClick={() => setEtapeActive(i)}
                                className="flex-1 py-3 text-center transition-all active:translate-y-[1px]"
                                style={{
                                    ...px,
                                    fontSize: '0.45rem',
                                    background: active ? 'rgba(26,20,32,0.95)' : 'rgba(20,14,24,0.6)',
                                    outline: active ? `2px solid ${e.couleur}` : '2px solid #1a0a00',
                                    boxShadow: active
                                        ? `0 3px 0 #1a0a00, 0 0 12px ${e.couleur}20`
                                        : '0 2px 0 #0a0a10',
                                    color: active ? e.couleur : 'rgba(180,170,150,0.4)',
                                    lineHeight: '2',
                                }}
                            >
                                <span className="text-base block mb-1">{e.icon}</span>
                                {e.titre}
                            </button>
                        );
                    })}
                </div>

                {/* ════════ Contenu ════════ */}
                <AnimatePresence mode="wait">
                    <motion.div
                        key={etapeActive}
                        initial={{ opacity: 0, y: 10 }}
                        animate={{ opacity: 1, y: 0 }}
                        exit={{ opacity: 0, y: -10 }}
                        transition={{ duration: 0.25 }}
                        className="relative p-5"
                        style={{
                            background: 'rgba(26,20,32,0.9)',
                            outline: '3px solid #1a0a00',
                            boxShadow: 'inset 0 3px 0 rgba(220,180,100,0.08), inset 0 -3px 0 rgba(0,0,0,0.3), 0 4px 0 #1a0a00',
                        }}
                    >
                        <PixelBorder className="absolute top-0 left-0 right-0" />
                        <div className="pt-3">
                            <h2 className="mb-4 flex items-center gap-2"
                                style={{
                                    ...px,
                                    fontSize: '0.7rem',
                                    color: ETAPES[etapeActive].couleur,
                                    lineHeight: '1.8',
                                }}>
                                <span className="text-xl">{ETAPES[etapeActive].icon}</span>
                                {ETAPES[etapeActive].titre}
                            </h2>
                            {ETAPES[etapeActive].contenu}
                        </div>
                    </motion.div>
                </AnimatePresence>

                {/* ════════ Navigation rapide ════════ */}
                <div className="flex gap-3">
                    {etapeActive > 0 && (
                        <button
                            onClick={() => setEtapeActive(etapeActive - 1)}
                            className="btn-stone flex-1 py-3"
                            style={{ ...px, fontSize: '0.55rem' }}
                        >
                            ← {ETAPES[etapeActive - 1].titre}
                        </button>
                    )}
                    {etapeActive < ETAPES.length - 1 && (
                        <button
                            onClick={() => setEtapeActive(etapeActive + 1)}
                            className="btn-gold flex-1 py-3"
                            style={{ ...px, fontSize: '0.55rem' }}
                        >
                            {ETAPES[etapeActive + 1].titre} →
                        </button>
                    )}
                </div>

                {/* ════════ CTA ════════ */}
                <Link href="/setup" className="w-full">
                    <button className="btn-blood w-full py-4 flex items-center justify-center gap-3"
                            style={{ ...px, fontSize: '0.65rem' }}>
                        <PixelTourelle size={20} />
                        Commencer la bataille
                    </button>
                </Link>

                {/* ════════ Retour ════════ */}
                <button
                    onClick={() => window.history.back()}
                    className="btn-stone w-full py-3"
                    style={{ ...px, fontSize: '0.55rem' }}
                >
                    ← Retour à l&apos;accueil
                </button>
            </div>
        </main>
    );
}