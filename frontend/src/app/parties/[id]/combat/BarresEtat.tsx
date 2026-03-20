'use client';

import { motion } from 'framer-motion';

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
            <div className="flex-1 bg-[#1a1a22] border border-[#3a3a48] rounded-xl p-3">
                <div className="flex justify-between items-center mb-1">
                    <p className="text-xs text-gray-400 uppercase tracking-widest">🏰 Citadelle</p>
                    <p className="text-xs text-red-400">
                        {forteressePvActuels} / {forteressePvMax} PV
                    </p>
                </div>
                <div className="w-full bg-[#2a2a35] rounded-full h-2">
                    <motion.div
                        animate={{ width: `${forteressePct}%` }}
                        transition={{ duration: 0.5 }}
                        className={`h-2 rounded-full ${
                            forteressePct > 60
                                ? 'bg-gradient-to-r from-[#8b1a1a] to-[#c0392b]'
                                : forteressePct > 30
                                    ? 'bg-gradient-to-r from-orange-700 to-orange-500'
                                    : 'bg-gradient-to-r from-red-900 to-red-500 animate-pulse'
                        }`}
                    />
                </div>
            </div>

            {/* Vague : timer ou progression ennemis selon dernière vague */}
            <div className="flex-1 bg-[#1a1a22] border border-[#3a3a48] rounded-xl p-3">
                {derniereVague ? (
                    <>
                        <div className="flex justify-between items-center mb-1">
                            <p className="text-xs text-red-400 uppercase tracking-widest font-bold">⚔️ Vague finale</p>
                            <p className="text-xs text-gray-400">
                                💀 {ennemisTotal - ennemisVivants} / {ennemisTotal} éliminés
                            </p>
                        </div>
                        <div className="w-full bg-[#2a2a35] rounded-full h-2">
                            <motion.div
                                animate={{ width: `${ennemisEliminePct}%` }}
                                transition={{ duration: 0.5 }}
                                className={`h-2 rounded-full ${
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
                        <div className="flex justify-between items-center mb-1">
                            <p className="text-xs text-gray-400 uppercase tracking-widest">⏱ Vague</p>
                            <p className="text-xs text-gray-400">
                                👾 {ennemisVivants} / {ennemisTotal}
                            </p>
                        </div>
                        <div className="w-full bg-[#2a2a35] rounded-full h-2">
                            <motion.div
                                animate={{ width: `${timerPct}%` }}
                                transition={{ duration: 0.5 }}
                                className={`h-2 rounded-full ${
                                    timerPct > 80
                                        ? 'bg-gradient-to-r from-red-700 to-red-500 animate-pulse'
                                        : timerPct > 50
                                            ? 'bg-gradient-to-r from-yellow-700 to-yellow-500'
                                            : 'bg-gradient-to-r from-[#c9a84c] to-[#e8c96d]'
                                }`}
                            />
                        </div>
                    </>
                )}
            </div>
        </div>
    );
}