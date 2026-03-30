'use client';

import { motion } from 'framer-motion';

const px = { fontFamily: 'var(--font-pixel)' };

interface BarresEtatProps {
    forteressePvActuels: number;
    forteressePvMax: number;
    ennemisVivants: number;
    ennemisTotal: number;
    tempsEcoule: number;
    dureeSecondes: number;
    derniereVague: boolean;
}

export default function BarresEtat({
                                       forteressePvActuels, forteressePvMax,
                                       ennemisVivants, ennemisTotal,
                                       tempsEcoule, dureeSecondes,
                                       derniereVague
                                   }: BarresEtatProps) {
    const forteressePct = (forteressePvActuels / forteressePvMax) * 100;
    const timerPct = (tempsEcoule / dureeSecondes) * 100;
    const ennemisEliminePct = ennemisTotal > 0
        ? ((ennemisTotal - ennemisVivants) / ennemisTotal) * 100
        : 0;

    return (
        <div className="flex gap-3 flex-shrink-0">
            {/* Forteresse */}
            <div className="flex-1 p-3"
                 style={{
                     background: 'rgba(26,20,32,0.85)',
                     outline: '2px solid #1a0a00',
                     boxShadow: 'inset 0 2px 0 rgba(196,64,48,0.1), 0 2px 0 #0a0508',
                 }}>
                <div className="flex justify-between items-center mb-2">
                    <p style={{ ...px, fontSize: '0.3rem', color: 'rgba(180,170,150,0.5)', letterSpacing: '0.1em', textTransform: 'uppercase' }}>
                        🏰 Citadelle
                    </p>
                    <p style={{ ...px, fontSize: '0.3rem', color: '#c44030' }}>
                        {forteressePvActuels} / {forteressePvMax} PV
                    </p>
                </div>
                <div className="w-full h-2" style={{ background: 'rgba(10,10,15,0.5)', outline: '1px solid #1a0a00' }}>
                    <motion.div
                        animate={{ width: `${forteressePct}%` }}
                        transition={{ duration: 0.5 }}
                        className={`h-2 ${
                            forteressePct > 60
                                ? 'bg-gradient-to-r from-[#8b1a1a] to-[#c0392b]'
                                : forteressePct > 30
                                    ? 'bg-gradient-to-r from-orange-700 to-orange-500'
                                    : 'bg-gradient-to-r from-red-900 to-red-500 animate-pulse'
                        }`}
                    />
                </div>
            </div>

            {/* Vague */}
            <div className="flex-1 p-3"
                 style={{
                     background: 'rgba(26,20,32,0.85)',
                     outline: '2px solid #1a0a00',
                     boxShadow: 'inset 0 2px 0 rgba(220,180,100,0.08), 0 2px 0 #0a0508',
                 }}>
                {derniereVague ? (
                    <>
                        <div className="flex justify-between items-center mb-2">
                            <p style={{ ...px, fontSize: '0.3rem', color: '#c44030', letterSpacing: '0.1em', textTransform: 'uppercase' }}>
                                ⚔️ Vague finale
                            </p>
                            <p style={{ ...px, fontSize: '0.3rem', color: 'rgba(180,170,150,0.5)' }}>
                                💀 {ennemisTotal - ennemisVivants} / {ennemisTotal}
                            </p>
                        </div>
                        <div className="w-full h-2" style={{ background: 'rgba(10,10,15,0.5)', outline: '1px solid #1a0a00' }}>
                            <motion.div
                                animate={{ width: `${ennemisEliminePct}%` }}
                                transition={{ duration: 0.5 }}
                                className={`h-2 ${
                                    ennemisEliminePct > 75
                                        ? 'bg-gradient-to-r from-green-700 to-green-500'
                                        : ennemisEliminePct > 40
                                            ? 'bg-gradient-to-r from-yellow-700 to-yellow-500'
                                            : 'bg-gradient-to-r from-red-700 to-red-500'
                                }`}
                            />
                        </div>
                    </>
                ) : (
                    <>
                        <div className="flex justify-between items-center mb-2">
                            <p style={{ ...px, fontSize: '0.3rem', color: 'rgba(180,170,150,0.5)', letterSpacing: '0.1em', textTransform: 'uppercase' }}>
                                ⏱ Vague
                            </p>
                            <p style={{ ...px, fontSize: '0.3rem', color: 'rgba(180,170,150,0.5)' }}>
                                👾 {ennemisVivants} / {ennemisTotal}
                            </p>
                        </div>
                        <div className="w-full h-2" style={{ background: 'rgba(10,10,15,0.5)', outline: '1px solid #1a0a00' }}>
                            <motion.div
                                animate={{ width: `${timerPct}%` }}
                                transition={{ duration: 0.5 }}
                                className={`h-2 ${
                                    timerPct > 80
                                        ? 'bg-gradient-to-r from-red-700 to-red-500 animate-pulse'
                                        : timerPct > 50
                                            ? 'bg-gradient-to-r from-yellow-700 to-yellow-500'
                                            : 'bg-gradient-to-r from-[#dcb464] to-[#f0d070]'
                                }`}
                            />
                        </div>
                    </>
                )}
            </div>
        </div>
    );
}