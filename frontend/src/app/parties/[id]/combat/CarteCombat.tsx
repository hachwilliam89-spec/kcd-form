'use client';

import { forwardRef, useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import EnnemiSprite from './EnnemiSprite';
import { Tourelle } from '@/lib/api';
import { EnnemiEtat, MurailleEtat } from '@/lib/types';



interface Explosion {
    id: number;
    x: number;
    y: number;
}

interface CarteCombatProps {
    tourelles: Tourelle[];
    ennemis: EnnemiEtat[];
    murailles: MurailleEtat[];
    forteressePvActuels: number;
    forteressePvMax: number;
    getPixelForPos: (pos: number) => { x: number; y: number };
}

const TAILLE_CHEMIN = 21;

const casesPlacement: Record<number, { col: number; row: number }> = {
    1: { col: 1, row: 1 }, 2: { col: 2, row: 1 },
    3: { col: 3, row: 1 }, 4: { col: 4, row: 1 },
    5: { col: 6, row: 2 }, 6: { col: 6, row: 3 },
    7: { col: 6, row: 4 }, 8: { col: 2, row: 5 },
    9: { col: 4, row: 5 }, 10: { col: 8, row: 5 },
};

const emplacementsMuraille: Record<number, { col: number; row: number }> = {
    3: { col: 3, row: 2 },
    6: { col: 5, row: 3 },
    9: { col: 2, row: 4 },
    14: { col: 3, row: 6 },
    18: { col: 7, row: 6 },
};

const cheminCases = [
    { col: 1, row: 2 }, { col: 2, row: 2 }, { col: 3, row: 2 },
    { col: 4, row: 2 }, { col: 5, row: 2 },
    { col: 5, row: 3 }, { col: 5, row: 4 },
    { col: 4, row: 4 }, { col: 3, row: 4 }, { col: 2, row: 4 }, { col: 1, row: 4 },
    { col: 1, row: 5 }, { col: 1, row: 6 },
    { col: 2, row: 6 }, { col: 3, row: 6 }, { col: 4, row: 6 },
    { col: 5, row: 6 }, { col: 6, row: 6 }, { col: 7, row: 6 },
    { col: 8, row: 6 }, { col: 9, row: 6 },
];

const estChemin = (col: number, row: number) => cheminCases.some(c => c.col === col && c.row === row);

const CarteCombat = forwardRef<HTMLDivElement, CarteCombatProps>(
    ({ tourelles, ennemis, murailles, forteressePvActuels, forteressePvMax, getPixelForPos }, ref) => {

        const [forteresseFlash, setForteresseFlash] = useState(false);
        const [explosions, setExplosions] = useState<Explosion[]>([]);
        const prevForteressePv = useRef(forteressePvActuels);
        const prevEnnemisRef = useRef<Map<number, boolean>>(new Map());
        const explosionIdRef = useRef(0);

        // Flash citadelle quand elle perd des PV
        useEffect(() => {
            if (forteressePvActuels < prevForteressePv.current) {
                setForteresseFlash(true);
                setTimeout(() => setForteresseFlash(false), 400);
            }
            prevForteressePv.current = forteressePvActuels;
        }, [forteressePvActuels]);

        // Explosion quand un ennemi meurt
        useEffect(() => {
            const prevMap = prevEnnemisRef.current;
            const newExplosions: Explosion[] = [];

            for (const e of ennemis) {
                const wasAlive = prevMap.get(e.id);
                if (wasAlive === true && !e.vivant) {
                    const px = getPixelForPos(e.position);
                    newExplosions.push({
                        id: explosionIdRef.current++,
                        x: px.x + 16,
                        y: px.y + 10,
                    });
                }
            }

            if (newExplosions.length > 0) {
                setExplosions(prev => [...prev, ...newExplosions]);
                setTimeout(() => {
                    setExplosions(prev => prev.filter(
                        exp => !newExplosions.some(ne => ne.id === exp.id)
                    ));
                }, 800);
            }

            const newMap = new Map<number, boolean>();
            for (const e of ennemis) {
                newMap.set(e.id, e.vivant);
            }
            prevEnnemisRef.current = newMap;
        }, [ennemis]);

        const getMurailleAt = (col: number, row: number): MurailleEtat | undefined => {
            const entry = Object.entries(emplacementsMuraille).find(([, v]) => v.col === col && v.row === row);
            if (!entry) return undefined;
            const pos = Number(entry[0]);
            return murailles.find(m => m.position === pos);
        };

        const getMurailleEmoji = (pvPct: number, detruite: boolean) => {
            if (detruite) return '💥';
            if (pvPct <= 25) return '🧱';
            if (pvPct <= 50) return '🧱';
            return '🧱';
        };

        const getMurailleBorderClass = (pvPct: number) => {
            if (pvPct <= 25) return 'border-red-700 bg-[#3a1a10]';
            if (pvPct <= 50) return 'border-yellow-700 bg-[#3a2a10]';
            return 'border-[#8b6914] bg-[#3a2a10]';
        };

        return (
            <div className="bg-[#111820] border border-[#2a3a2a] rounded-xl p-3 relative flex-1 overflow-hidden">
                <div className="absolute inset-0 bg-gradient-to-br from-[#0a150a] via-[#111820] to-[#0a150a] opacity-80" />

                <div className="relative z-10 h-full">
                    <div
                        ref={ref}
                        className="grid gap-1.5 h-full"
                        style={{
                            gridTemplateColumns: 'repeat(10, 1fr)',
                            gridTemplateRows: 'repeat(7, 1fr)',
                        }}>
                        {Array.from({ length: 7 }, (_, rowIdx) =>
                            Array.from({ length: 10 }, (_, colIdx) => {
                                const col = colIdx + 1;
                                const row = rowIdx + 1;
                                const key = `${col}-${row}`;

                                // Citadelle avec flash
                                if (col === 10 && row === 6) {
                                    return (
                                        <motion.div
                                            key={key}
                                            data-col={col} data-row={row}
                                            style={{ gridColumn: col, gridRow: row }}
                                            animate={forteresseFlash
                                                ? { borderColor: ['#8b1a1a', '#ff0000', '#8b1a1a'], scale: [1, 1.05, 1] }
                                                : {}}
                                            transition={{ duration: 0.4 }}
                                            className={`border-2 rounded-lg flex flex-col items-center justify-center transition-colors ${
                                                forteresseFlash
                                                    ? 'bg-red-900/80 border-red-500'
                                                    : 'bg-[#8b1a1a]/50 border-[#8b1a1a]'
                                            }`}>
                                            <motion.span
                                                animate={forteresseFlash ? { scale: [1, 1.2, 1] } : {}}
                                                transition={{ duration: 0.3 }}
                                                className="text-xl">
                                                🏰
                                            </motion.span>
                                            <span className="text-red-400 text-[7px] font-bold uppercase">Citadelle</span>
                                            {/* Mini barre PV */}
                                            <div className="w-4/5 h-0.5 bg-gray-700 rounded mt-0.5">
                                                <motion.div
                                                    animate={{ width: `${forteressePvMax > 0 ? (forteressePvActuels / forteressePvMax) * 100 : 0}%` }}
                                                    className="h-0.5 rounded bg-red-500"
                                                />
                                            </div>
                                        </motion.div>
                                    );
                                }

                                // Emplacement tourelle
                                const posEntry = Object.entries(casesPlacement).find(
                                    ([, v]) => v.col === col && v.row === row
                                );
                                if (posEntry) {
                                    const tourelle = tourelles.find(t => t.position === Number(posEntry[0]));
                                    return (
                                        <div key={key}
                                             data-col={col} data-row={row}
                                             style={{ gridColumn: col, gridRow: row }}
                                             className="rounded-lg border-2 border-[#c9a84c] bg-[#2a2a35] flex flex-col items-center justify-center gap-0.5">
                                            {tourelle ? (
                                                <>
                                                    <motion.span
                                                        animate={{ rotate: [0, -5, 5, -3, 3, 0] }}
                                                        transition={{ duration: 2, repeat: Infinity, repeatDelay: 1 }}
                                                        className="text-base">
                                                        {tourelle.aoe ? '🪨' : '🏹'}
                                                    </motion.span>
                                                    <p className="text-[7px] text-[#c9a84c] truncate w-full text-center px-0.5">{tourelle.nom}</p>
                                                </>
                                            ) : (
                                                <span className="text-gray-600 text-xs">{posEntry[0]}</span>
                                            )}
                                        </div>
                                    );
                                }

                                // Emplacement muraille
                                const murEntry = Object.entries(emplacementsMuraille).find(
                                    ([, v]) => v.col === col && v.row === row
                                );
                                if (murEntry) {
                                    const muraille = getMurailleAt(col, row);
                                    if (muraille && !muraille.detruite) {
                                        const pvPct = (muraille.pvActuels / muraille.pvMax) * 100;
                                        return (
                                            <motion.div
                                                key={key}
                                                data-col={col} data-row={row}
                                                style={{ gridColumn: col, gridRow: row }}
                                                animate={pvPct <= 25 ? { x: [-1, 1, -1, 0] } : {}}
                                                transition={pvPct <= 25 ? { duration: 0.3, repeat: Infinity, repeatDelay: 0.5 } : {}}
                                                className={`rounded-lg border-2 flex flex-col items-center justify-center gap-0.5 ${getMurailleBorderClass(pvPct)}`}>
                                                <span className="text-base">{getMurailleEmoji(pvPct, false)}</span>
                                                <div className="w-4/5 h-1 bg-gray-700 rounded">
                                                    <motion.div
                                                        animate={{ width: `${pvPct}%` }}
                                                        transition={{ duration: 0.3 }}
                                                        className={`h-1 rounded ${
                                                            pvPct > 50 ? 'bg-blue-500'
                                                                : pvPct > 25 ? 'bg-yellow-500'
                                                                    : 'bg-red-500'
                                                        }`}
                                                    />
                                                </div>
                                            </motion.div>
                                        );
                                    }
                                    // Muraille détruite ou absente
                                    return (
                                        <div key={key}
                                             data-col={col} data-row={row}
                                             style={{ gridColumn: col, gridRow: row }}
                                             className={`rounded flex items-center justify-center ${
                                                 muraille?.detruite
                                                     ? 'bg-[#2a1f0f]/40 border border-[#4a3a1a]/20'
                                                     : 'bg-[#2a1f0f]/90 border border-[#4a3a1a]/50'
                                             }`}>
                                            {muraille?.detruite && (
                                                <motion.span
                                                    initial={{ scale: 1.5, opacity: 1 }}
                                                    animate={{ scale: 1, opacity: 0.5 }}
                                                    transition={{ duration: 1 }}
                                                    className="text-[10px]">
                                                    💥
                                                </motion.span>
                                            )}
                                        </div>
                                    );
                                }

                                // Chemin
                                if (estChemin(col, row)) {
                                    return (
                                        <div key={key}
                                             data-col={col} data-row={row}
                                             style={{ gridColumn: col, gridRow: row }}
                                             className="bg-[#2a1f0f]/90 border border-[#4a3a1a]/50 rounded" />
                                    );
                                }

                                // Case vide
                                return (
                                    <div key={key}
                                         data-col={col} data-row={row}
                                         style={{ gridColumn: col, gridRow: row }}
                                         className="rounded bg-[#0d1a0d]/30 border border-[#1a2a1a]/20" />
                                );
                            })
                        )}
                    </div>

                    {/* Ennemis animés */}
                    <div className="absolute inset-3 pointer-events-none">
                        <AnimatePresence>
                            {ennemis.filter(e => e.vivant).map((ennemi) => (
                                <EnnemiSprite
                                    key={`ennemi-${ennemi.id}`}
                                    ennemi={ennemi}
                                    getPixelForPos={getPixelForPos}
                                    tailleChemin={TAILLE_CHEMIN}
                                />
                            ))}
                        </AnimatePresence>

                        {/* Explosions de mort */}
                        <AnimatePresence>
                            {explosions.map(exp => (
                                <motion.div
                                    key={`exp-${exp.id}`}
                                    initial={{ scale: 0, opacity: 1 }}
                                    animate={{ scale: [0, 1.5, 2], opacity: [1, 0.8, 0] }}
                                    exit={{ opacity: 0 }}
                                    transition={{ duration: 0.6 }}
                                    className="absolute pointer-events-none"
                                    style={{ left: exp.x - 16, top: exp.y - 16 }}>
                                    <span className="text-2xl">💀</span>
                                </motion.div>
                            ))}
                        </AnimatePresence>
                    </div>
                </div>
            </div>
        );
    }
);

CarteCombat.displayName = 'CarteCombat';

export default CarteCombat;