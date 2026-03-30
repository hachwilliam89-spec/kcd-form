'use client';

/**
 * KCD Formes — Pixel Art SVG Components
 *
 * Tous les éléments visuels pixel art du jeu en SVG inline.
 * Scalables, légers, pas de fichier externe.
 *
 * Usage: <PixelTriangle size={32} /> ou <PixelTorch size={48} />
 */

interface PixelProps {
    size?: number;
    className?: string;
}

// Helper : dessiner un pixel (rectangle) dans un grid
const P = ({ x, y, fill, s = 1 }: { x: number; y: number; fill: string; s?: number }) => (
    <rect x={x} y={y} width={s} height={s} fill={fill} />
);

// ============================================
// FORMES GÉOMÉTRIQUES
// ============================================

/** Triangle / Archer — flèche stylisée */
export function PixelTriangle({ size = 32, className }: PixelProps) {
    return (
        <svg width={size} height={size} viewBox="0 0 16 16" className={className} style={{ imageRendering: 'pixelated' }}>
            {/* Pointe */}
            <P x={7} y={1} fill="#c9a84c" />
            <P x={8} y={1} fill="#c9a84c" />
            {/* Corps */}
            <P x={6} y={3} fill="#c9a84c" />
            <P x={7} y={3} fill="#e8c96d" />
            <P x={8} y={3} fill="#e8c96d" />
            <P x={9} y={3} fill="#c9a84c" />
            <P x={5} y={5} fill="#c9a84c" />
            <P x={6} y={5} fill="#e8c96d" />
            <P x={7} y={5} fill="#f0d878" />
            <P x={8} y={5} fill="#f0d878" />
            <P x={9} y={5} fill="#e8c96d" />
            <P x={10} y={5} fill="#c9a84c" />
            <P x={4} y={7} fill="#c9a84c" />
            <P x={5} y={7} fill="#e8c96d" />
            <P x={6} y={7} fill="#f0d878" />
            <P x={7} y={7} fill="#f0d878" />
            <P x={8} y={7} fill="#f0d878" />
            <P x={9} y={7} fill="#f0d878" />
            <P x={10} y={7} fill="#e8c96d" />
            <P x={11} y={7} fill="#c9a84c" />
            {/* Base */}
            <P x={3} y={9} fill="#a88a30" />
            <P x={4} y={9} fill="#c9a84c" />
            <P x={5} y={9} fill="#e8c96d" />
            <P x={6} y={9} fill="#e8c96d" />
            <P x={7} y={9} fill="#e8c96d" />
            <P x={8} y={9} fill="#e8c96d" />
            <P x={9} y={9} fill="#e8c96d" />
            <P x={10} y={9} fill="#e8c96d" />
            <P x={11} y={9} fill="#c9a84c" />
            <P x={12} y={9} fill="#a88a30" />
            {/* Flèche dessous */}
            <P x={7} y={11} fill="#8b6914" />
            <P x={8} y={11} fill="#8b6914" />
            <P x={7} y={12} fill="#5a3d2b" />
            <P x={8} y={12} fill="#5a3d2b" />
            <P x={7} y={13} fill="#3d2b1f" />
            <P x={8} y={13} fill="#3d2b1f" />
        </svg>
    );
}

