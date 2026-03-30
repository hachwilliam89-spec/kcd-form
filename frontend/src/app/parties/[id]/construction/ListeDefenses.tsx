'use client';

import { Trash2 } from 'lucide-react';
import { Tourelle, Muraille } from '@/lib/api';
import { PixelCoin } from '@/components/PixelSprites';

const px = { fontFamily: 'var(--font-pixel)' };

interface ListeDefensesProps {
    tourelles: Tourelle[];
    murailles: Muraille[];
    budget: number;
    onSupprimerTourelle: (id: number) => void;
    onSupprimerMuraille: (id: number) => void;
}

export default function ListeDefenses({ tourelles, murailles, budget, onSupprimerTourelle, onSupprimerMuraille }: ListeDefensesProps) {
    return (
        <div className="flex flex-col gap-2 flex-1 p-4"
             style={{
                 background: 'rgba(26,20,32,0.85)',
                 outline: '3px solid #1a0a00',
                 boxShadow: 'inset 0 3px 0 rgba(220,180,100,0.08), inset 0 -3px 0 rgba(0,0,0,0.3), 0 3px 0 #1a0a00',
             }}>
            <h2 style={{ ...px, fontSize: '0.4rem', color: '#dcb464', letterSpacing: '0.1em', textTransform: 'uppercase' }}>
                Défenses ({tourelles.length} tourelles, {murailles.length} murailles)
            </h2>
            {tourelles.length === 0 && murailles.length === 0 ? (
                <p className="text-center py-4" style={{ ...px, fontSize: '0.32rem', color: 'rgba(180,170,150,0.4)' }}>
                    Aucune défense placée
                </p>
            ) : (
                <>
                    {tourelles.map(t => (
                        <div key={`t-${t.id}`} className="flex justify-between items-center p-2"
                             style={{ background: 'rgba(10,10,15,0.4)', outline: '2px solid #1a0a00', boxShadow: '0 2px 0 #0a0508' }}>
                            <div>
                                <p style={{ ...px, fontSize: '0.4rem', color: '#d4c8a0', lineHeight: '1.8' }}>{t.nom}</p>
                                <p style={{ ...px, fontSize: '0.3rem', color: 'rgba(180,170,150,0.4)', lineHeight: '1.8' }}>
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
                        <div key={`m-${m.id}`} className="flex justify-between items-center p-2"
                             style={{ background: 'rgba(42,31,15,0.4)', outline: '2px solid rgba(139,105,20,0.3)', boxShadow: '0 2px 0 #0a0508' }}>
                            <div>
                                <p style={{ ...px, fontSize: '0.4rem', color: '#8b6914', lineHeight: '1.8' }}>🧱 Muraille</p>
                                <p style={{ ...px, fontSize: '0.3rem', color: 'rgba(180,170,150,0.4)', lineHeight: '1.8' }}>
                                    chemin pos.{m.position} — {m.pvMax} PV — {m.cout} or
                                </p>
                            </div>
                            <button onClick={() => onSupprimerMuraille(m.id)} className="text-red-500 hover:text-red-400 p-1 flex-shrink-0">
                                <Trash2 className="w-4 h-4" />
                            </button>
                        </div>
                    ))}
                    <div className="flex justify-between pt-2 mt-auto" style={{ borderTop: '2px solid rgba(26,10,0,0.5)' }}>
                        <span style={{ ...px, fontSize: '0.35rem', color: 'rgba(180,170,150,0.5)' }}>Budget restant</span>
                        <span className="flex items-center gap-1" style={{ ...px, fontSize: '0.4rem', color: '#dcb464' }}>
                            <PixelCoin size={12} />{budget}
                        </span>
                    </div>
                </>
            )}
        </div>
    );
}