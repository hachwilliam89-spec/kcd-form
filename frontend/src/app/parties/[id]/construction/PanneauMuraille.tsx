'use client';

import { motion } from 'framer-motion';
import { X } from 'lucide-react';

interface PanneauMurailleProps {
    position: number;
    pvMax: number;
    cout: number;
    loading: boolean;
    onPlacer: () => void;
    onAnnuler: () => void;
}

export default function PanneauMuraille({ position, pvMax, cout, loading, onPlacer, onAnnuler }: PanneauMurailleProps) {
    return (
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}
                    className="bg-[#2a2a35] border border-[#8b6914]/30 rounded-lg p-4 flex flex-col gap-4">
            <div className="flex justify-between items-center">
                <h2 className="text-[#8b6914] text-sm uppercase tracking-widest" style={{ fontFamily: 'var(--font-cinzel)' }}>
                    🧱 Muraille — Position {position}
                </h2>
                <button onClick={onAnnuler} className="text-gray-500 hover:text-white"><X className="w-4 h-4" /></button>
            </div>
            <div className="bg-[#1a1a22] rounded p-3 flex flex-col gap-2">
                <p className="text-gray-400 text-xs">Obstacle placé sur le chemin. Les ennemis doivent le détruire pour avancer.</p>
                <div className="grid grid-cols-2 gap-2 text-center">
                    <div className="bg-[#2a2a35] rounded p-2">
                        <p className="text-blue-400 font-black text-lg">{pvMax}</p>
                        <p className="text-[9px] text-gray-500">PV</p>
                    </div>
                    <div className="bg-[#2a2a35] rounded p-2">
                        <p className="text-[#c9a84c] font-black text-lg">{cout}</p>
                        <p className="text-[9px] text-gray-500">Coût en or</p>
                    </div>
                </div>
                <p className="text-red-400 text-xs">⚠️ Le bélier inflige ×2 dégâts aux murailles</p>
            </div>
            <button onClick={onPlacer} disabled={loading}
                    className="bg-[#8b6914] hover:bg-[#a67d1a] disabled:opacity-40 text-black font-black py-3 rounded uppercase tracking-widest transition-all"
                    style={{ fontFamily: 'var(--font-cinzel)' }}>
                {loading ? 'Placement...' : '🧱 Placer la muraille'}
            </button>
        </motion.div>
    );
}