/** Cercle / Catapulte — boulet stylisé */
export function PixelCercle({ size = 32, className }: PixelProps) {
    return (
        <svg width={size} height={size} viewBox="0 0 16 16" className={className} style={{ imageRendering: 'pixelated' }}>
            {/* Rangée du haut */}
            <P x={5} y={3} fill="#4a6fa5" />
            <P x={6} y={3} fill="#5a8abf" />
            <P x={7} y={3} fill="#5a8abf" />
            <P x={8} y={3} fill="#5a8abf" />
            <P x={9} y={3} fill="#5a8abf" />
            <P x={10} y={3} fill="#4a6fa5" />
            {/* Rangée 2 */}
            <P x={4} y={4} fill="#4a6fa5" />
            <P x={5} y={4} fill="#6a9fd4" />
            <P x={6} y={4} fill="#7ab4e8" />
            <P x={7} y={4} fill="#8ac4f0" />
            <P x={8} y={4} fill="#7ab4e8" />
            <P x={9} y={4} fill="#6a9fd4" />
            <P x={10} y={4} fill="#5a8abf" />
            <P x={11} y={4} fill="#4a6fa5" />
            {/* Rangées centrales */}
            <P x={3} y={5} fill="#4a6fa5" />
            <P x={4} y={5} fill="#6a9fd4" />
            <P x={5} y={5} fill="#7ab4e8" />
            <P x={6} y={5} fill="#8ac4f0" />
            <P x={7} y={5} fill="#9ad4ff" />
            <P x={8} y={5} fill="#8ac4f0" />
            <P x={9} y={5} fill="#7ab4e8" />
            <P x={10} y={5} fill="#6a9fd4" />
            <P x={11} y={5} fill="#5a8abf" />
            <P x={12} y={5} fill="#4a6fa5" />
            {/* Centre */}
            <P x={3} y={6} fill="#3a5a8a" />
            <P x={4} y={6} fill="#5a8abf" />
            <P x={5} y={6} fill="#6a9fd4" />
            <P x={6} y={6} fill="#7ab4e8" />
            <P x={7} y={6} fill="#8ac4f0" />
            <P x={8} y={6} fill="#7ab4e8" />
            <P x={9} y={6} fill="#6a9fd4" />
            <P x={10} y={6} fill="#5a8abf" />
            <P x={11} y={6} fill="#4a6fa5" />
            <P x={12} y={6} fill="#3a5a8a" />
            {/* Rangées basses (miroir) */}
            <P x={3} y={7} fill="#3a5a8a" />
            <P x={4} y={7} fill="#4a6fa5" />
            <P x={5} y={7} fill="#5a8abf" />
            <P x={6} y={7} fill="#6a9fd4" />
            <P x={7} y={7} fill="#7ab4e8" />
            <P x={8} y={7} fill="#6a9fd4" />
            <P x={9} y={7} fill="#5a8abf" />
            <P x={10} y={7} fill="#4a6fa5" />
            <P x={11} y={7} fill="#3a5a8a" />
            <P x={12} y={7} fill="#2a4a7a" />
            <P x={3} y={8} fill="#2a4a7a" />
            <P x={4} y={8} fill="#3a5a8a" />
            <P x={5} y={8} fill="#4a6fa5" />
            <P x={6} y={8} fill="#5a8abf" />
            <P x={7} y={8} fill="#6a9fd4" />
            <P x={8} y={8} fill="#5a8abf" />
            <P x={9} y={8} fill="#4a6fa5" />
            <P x={10} y={8} fill="#3a5a8a" />
            <P x={11} y={8} fill="#2a4a7a" />
            {/* Bas */}
            <P x={4} y={9} fill="#2a4a7a" />
            <P x={5} y={9} fill="#3a5a8a" />
            <P x={6} y={9} fill="#4a6fa5" />
            <P x={7} y={9} fill="#5a8abf" />
            <P x={8} y={9} fill="#4a6fa5" />
            <P x={9} y={9} fill="#3a5a8a" />
            <P x={10} y={9} fill="#2a4a7a" />
            <P x={5} y={10} fill="#2a4a7a" />
            <P x={6} y={10} fill="#3a5a8a" />
            <P x={7} y={10} fill="#3a5a8a" />
            <P x={8} y={10} fill="#3a5a8a" />
            <P x={9} y={10} fill="#2a4a7a" />
        </svg>
    );
}

