'use client';

import { Trash2 } from 'lucide-react';
import { Tourelle, Muraille } from '@/lib/api';


interface ListeDefensesProps {
    tourelles: Tourelle[];
    murailles: Muraille[];
    budget: number;
    onSupprimerTourelle: (id: number) => void;
    onSupprimerMuraille: (id: number) => void;
}

export default function ListeDefenses({ tourelles, murailles, budget, onSupprimerTourelle, onSupprimerMuraille }: ListeDefensesProps) {
    return (
        <div className="bg-[#1a1a22] border border-[#3a3a48] rounded-lg p-4 flex flex-col gap-2 flex-1">
            <h2 className="text-[#c9a84c] text-xs uppercase tracking-widest mb-1" style={{ fontFamily: 'var(--font-cinzel)' }}>
                Défenses ({tourelles.length} tourelles, {murailles.length} murailles)
            </h2>
            {tourelles.length === 0 && murailles.length === 0 ? (
                <p className="text-gray-600 text-xs text-center py-4">Aucune défense placée</p>
            ) : (
                <>
                    {tourelles.map(t => (
                        <div key={`t-${t.id}`} className="flex justify-between items-center bg-[#2a2a35] rounded p-2 border border-[#3a3a48]">
                            <div>
                                <p className="text-base font-bold">{t.nom}</p>
                                <p className="text-sm text-gray-400">
                                    pos.{t.position}
                                    {t.aoe ? ' 🪨' : ''}
                                    {t.nombreTirs > 0 ? ' 🏹' : ''}
                                    {' '}— {t.cout} or
                                </p>
                            </div>
                            <button onClick={() => onSupprimerTourelle(t.id)} className="text-red-500 hover:text-red-400 p-1 flex-shrink-0">
                                <Trash2 className="w-4 h-4" />
                            </button>
                        </div>
                    ))}
                    {murailles.map(m => (
                        <div key={`m-${m.id}`} className="flex justify-between items-center bg-[#3a2a10] rounded p-2 border border-[#8b6914]/30">
                            <div>
                                <p className="text-base font-bold">🧱 Muraille</p>
                                <p className="text-sm text-gray-400">
                                    chemin pos.{m.position} — {m.pvMax} PV — {m.cout} or
                                </p>
                            </div>
                            <button onClick={() => onSupprimerMuraille(m.id)} className="text-red-500 hover:text-red-400 p-1 flex-shrink-0">
                                <Trash2 className="w-4 h-4" />
                            </button>
                        </div>
                    ))}
                    <div className="border-t border-[#3a3a48] pt-2 flex justify-between text-xs mt-auto">
                        <span className="text-gray-400">Budget restant</span>
                        <span className="text-[#c9a84c] font-bold">{budget} or</span>
                    </div>
                </>
            )}
        </div>
    );
}