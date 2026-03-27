'use client';

import { motion } from 'framer-motion';
import { X } from 'lucide-react';
import { PixelBorder, PixelCoin } from '@/components/PixelSprites';

const px = { fontFamily: 'var(--font-pixel)' };

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
                    className="flex flex-col gap-4 p-4 relative"
                    style={{
                        background: 'rgba(26,20,32,0.85)',
                        outline: '3px solid #1a0a00',
                        boxShadow: 'inset 0 3px 0 rgba(139,105,20,0.1), inset 0 -3px 0 rgba(0,0,0,0.3), 0 3px 0 #1a0a00',
                    }}>
            <PixelBorder className="absolute top-0 left-0 right-0" />
            <div className="flex justify-between items-center pt-2">
                <h2 style={{ ...px, fontSize: '0.45rem', color: '#8b6914', lineHeight: '1.8' }}>
                    🧱 Muraille — Pos {position}
                </h2>
                <button onClick={onAnnuler} className="text-gray-500 hover:text-white"><X className="w-4 h-4" /></button>
            </div>

            <p style={{ ...px, fontSize: '0.32rem', color: 'rgba(180,170,150,0.5)', lineHeight: '2.2' }}>
                Obstacle sur le chemin. Les ennemis doivent le détruire pour avancer.
            </p>

            <div className="grid grid-cols-2 gap-2">
                <div className="text-center p-3"
                     style={{ background: 'rgba(10,10,15,0.4)', outline: '2px solid #1a0a00', boxShadow: '0 2px 0 #0a0508' }}>
                    <p style={{ ...px, fontSize: '0.6rem', color: '#3498db' }}>{pvMax}</p>
                    <p style={{ ...px, fontSize: '0.28rem', color: 'rgba(180,170,150,0.4)', marginTop: '4px', textTransform: 'uppercase', letterSpacing: '0.1em' }}>PV</p>
                </div>
                <div className="text-center p-3"
                     style={{ background: 'rgba(10,10,15,0.4)', outline: '2px solid #1a0a00', boxShadow: '0 2px 0 #0a0508' }}>
                    <p className="flex items-center justify-center gap-1" style={{ ...px, fontSize: '0.6rem', color: '#dcb464' }}>
                        <PixelCoin size={14} />{cout}
                    </p>
                    <p style={{ ...px, fontSize: '0.28rem', color: 'rgba(180,170,150,0.4)', marginTop: '4px', textTransform: 'uppercase', letterSpacing: '0.1em' }}>Coût</p>
                </div>
            </div>

            <p style={{ ...px, fontSize: '0.32rem', color: '#c44030', lineHeight: '1.8' }}>
                ⚠️ Le bélier inflige ×2 dégâts aux murailles
            </p>

            <button onClick={onPlacer} disabled={loading}
                    className={`btn-gold w-full py-3 ${loading ? 'opacity-50' : ''}`}
                    style={{ ...px, fontSize: '0.45rem' }}>
                {loading ? 'Placement...' : '🧱 Placer la muraille'}
            </button>
        </motion.div>
    );
}