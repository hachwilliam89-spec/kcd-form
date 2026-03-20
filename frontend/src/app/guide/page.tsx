'use client';

import { motion } from 'framer-motion';
import Link from 'next/link';
import { useState } from 'react';
import { ArrowLeft } from 'lucide-react';

const fadeUp = {
    initial: { opacity: 0, y: 30 },
    whileInView: { opacity: 1, y: 0 },
    viewport: { once: true, margin: '-50px' },
    transition: { duration: 0.6 },
};

const TOURELLES = [
    {
        emoji: '🏹', nom: 'Archer', forme: 'Triangle',
        couleur: '#e74c3c', border: 'border-red-700', bg: 'bg-red-950/20',
        role: 'DPS mono-cible',
        desc: 'Tire sur un seul ennemi à la fois avec des dégâts concentrés. Cadence rapide.',
        stats: { dps: 'Élevé', portee: '3 cases', cible: '1 ennemi', special: '—' },
        forces: ['Efficace contre l\'Infanterie (cible lente)'],
        faiblesses: ['Malus ×0.75 contre le Bélier (trop solide)'],
    },
    {
        emoji: '🪨', nom: 'Catapulte', forme: 'Cercle',
        couleur: '#3498db', border: 'border-blue-700', bg: 'bg-blue-950/20',
        role: 'DPS de zone (AoE)',
        desc: 'Attaque tous les ennemis dans sa zone circulaire. Cadence lente mais dévastatrice.',
        stats: { dps: 'Moyen', portee: '3 cases', cible: 'Zone', special: 'AoE' },
        forces: ['Bonus ×1.25 contre le Bélier (cible lente et large)'],
        faiblesses: ['Dégâts répartis, moins efficace en mono-cible'],
    },
    {
        emoji: '🧱', nom: 'Muraille', forme: 'Rectangle',
        couleur: '#8b6914', border: 'border-yellow-800', bg: 'bg-yellow-950/20',
        role: 'Bloqueur (obstacle)',
        desc: 'Placée sur le chemin, bloque les ennemis. Ils doivent la détruire pour avancer.',
        stats: { dps: '0', portee: 'Sur le chemin', cible: '—', special: 'Bloque' },
        forces: ['Bloque tous les types d\'ennemis', 'Donne du temps aux tourelles'],
        faiblesses: ['Le Bélier inflige ×2 dégâts contre elle'],
    },
];

const ENNEMIS = [
    {
        emoji: '⚔️', nom: 'Cavalerie', forme: 'Triangle',
        couleur: '#e74c3c', border: 'border-red-700', bg: 'bg-red-950/20',
        desc: 'Rapide et fragile. Fonce sur le chemin et traverse vite les zones d\'attaque.',
        stats: { pv: 'Faibles', vitesse: 'Rapide (3.0)', degats: '10 / attaque' },
        danger: 'Arrive vite à la forteresse si non stoppée',
        contreParQuoi: '🪨 Catapulte (zone large) et 🧱 Muraille (le bloque)',
    },
    {
        emoji: '🛡️', nom: 'Infanterie', forme: 'Cercle',
        couleur: '#3498db', border: 'border-blue-700', bg: 'bg-blue-950/20',
        desc: 'Tank lent avec beaucoup de PV. Avance comme un mur de boucliers en groupe.',
        stats: { pv: 'Élevés', vitesse: 'Lent (1.0)', degats: '15 / attaque' },
        danger: 'Absorbe énormément de dégâts, dur à tuer',
        contreParQuoi: '🏹 Archer (dégâts concentrés mono-cible)',
    },
    {
        emoji: '🐏', nom: 'Bélier', forme: 'Rectangle',
        couleur: '#8b6914', border: 'border-yellow-800', bg: 'bg-yellow-950/20',
        desc: 'Très solide et très lent. Dégâts doublés contre toutes les structures rectangulaires.',
        stats: { pv: 'Très élevés', vitesse: 'Très lent (1.0)', degats: '40 / attaque' },
        danger: '×2 dégâts contre murailles ET forteresse',
        contreParQuoi: '🪨 Catapulte (bonus ×1.25, cible lente)',
    },
];

const MATRICE = [
    { ennemi: '⚔️ Cavalerie', archer: { text: '❌ Difficile', desc: 'Trop rapide' }, catapulte: { text: '✅ Efficace', desc: 'Zone large' }, muraille: { text: '✅ Bloque', desc: 'Vitesse inutile' } },
    { ennemi: '🛡️ Infanterie', archer: { text: '✅ Efficace', desc: 'Cible facile' }, catapulte: { text: '⚠️ Moyen', desc: 'Dégâts répartis' }, muraille: { text: '⚠️ Ralentit', desc: 'Ne le tue pas' } },
    { ennemi: '🐏 Bélier', archer: { text: '⚠️ ×0.75', desc: 'Trop solide' }, catapulte: { text: '✅ ×1.25', desc: 'Cible lente' }, muraille: { text: '💀 ×2 dégâts', desc: 'La détruit vite' } },
];

