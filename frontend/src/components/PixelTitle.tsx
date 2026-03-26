'use client';

/**
 * Titre "KCD FORMES" en pixel art SVG
 * Chaque lettre sur une grille 5x7 pixels, espacement 1px
 * Couleurs dorées pour matcher le thème
 */

interface Props {
    size?: number;
    className?: string;
}

const GOLD = '#f0d070';
const GOLD_MID = '#dcb464';
const GOLD_DARK = '#a88838';

// Chaque lettre = tableau de [x,y] sur une grille 5x7
const LETTERS: Record<string, [number, number][]> = {
    K: [
        [0,0],[0,1],[0,2],[0,3],[0,4],[0,5],[0,6],
        [3,0],[2,1],[1,2],[1,3],[1,4],[2,5],[3,6],
        [4,0],[3,1],[2,3],[3,5],[4,6],
    ],
    C: [
        [1,0],[2,0],[3,0],[4,0],
        [0,1],[0,2],[0,3],[0,4],[0,5],
        [1,6],[2,6],[3,6],[4,6],
    ],
    D: [
        [0,0],[0,1],[0,2],[0,3],[0,4],[0,5],[0,6],
        [1,0],[2,0],[3,0],
        [4,1],[4,2],[4,3],[4,4],[4,5],
        [1,6],[2,6],[3,6],
    ],
    F: [
        [0,0],[0,1],[0,2],[0,3],[0,4],[0,5],[0,6],
        [1,0],[2,0],[3,0],[4,0],
        [1,3],[2,3],[3,3],
    ],
    O: [
        [1,0],[2,0],[3,0],
        [0,1],[0,2],[0,3],[0,4],[0,5],
        [4,1],[4,2],[4,3],[4,4],[4,5],
        [1,6],[2,6],[3,6],
    ],
    R: [
        [0,0],[0,1],[0,2],[0,3],[0,4],[0,5],[0,6],
        [1,0],[2,0],[3,0],
        [4,1],[4,2],
        [1,3],[2,3],[3,3],
        [2,4],[3,5],[4,6],
    ],
    M: [
        [0,0],[0,1],[0,2],[0,3],[0,4],[0,5],[0,6],
        [4,0],[4,1],[4,2],[4,3],[4,4],[4,5],[4,6],
        [1,1],[3,1],
        [2,2],
    ],
    E: [
        [0,0],[0,1],[0,2],[0,3],[0,4],[0,5],[0,6],
        [1,0],[2,0],[3,0],[4,0],
        [1,3],[2,3],[3,3],
        [1,6],[2,6],[3,6],[4,6],
    ],
    S: [
        [1,0],[2,0],[3,0],[4,0],
        [0,1],[0,2],
        [1,3],[2,3],[3,3],
        [4,4],[4,5],
        [0,6],[1,6],[2,6],[3,6],
    ],
};

function PixelLetter({ letter, offsetX, color }: { letter: string; offsetX: number; color: string }) {
    const pixels = LETTERS[letter] || [];
    return (
        <>
            {pixels.map(([x, y], i) => (
                <rect
                    key={`${letter}-${i}`}
                    x={offsetX + x}
                    y={y}
                    width={1}
                    height={1}
                    fill={color}
                />
            ))}
        </>
    );
}

export default function PixelTitle({ size = 48, className }: Props) {
    // "KCD FORMES" = K C D [space] F O R M E S
    // Chaque lettre = 5 de large + 1 espace = 6 par lettre
    // "KCD" = 3 lettres = 18, espace = 3, "FORMES" = 6 lettres = 36
    // Total = 18 + 3 + 36 = 57

    const text1 = ['K', 'C', 'D'];
    const text2 = ['F', 'O', 'R', 'M', 'E', 'S'];
    const totalWidth = 59; // +2 for outline padding
    const totalHeight = 9; // +2 for outline padding

    const scale = size / totalHeight;
    const svgWidth = totalWidth * scale;

    // Generate outline pixels (1px border around each filled pixel)
    function getOutlinePixels(letters: string[], startX: number) {
        const filled = new Set<string>();
        const outline: [number, number][] = [];

        // Collect all filled positions
        letters.forEach((letter, i) => {
            const pixels = LETTERS[letter] || [];
            pixels.forEach(([x, y]) => {
                filled.add(`${startX + i * 6 + x},${y + 1}`);
            });
        });

        // For each filled pixel, check 8 neighbors
        filled.forEach(key => {
            const [cx, cy] = key.split(',').map(Number);
            for (let dx = -1; dx <= 1; dx++) {
                for (let dy = -1; dy <= 1; dy++) {
                    if (dx === 0 && dy === 0) continue;
                    const nk = `${cx + dx},${cy + dy}`;
                    if (!filled.has(nk)) {
                        outline.push([cx + dx, cy + dy]);
                    }
                }
            }
        });

        return outline;
    }

    const outline1 = getOutlinePixels(text1, 1);
    const outline2 = getOutlinePixels(text2, 22);

    return (
        <svg
            width={svgWidth}
            height={size}
            viewBox={`0 0 ${totalWidth} ${totalHeight}`}
            className={className}
            style={{ imageRendering: 'pixelated' }}
        >
            {/* Outline noir (contour de chaque lettre) */}
            {outline1.map(([x, y], i) => (
                <rect key={`o1-${i}`} x={x} y={y} width={1} height={1} fill="#1a0a00" />
            ))}
            {outline2.map(([x, y], i) => (
                <rect key={`o2-${i}`} x={x} y={y} width={1} height={1} fill="#1a0a00" />
            ))}

            {/* Texte principal (décalé de 1 pour l'outline) */}
            {text1.map((letter, i) => (
                <PixelLetter key={`t1-${i}`} letter={letter} offsetX={1 + i * 6} color={GOLD} />
            ))}
            {text2.map((letter, i) => (
                <PixelLetter key={`t2-${i}`} letter={letter} offsetX={22 + i * 6} color={GOLD_MID} />
            ))}

            {/* Reflet en haut (rangée 0 plus claire) */}
            {text1.map((letter, i) => {
                const pixels = LETTERS[letter] || [];
                return pixels
                    .filter(([, y]) => y === 0)
                    .map(([x], j) => (
                        <rect
                            key={`h1-${i}-${j}`}
                            x={1 + i * 6 + x}
                            y={1}
                            width={1}
                            height={1}
                            fill="#f8e898"
                        />
                    ));
            })}
            {text2.map((letter, i) => {
                const pixels = LETTERS[letter] || [];
                return pixels
                    .filter(([, y]) => y === 0)
                    .map(([x], j) => (
                        <rect
                            key={`h2-${i}-${j}`}
                            x={22 + i * 6 + x}
                            y={1}
                            width={1}
                            height={1}
                            fill="#f8e898"
                        />
                    ));
            })}
        </svg>
    );
}