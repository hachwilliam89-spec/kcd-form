'use client';

import { motion } from 'framer-motion';
import { Tourelle } from '@/lib/api';
import { PixelTowerSprite } from '@/components/PixelMapSprites';

const px = { fontFamily: 'var(--font-pixel)' };

const STONE_BG = `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='32' height='32' viewBox='0 0 32 32'%3E%3Crect width='32' height='32' fill='%23706050'/%3E%3Crect x='0' y='0' width='15' height='9' fill='%23807060' rx='1'/%3E%3Crect x='17' y='0' width='15' height='9' fill='%23786858' rx='1'/%3E%3Crect x='8' y='11' width='16' height='9' fill='%23807060' rx='1'/%3E%3Crect x='0' y='11' width='6' height='9' fill='%23786858' rx='1'/%3E%3Crect x='26' y='11' width='6' height='9' fill='%23786858' rx='1'/%3E%3Crect x='0' y='22' width='15' height='10' fill='%23786858' rx='1'/%3E%3Crect x='17' y='22' width='15' height='10' fill='%23807060' rx='1'/%3E%3Crect x='0' y='10' width='32' height='1' fill='%23605040'/%3E%3Crect x='0' y='21' width='32' height='1' fill='%23605040'/%3E%3Crect x='16' y='0' width='1' height='10' fill='%23605040'/%3E%3Crect x='7' y='11' width='1' height='10' fill='%23605040'/%3E%3Crect x='25' y='11' width='1' height='10' fill='%23605040'/%3E%3Crect x='16' y='22' width='1' height='10' fill='%23605040'/%3E%3C/svg%3E")`;

const DIRECTION_PAR_POSITION: Record<number, 'down' | 'left' | 'right' | 'up'> = {
    1: 'down', 2: 'down', 3: 'down', 4: 'down',
    5: 'left', 6: 'left', 7: 'left',
    8: 'down', 9: 'down', 10: 'down',
};

interface CaseButtonProps {
    pos: number;
    tourelle?: Tourelle;
    selectionnee: boolean;
    onSelect: (pos: number) => void;
    style?: React.CSSProperties;
}

export default function CaseButton({ pos, tourelle, selectionnee, onSelect, style }: CaseButtonProps) {
    const direction = DIRECTION_PAR_POSITION[pos] ?? 'down';

    return (
        <motion.button
            style={{
                ...style,
                backgroundImage: STONE_BG,
                backgroundSize: '100% 100%',
                outline: selectionnee ? '2px solid rgba(220,180,100,0.6)' : '2px solid rgba(60,50,40,0.4)',
                boxShadow: selectionnee ? '0 0 12px rgba(220,180,100,0.3), 0 2px 0 #0a0508' :
                    'inset 0 2px 0 rgba(255,255,255,0.03), 0 2px 0 #0a0508',
            }}
            whileHover={{ scale: tourelle ? 1 : 1.08 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => { if (!tourelle) onSelect(pos); }}
            title={tourelle ? tourelle.nom : `Case ${pos}`}
            className={`flex flex-col items-center justify-center transition-all pixel-render ${
                tourelle ? 'cursor-default' : 'cursor-pointer'
            }`}
        >
            {tourelle ? (
                <PixelTowerSprite firing={false} direction={direction} />
            ) : (
                <span style={{ ...px, fontSize: '0.35rem', color: 'rgba(220,200,160,0.5)' }}>{pos}</span>
            )}
        </motion.button>
    );
}