function CarteUnite({ emoji, nom, forme, couleur, border, bg, role, desc, stats, children }: {
    emoji: string; nom: string; forme: string; couleur: string;
    border: string; bg: string; role?: string; desc: string;
    stats: Record<string, string>; children: React.ReactNode;
}) {
    const statEntries = Object.entries(stats);

    return (
        <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.4 }}
            className={`${bg} border ${border} rounded-2xl overflow-hidden w-full`}>

            <div className="px-8 pt-8 pb-6 text-center">
                <span className="text-5xl block mb-4">{emoji}</span>
                <div className="flex items-center justify-center gap-3 flex-wrap mb-3">
                    <h3 className="text-2xl font-black text-white" style={{ fontFamily: 'var(--font-cinzel)' }}>
                        {nom}
                    </h3>
                    <span className="text-xs px-2 py-1 rounded-full bg-[#2a2a35] text-gray-400">{forme}</span>
                    {role && (
                        <span className="text-xs px-2 py-1 rounded-full text-black font-bold" style={{ backgroundColor: couleur }}>
                            {role}
                        </span>
                    )}
                </div>
                <p className="text-gray-400 max-w-md mx-auto" style={{ fontFamily: 'var(--font-crimson)' }}>
                    {desc}
                </p>
            </div>

            <div className="px-8 pb-6">
                <div className="grid gap-3" style={{ gridTemplateColumns: `repeat(${statEntries.length}, 1fr)` }}>
                    {statEntries.map(([key, val]) => (
                        <div key={key} className="bg-[#0a0a0f] border border-[#2a2a35] rounded-lg p-4 text-center">
                            <p className="text-[#c9a84c] font-bold">{val}</p>
                            <p className="text-[10px] text-gray-500 uppercase tracking-widest mt-1">{key}</p>
                        </div>
                    ))}
                </div>
            </div>

            <div className="px-8 pb-8">
                {children}
            </div>
        </motion.div>
    );
}