/** Rectangle / Muraille — mur de briques */
export function PixelRectangle({ size = 32, className }: PixelProps) {
    return (
        <svg width={size} height={size} viewBox="0 0 16 16" className={className} style={{ imageRendering: 'pixelated' }}>
            {/* Créneaux */}
            <P x={2} y={2} fill="#8a7a5a" />
            <P x={3} y={2} fill="#8a7a5a" />
            <P x={6} y={2} fill="#8a7a5a" />
            <P x={7} y={2} fill="#8a7a5a" />
            <P x={8} y={2} fill="#8a7a5a" />
            <P x={9} y={2} fill="#8a7a5a" />
            <P x={12} y={2} fill="#8a7a5a" />
            <P x={13} y={2} fill="#8a7a5a" />
            {/* Rang haut */}
            {[2,3,4,5,6,7,8,9,10,11,12,13].map(x => <P key={`r3-${x}`} x={x} y={3} fill="#9a8a6a" />)}
            {/* Briques rang 1 */}
            {[2,3,4].map(x => <P key={`b4a-${x}`} x={x} y={4} fill="#b8a67a" />)}
            <P x={5} y={4} fill="#6a5a3a" />
            {[6,7,8,9].map(x => <P key={`b4b-${x}`} x={x} y={4} fill="#b8a67a" />)}
            <P x={10} y={4} fill="#6a5a3a" />
            {[11,12,13].map(x => <P key={`b4c-${x}`} x={x} y={4} fill="#b8a67a" />)}
            {/* Briques rang 2 */}
            {[2,3,4].map(x => <P key={`b5a-${x}`} x={x} y={5} fill="#a89468" />)}
            <P x={5} y={5} fill="#6a5a3a" />
            {[6,7,8,9].map(x => <P key={`b5b-${x}`} x={x} y={5} fill="#a89468" />)}
            <P x={10} y={5} fill="#6a5a3a" />
            {[11,12,13].map(x => <P key={`b5c-${x}`} x={x} y={5} fill="#a89468" />)}
            {/* Joint */}
            {[2,3,4,5,6,7,8,9,10,11,12,13].map(x => <P key={`j6-${x}`} x={x} y={6} fill="#6a5a3a" />)}
            {/* Briques rang 3 (décalé) */}
            {[2,3].map(x => <P key={`b7a-${x}`} x={x} y={7} fill="#b8a67a" />)}
            <P x={4} y={7} fill="#6a5a3a" />
            {[5,6,7,8].map(x => <P key={`b7b-${x}`} x={x} y={7} fill="#b8a67a" />)}
            <P x={9} y={7} fill="#6a5a3a" />
            {[10,11,12,13].map(x => <P key={`b7c-${x}`} x={x} y={7} fill="#b8a67a" />)}
            {/* Briques rang 4 */}
            {[2,3].map(x => <P key={`b8a-${x}`} x={x} y={8} fill="#a89468" />)}
            <P x={4} y={8} fill="#6a5a3a" />
            {[5,6,7,8].map(x => <P key={`b8b-${x}`} x={x} y={8} fill="#a89468" />)}
            <P x={9} y={8} fill="#6a5a3a" />
            {[10,11,12,13].map(x => <P key={`b8c-${x}`} x={x} y={8} fill="#a89468" />)}
            {/* Joint */}
            {[2,3,4,5,6,7,8,9,10,11,12,13].map(x => <P key={`j9-${x}`} x={x} y={9} fill="#6a5a3a" />)}
            {/* Briques rang 5 */}
            {[2,3,4].map(x => <P key={`b10a-${x}`} x={x} y={10} fill="#b8a67a" />)}
            <P x={5} y={10} fill="#6a5a3a" />
            {[6,7,8,9].map(x => <P key={`b10b-${x}`} x={x} y={10} fill="#b8a67a" />)}
            <P x={10} y={10} fill="#6a5a3a" />
            {[11,12,13].map(x => <P key={`b10c-${x}`} x={x} y={10} fill="#b8a67a" />)}
            {/* Base */}
            {[2,3,4,5,6,7,8,9,10,11,12,13].map(x => <P key={`b11-${x}`} x={x} y={11} fill="#7a6a4a" />)}
            {[2,3,4,5,6,7,8,9,10,11,12,13].map(x => <P key={`b12-${x}`} x={x} y={12} fill="#5a4a2a" />)}
        </svg>
    );
}

// ============================================
// TOURELLE
// ============================================

