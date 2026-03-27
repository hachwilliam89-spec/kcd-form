'use client';

import { motion } from 'framer-motion';
import { Muraille } from '@/lib/api';

const px = { fontFamily: 'var(--font-pixel)' };

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
            style={{
                ...style,
                background: muraille ? 'rgba(42,31,15,0.7)' :
                    selectionnee ? 'rgba(139,105,20,0.2)' : 'rgba(42,31,15,0.4)',
                outline: muraille ? '2px solid rgba(139,105,20,0.5)' :
                    selectionnee ? '2px solid rgba(139,105,20,0.6)' : '2px solid rgba(74,58,26,0.3)',
                boxShadow: selectionnee ? '0 0 12px rgba(139,105,20,0.3), 0 2px 0 #0a0508' :
                    'inset 0 2px 0 rgba(255,255,255,0.03), 0 2px 0 #0a0508',
                opacity: !muraille && !peutPayer ? 0.4 : 1,
            }}
            whileHover={{ scale: muraille ? 1 : 1.08 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => { if (!muraille && peutPayer) onSelect(pos); }}
            title={muraille ? `Muraille (${muraille.pvMax} PV)` : `Muraille pos.${pos}`}
            className={`flex flex-col items-center justify-center gap-0.5 transition-all ${
                muraille ? 'cursor-default' : peutPayer ? 'cursor-pointer' : 'cursor-not-allowed'
            }`}
        >
            {muraille ? (
                <>
                    <span className="text-base">🧱</span>
                    <p style={{ ...px, fontSize: '0.25rem', color: '#8b6914', lineHeight: '1.4' }}
                       className="truncate w-full text-center px-0.5">{muraille.pvMax} PV</p>
                </>
            ) : (
                <span className="text-xs" style={{ opacity: 0.6 }}>🧱</span>
            )}
        </motion.button>
    );
}