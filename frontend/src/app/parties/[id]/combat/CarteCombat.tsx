'use client';

import { forwardRef, useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import EnnemiSprite from './EnnemiSprite';
import { Tourelle } from '@/lib/api';
import { EnnemiEtat, MurailleEtat } from '@/lib/types';
import { PixelTowerSprite, PixelStoneWall, PixelCitadel } from '@/components/PixelMapSprites';

const px = { fontFamily: 'var(--font-pixel)' };

interface Explosion { id: number; x: number; y: number; }

interface CarteCombatProps {
    tourelles: Tourelle[];
    ennemis: EnnemiEtat[];
    murailles: MurailleEtat[];
    forteressePvActuels: number;
    forteressePvMax: number;
    getPixelForPos: (pos: number) => { x: number; y: number };
}

const TAILLE_CHEMIN = 22; // +1 pour la case de spawn

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
    { col: 1, row: 1 }, // case de spawn
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

const getTileIndex = (col: number, row: number) => ((col * 7 + row * 13) % 12) + 53;

const pixelCaseStyles = {
 tourelle: {
     backgroundImage: `url("data:image/svg+xml,...")`,
     backgroundSize: '100% 100%',
     imageRendering: 'pixelated' as const,
     outline: '3px solid #1a0a00',
     boxShadow: 'inset 0 3px 0 rgba(220,180,100,0.12), inset 0 -3px 0 rgba(0,0,0,0.4), 0 3px 0 #0a0508',
 },
    citadelle: {
        background: 'rgba(139,26,26,0.4)',
        outline: '3px solid #1a0a00',
        boxShadow: 'inset 0 3px 0 rgba(196,64,48,0.2), inset 0 -3px 0 rgba(0,0,0,0.4), 0 3px 0 #0a0508, 0 0 12px rgba(196,64,48,0.15)',
    },
    citadelleFlash: {
        background: 'rgba(196,64,48,0.6)',
        outline: '3px solid #c44030',
        boxShadow: 'inset 0 3px 0 rgba(255,100,80,0.3), inset 0 -3px 0 rgba(0,0,0,0.4), 0 3px 0 #0a0508, 0 0 20px rgba(196,64,48,0.4)',
    },
    vide: {
        background: 'rgba(10,10,15,0.2)',
        border: '1px solid rgba(255,255,255,0.03)',
    },
};
const CarteCombat = forwardRef<HTMLDivElement, CarteCombatProps>(
    ({ tourelles, ennemis, murailles, forteressePvActuels, forteressePvMax, getPixelForPos }, ref) => {

        const [forteresseFlash, setForteresseFlash] = useState(false);
        const [explosions, setExplosions] = useState<Explosion[]>([]);
        const prevForteressePv = useRef(forteressePvActuels);
        const prevEnnemisRef = useRef<Map<number, boolean>>(new Map());
        const explosionIdRef = useRef(0);
        const combatActif = ennemis.length > 0;

        useEffect(() => {
            if (forteressePvActuels < prevForteressePv.current) {
                setForteresseFlash(true);
                setTimeout(() => setForteresseFlash(false), 400);
            }
            prevForteressePv.current = forteressePvActuels;
        }, [forteressePvActuels]);

        useEffect(() => {
            const prevMap = prevEnnemisRef.current;
            const newExplosions: Explosion[] = [];
            for (const e of ennemis) {
                const wasAlive = prevMap.get(e.id);
                if (wasAlive === true && !e.vivant) {
                    const p = getPixelForPos(e.position);
                    newExplosions.push({ id: explosionIdRef.current++, x: p.x + 16, y: p.y + 10 });
                }
            }
            if (newExplosions.length > 0) {
                setExplosions(prev => [...prev, ...newExplosions]);
                setTimeout(() => {
                    setExplosions(prev => prev.filter(exp => !newExplosions.some(ne => ne.id === exp.id)));
                }, 800);
            }
            const newMap = new Map<number, boolean>();
            for (const e of ennemis) newMap.set(e.id, e.vivant);
            prevEnnemisRef.current = newMap;
        }, [ennemis]);

        const getMurailleAt = (col: number, row: number): MurailleEtat | undefined => {
            const entry = Object.entries(emplacementsMuraille).find(([, v]) => v.col === col && v.row === row);
            if (!entry) return undefined;
            return murailles.find(m => m.position === Number(entry[0]));
        };

        return (
            <div className="relative flex-1 overflow-hidden"
                 style={{ background: 'rgba(10,10,15,0.25)', outline: '3px solid #1a0a00', boxShadow: 'inset 0 0 30px rgba(0,0,0,0.2), 0 4px 0 #0a0508' }}>
                <div className="relative z-10 h-full p-3">
                    <div ref={ref} className="grid gap-1.5 h-full"
                         style={{ gridTemplateColumns: 'repeat(10, 1fr)', gridTemplateRows: 'repeat(7, 1fr)' }}>
                        {Array.from({ length: 7 }, (_, rowIdx) =>
                            Array.from({ length: 10 }, (_, colIdx) => {
                                const col = colIdx + 1;
                                const row = rowIdx + 1;
                                const key = `${col}-${row}`;

                                // ═══ CITADELLE ═══
                                if (col === 10 && row === 6) {
                                    const pvPct = forteressePvMax > 0 ? (forteressePvActuels / forteressePvMax) * 100 : 0;
                                    return (
                                        <motion.div key={key} data-col={col} data-row={row}
                                            style={{ gridColumn: col, gridRow: row, ...(forteresseFlash ? pixelCaseStyles.citadelleFlash : pixelCaseStyles.citadelle) }}
                                            animate={forteresseFlash ? { scale: [1, 1.05, 1] } : {}}
                                            transition={{ duration: 0.4 }}
                                            className="flex flex-col items-center justify-center">
                                            <PixelCitadel damaged={pvPct < 50} />
                                            <div className="w-4/5 h-1 mt-0.5" style={{ background: 'rgba(0,0,0,0.5)', outline: '1px solid rgba(0,0,0,0.3)' }}>
                                                <motion.div animate={{ width: `${pvPct}%` }} className="h-1" style={{ background: '#c44030' }} />
                                            </div>
                                        </motion.div>
                                    );
                                }

                                // ═══ TOURELLE ═══
                                const posEntry = Object.entries(casesPlacement).find(([, v]) => v.col === col && v.row === row);
                                if (posEntry) {
                                    const posNum = Number(posEntry[0]);
                                    const tourelle = tourelles.find(t => t.position === posNum);
                                    const dirMap: Record<number, 'down' | 'left' | 'right' | 'up'> = {
                                        1: 'down', 2: 'down', 3: 'down', 4: 'down',
                                        5: 'left', 6: 'left', 7: 'left',
                                        8: 'down', 9: 'down', 10: 'down',
                                    };
                                    const dir = dirMap[posNum] ?? 'down';
                                    return (
                                        <div key={key} data-col={col} data-row={row}
                                             style={{ gridColumn: col, gridRow: row, ...pixelCaseStyles.tourelle }}
                                             className="flex flex-col items-center justify-center">
                                            {tourelle ? (
                                                <PixelTowerSprite firing={combatActif} direction={dir} />
                                            ) : (
                                                <span style={{ ...px, fontSize: '0.3rem', color: 'rgba(220,180,100,0.35)' }}>{posEntry[0]}</span>
                                            )}
                                        </div>
                                    );
                                }

                                // ═══ MURAILLE ═══
                                const murEntry = Object.entries(emplacementsMuraille).find(([, v]) => v.col === col && v.row === row);
                                if (murEntry) {
                                    const tileIdx = getTileIndex(col, row);
                                    const tileBg = {
                                        backgroundImage: `url(/sprites/tiles/FieldsTile_${tileIdx}.png)`,
                                        backgroundSize: '100% 100%',
                                        backgroundRepeat: 'no-repeat' as const,
                                    };
                                    const muraille = getMurailleAt(col, row);
                                    if (muraille && !muraille.detruite) {
                                        const pvPct = (muraille.pvActuels / muraille.pvMax) * 100;
                                        const pvColor = pvPct > 50 ? '#3c8cdc' : pvPct > 25 ? '#dcb464' : '#c44030';
                                        return (
                                            <motion.div key={key} data-col={col} data-row={row}
                                                style={{
                                                    gridColumn: col, gridRow: row,
                                                    ...tileBg,
                                                    outline: '3px solid #1a0a00',
                                                    boxShadow: '0 3px 0 #0a0508',
                                                }}
                                                animate={pvPct <= 25 ? { x: [-1, 1, -1, 0] } : {}}
                                                transition={pvPct <= 25 ? { duration: 0.3, repeat: Infinity, repeatDelay: 0.5 } : {}}
                                                className="pixel-render flex flex-col items-center justify-center gap-0.5">
                                                <PixelStoneWall pvPct={pvPct} />
                                                <div className="w-4/5 h-1" style={{ background: 'rgba(0,0,0,0.5)', outline: '1px solid rgba(0,0,0,0.3)' }}>
                                                    <motion.div animate={{ width: `${pvPct}%` }} transition={{ duration: 0.3 }} className="h-1" style={{ background: pvColor }} />
                                                </div>
                                            </motion.div>
                                        );
                                    }
                                    return (
                                        <div key={key} data-col={col} data-row={row}
                                             className="pixel-render flex items-center justify-center"
                                             style={{
                                                 gridColumn: col, gridRow: row,
                                                 ...tileBg,
                                                 outline: '2px solid #1a0a00',
                                                 boxShadow: 'inset 0 2px 0 rgba(180,140,80,0.05), 0 2px 0 #0a0508',
                                             }}>
                                            {muraille?.detruite && (
                                                <motion.span initial={{ scale: 1.5, opacity: 1 }} animate={{ scale: 1, opacity: 0.5 }} transition={{ duration: 1 }} className="text-[10px]">💥</motion.span>
                                            )}
                                        </div>
                                    );
                                }

                                // ═══ CHEMIN ═══
                                if (estChemin(col, row)) {
                                    const tileIdx = getTileIndex(col, row);
                                    return (
                                        <div key={key} data-col={col} data-row={row}
                                             className="pixel-render"
                                             style={{
                                                 gridColumn: col,
                                                 gridRow: row,
                                                 backgroundImage: `url(/sprites/tiles/FieldsTile_${tileIdx}.png)`,
                                                 backgroundSize: '100% 100%',
                                                 backgroundRepeat: 'no-repeat',
                                                 outline: '2px solid #1a0a00',
                                                 boxShadow: 'inset 0 2px 0 rgba(180,140,80,0.08), inset 0 -2px 0 rgba(0,0,0,0.3), 0 2px 0 #0a0508',
                                             }}
                                        />
                                    );
                                }

                                // ═══ CASE VIDE ═══
                                return <div key={key} data-col={col} data-row={row} style={{ gridColumn: col, gridRow: row, ...pixelCaseStyles.vide }} />;
                            })
                        )}
                    </div>

                    <div className="absolute inset-3 pointer-events-none">
                        <AnimatePresence>
                            {ennemis.filter(e => e.vivant).map(ennemi => (
                                <EnnemiSprite key={`ennemi-${ennemi.id}`} ennemi={ennemi} getPixelForPos={getPixelForPos} tailleChemin={TAILLE_CHEMIN} />
                            ))}
                        </AnimatePresence>
                        <AnimatePresence>
                            {explosions.map(exp => (
                                <motion.div key={`exp-${exp.id}`} initial={{ scale: 0, opacity: 1 }} animate={{ scale: [0, 1.5, 2], opacity: [1, 0.8, 0] }} exit={{ opacity: 0 }} transition={{ duration: 0.6 }}
                                    className="absolute pointer-events-none" style={{ left: exp.x - 16, top: exp.y - 16 }}>
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