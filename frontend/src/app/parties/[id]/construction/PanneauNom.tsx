'use client';

import { motion } from 'framer-motion';

interface FormeDisponible {
    type: 'TRIANGLE' | 'CERCLE';
    emoji: string;
}

interface PanneauNomProps {
    formesSelectionnees: { type: 'TRIANGLE' | 'CERCLE' }[];
    formesDisponibles: FormeDisponible[];
    coutActuel: number;
    nomTourelle: string;
    loading: boolean;
    onNomChange: (nom: string) => void;
    onPlacer: () => void;
    onRetour: () => void;
}

export default function PanneauNom({
                                       formesSelectionnees, formesDisponibles, coutActuel,
                                       nomTourelle, loading, onNomChange, onPlacer, onRetour
                                   }: PanneauNomProps) {
    return (
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}
                    className="bg-[#2a2a35] border border-[#c9a84c]/30 rounded-lg p-4 flex flex-col gap-4">
            <div className="flex justify-between items-center">
                <h2 className="text-[#c9a84c] text-sm uppercase tracking-widest" style={{ fontFamily: 'var(--font-cinzel)' }}>
                    Nommer la tourelle
                </h2>
                <button onClick={onRetour} className="text-gray-500 hover:text-white text-xs">← Retour</button>
            </div>
            <div className="bg-[#1a1a22] rounded p-3 flex flex-col gap-1">
                <p className="text-xs text-gray-400">Composition :</p>
                <div className="flex gap-2 flex-wrap">
                    {formesSelectionnees.map((f, i) => {
                        const fd = formesDisponibles.find(fd => fd.type === f.type)!;
                        return <span key={i} className="text-lg">{fd.emoji}</span>;
                    })}
                </div>
                <p className="text-[#c9a84c] text-sm font-bold mt-1">{coutActuel} or</p>
            </div>
            <input
                className="bg-[#1a1a22] border border-[#c9a84c]/30 focus:border-[#c9a84c] rounded px-3 py-2 text-white outline-none transition-colors"
                placeholder="Ex: Tour du Nord, Sentinelle..."
                value={nomTourelle}
                onChange={e => onNomChange(e.target.value)}
                autoFocus
            />
            <button onClick={onPlacer} disabled={loading || !nomTourelle.trim()}
                    className="bg-[#c9a84c] hover:bg-[#e8c96d] disabled:opacity-40 text-black font-black py-3 rounded uppercase tracking-widest transition-all"
                    style={{ fontFamily: 'var(--font-cinzel)' }}>
                {loading ? 'Placement...' : '⚔️ Placer la tourelle'}
            </button>
        </motion.div>
    );
}