/** Tour de guet avec créneaux */
export function PixelTourelle({ size = 32, className }: PixelProps) {
    return (
        <svg width={size} height={size} viewBox="0 0 16 16" className={className} style={{ imageRendering: 'pixelated' }}>
            {/* Drapeau */}
            <P x={8} y={0} fill="#8b1a1a" />
            <P x={9} y={0} fill="#a52020" />
            <P x={10} y={0} fill="#8b1a1a" />
            <P x={8} y={1} fill="#a52020" />
            <P x={9} y={1} fill="#c42828" />
            <P x={7} y={1} fill="#5a4a2a" />
            <P x={7} y={2} fill="#5a4a2a" />
            {/* Créneaux */}
            <P x={4} y={3} fill="#8a7a5a" />
            <P x={5} y={3} fill="#8a7a5a" />
            <P x={7} y={3} fill="#9a8a6a" />
            <P x={8} y={3} fill="#9a8a6a" />
            <P x={10} y={3} fill="#8a7a5a" />
            <P x={11} y={3} fill="#8a7a5a" />
            {/* Toit */}
            {[4,5,6,7,8,9,10,11].map(x => <P key={`t4-${x}`} x={x} y={4} fill="#7a6a4a" />)}
            {/* Corps */}
            {[5,6,7,8,9,10].map(x => <P key={`c5-${x}`} x={x} y={5} fill="#b8a67a" />)}
            {[5,6].map(x => <P key={`c6a-${x}`} x={x} y={6} fill="#a89468" />)}
            <P x={7} y={6} fill="#2a1f0f" />
            <P x={8} y={6} fill="#2a1f0f" />
            {[9,10].map(x => <P key={`c6b-${x}`} x={x} y={6} fill="#a89468" />)}
            {[5,6,7,8,9,10].map(x => <P key={`c7-${x}`} x={x} y={7} fill="#b8a67a" />)}
            {[5,6,7,8,9,10].map(x => <P key={`c8-${x}`} x={x} y={8} fill="#a89468" />)}
            {[5,6,7,8,9,10].map(x => <P key={`c9-${x}`} x={x} y={9} fill="#b8a67a" />)}
            {/* Porte */}
            {[5,6].map(x => <P key={`c10a-${x}`} x={x} y={10} fill="#a89468" />)}
            <P x={7} y={10} fill="#3d2b1f" />
            <P x={8} y={10} fill="#3d2b1f" />
            {[9,10].map(x => <P key={`c10b-${x}`} x={x} y={10} fill="#a89468" />)}
            {[5,6].map(x => <P key={`c11a-${x}`} x={x} y={11} fill="#9a8a6a" />)}
            <P x={7} y={11} fill="#3d2b1f" />
            <P x={8} y={11} fill="#3d2b1f" />
            {[9,10].map(x => <P key={`c11b-${x}`} x={x} y={11} fill="#9a8a6a" />)}
            {/* Base élargie */}
            {[4,5,6,7,8,9,10,11].map(x => <P key={`b12-${x}`} x={x} y={12} fill="#7a6a4a" />)}
            {[3,4,5,6,7,8,9,10,11,12].map(x => <P key={`b13-${x}`} x={x} y={13} fill="#5a4a2a" />)}
        </svg>
    );
}

// ============================================
// DÉCOR
// ============================================

/** Torche animée (flamme en CSS) */
export function PixelTorch({ size = 32, className }: PixelProps) {
    return (
        <div className={`relative inline-block ${className}`} style={{ width: size, height: size }}>
            <svg width={size} height={size} viewBox="0 0 16 16" style={{ imageRendering: 'pixelated' }}>
                {/* Flamme */}
                <P x={7} y={1} fill="#ff6b00" />
                <P x={6} y={2} fill="#ff8c00" />
                <P x={7} y={2} fill="#ffcc00" />
                <P x={8} y={2} fill="#ff8c00" />
                <P x={6} y={3} fill="#ff6b00" />
                <P x={7} y={3} fill="#ffcc00" />
                <P x={8} y={3} fill="#ffaa00" />
                <P x={7} y={4} fill="#ff6b00" />
                <P x={8} y={4} fill="#ff4500" />
                {/* Support */}
                <P x={7} y={5} fill="#5a4a2a" />
                <P x={8} y={5} fill="#5a4a2a" />
                <P x={7} y={6} fill="#3d2b1f" />
                <P x={8} y={6} fill="#5a4a2a" />
                {[7,8].map(x => <P key={`s7-${x}`} x={x} y={7} fill="#3d2b1f" />)}
                {[7,8].map(x => <P key={`s8-${x}`} x={x} y={8} fill="#3d2b1f" />)}
                {[7,8].map(x => <P key={`s9-${x}`} x={x} y={9} fill="#2a1f0f" />)}
                {[7,8].map(x => <P key={`s10-${x}`} x={x} y={10} fill="#2a1f0f" />)}
                {[7,8].map(x => <P key={`s11-${x}`} x={x} y={11} fill="#2a1f0f" />)}
                {/* Socle */}
                {[6,7,8,9].map(x => <P key={`s12-${x}`} x={x} y={12} fill="#4a4a5a" />)}
                {[5,6,7,8,9,10].map(x => <P key={`s13-${x}`} x={x} y={13} fill="#3a3a48" />)}
            </svg>
            {/* Lueur */}
            <div className="absolute top-0 left-1/2 -translate-x-1/2 w-6 h-6 bg-[#ff8c00]/20 rounded-full blur-md animate-torch" />
        </div>
    );
}

