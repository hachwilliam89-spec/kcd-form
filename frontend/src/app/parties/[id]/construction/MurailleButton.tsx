'use client';

import { motion } from 'framer-motion';
import { Muraille } from '@/lib/api';


interface MurailleButtonProps {
    pos: number;
    muraille?: Muraille;
    selectionnee: boolean;
    peutPayer: boolean;
    onSelect: (pos: number) => void;
    style?: React.CSSProperties;
}

export default function MurailleButton({ pos, muraille, selectionnee, peutPayer, onSelect, style }: MurailleButtonProps) {
    return (
        <motion.button
            style={style}
            whileHover={{ scale: muraille ? 1 : 1.08 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => { if (!muraille && peutPayer) onSelect(pos); }}
            title={muraille ? `Muraille (${muraille.pvMax} PV)` : `Muraille pos.${pos}`}
            className={`rounded-lg border-2 flex flex-col items-center justify-center gap-0.5 transition-all ${
                muraille ? 'border-[#8b6914] bg-[#3a2a10] cursor-default'
                    : selectionnee ? 'border-[#8b6914] bg-[#8b6914]/20 cursor-pointer shadow-[0_0_12px_rgba(139,105,20,0.4)]'
                        : peutPayer
                            ? 'border-[#4a3a1a]/80 bg-[#2a1f0f]/90 hover:border-[#8b6914]/60 cursor-pointer'
                            : 'border-[#4a3a1a]/30 bg-[#2a1f0f]/40 cursor-not-allowed opacity-40'
            }`}
        >
            {muraille ? (
                <>
                    <span className="text-base">🧱</span>
                    <p className="text-[7px] text-[#8b6914] truncate w-full text-center px-0.5 leading-tight">{muraille.pvMax} PV</p>
                </>
            ) : (
                <span className="text-[#8b6914]/60 text-xs">🧱</span>
            )}
        </motion.button>
    );
}