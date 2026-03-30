'use client';
import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';

interface SpriteProps {
    size?: number;
    className?: string;
    firing?: boolean;
    direction?: 'down' | 'left' | 'right' | 'up';
}

interface TowerSpriteProps {
    className?: string;
    firing?: boolean;
    direction?: 'down' | 'left' | 'right' | 'up';
}

const FRAME_WIDTH = 70;
const FRAME_HEIGHT = 130;
const TOTAL_FRAMES = 6;

const ARROW_ANIM = {
    down: { x: [0, 0, 0], y: [0, 40, 80], opacity: [1, 1, 0] },
    left: { x: [0, -40, -80], y: [0, 0, 0], opacity: [1, 1, 0] },
    right: { x: [0, 40, 80], y: [0, 0, 0], opacity: [1, 1, 0] },
    up: { x: [0, 0, 0], y: [0, -40, -80], opacity: [1, 1, 0] },
};

const ARROW_POS = {
    down: { bottom: '-5%', left: '45%' },
    left: { top: '40%', left: '-10%' },
    right: { top: '40%', right: '-10%' },
    up: { top: '-10%', left: '45%' },
};

const ARROW_ROTATE = { down: 90, left: 180, right: 0, up: -90 };

export function PixelTowerSprite({ className = '', firing = false, direction = 'down' }: TowerSpriteProps) {
    const [frame, setFrame] = useState(0);

    useEffect(() => {
        const interval = setInterval(() => {
            setFrame(f => (f + 1) % TOTAL_FRAMES);
        }, 200);
        return () => clearInterval(interval);
    }, []);

    return (
        <div className={`relative ${className}`} style={{ width: '85%', height: '85%' }}>
            <div style={{
                width: '100%',
                height: '100%',
                overflow: 'hidden',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
            }}>
                <div style={{
                    width: FRAME_WIDTH,
                    height: FRAME_HEIGHT,
                    overflow: 'hidden',
                    transform: 'scale(0.7)',
                    transformOrigin: 'center center',
                }}>
                    <div style={{
                        width: FRAME_WIDTH * TOTAL_FRAMES,
                        height: FRAME_HEIGHT,
                        backgroundImage: 'url(/sprites/tower.png)',
                        backgroundSize: `${FRAME_WIDTH * TOTAL_FRAMES}px ${FRAME_HEIGHT}px`,
                        backgroundPosition: `${-(frame * FRAME_WIDTH)}px 0px`,
                        backgroundRepeat: 'no-repeat',
                        imageRendering: 'pixelated',
                    }} />
                </div>
            </div>
            {firing && (
                <motion.div
                    initial={{ opacity: 1 }}
                    animate={ARROW_ANIM[direction]}
                    transition={{ duration: 0.4, repeat: Infinity, repeatDelay: 0.8 }}
                    className="absolute"
                    style={{
                        ...ARROW_POS[direction],
                        transform: `rotate(${ARROW_ROTATE[direction]}deg)`,
                    }}>
                    <img src="/sprites/arrow.png" alt=""
                         width={18} height={9}
                         style={{ imageRendering: 'pixelated' }} />
                </motion.div>
            )}
        </div>
    );
}


export function PixelStoneWall({ size = 48, className = '', damaged = false, pvPct = 100 }: SpriteProps & { damaged?: boolean; pvPct?: number }) {
    const sprite = pvPct > 50
        ? '/sprites/tiles/Tile2_04.png'
        : pvPct > 0
            ? '/sprites/tiles/Tile2_48.png'
            : '/sprites/tiles/Tile2_64.png';

    return (
        <div className={`pixel-render ${className}`} style={{
            width: '60%',
            height: '70%',
            backgroundImage: `url(${sprite})`,
            backgroundSize: '100% 100%',
            backgroundRepeat: 'no-repeat',
        }} />
    );
}