/** Bannière / drapeau */
export function PixelBanner({ size = 32, className, color = '#8b1a1a' }: PixelProps & { color?: string }) {
    const light = color === '#8b1a1a' ? '#c42828' : '#e8c96d';
    return (
        <svg width={size} height={size} viewBox="0 0 16 16" className={`${className} animate-banner`} style={{ imageRendering: 'pixelated' }}>
            {/* Mat */}
            <P x={3} y={1} fill="#5a4a2a" />
            <P x={3} y={2} fill="#5a4a2a" />
            {[3].map(y => Array.from({length: 12}, (_, i) => <P key={`m-${y}-${i}`} x={3} y={y + i} fill="#3d2b1f" />))}
            {/* Tissu */}
            {[4,5,6,7,8,9,10].map(x => <P key={`b2-${x}`} x={x} y={2} fill={color} />)}
            {[4,5,6,7,8,9,10,11].map(x => <P key={`b3-${x}`} x={x} y={3} fill={color} />)}
            {[4,5,6,7].map(x => <P key={`b3l-${x}`} x={x} y={3} fill={light} />)}
            {[4,5,6,7,8,9,10,11].map(x => <P key={`b4-${x}`} x={x} y={4} fill={color} />)}
            {[4,5,6,7,8,9,10].map(x => <P key={`b5-${x}`} x={x} y={5} fill={color} />)}
            {[4,5,6,7,8,9].map(x => <P key={`b6-${x}`} x={x} y={6} fill={color} />)}
            {/* Pointe */}
            {[4,5,6,7,8].map(x => <P key={`b7-${x}`} x={x} y={7} fill={color} />)}
            {[5,6,7].map(x => <P key={`b8-${x}`} x={x} y={8} fill={color} />)}
            <P x={6} y={9} fill={color} />
        </svg>
    );
}

/** Bouclier / blason */
export function PixelShield({ size = 32, className }: PixelProps) {
    return (
        <svg width={size} height={size} viewBox="0 0 16 16" className={className} style={{ imageRendering: 'pixelated' }}>
            {/* Bord haut */}
            {[5,6,7,8,9,10].map(x => <P key={`s2-${x}`} x={x} y={2} fill="#c9a84c" />)}
            {/* Corps */}
            <P x={4} y={3} fill="#c9a84c" />
            {[5,6,7,8,9,10].map(x => <P key={`s3-${x}`} x={x} y={3} fill="#1a1a22" />)}
            <P x={11} y={3} fill="#c9a84c" />
            <P x={3} y={4} fill="#c9a84c" />
            {[4,5,6].map(x => <P key={`s4a-${x}`} x={x} y={4} fill="#1a1a22" />)}
            <P x={7} y={4} fill="#c9a84c" />
            <P x={8} y={4} fill="#c9a84c" />
            {[9,10,11].map(x => <P key={`s4b-${x}`} x={x} y={4} fill="#1a1a22" />)}
            <P x={12} y={4} fill="#c9a84c" />
            {/* Milieu */}
            <P x={3} y={5} fill="#c9a84c" />
            {[4,5,6].map(x => <P key={`s5a-${x}`} x={x} y={5} fill="#1a1a22" />)}
            <P x={7} y={5} fill="#8b1a1a" />
            <P x={8} y={5} fill="#8b1a1a" />
            {[9,10,11].map(x => <P key={`s5b-${x}`} x={x} y={5} fill="#1a1a22" />)}
            <P x={12} y={5} fill="#c9a84c" />
            <P x={3} y={6} fill="#a88a30" />
            {[4,5,6].map(x => <P key={`s6a-${x}`} x={x} y={6} fill="#1a1a22" />)}
            <P x={7} y={6} fill="#a52020" />
            <P x={8} y={6} fill="#a52020" />
            {[9,10,11].map(x => <P key={`s6b-${x}`} x={x} y={6} fill="#1a1a22" />)}
            <P x={12} y={6} fill="#a88a30" />
            {/* Bas */}
            <P x={4} y={7} fill="#a88a30" />
            {[5,6,7,8,9,10].map(x => <P key={`s7-${x}`} x={x} y={7} fill="#1a1a22" />)}
            <P x={11} y={7} fill="#a88a30" />
            <P x={5} y={8} fill="#a88a30" />
            {[6,7,8,9].map(x => <P key={`s8-${x}`} x={x} y={8} fill="#1a1a22" />)}
            <P x={10} y={8} fill="#a88a30" />
            <P x={6} y={9} fill="#8a7a5a" />
            <P x={7} y={9} fill="#1a1a22" />
            <P x={8} y={9} fill="#1a1a22" />
            <P x={9} y={9} fill="#8a7a5a" />
            <P x={7} y={10} fill="#8a7a5a" />
            <P x={8} y={10} fill="#8a7a5a" />
        </svg>
    );
}

