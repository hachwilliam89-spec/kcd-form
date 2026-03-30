'use client';

import CaseButton from './CaseButton';
import MurailleButton from './MurailleButton';
import { Tourelle, Muraille } from '@/lib/api';
import { PixelCitadel } from '@/components/PixelMapSprites';

const px = { fontFamily: 'var(--font-pixel)' };

const getTileIndex = (col: number, row: number) => ((col * 7 + row * 13) % 12) + 53;

interface CarteConstructionProps {
    tourelles: Tourelle[];
    murailles: Muraille[];
    caseSelectionnee: number | null;
    murailleSelectionnee: number | null;
    budget: number;
    murailleCout: number;
    onSelectCase: (pos: number) => void;
    onSelectMuraille: (pos: number) => void;
}

const casesPlacement: Record<number, { col: number; row: number }> = {
    1: { col: 1, row: 1 }, 2: { col: 2, row: 1 }, 3: { col: 3, row: 1 }, 4: { col: 4, row: 1 },
    5: { col: 6, row: 2 }, 6: { col: 6, row: 3 }, 7: { col: 6, row: 4 },
    8: { col: 2, row: 5 }, 9: { col: 4, row: 5 }, 10: { col: 8, row: 5 },
};

const emplacementsMuraille: Record<number, { col: number; row: number }> = {
    3: { col: 3, row: 2 },
    6: { col: 5, row: 3 },
    9: { col: 2, row: 4 },
    14: { col: 3, row: 6 },
    18: { col: 7, row: 6 },
};

const chemin = [
    { col: 1, row: 2, fleche: '👾' }, { col: 2, row: 2 }, { col: 3, row: 2 }, { col: 4, row: 2 }, { col: 5, row: 2 },
    { col: 5, row: 3 }, { col: 5, row: 4 },
    { col: 4, row: 4 }, { col: 3, row: 4 }, { col: 2, row: 4 }, { col: 1, row: 4 },
    { col: 1, row: 5 }, { col: 1, row: 6 },
    { col: 2, row: 6 }, { col: 3, row: 6 }, { col: 4, row: 6 }, { col: 5, row: 6 },
    { col: 6, row: 6 }, { col: 7, row: 6 }, { col: 8, row: 6 }, { col: 9, row: 6 },
];

const estChemin = (col: number, row: number) => chemin.some(c => c.col === col && c.row === row);
const getFleche = (col: number, row: number) => chemin.find(c => c.col === col && c.row === row)?.fleche;

export default function CarteConstruction({
                                              tourelles, murailles, caseSelectionnee, murailleSelectionnee,
                                              budget, murailleCout, onSelectCase, onSelectMuraille
                                          }: CarteConstructionProps) {
    return (
        <div className="relative flex-1 min-h-0 overflow-hidden"
             style={{
                 background: 'rgba(10,10,15,0.25)',
                 outline: '3px solid #1a0a00',
                 boxShadow: 'inset 0 0 30px rgba(0,0,0,0.2), 0 4px 0 #0a0508',
             }}>
            <div className="relative z-10 h-full p-3">
                <div className="grid gap-1.5 h-full" style={{ gridTemplateColumns: 'repeat(10, 1fr)', gridTemplateRows: 'repeat(7, 1fr)' }}>
                    {Array.from({ length: 7 }, (_, rowIdx) =>
                        Array.from({ length: 10 }, (_, colIdx) => {
                            const col = colIdx + 1;
                            const row = rowIdx + 1;
                            const key = `${col}-${row}`;

                            // ═══ CITADELLE ═══
                            if (col === 10 && row === 6) {
                                return (
                                    <div key={key} style={{
                                        gridColumn: col,
                                        gridRow: row,
                                        background: 'rgba(139,26,26,0.4)',
                                        outline: '3px solid #1a0a00',
                                        boxShadow: 'inset 0 3px 0 rgba(196,64,48,0.2), inset 0 -3px 0 rgba(0,0,0,0.4), 0 3px 0 #0a0508, 0 0 12px rgba(196,64,48,0.15)',
                                    }}
                                         className="flex flex-col items-center justify-center">
                                        <PixelCitadel size={40} />
                                        <span style={{ ...px, fontSize: '0.25rem', color: '#c44030', letterSpacing: '0.1em', textTransform: 'uppercase' }}>
                                            Citadelle
                                        </span>
                                    </div>
                                );
                            }

                            // ═══ TOURELLE ═══
                            const posEntry = Object.entries(casesPlacement).find(([, v]) => v.col === col && v.row === row);
                            if (posEntry) {
                                const pos = Number(posEntry[0]);
                                return (
                                    <CaseButton
                                        key={key}
                                        pos={pos}
                                        tourelle={tourelles.find(t => t.position === pos)}
                                        selectionnee={caseSelectionnee === pos}
                                        onSelect={onSelectCase}
                                        style={{ gridColumn: col, gridRow: row }}
                                    />
                                );
                            }

                     // ═══ MURAILLE ═══
                     const murEntry = Object.entries(emplacementsMuraille).find(([, v]) => v.col === col && v.row === row);
                     if (murEntry) {
                         const pos = Number(murEntry[0]);
                         const tileIdx = getTileIndex(col, row);
                         return (
                             <MurailleButton
                                 key={key}
                                 pos={pos}
                                 muraille={murailles.find(m => m.position === pos)}
                                 selectionnee={murailleSelectionnee === pos}
                                 peutPayer={budget >= murailleCout}
                                 onSelect={onSelectMuraille}
                                 style={{
                                     gridColumn: col,
                                     gridRow: row,
                                     backgroundImage: `url(/sprites/tiles/FieldsTile_${tileIdx}.png)`,
                                     backgroundSize: '100% 100%',
                                     backgroundRepeat: 'no-repeat',
                                 }}
                             />
                         );
                     }
                            // ═══ CHEMIN ═══
                            if (estChemin(col, row)) {
                                const tileIdx = getTileIndex(col, row);
                                return (
                                    <div key={key} data-col={col} data-row={row}
                                         className="pixel-render flex items-center justify-center"
                                         style={{
                                             gridColumn: col,
                                             gridRow: row,
                                             backgroundImage: `url(/sprites/tiles/FieldsTile_${tileIdx}.png)`,
                                             backgroundSize: '100% 100%',
                                             backgroundRepeat: 'no-repeat',
                                             outline: '2px solid #1a0a00',
                                             boxShadow: 'inset 0 2px 0 rgba(180,140,80,0.08), inset 0 -2px 0 rgba(0,0,0,0.3), 0 2px 0 #0a0508',
                                         }}>
                                        {getFleche(col, row) && <span className="text-sm">{getFleche(col, row)}</span>}
                                    </div>
                                );
                            }

                            // ═══ CASE VIDE ═══
                            return (
                                <div key={key}
                                     style={{
                                         gridColumn: col,
                                         gridRow: row,
                                         background: 'rgba(10,10,15,0.2)',
                                         border: '1px solid rgba(255,255,255,0.03)',
                                     }}
                                />
                            );
                        })
                    )}
                </div>
            </div>
        </div>
    );
}