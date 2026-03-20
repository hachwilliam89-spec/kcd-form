'use client';

import { motion, AnimatePresence } from 'framer-motion';
import { X } from 'lucide-react';

interface FormeSelectionnee {
    type: 'TRIANGLE' | 'CERCLE';
    valeur1: number;
    valeur2: number;
    couleur: string;
}

interface FormeDisponible {
    type: 'TRIANGLE' | 'CERCLE';
    label: string;
    emoji: string;
    desc: string;
    cout: number;
    couleur: string;
    valeur1: number;
    valeur2: number;
}

interface PanneauFormesProps {
    caseSelectionnee: number;
    formesSelectionnees: FormeSelectionnee[];
    formesDisponibles: FormeDisponible[];
    budget: number;
    coutActuel: number;
    onAjouterForme: (forme: FormeDisponible) => void;
    onRetirerForme: (index: number) => void;
    onContinuer: () => void;
    onAnnuler: () => void;
}

export default function PanneauFormes({
                                          caseSelectionnee, formesSelectionnees, formesDisponibles,
                                          budget, coutActuel, onAjouterForme, onRetirerForme, onContinuer, onAnnuler
                                      }: PanneauFormesProps) {
    return (
        <AnimatePresence>
            <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}
                        className="bg-[#2a2a35] border border-[#c9a84c]/30 rounded-lg p-4 flex flex-col gap-3">
                <div className="flex justify-between items-center">
                    <h2 className="text-[#c9a84c] text-sm uppercase tracking-widest" style={{ fontFamily: 'var(--font-cinzel)' }}>
                        Case {caseSelectionnee} — Formes
                    </h2>
                    <button onClick={onAnnuler} className="text-gray-500 hover:text-white"><X className="w-4 h-4" /></button>
                </div>

                {formesSelectionnees.length > 0 && (
                    <div className="flex flex-col gap-2">
                        <p className="text-xs text-gray-400 uppercase tracking-widest">Composition</p>
                        {formesSelectionnees.map((f, i) => {
                            const fd = formesDisponibles.find(fd => fd.type === f.type)!;
                            return (
                                <div key={i} className="flex items-center justify-between bg-[#1a1a22] rounded p-2">
                                    <span className="text-sm">{fd.emoji} {fd.label}</span>
                                    <div className="flex items-center gap-2">
                                        <span className="text-[#c9a84c] text-xs">{fd.cout} or</span>
                                        <button onClick={() => onRetirerForme(i)} className="text-red-500 hover:text-red-400">
                                            <X className="w-3 h-3" />
                                        </button>
                                    </div>
                                </div>
                            );
                        })}
                        <div className="flex justify-between text-sm border-t border-[#3a3a48] pt-2">
                            <span className="text-gray-400">Coût total</span>
                            <span className="text-[#c9a84c] font-bold">{coutActuel} or</span>
                        </div>
                        <div className="flex flex-col gap-1 text-xs text-gray-400">
                            {formesSelectionnees.some(f => f.type === 'TRIANGLE') && <p>🏹 Tirs directs activés</p>}
                            {formesSelectionnees.some(f => f.type === 'CERCLE') && <p>🪨 Zone d'effet activée</p>}
                        </div>
                    </div>
                )}

                {formesSelectionnees.length < 3 && (
                    <div className="flex flex-col gap-2">
                        <p className="text-xs text-gray-400 uppercase tracking-widest">Ajouter ({formesSelectionnees.length}/3)</p>
                        {formesDisponibles.map(f => {
                            const peutAjouter = budget >= coutActuel + f.cout;
                            return (
                                <button key={f.type} onClick={() => onAjouterForme(f)} disabled={!peutAjouter}
                                        className={`border rounded-lg p-3 text-left transition-all ${peutAjouter ? 'border-[#3a3a48] hover:border-[#c9a84c]/60 cursor-pointer' : 'border-[#2a2a2a] opacity-30 cursor-not-allowed'}`}>
                                    <div className="flex items-center gap-3">
                                        <span className="text-2xl">{f.emoji}</span>
                                        <div className="flex-1">
                                            <p className="font-bold text-sm">{f.label}</p>
                                            <p className="text-gray-400 text-xs">{f.desc}</p>
                                        </div>
                                        <span className="text-[#c9a84c] text-sm font-bold">{f.cout} or</span>
                                    </div>
                                </button>
                            );
                        })}
                    </div>
                )}

                {formesSelectionnees.length > 0 && (
                    <button onClick={onContinuer}
                            className="bg-[#c9a84c] hover:bg-[#e8c96d] text-black font-bold py-2 rounded uppercase tracking-widest transition-all text-sm">
                        Continuer →
                    </button>
                )}
            </motion.div>
        </AnimatePresence>
    );
}