/** Pièce d'or */
export function PixelCoin({ size = 16, className }: PixelProps) {
    return (
        <svg width={size} height={size} viewBox="0 0 8 8" className={className} style={{ imageRendering: 'pixelated' }}>
            <P x={2} y={1} fill="#c9a84c" />
            <P x={3} y={1} fill="#c9a84c" />
            <P x={4} y={1} fill="#c9a84c" />
            <P x={5} y={1} fill="#c9a84c" />
            <P x={1} y={2} fill="#c9a84c" />
            <P x={2} y={2} fill="#e8c96d" />
            <P x={3} y={2} fill="#f0d878" />
            <P x={4} y={2} fill="#e8c96d" />
            <P x={5} y={2} fill="#c9a84c" />
            <P x={6} y={2} fill="#a88a30" />
            <P x={1} y={3} fill="#c9a84c" />
            <P x={2} y={3} fill="#f0d878" />
            <P x={3} y={3} fill="#c9a84c" />
            <P x={4} y={3} fill="#f0d878" />
            <P x={5} y={3} fill="#e8c96d" />
            <P x={6} y={3} fill="#a88a30" />
            <P x={1} y={4} fill="#a88a30" />
            <P x={2} y={4} fill="#e8c96d" />
            <P x={3} y={4} fill="#f0d878" />
            <P x={4} y={4} fill="#e8c96d" />
            <P x={5} y={4} fill="#c9a84c" />
            <P x={6} y={4} fill="#8a7a5a" />
            <P x={1} y={5} fill="#a88a30" />
            <P x={2} y={5} fill="#c9a84c" />
            <P x={3} y={5} fill="#e8c96d" />
            <P x={4} y={5} fill="#c9a84c" />
            <P x={5} y={5} fill="#a88a30" />
            <P x={6} y={5} fill="#8a7a5a" />
            <P x={2} y={6} fill="#8a7a5a" />
            <P x={3} y={6} fill="#a88a30" />
            <P x={4} y={6} fill="#a88a30" />
            <P x={5} y={6} fill="#8a7a5a" />
        </svg>
    );
}

// ============================================
// BORDURE PIXEL ART
// ============================================

/** Bordure horizontale pixel art (haut ou bas) */
export function PixelBorder({ width = '100%', className }: { width?: string | number; className?: string }) {
    return (
        <div className={className} style={{ width, height: 8, overflow: 'hidden' }}>
            <svg width="100%" height="8" preserveAspectRatio="none" viewBox="0 0 64 4" style={{ imageRendering: 'pixelated' }}>
                {/* Pattern répété */}
                {Array.from({ length: 32 }, (_, i) => (
                    <g key={i}>
                        <rect x={i * 2} y={0} width={1} height={1} fill="#5a4a2a" />
                        <rect x={i * 2 + 1} y={0} width={1} height={1} fill="#3d2b1f" />
                        <rect x={i * 2} y={1} width={2} height={1} fill="#c9a84c" />
                        <rect x={i * 2} y={2} width={1} height={1} fill="#a88a30" />
                        <rect x={i * 2 + 1} y={2} width={1} height={1} fill="#c9a84c" />
                        <rect x={i * 2} y={3} width={1} height={1} fill="#3d2b1f" />
                        <rect x={i * 2 + 1} y={3} width={1} height={1} fill="#5a4a2a" />
                    </g>
                ))}
            </svg>
        </div>
    );
}

/** Cadre pixel art complet autour d'un contenu */
export function PixelFrame({ children, className }: { children: React.ReactNode; className?: string }) {
    return (
        <div className={`relative ${className}`}>
            {/* Bordures */}
            <PixelBorder className="absolute top-0 left-0 right-0" />
            <PixelBorder className="absolute bottom-0 left-0 right-0 rotate-180" />
            {/* Contenu avec padding pour les bordures */}
            <div className="pt-3 pb-3">
                {children}
            </div>
        </div>
    );
}