'use client';

import CaseButton from './CaseButton';
import MurailleButton from './MurailleButton';
import { Tourelle, Muraille } from '@/lib/api';



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
        <div className="bg-[#111820] border border-[#2a3a2a] rounded-xl p-3 relative flex-1 min-h-0 overflow-hidden">
            <div className="absolute inset-0 bg-gradient-to-br from-[#0a150a] via-[#111820] to-[#0a150a] opacity-80" />
            <div className="relative z-10 h-full">
                <div className="grid gap-1.5 h-full" style={{ gridTemplateColumns: 'repeat(10, 1fr)', gridTemplateRows: 'repeat(7, 1fr)' }}>
                    {Array.from({ length: 7 }, (_, rowIdx) =>
                        Array.from({ length: 10 }, (_, colIdx) => {
                            const col = colIdx + 1;
                            const row = rowIdx + 1;
                            const key = `${col}-${row}`;

                            // Citadelle
                            if (col === 10 && row === 6) {
                                return (
                                    <div key={key} style={{ gridColumn: col, gridRow: row }}
                                         className="bg-[#8b1a1a]/50 border-2 border-[#8b1a1a] rounded-lg flex flex-col items-center justify-center">
                                        <span className="text-xl">🏰</span>
                                        <span className="text-red-400 text-[7px] font-bold uppercase tracking-wider">Citadelle</span>
                                    </div>
                                );
                            }

                            // Emplacement tourelle
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

                            // Emplacement muraille
                            const murEntry = Object.entries(emplacementsMuraille).find(([, v]) => v.col === col && v.row === row);
                            if (murEntry) {
                                const pos = Number(murEntry[0]);
                                return (
                                    <MurailleButton
                                        key={key}
                                        pos={pos}
                                        muraille={murailles.find(m => m.position === pos)}
                                        selectionnee={murailleSelectionnee === pos}
                                        peutPayer={budget >= murailleCout}
                                        onSelect={onSelectMuraille}
                                        style={{ gridColumn: col, gridRow: row }}
                                    />
                                );
                            }

                            // Chemin
                            if (estChemin(col, row)) {
                                const f = getFleche(col, row);
                                return (
                                    <div key={key} style={{ gridColumn: col, gridRow: row }}
                                         className="bg-[#2a1f0f]/90 border border-[#4a3a1a]/50 rounded flex items-center justify-center">
                                        {f && <span className="text-sm">{f}</span>}
                                    </div>
                                );
                            }

                            // Case vide
                            return <div key={key} style={{ gridColumn: col, gridRow: row }} className="rounded bg-[#0d1a0d]/30 border border-[#1a2a1a]/20" />;
                        })
                    )}
                </div>
            </div>
        </div>
    );
}