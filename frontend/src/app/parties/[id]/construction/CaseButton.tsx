'use client';

import { motion } from 'framer-motion';
import { Tourelle } from '@/lib/api';

interface CaseButtonProps {
    pos: number;
    tourelle?: Tourelle;
    selectionnee: boolean;
    onSelect: (pos: number) => void;
    style?: React.CSSProperties;
}

export default function CaseButton({ pos, tourelle, selectionnee, onSelect, style }: CaseButtonProps) {
    return (
        <motion.button
            style={style}
            whileHover={{ scale: tourelle ? 1 : 1.08 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => { if (!tourelle) onSelect(pos); }}
            title={tourelle ? tourelle.nom : `Case ${pos}`}
            className={`rounded-lg border-2 flex flex-col items-center justify-center gap-0.5 transition-all ${
                tourelle ? 'border-[#c9a84c] bg-[#2a2a35] cursor-default'
                    : selectionnee ? 'border-[#c9a84c] bg-[#c9a84c]/20 cursor-pointer shadow-[0_0_12px_rgba(201,168,76,0.4)]'
                        : 'border-[#3a3a48] bg-[#1a1a22]/80 hover:border-[#c9a84c]/60 cursor-pointer'
            }`}
        >
            {tourelle ? (
                <>
                    <span className="text-base">{tourelle.aoe ? '🪨' : '🏹'}</span>
                    <p className="text-[7px] text-[#c9a84c] truncate w-full text-center px-0.5 leading-tight">{tourelle.nom}</p>
                </>
            ) : (
                <span className="text-gray-500 text-xs font-bold">{pos}</span>
            )}
        </motion.button>
    );
}