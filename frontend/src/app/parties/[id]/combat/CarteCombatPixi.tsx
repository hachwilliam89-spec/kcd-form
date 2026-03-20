'use client';

import { useEffect, useRef } from 'react';
import { Tourelle } from '@/lib/api';
import { EnnemiEtat, MurailleEtat } from '@/lib/types';

const COLS = 10;
const ROWS = 7;
const CELL_SIZE = 80;
const WIDTH = COLS * CELL_SIZE;
const HEIGHT = ROWS * CELL_SIZE;

const FRAME_W = 64;
const FRAME_H = 64;

const CHEMIN_GRID: { col: number; row: number }[] = [
    { col: 1, row: 2 }, { col: 2, row: 2 }, { col: 3, row: 2 },
    { col: 4, row: 2 }, { col: 5, row: 2 },
    { col: 5, row: 3 }, { col: 5, row: 4 },
    { col: 4, row: 4 }, { col: 3, row: 4 }, { col: 2, row: 4 }, { col: 1, row: 4 },
    { col: 1, row: 5 }, { col: 1, row: 6 },
    { col: 2, row: 6 }, { col: 3, row: 6 }, { col: 4, row: 6 },
    { col: 5, row: 6 }, { col: 6, row: 6 }, { col: 7, row: 6 },
    { col: 8, row: 6 }, { col: 9, row: 6 },
];

const casesPlacement: Record<number, { col: number; row: number }> = {
    1: { col: 1, row: 1 }, 2: { col: 2, row: 1 }, 3: { col: 3, row: 1 }, 4: { col: 4, row: 1 },
    5: { col: 6, row: 2 }, 6: { col: 6, row: 3 }, 7: { col: 6, row: 4 },
    8: { col: 2, row: 5 }, 9: { col: 4, row: 5 }, 10: { col: 8, row: 5 },
};

const emplacementsMuraille: Record<number, { col: number; row: number }> = {
    3: { col: 3, row: 2 },
    6: { col: 5, row: 3 },
    9: { col: 2, row: 4 },
    14: { col: 3, row: 6 },
    18: { col: 7, row: 6 },
};

const estChemin = (col: number, row: number) =>
    CHEMIN_GRID.some(c => c.col === col && c.row === row);
const estMuraille = (col: number, row: number) =>
    Object.values(emplacementsMuraille).some(m => m.col === col && m.row === row);
const estTourelle = (col: number, row: number) =>
    Object.values(casesPlacement).some(t => t.col === col && t.row === row);

const toX = (col: number) => (col - 1) * CELL_SIZE + CELL_SIZE / 2;
const toY = (row: number) => (row - 1) * CELL_SIZE + CELL_SIZE / 2;

const ENEMY_SPRITES: Record<string, string> = {
    'Triangle': '/sprites/sheets/LanceCavalier/LanceCavalier_ShortHair_Red1.png',
    'Cercle': '/sprites/sheets/SpearFighter/SpearFighter_ShortHair_Red1.png',
    'Rectangle': '/sprites/sheets/AxeKnight/AxeKnight_Red.png',
};

interface Props {
    tourelles: Tourelle[];
    ennemis: EnnemiEtat[];
    murailles: MurailleEtat[];
    forteressePvActuels: number;
    forteressePvMax: number;
}

