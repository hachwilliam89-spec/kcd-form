'use client';

import { motion } from 'framer-motion';
import { Tourelle } from '@/lib/api';

const px = { fontFamily: 'var(--font-pixel)' };

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
            style={{
                ...style,
                background: tourelle ? 'rgba(26,20,32,0.8)' :
                    selectionnee ? 'rgba(220,180,100,0.15)' : 'rgba(26,20,32,0.5)',
                outline: tourelle ? '2px solid rgba(220,180,100,0.4)' :
                    selectionnee ? '2px solid rgba(220,180,100,0.6)' : '2px solid rgba(60,50,40,0.4)',
                boxShadow: selectionnee ? '0 0 12px rgba(220,180,100,0.3), 0 2px 0 #0a0508' :
                    'inset 0 2px 0 rgba(255,255,255,0.03), 0 2px 0 #0a0508',
            }}
            whileHover={{ scale: tourelle ? 1 : 1.08 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => { if (!tourelle) onSelect(pos); }}
            title={tourelle ? tourelle.nom : `Case ${pos}`}
            className={`flex flex-col items-center justify-center gap-0.5 transition-all ${
                tourelle ? 'cursor-default' : 'cursor-pointer'
            }`}
        >
            {tourelle ? (
                <>
                    <span className="text-base">{tourelle.aoe ? '🪨' : '🏹'}</span>
                    <p style={{ ...px, fontSize: '0.25rem', color: '#dcb464', lineHeight: '1.4' }}
                       className="truncate w-full text-center px-0.5">{tourelle.nom}</p>
                </>
            ) : (
                <span style={{ ...px, fontSize: '0.35rem', color: 'rgba(180,170,150,0.4)' }}>{pos}</span>
            )}
        </motion.button>
    );
}