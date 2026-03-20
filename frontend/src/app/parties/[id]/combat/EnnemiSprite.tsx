'use client';

import { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { EnnemiEtat } from '@/lib/types';


const getEnnemiEmoji = (type: string) => {
    if (type === 'Triangle') return '⚔️';
    if (type === 'Cercle') return '🛡️';
    if (type === 'Rectangle') return '🐏';
    return '👾';
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
    const animatingRef = useRef(false);
    const queueRef = useRef<number[]>([]);
    const prevPvRef = useRef(ennemi.pvActuels);
    const pvPct = (ennemi.pvActuels / ennemi.pvMax) * 100;
    const estALaForteresse = ennemi.position >= tailleChemin - 1;

    // Flash rouge si perte de PV
    useEffect(() => {
        if (ennemi.pvActuels < prevPvRef.current) {
            setFlashing(true);
            setTimeout(() => setFlashing(false), 300);
        }
        prevPvRef.current = ennemi.pvActuels;
    }, [ennemi.pvActuels]);

    // Animation attaque forteresse (pulsation continue)
    useEffect(() => {
        if (estALaForteresse && ennemi.vivant) {
            setAttaqueForteresse(true);
        } else {
            setAttaqueForteresse(false);
        }
    }, [estALaForteresse, ennemi.vivant]);

    // Interpolation case par case
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

    return (
        <motion.div
            animate={{ left: px.x, top: px.y }}
            transition={{ duration: 0.25, ease: 'linear' }}
            className="absolute flex flex-col items-center"
            style={{ width: 32 }}>

            {/* Emoji ennemi */}
            <motion.span
                animate={
                    attaqueForteresse
                        ? { x: [0, 4, -4, 3, -3, 0], scale: [1, 1.15, 1, 1.1, 1] }
                        : flashing
                            ? { filter: ['brightness(1)', 'brightness(3)', 'brightness(1)'], scale: [1, 1.3, 1] }
                            : { scale: 1 }
                }
                transition={
                    attaqueForteresse
                        ? { duration: 0.6, repeat: Infinity, repeatType: 'loop' }
                        : { duration: 0.3 }
                }
                className="text-xl leading-none drop-shadow-lg"
                style={{ filter: flashing ? 'hue-rotate(0deg) saturate(5)' : 'none' }}>
                {getEnnemiEmoji(ennemi.type)}
            </motion.span>

            {/* Texte dégâts flottant */}
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

            {/* Indicateur attaque forteresse */}
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

            {/* Barre de PV */}
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