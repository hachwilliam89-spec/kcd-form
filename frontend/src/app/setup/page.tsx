'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/api';
import { Shield, ChevronRight } from 'lucide-react';

const BUDGET_PAR_DIFFICULTE: Record<string, number> = {
    ECUYER: 1000,
    CHEVALIER: 700,
    SEIGNEUR: 400,
};

const DIFFICULTES = [
    {
        id: 'ECUYER',
        label: 'Écuyer',
        emoji: '🛡️',
        desc: 'Pour les débutants. Budget généreux, ennemis faibles.',
        budget: 1000,
        couleur: 'border-green-600 hover:border-green-400',
        badge: 'bg-green-900/40 text-green-400',
    },
    {
        id: 'CHEVALIER',
        label: 'Chevalier',
        emoji: '⚔️',
        desc: 'Équilibré. Budget standard, ennemis redoutables.',
        budget: 700,
        couleur: 'border-yellow-600 hover:border-yellow-400',
        badge: 'bg-yellow-900/40 text-yellow-400',
    },
    {
        id: 'SEIGNEUR',
        label: 'Seigneur',
        emoji: '👑',
        desc: 'Pour les experts. Budget serré, ennemis impitoyables.',
        budget: 400,
        couleur: 'border-red-700 hover:border-red-500',
        badge: 'bg-red-900/40 text-red-400',
    },
];

export default function SetupPage() {
    const router = useRouter();
    const [nom, setNom] = useState('');
    const [difficulte, setDifficulte] = useState<'ECUYER' | 'CHEVALIER' | 'SEIGNEUR'>('CHEVALIER');
    const [loading, setLoading] = useState(false);
    const [erreur, setErreur] = useState('');

    const lancer = async () => {
        if (!nom.trim()) { setErreur('Entre ton nom de seigneur !'); return; }
        setLoading(true);
        try {
            const budgetInitial = BUDGET_PAR_DIFFICULTE[difficulte] ?? 700;
            const joueur = await api.creerJoueur({ nom, budget: budgetInitial, vies: 3 });
            const partie = await api.creerPartie({ joueurId: joueur.id, difficulte });
            router.push(`/parties/${partie.id}/construction`);
        } catch {
            setErreur('Erreur serveur, vérifie que Spring Boot tourne.');
            setLoading(false);
        }
    };

    const diff = DIFFICULTES.find(d => d.id === difficulte)!;

    return (
        <main className="min-h-screen bg-[#0a0a0f] flex flex-col items-center justify-center px-6 py-12">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                className="w-full max-w-xl flex flex-col gap-8"
            >
                {/* Header */}
                <div className="text-center">
                    <Shield className="w-10 h-10 text-[#c9a84c] mx-auto mb-3" strokeWidth={1.5} />
                    <h1 className="text-3xl font-black text-[#c9a84c] tracking-widest uppercase"
                        style={{ fontFamily: 'var(--font-cinzel)' }}>
                        Nouvelle Partie
                    </h1>
                </div>

                {/* Nom du joueur */}
                <div className="flex flex-col gap-2">
                    <label className="text-[#c9a84c] text-sm tracking-widest uppercase"
                           style={{ fontFamily: 'var(--font-cinzel)' }}>
                        Ton nom de seigneur
                    </label>
                    <input
                        className="bg-[#2a2a35] border border-[#c9a84c]/30 focus:border-[#c9a84c] rounded-lg px-4 py-3 text-white outline-none transition-colors text-lg"
                        placeholder="Ex: Arthur, Lancelot..."
                        value={nom}
                        onChange={e => { setNom(e.target.value); setErreur(''); }}
                    />
                </div>

                {/* Choix difficulté */}
                <div className="flex flex-col gap-3">
                    <label className="text-[#c9a84c] text-sm tracking-widest uppercase"
                           style={{ fontFamily: 'var(--font-cinzel)' }}>
                        Difficulté
                    </label>
                    {DIFFICULTES.map(d => (
                        <button
                            key={d.id}
                            onClick={() => setDifficulte(d.id as 'ECUYER' | 'CHEVALIER' | 'SEIGNEUR')}
                            className={`border-2 rounded-lg p-4 text-left transition-all duration-200 ${
                                difficulte === d.id
                                    ? d.couleur + ' bg-[#2a2a35]'
                                    : 'border-[#2a2a35] hover:border-gray-600 bg-[#1a1a22]'
                            }`}
                        >
                            <div className="flex items-center gap-3">
                                <span className="text-2xl">{d.emoji}</span>
                                <div className="flex-1">
                                    <div className="flex items-center gap-2">
                                        <span className="font-bold text-white" style={{ fontFamily: 'var(--font-cinzel)' }}>
                                            {d.label}
                                        </span>
                                        <span className={`text-xs px-2 py-0.5 rounded-full ${d.badge}`}>
                                            {d.budget} or
                                        </span>
                                    </div>
                                    <p className="text-gray-400 text-sm mt-0.5">{d.desc}</p>
                                </div>
                                {difficulte === d.id && (
                                    <ChevronRight className="w-5 h-5 text-[#c9a84c]" />
                                )}
                            </div>
                        </button>
                    ))}
                </div>

                {/* Erreur */}
                {erreur && <p className="text-red-400 text-sm text-center">{erreur}</p>}

                {/* Résumé */}
                <div className="bg-[#2a2a35]/50 border border-[#c9a84c]/20 rounded-lg p-4 text-sm text-gray-400">
                    <p>⚔️ <span className="text-white">5 vagues</span> d'envahisseurs à repousser</p>
                    <p>💰 Budget de départ : <span className="text-[#c9a84c]">{diff.budget} or</span></p>
                    <p>🏰 Défendez la Citadelle jusqu'au dernier assaut</p>
                </div>

                {/* Bouton lancer */}
                <button
                    onClick={lancer}
                    disabled={loading}
                    className="bg-[#c9a84c] hover:bg-[#e8c96d] disabled:opacity-50 text-black font-black text-lg px-8 py-4 rounded-lg tracking-widest uppercase transition-all duration-300 hover:shadow-[0_0_30px_rgba(201,168,76,0.4)]"
                    style={{ fontFamily: 'var(--font-cinzel)' }}
                >
                    {loading ? 'Chargement...' : 'Lancer la bataille ⚔️'}
                </button>
            </motion.div>
        </main>
    );
}