export default function CarteCombatPixi({ tourelles, ennemis, murailles, forteressePvActuels, forteressePvMax }: Props) {
    const canvasRef = useRef<HTMLDivElement>(null);
    const appRef = useRef<any>(null);
    const refs = useRef<Map<string, any>>(new Map());
    const enemyRefs = useRef<Map<number, { container: any; hpFg: any }>>(new Map());
    const prevPos = useRef<Map<number, number>>(new Map());

    useEffect(() => {
        const init = async () => {
            if (appRef.current) return;
            const PIXI = await import('pixi.js');

            const app = new PIXI.Application();
            await app.init({
                width: WIDTH,
                height: HEIGHT,
                background: 0x111820,
                preference: 'webgl',
            });
            appRef.current = app;
            canvasRef.current?.appendChild(app.canvas);

            // Précharger textures
            await Promise.all([
                PIXI.Assets.load('/sprites/sheets/Archer/Archer_Blue1.png'),
                PIXI.Assets.load('/sprites/sheets/Wizard/Wizard_Blue1.png'),
                PIXI.Assets.load('/sprites/sheets/LanceCavalier/LanceCavalier_ShortHair_Red1.png'),
                PIXI.Assets.load('/sprites/sheets/SpearFighter/SpearFighter_ShortHair_Red1.png'),
                PIXI.Assets.load('/sprites/sheets/AxeKnight/AxeKnight_Red.png'),
            ]);

            // === TERRAIN ===
            for (let row = 1; row <= ROWS; row++) {
                for (let col = 1; col <= COLS; col++) {
                    const isCit = col === 10 && row === 6;
                    const isPath = estChemin(col, row);
                    const isMur = estMuraille(col, row);
                    const isTour = estTourelle(col, row);
                    const x = (col - 1) * CELL_SIZE;
                    const y = (row - 1) * CELL_SIZE;

                    const cell = new PIXI.Graphics();
                    cell.rect(x, y, CELL_SIZE, CELL_SIZE);

                    if (isCit) {
                        cell.fill({ color: 0x8b1a1a });
                        cell.stroke({ color: 0xff3333, width: 2 });
                    } else if (isPath || isMur) {
                        cell.fill({ color: 0x3a2a10 });
                        cell.stroke({ color: 0x4a3a1a, width: 1 });
                    } else if (isTour) {
                        cell.fill({ color: 0x1a1a22 });
                        cell.stroke({ color: 0xc9a84c, width: 2 });
                    } else {
                        cell.fill({ color: 0x0d1a0d });
                        cell.stroke({ color: 0x1a2a1a, width: 1 });
                    }
                    app.stage.addChild(cell);
                }
            }

            // Citadelle (dessinée, pas emoji)
            const citG = new PIXI.Graphics();
            const cx = toX(10);
            const cy = toY(6);
            // Tour principale
            citG.rect(cx - 15, cy - 20, 30, 40);
            citG.fill({ color: 0xaa3333 });
            citG.stroke({ color: 0xff4444, width: 2 });
            // Crénaux
            citG.rect(cx - 20, cy - 25, 10, 10);
            citG.fill({ color: 0xaa3333 });
            citG.rect(cx + 10, cy - 25, 10, 10);
            citG.fill({ color: 0xaa3333 });
            // Porte
            citG.rect(cx - 6, cy + 5, 12, 15);
            citG.fill({ color: 0x441111 });
            app.stage.addChild(citG);

            // Label citadelle
            const citLabel = new PIXI.Text({ text: 'CITADELLE', style: { fontSize: 8, fill: 0xff6666, fontWeight: 'bold' } });
            citLabel.x = cx;
            citLabel.y = cy + 28;
            citLabel.anchor.set(0.5);
            app.stage.addChild(citLabel);

            // Barre PV citadelle
            const citHpBg = new PIXI.Graphics();
            citHpBg.rect(0, 0, 60, 5);
            citHpBg.fill({ color: 0x333333 });
            citHpBg.x = cx - 30;
            citHpBg.y = cy + 33;
            app.stage.addChild(citHpBg);

            const citHpFg = new PIXI.Graphics();
            citHpFg.rect(0, 0, 60, 5);
            citHpFg.fill({ color: 0xcc2222 });
            citHpFg.x = cx - 30;
            citHpFg.y = cy + 33;
            app.stage.addChild(citHpFg);
            refs.current.set('citHpFg', citHpFg);

            // Numéros cases tourelles
            for (const [num, pos] of Object.entries(casesPlacement)) {
                const label = new PIXI.Text({
                    text: num,
                    style: { fontSize: 12, fill: 0x555555 }
                });
                label.x = toX(pos.col);
                label.y = toY(pos.row);
                label.anchor.set(0.5);
                app.stage.addChild(label);
            }
        };

        init();
        return () => {
            if (appRef.current) { appRef.current.destroy(true); appRef.current = null; }
            if (canvasRef.current) canvasRef.current.innerHTML = '';
            refs.current.clear();
            enemyRefs.current.clear();
            prevPos.current.clear();
        };
    }, []);

    // Tourelles
    useEffect(() => {
        const update = async () => {
            const app = appRef.current;
            if (!app) return;
            const PIXI = await import('pixi.js');

            refs.current.forEach((s, k) => {
                if (k.startsWith('t-')) { app.stage.removeChild(s); s.destroy(); refs.current.delete(k); }
            });

            for (const t of tourelles) {
                const pos = casesPlacement[t.position];
                if (!pos) continue;

                const path = t.aoe ? '/sprites/sheets/Wizard/Wizard_Blue1.png' : '/sprites/sheets/Archer/Archer_Blue1.png';
                const tex = PIXI.Assets.get(path);
                if (!tex) continue;

                const frames: any[] = [];
                for (let i = 0; i < 4; i++) {
                    frames.push(new PIXI.Texture({ source: tex.source, frame: new PIXI.Rectangle(i * FRAME_W, 0, FRAME_W, FRAME_H) }));
                }

                const sprite = new PIXI.AnimatedSprite(frames);
                sprite.x = toX(pos.col);
                sprite.y = toY(pos.row);
                sprite.anchor.set(0.5);
                sprite.scale.set(1.8);
                sprite.animationSpeed = 0.06;
                sprite.play();
                app.stage.addChild(sprite);
                refs.current.set(`t-${t.id}`, sprite);
            }
        };
        update();
    }, [tourelles]);

    // Ennemis
    useEffect(() => {
        const update = async () => {
            const app = appRef.current;
            if (!app) return;
            const PIXI = await import('pixi.js');

            const ids = new Set(ennemis.map(e => e.id));

            enemyRefs.current.forEach((r, id) => {
                if (!ids.has(id)) {
                    app.stage.removeChild(r.container);
                    r.container.destroy({ children: true });
                    enemyRefs.current.delete(id);
                    prevPos.current.delete(id);
                }
            });

            for (const e of ennemis) {
                let r = enemyRefs.current.get(e.id);

                if (!e.vivant) {
                    if (r) r.container.visible = false;
                    continue;
                }

                const idx = Math.max(0, Math.min(e.position, CHEMIN_GRID.length - 1));
                const cell = CHEMIN_GRID[idx];
                const tx = toX(cell.col);
                const ty = toY(cell.row);

                if (!r) {
                    const path = ENEMY_SPRITES[e.type] || ENEMY_SPRITES['Triangle'];
                    const tex = PIXI.Assets.get(path);
                    if (!tex) continue;

                    const frames: any[] = [];
                    for (let i = 0; i < 4; i++) {
                        frames.push(new PIXI.Texture({ source: tex.source, frame: new PIXI.Rectangle(i * FRAME_W, 0, FRAME_W, FRAME_H) }));
                    }

                    const container = new PIXI.Container();
                    container.x = tx;
                    container.y = ty;

                    const sprite = new PIXI.AnimatedSprite(frames);
                    sprite.anchor.set(0.5);
                    sprite.scale.set(1.8);
                    sprite.animationSpeed = 0.1;
                    sprite.play();
                    container.addChild(sprite);

                    const barW = 50;
                    const hpBg = new PIXI.Graphics();
                    hpBg.rect(-barW / 2, -45, barW, 5);
                    hpBg.fill({ color: 0x333333 });
                    container.addChild(hpBg);

                    const hpFg = new PIXI.Graphics();
                    hpFg.rect(-barW / 2, -45, barW, 5);
                    hpFg.fill({ color: 0x22cc22 });
                    container.addChild(hpFg);

                    app.stage.addChild(container);
                    r = { container, hpFg };
                    enemyRefs.current.set(e.id, r);
                    prevPos.current.set(e.id, e.position);
                }

                const prev = prevPos.current.get(e.id) ?? 0;
                if (e.position >= prev) {
                    r.container.x = tx;
                    r.container.y = ty;
                    prevPos.current.set(e.id, e.position);
                }

                r.container.visible = true;
                const pct = e.pvMax > 0 ? e.pvActuels / e.pvMax : 0;
                r.hpFg.scale.x = pct;
                r.hpFg.tint = pct > 0.5 ? 0x22cc22 : pct > 0.25 ? 0xcccc22 : 0xcc2222;
            }
        };
        update();
    }, [ennemis]);

    // Murailles
    useEffect(() => {
        const update = async () => {
            const app = appRef.current;
            if (!app) return;
            const PIXI = await import('pixi.js');

            refs.current.forEach((s, k) => {
                if (k.startsWith('m-')) { app.stage.removeChild(s); s.destroy({ children: true }); refs.current.delete(k); }
            });

            for (const m of murailles) {
                if (m.detruite) continue;
                const entry = Object.entries(emplacementsMuraille).find(([p]) => Number(p) === m.position);
                if (!entry) continue;
                const { col, row } = entry[1];
                const pvPct = m.pvMax > 0 ? m.pvActuels / m.pvMax : 0;

                const container = new PIXI.Container();
                container.x = toX(col);
                container.y = toY(row);

                const block = new PIXI.Graphics();
                block.rect(-30, -30, 60, 60);
                block.fill({ color: pvPct > 0.5 ? 0x8b6914 : pvPct > 0.25 ? 0x886622 : 0x883322 });
                block.stroke({ color: 0xaa8833, width: 2 });
                container.addChild(block);

                // Briques
                const bricks = new PIXI.Graphics();
                bricks.rect(-20, -20, 18, 8); bricks.fill({ color: 0x6b5010 });
                bricks.rect(2, -20, 18, 8); bricks.fill({ color: 0x6b5010 });
                bricks.rect(-15, -10, 18, 8); bricks.fill({ color: 0x6b5010 });
                bricks.rect(5, -10, 18, 8); bricks.fill({ color: 0x6b5010 });
                bricks.rect(-20, 0, 18, 8); bricks.fill({ color: 0x6b5010 });
                bricks.rect(2, 0, 18, 8); bricks.fill({ color: 0x6b5010 });
                container.addChild(bricks);

                const barW = 50;
                const hpBg = new PIXI.Graphics();
                hpBg.rect(-barW / 2, 25, barW, 5);
                hpBg.fill({ color: 0x333333 });
                container.addChild(hpBg);

                const hpFg = new PIXI.Graphics();
                hpFg.rect(-barW / 2, 25, barW * pvPct, 5);
                hpFg.fill({ color: pvPct > 0.5 ? 0x3388ff : 0xcc2222 });
                container.addChild(hpFg);

                app.stage.addChild(container);
                refs.current.set(`m-${m.position}`, container);
            }
        };
        update();
    }, [murailles]);

    // PV citadelle
    useEffect(() => {
        const fg = refs.current.get('citHpFg');
        if (fg && forteressePvMax > 0) fg.scale.x = forteressePvActuels / forteressePvMax;
    }, [forteressePvActuels, forteressePvMax]);

    return (
        <div className="flex-1 min-h-0 rounded-xl overflow-hidden border border-[#2a3a2a] flex items-center justify-center bg-[#0a0a0f]">
            <div ref={canvasRef} />
        </div>
    );
}