export default function GuidePage() {
    const [tourelleActive, setTourelleActive] = useState(0);
    const [ennemiActif, setEnnemiActif] = useState(0);

    const t = TOURELLES[tourelleActive];
    const e = ENNEMIS[ennemiActif];

    return (
        <main className="min-h-screen bg-[#0a0a0f] pb-20 flex flex-col items-center">

            {/* Header fixe */}
            <div className="sticky top-0 z-50 w-full bg-[#0a0a0f]/90 backdrop-blur border-b border-[#c9a84c]/20">
                <div className="w-full max-w-3xl mx-auto flex justify-between items-center px-6 py-4">
                    <Link href="/" className="flex items-center gap-2 text-[#8a7a5a] hover:text-[#c9a84c] transition-colors">
                        <ArrowLeft className="w-4 h-4" />
                        <span className="text-sm uppercase tracking-widest" style={{ fontFamily: 'var(--font-cinzel)' }}>Accueil</span>
                    </Link>

                </div>
            </div>

            {/* ===== VOS DÉFENSES ===== */}
            <section className="pt-16 pb-12 px-6 w-full flex justify-center">
                <div className="w-full max-w-3xl">
                    <motion.div {...fadeUp} className="text-center mb-10">
                        <h2 className="text-3xl font-black text-[#c9a84c] tracking-widest uppercase mb-3"
                            style={{ fontFamily: 'var(--font-cinzel)' }}>
                            🛡️ Vos Défenses
                        </h2>
                        <p className="text-gray-400" style={{ fontFamily: 'var(--font-crimson)' }}>
                            Chaque forme géométrique donne un type d'attaque unique à vos tourelles
                        </p>
                    </motion.div>

                    <motion.div {...fadeUp} className="flex justify-center gap-3 mb-8">
                        {TOURELLES.map((item, i) => (
                            <button
                                key={i}
                                onClick={() => setTourelleActive(i)}
                                className={`px-5 py-3 rounded-lg border-2 transition-all text-sm font-bold uppercase tracking-widest ${
                                    tourelleActive === i
                                        ? `${item.border} bg-[#2a2a35] text-white`
                                        : 'border-[#3a3a48] bg-[#1a1a22] text-gray-500 hover:border-gray-600'
                                }`}
                                style={{ fontFamily: 'var(--font-cinzel)' }}>
                                {item.emoji} {item.nom}
                            </button>
                        ))}
                    </motion.div>

                    <CarteUnite key={`t-${tourelleActive}`} {...t} stats={t.stats}>
                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <p className="text-green-400 text-xs uppercase tracking-widest mb-2 font-bold">✅ Forces</p>
                                {t.forces.map((f, i) => (
                                    <p key={i} className="text-gray-400 text-sm mb-1">• {f}</p>
                                ))}
                            </div>
                            <div>
                                <p className="text-red-400 text-xs uppercase tracking-widest mb-2 font-bold">❌ Faiblesses</p>
                                {t.faiblesses.map((f, i) => (
                                    <p key={i} className="text-gray-400 text-sm mb-1">• {f}</p>
                                ))}
                            </div>
                        </div>
                    </CarteUnite>
                </div>
            </section>

            {/* ===== LES ENVAHISSEURS ===== */}
            <section className="py-12 px-6 w-full flex justify-center">
                <div className="w-full max-w-3xl">
                    <motion.div {...fadeUp} className="text-center mb-10">
                        <h2 className="text-3xl font-black text-red-500 tracking-widest uppercase mb-3"
                            style={{ fontFamily: 'var(--font-cinzel)' }}>
                            👾 Les Envahisseurs
                        </h2>
                        <p className="text-gray-400" style={{ fontFamily: 'var(--font-crimson)' }}>
                            Trois types d'ennemis aux comportements distincts — adaptez votre stratégie
                        </p>
                    </motion.div>

                    <motion.div {...fadeUp} className="flex justify-center gap-3 mb-8">
                        {ENNEMIS.map((item, i) => (
                            <button
                                key={i}
                                onClick={() => setEnnemiActif(i)}
                                className={`px-5 py-3 rounded-lg border-2 transition-all text-sm font-bold uppercase tracking-widest ${
                                    ennemiActif === i
                                        ? `${item.border} bg-[#2a2a35] text-white`
                                        : 'border-[#3a3a48] bg-[#1a1a22] text-gray-500 hover:border-gray-600'
                                }`}
                                style={{ fontFamily: 'var(--font-cinzel)' }}>
                                {item.emoji} {item.nom}
                            </button>
                        ))}
                    </motion.div>

                    <CarteUnite key={`e-${ennemiActif}`} {...e} stats={e.stats}>
                        <div className="grid grid-cols-2 gap-4">
                            <div className="bg-red-950/30 border border-red-900/50 rounded-lg p-4">
                                <p className="text-red-400 text-xs uppercase tracking-widest mb-2 font-bold">⚠️ Danger</p>
                                <p className="text-gray-400 text-sm">{e.danger}</p>
                            </div>
                            <div className="bg-green-950/30 border border-green-900/50 rounded-lg p-4">
                                <p className="text-green-400 text-xs uppercase tracking-widest mb-2 font-bold">🎯 Contré par</p>
                                <p className="text-gray-400 text-sm">{e.contreParQuoi}</p>
                            </div>
                        </div>
                    </CarteUnite>
                </div>
            </section>

            {/* ===== MATRICE ===== */}
            <section className="py-12 px-6 w-full flex justify-center">
                <div className="w-full max-w-3xl">
                    <motion.div {...fadeUp} className="text-center mb-10">
                        <h2 className="text-3xl font-black text-[#c9a84c] tracking-widest uppercase mb-3"
                            style={{ fontFamily: 'var(--font-cinzel)' }}>
                            ⚔️ Forces & Faiblesses
                        </h2>
                        <p className="text-gray-400" style={{ fontFamily: 'var(--font-crimson)' }}>
                            Diversifiez vos défenses — aucune tourelle ne couvre tout
                        </p>
                    </motion.div>

                    <motion.div {...fadeUp} className="bg-[#1a1a22] border border-[#3a3a48] rounded-2xl overflow-hidden w-full">
                        <div className="grid grid-cols-4 border-b border-[#3a3a48]">
                            <div className="p-4 bg-[#2a2a35]">
                                <p className="text-[10px] text-gray-500 uppercase tracking-widest">Ennemi ↓ Tour →</p>
                            </div>
                            {['🏹 Archer', '🪨 Catapulte', '🧱 Muraille'].map((label, i) => (
                                <div key={i} className="p-4 text-center border-l border-[#3a3a48] bg-[#2a2a35]">
                                    <p className="text-sm font-bold">{label}</p>
                                </div>
                            ))}
                        </div>
                        {MATRICE.map((row, i) => (
                            <div key={i} className={`grid grid-cols-4 ${i < MATRICE.length - 1 ? 'border-b border-[#3a3a48]' : ''}`}>
                                <div className="p-4 bg-[#1a1a22] flex items-center border-r border-[#3a3a48]">
                                    <p className="text-sm font-bold">{row.ennemi}</p>
                                </div>
                                {[row.archer, row.catapulte, row.muraille].map((cell, j) => (
                                    <div key={j} className="p-4 text-center border-l border-[#3a3a48] hover:bg-[#2a2a35]/50 transition-colors">
                                        <p className="text-sm font-bold">{cell.text}</p>
                                        <p className="text-[10px] text-gray-500 mt-1">{cell.desc}</p>
                                    </div>
                                ))}
                            </div>
                        ))}
                    </motion.div>
                </div>
            </section>

            {/* ===== DÉROULEMENT ===== */}
            <section className="py-12 px-6 w-full flex justify-center">
                <div className="w-full max-w-3xl">
                    <motion.div {...fadeUp} className="text-center mb-10">
                        <h2 className="text-3xl font-black text-[#c9a84c] tracking-widest uppercase mb-3"
                            style={{ fontFamily: 'var(--font-cinzel)' }}>
                            📜 Déroulement
                        </h2>
                    </motion.div>

                    <motion.div {...fadeUp} className="grid grid-cols-2 gap-6">
                        <div className="bg-[#1a1a22] border border-[#c9a84c]/30 rounded-2xl p-8">
                            <div className="text-3xl mb-4">🔨</div>
                            <h3 className="text-lg font-black text-[#c9a84c] uppercase tracking-widest mb-4"
                                style={{ fontFamily: 'var(--font-cinzel)' }}>
                                Fortification
                            </h3>
                            <div className="flex flex-col gap-3 text-gray-400 text-sm" style={{ fontFamily: 'var(--font-crimson)' }}>
                                <p>1. Consultez votre budget en or</p>
                                <p>2. Construisez des tourelles (🏹 + 🪨)</p>
                                <p>3. Placez des murailles sur le chemin (🧱)</p>
                                <p>4. Lancez l'assaut quand vous êtes prêt</p>
                            </div>
                        </div>
                        <div className="bg-[#1a1a22] border border-red-900/30 rounded-2xl p-8">
                            <div className="text-3xl mb-4">⚔️</div>
                            <h3 className="text-lg font-black text-red-500 uppercase tracking-widest mb-4"
                                style={{ fontFamily: 'var(--font-cinzel)' }}>
                                Combat
                            </h3>
                            <div className="flex flex-col gap-3 text-gray-400 text-sm" style={{ fontFamily: 'var(--font-crimson)' }}>
                                <p>1. Les envahisseurs avancent sur le chemin</p>
                                <p>2. Vos tourelles tirent automatiquement</p>
                                <p>3. Les murailles bloquent les ennemis</p>
                                <p>4. Survivants reportés à la vague suivante</p>
                            </div>
                        </div>
                    </motion.div>

                    <motion.div {...fadeUp} className="mt-8 bg-[#2a2a35]/50 border border-[#3a3a48] rounded-xl p-6 text-center">
                        <p className="text-gray-400 text-sm leading-relaxed" style={{ fontFamily: 'var(--font-crimson)' }}>
                            Entre chaque vague, vous revenez en phase de fortification pour renforcer vos défenses.
                            Le score final est calculé sur 100 : <span className="text-[#c9a84c]">citadelle restante (30pts)</span> +
                            <span className="text-purple-400"> ennemis éliminés (40pts)</span> +
                            <span className="text-[#c9a84c]"> efficacité budget (30pts)</span>.
                            Obtenez ⭐⭐⭐ avec un score ≥ 80 !
                        </p>
                    </motion.div>
                </div>
            </section>

            {/* ===== CTA ===== */}
            <section className="py-12 px-6 w-full flex justify-center">
                <div className="w-full max-w-3xl flex flex-col items-center gap-6">
                    <div className="w-64 h-px bg-gradient-to-r from-transparent via-[#c9a84c] to-transparent" />
                    <Link href="/setup"
                          className="inline-block w-full max-w-md bg-[#c9a84c] hover:bg-[#e8c96d] text-black font-black text-2xl px-16 py-5 rounded-lg tracking-widest uppercase transition-all duration-300 hover:shadow-[0_0_40px_rgba(201,168,76,0.6)] text-center"
                          style={{ fontFamily: 'var(--font-cinzel)' }}>
                        ⚔️ Commencer ⚔️
                    </Link>
                </div>
            </section>
        </main>
    );
}