export function PixelCitadel({ size = 48, className = '', damaged = false }: SpriteProps & { damaged?: boolean }) {
    const w = damaged ? '#6a2020' : '#8b1a1a';
    const wl = damaged ? '#8a3030' : '#a82828';
    const wd = damaged ? '#4a1010' : '#5a0e0e';

    return (
        <div className={className} style={{ width: '85%', height: '85%' }}>
            <svg viewBox="0 0 32 32" width="100%" height="100%" style={{ imageRendering: 'pixelated' }}>
                <rect x="0" y="6" width="10" height="26" fill={w} />
                <rect x="0" y="6" width="10" height="1" fill={wd} />
                <rect x="1" y="7" width="8" height="1" fill={wl} />
                <rect x="0" y="3" width="3" height="3" fill={w} />
                <rect x="4" y="3" width="3" height="3" fill={w} />
                <rect x="8" y="3" width="2" height="3" fill={w} />
                <rect x="0" y="3" width="3" height="1" fill={wd} />
                <rect x="4" y="3" width="3" height="1" fill={wd} />
                <rect x="8" y="3" width="2" height="1" fill={wd} />
                <rect x="10" y="2" width="12" height="30" fill={w} />
                <rect x="10" y="2" width="12" height="1" fill={wd} />
                <rect x="11" y="3" width="10" height="1" fill={wl} />
                <rect x="10" y="0" width="3" height="2" fill={w} />
                <rect x="15" y="0" width="3" height="2" fill={w} />
                <rect x="19" y="0" width="3" height="2" fill={w} />
                <rect x="16" y="-2" width="1" height="3" fill="#5a3d28" />
                <rect x="17" y="-2" width="5" height="2" fill="#c44030" />
                <rect x="17" y="-1" width="4" height="1" fill="#dc6428" />
                <rect x="22" y="6" width="10" height="26" fill={w} />
                <rect x="22" y="6" width="10" height="1" fill={wd} />
                <rect x="23" y="7" width="8" height="1" fill={wl} />
                <rect x="22" y="3" width="3" height="3" fill={w} />
                <rect x="26" y="3" width="3" height="3" fill={w} />
                <rect x="30" y="3" width="2" height="3" fill={w} />
                <rect x="22" y="3" width="3" height="1" fill={wd} />
                <rect x="26" y="3" width="3" height="1" fill={wd} />
                <rect x="30" y="3" width="2" height="1" fill={wd} />
                <rect x="12" y="22" width="8" height="10" fill="#1a0a00" />
                <rect x="12" y="22" width="8" height="1" fill={wd} />
                <rect x="13" y="23" width="1" height="9" fill="#4a4a5a" />
                <rect x="15" y="23" width="1" height="9" fill="#4a4a5a" />
                <rect x="17" y="23" width="1" height="9" fill="#4a4a5a" />
                <rect x="19" y="23" width="1" height="9" fill="#4a4a5a" />
                <rect x="12" y="26" width="8" height="1" fill="#4a4a5a" />
                <rect x="12" y="29" width="8" height="1" fill="#4a4a5a" />
                <rect x="3" y="12" width="3" height="4" fill="#1a0a00" />
                <rect x="3" y="12" width="3" height="1" fill={wd} />
                <rect x="26" y="12" width="3" height="4" fill="#1a0a00" />
                <rect x="26" y="12" width="3" height="1" fill={wd} />
                <rect x="14" y="10" width="4" height="4" fill="#1a0a00" />
                <rect x="14" y="10" width="4" height="1" fill={wd} />
                {damaged && (
                    <>
                        <rect x="4" y="18" width="1" height="5" fill="#3a0808" />
                        <rect x="5" y="22" width="1" height="3" fill="#3a0808" />
                        <rect x="25" y="16" width="1" height="4" fill="#3a0808" />
                        <rect x="26" y="19" width="1" height="3" fill="#3a0808" />
                    </>
                )}
            </svg>
        </div>
    );



}