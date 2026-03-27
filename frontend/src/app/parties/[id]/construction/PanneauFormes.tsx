'use client';

import { motion, AnimatePresence } from 'framer-motion';
import { X } from 'lucide-react';
import { PixelBorder, PixelCoin } from '@/components/PixelSprites';

const px = { fontFamily: 'var(--font-pixel)' };

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
className="flex flex-col gap-3 p-4 relative"
style={{
background: 'rgba(26,20,32,0.85)',
outline: '3px solid #1a0a00',
boxShadow: 'inset 0 3px 0 rgba(220,180,100,0.08), inset 0 -3px 0 rgba(0,0,0,0.3), 0 3px 0 #1a0a00',
        }}>
                <PixelBorder className="absolute top-0 left-0 right-0" />
                <div className="flex justify-between items-center pt-2">
                    <h2 style={{ ...px, fontSize: '0.45rem', color: '#dcb464', lineHeight: '1.8' }}>
Case {caseSelectionnee} — Formes
        </h2>
                    <button onClick={onAnnuler} className="text-gray-500 hover:text-white"><X className="w-4 h-4" /></button>
                </div>

        {formesSelectionnees.length > 0 && (
                    <div className="flex flex-col gap-2">
                        <p style={{ ...px, fontSize: '0.35rem', color: 'rgba(180,170,150,0.5)', letterSpacing: '0.1em', textTransform: 'uppercase' }}>
Composition
        </p>
        {formesSelectionnees.map((f, i) => {
        const fd = formesDisponibles.find(fd => fd.type === f.type)!;
        return (
                                <div key={i} className="flex items-center justify-between p-2"
style={{ background: 'rgba(10,10,15,0.4)', outline: '2px solid #1a0a00', boxShadow: '0 2px 0 #0a0508' }}>
                                    <span style={{ ...px, fontSize: '0.4rem', color: '#d4c8a0' }}>{fd.emoji} {fd.label}</span>
                                    <div className="flex items-center gap-2">
                                        <span className="flex items-center gap-1" style={{ ...px, fontSize: '0.35rem', color: '#dcb464' }}>
                                            <PixelCoin size={10} />{fd.cout}
                                        </span>
                                        <button onClick={() => onRetirerForme(i)} className="text-red-500 hover:text-red-400">
                                            <X className="w-3 h-3" />
                                        </button>
                                    </div>
                                </div>
        );
        })}
                        <div className="flex justify-between pt-2" style={{ borderTop: '2px solid rgba(26,10,0,0.5)' }}>
                            <span style={{ ...px, fontSize: '0.35rem', color: 'rgba(180,170,150,0.5)' }}>Coût total</span>
                            <span className="flex items-center gap-1" style={{ ...px, fontSize: '0.4rem', color: '#dcb464' }}>
                                <PixelCoin size={12} />{coutActuel}
                            </span>
                        </div>
                        <div className="flex flex-col gap-1">
        {formesSelectionnees.some(f => f.type === 'TRIANGLE') && (
                                <p style={{ ...px, fontSize: '0.32rem', color: 'rgba(180,170,150,0.4)', lineHeight: '1.8' }}>🏹 Tirs directs activés</p>
        )}
        {formesSelectionnees.some(f => f.type === 'CERCLE') && (
                                <p style={{ ...px, fontSize: '0.32rem', color: 'rgba(180,170,150,0.4)', lineHeight: '1.8' }}>🪨 Zone d&apos;effet activée</p>
        )}
                        </div>
                    </div>
        )}

        {formesSelectionnees.length < 3 && (
                    <div className="flex flex-col gap-2">
                        <p style={{ ...px, fontSize: '0.35rem', color: 'rgba(180,170,150,0.5)', letterSpacing: '0.1em', textTransform: 'uppercase' }}>
Ajouter ({formesSelectionnees.length}/3)
                        </p>
        {formesDisponibles.map(f => {
                            const peutAjouter = budget >= coutActuel + f.cout;
    return (
            <button key={f.type} onClick={() => onAjouterForme(f)} disabled={!peutAjouter}
    className={`p-3 text-left transition-all active:translate-y-[1px] ${!peutAjouter ? 'opacity-30 cursor-not-allowed' : ''}`}
    style={{
            background: 'rgba(20,14,24,0.8)',
            outline: '2px solid #1a0a00',
            boxShadow: 'inset 0 2px 0 rgba(255,255,255,0.03), 0 2px 0 #1a0a00',
                                        }}>
                                    <div className="flex items-center gap-3">
            <span className="text-2xl">{f.emoji}</span>
            <div className="flex-1">
            <p style={{ ...px, fontSize: '0.4rem', color: '#d4c8a0', lineHeight: '1.8' }}>{f.label}</p>
            <p style={{ ...px, fontSize: '0.3rem', color: 'rgba(180,170,150,0.4)', lineHeight: '1.8' }}>{f.desc}</p>
            </div>
            <span className="flex items-center gap-1" style={{ ...px, fontSize: '0.4rem', color: '#dcb464' }}>
                                            <PixelCoin size={10} />{f.cout}
            </span>
            </div>
            </button>
                            );
})}
                    </div>
        )}

        {formesSelectionnees.length > 0 && (
                    <button onClick={onContinuer}
className="btn-gold w-full py-3"
style={{ ...px, fontSize: '0.45rem' }}>
Placer la tourelle →
                    </button>
        )}
            </motion.div>
        </AnimatePresence>
        );
        }