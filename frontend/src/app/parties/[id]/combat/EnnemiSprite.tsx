'use client';

import { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { EnnemiEtat } from '@/lib/types';

const FRAME_WIDTH = 64;
const FRAME_HEIGHT = 64;
const COLS = 4;

const getEnnemiSprite = (type: string) => {
    if (type === 'Triangle') return '/sprites/sheets/SwordCavalier/SwordCavalier_ShortHair_Red1.png';
    if (type === 'Cercle') return '/sprites/sheets/SpearFighter/SpearFighter_ShortHair_Blue1.png';
    return '/sprites/sheets/SwordCavalier/SwordCavalier_ShortHair_Red1.png';
};

const OFFSETS_PAR_TYPE: Record<string, { dx: number; dy: number }[]> = {
    Triangle: [
        { dx: 0, dy: 0 }, { dx: 18, dy: -5 }, { dx: -14, dy: -3 }, { dx: 8, dy: -10 },
    ],
    Cercle: [
        { dx: 0, dy: 0 }, { dx: 10, dy: -3 }, { dx: -8, dy: -2 }, { dx: 5, dy: -7 },
    ],
    Rectangle: [
        { dx: 0, dy: 0 }, { dx: 16, dy: 0 },
    ],
};

interface EnnemiSpriteProps {
    ennemi: EnnemiEtat;
    getPixelForPos: (pos: number) => { x: number; y: number };
    tailleChemin: number;
}

export default function EnnemiSprite({ ennemi, getPixelForPos, tailleChemin }: EnnemiSpriteProps) {
    const [displayPos, setDisplayPos] = useState(ennemi.position);
    const [flashing, setFlashing] = useState(false);
    const [attaqueForteresse, setAttaqueForteresse] = useState(false);
    const [frame, setFrame] = useState(0);
    const animatingRef = useRef(false);
    const queueRef = useRef<number[]>([]);
    const prevPvRef = useRef(ennemi.pvActuels);
    const pvPct = (ennemi.pvActuels / ennemi.pvMax) * 100;
    const estALaForteresse = ennemi.position >= tailleChemin - 1;

    const OFFSETS = OFFSETS_PAR_TYPE[ennemi.type] ?? OFFSETS_PAR_TYPE['Cercle'];

    // Animation spritesheet
    useEffect(() => {
        const interval = setInterval(() => {
            setFrame(f => (f + 1) % COLS);
        }, 150);
        return () => clearInterval(interval);
    }, []);

    useEffect(() => {
        if (ennemi.pvActuels < prevPvRef.current) {
            setFlashing(true);
            setTimeout(() => setFlashing(false), 300);
        }
        prevPvRef.current = ennemi.pvActuels;
    }, [ennemi.pvActuels]);

    useEffect(() => {
        if (estALaForteresse && ennemi.vivant) {
            setAttaqueForteresse(true);
        } else {
            setAttaqueForteresse(false);
        }
    }, [estALaForteresse, ennemi.vivant]);

    useEffect(() => {
        const current = displayPos;
        const target = ennemi.position;
        if (target <= current) return;

        const steps: number[] = [];
        for (let i = current + 1; i <= target; i++) steps.push(i);
        queueRef.current = [...queueRef.current, ...steps];

        if (animatingRef.current) return;

        const stepDuration = Math.max(100, 900 / Math.max(1, target - current));

        const animate = () => {
            if (queueRef.current.length === 0) {
                animatingRef.current = false;
                return;
            }
            animatingRef.current = true;
            const next = queueRef.current.shift()!;
            setDisplayPos(next);
            setTimeout(animate, stepDuration);
        };
        animate();
    }, [ennemi.position]);

    const px = getPixelForPos(displayPos);
    const bgX = -(frame * FRAME_WIDTH);
    const bgY = 0;

    return (
        <motion.div
            animate={{ left: px.x, top: px.y }}
            transition={{ duration: 0.25, ease: 'linear' }}
            className="absolute flex flex-col items-center"
            style={{ width: 50 }}>

            {ennemi.type === 'Rectangle' ? (
                // Bélier — emoji
                <div style={{ position: 'relative', width: 50, height: 40 }}>
                    {OFFSETS.map((offset, i) => (
                        <motion.div
                            key={i}
                            animate={
                                attaqueForteresse
                                    ? { x: [offset.dx, offset.dx + 4, offset.dx - 4, offset.dx + 3, offset.dx - 3, offset.dx], scale: [1, 1.15, 1, 1.1, 1] }
                                    : flashing
                                        ? { scale: [1, 1.3, 1] }
                                        : { scale: 1 }
                            }
                            transition={
                                attaqueForteresse
                                    ? { duration: 0.6, repeat: Infinity, repeatType: 'loop' }
                                    : { duration: 0.3 }
                            }
                            style={{
                                position: 'absolute',
                                left: offset.dx,
                                top: offset.dy,
                                fontSize: 28,
                                filter: flashing ? 'brightness(3) saturate(5)' : 'none',
                            }}>
                            🐏
                        </motion.div>
                    ))}
                </div>
            ) : (
                // Cavaliers (Triangle) et infanterie (Cercle) — spritesheet
                <div style={{ position: 'relative', width: 50, height: 40 }}>
                    {OFFSETS.map((offset, i) => (
                        <motion.div
                            key={i}
                            animate={
                                attaqueForteresse
                                    ? { x: [offset.dx, offset.dx + 4, offset.dx - 4, offset.dx + 3, offset.dx - 3, offset.dx], scale: [1, 1.15, 1, 1.1, 1] }
                                    : flashing
                                        ? { scale: [1, 1.3, 1] }
                                        : { scale: 1 }
                            }
                            transition={
                                attaqueForteresse
                                    ? { duration: 0.6, repeat: Infinity, repeatType: 'loop' }
                                    : { duration: 0.3 }
                            }
                            style={{
                                position: 'absolute',
                                left: offset.dx,
                                top: offset.dy,
                                width: 32,
                                height: 32,
                                backgroundImage: `url(${getEnnemiSprite(ennemi.type)})`,
                                backgroundPosition: `${bgX}px ${bgY}px`,
                                backgroundSize: `${FRAME_WIDTH * COLS}px auto`,
                                backgroundRepeat: 'no-repeat',
                                imageRendering: 'pixelated' as const,
                                filter: flashing ? 'brightness(3) saturate(5)' : 'none',
                            }}
                        />
                    ))}
                </div>
            )}

            <AnimatePresence>
                {flashing && !attaqueForteresse && (
                    <motion.span
                        initial={{ opacity: 1, y: 0 }}
                        animate={{ opacity: 0, y: -20 }}
                        exit={{ opacity: 0 }}
                        transition={{ duration: 0.5 }}
                        className="absolute text-red-400 font-black text-xs"
                        style={{ top: -16, left: 4 }}>
                        💢
                    </motion.span>
                )}
            </AnimatePresence>

            <AnimatePresence>
                {attaqueForteresse && (
                    <motion.span
                        initial={{ opacity: 0, scale: 0 }}
                        animate={{ opacity: [1, 0], scale: [0.5, 1.5], y: [0, -25] }}
                        transition={{ duration: 0.8, repeat: Infinity }}
                        className="absolute text-orange-400 font-black text-xs"
                        style={{ top: -18 }}>
                        💥
                    </motion.span>
                )}
            </AnimatePresence>

            <div className="w-full h-1 bg-gray-700 rounded mt-0.5">
                <motion.div
                    animate={{ width: `${pvPct}%` }}
                    transition={{ duration: 0.3 }}
                    className={`h-1 rounded ${
                        pvPct > 50 ? 'bg-green-500'
                            : pvPct > 25 ? 'bg-yellow-500'
                                : 'bg-red-500'
                    }`}
                />
            </div>
        </motion.div>
    );
}