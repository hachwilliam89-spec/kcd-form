'use client';

import { useState, useEffect } from 'react';
import { api, Tourelle, Muraille, Partie } from '@/lib/api';
import CarteConstruction from './CarteConstruction';
import PanneauFormes from './PanneauFormes';
import PanneauMuraille from './PanneauMuraille';
import ListeDefenses from './ListeDefenses';
import { useRouter, useParams, useSearchParams } from 'next/navigation';

interface FormeSelectionnee {
    type: 'TRIANGLE' | 'CERCLE';
    valeur1: number;
    valeur2: number;
    couleur: string;
}

const FORMES_DISPONIBLES = [
    { type: 'TRIANGLE' as const, label: 'Triangle', emoji: '🏹', desc: 'Archers — tirs rapides', cout: 54, couleur: 'rouge', valeur1: 4.0, valeur2: 3.0 },
    { type: 'CERCLE' as const, label: 'Cercle', emoji: '🪨', desc: "Catapulte — zone d'effet", cout: 74, couleur: 'bleu', valeur1: 3.0, valeur2: 0 },
];

const MURAILLE_CONFIG = {
    largeur: 5.0,
    longueur: 3.0,
    cout: 33,
    pvMax: 150,
};

const BUDGET_INITIAL: Record<string, number> = {
    ECUYER: 1000,
    CHEVALIER: 700,
    SEIGNEUR: 400,
};

const coutFormes = (formes: FormeSelectionnee[]) =>
    formes.reduce((acc, f) => {
        const fd = FORMES_DISPONIBLES.find(fd => fd.type === f.type);
        return acc + (fd?.cout ?? 0);
    }, 0);

type Etape = 'carte' | 'formes' | 'muraille';

export default function ConstructionPage() {
    const router = useRouter();
    const params = useParams();
    const partieId = Number(params.id);

    const [partie, setPartie] = useState<Partie | null>(null);
    const [tourelles, setTourelles] = useState<Tourelle[]>([]);
    const [murailles, setMurailles] = useState<Muraille[]>([]);
    const [caseSelectionnee, setCaseSelectionnee] = useState<number | null>(null);
    const [murailleSelectionnee, setMurailleSelectionnee] = useState<number | null>(null);
    const [formesSelectionnees, setFormesSelectionnees] = useState<FormeSelectionnee[]>([]);
    const [budget, setBudget] = useState(400);
    const [loading, setLoading] = useState(false);
    const [etape, setEtape] = useState<Etape>('carte');
    const searchParams = useSearchParams();
    const fromLobby = searchParams.get('from') === 'lobby';
    const lobbyId = searchParams.get('lobbyId');

    const estReprise = partie?.etat === 'ENTRE_VAGUES';
    const vagueAffichee = (partie?.vagueActuelle ?? 0) + 1;

    useEffect(() => { charger(); }, []);

    const charger = async () => {
        const p = await api.getPartie(partieId);
        setPartie(p);
        const t = await api.getTourelles(partieId);
        setTourelles(t);
        const m = await api.getMurailles(partieId);
        setMurailles(m);
        const budgetTourelles = t.reduce((acc: number, t: Tourelle) => acc + t.cout, 0);
        const budgetMurailles = m.reduce((acc: number, m: Muraille) => acc + m.cout, 0);
        setBudget((BUDGET_INITIAL[p.difficulte] ?? 400) - budgetTourelles - budgetMurailles);
    };

    const coutActuel = coutFormes(formesSelectionnees);

    const selectCase = (pos: number) => {
        setCaseSelectionnee(pos);
        setMurailleSelectionnee(null);
        setFormesSelectionnees([]);
        setEtape('formes');
    };

    const selectMuraille = (pos: number) => {
        setMurailleSelectionnee(pos);
        setCaseSelectionnee(null);
        setFormesSelectionnees([]);
        setEtape('muraille');
    };

    const annuler = () => {
        setCaseSelectionnee(null);
        setMurailleSelectionnee(null);
        setFormesSelectionnees([]);
        setEtape('carte');
    };

    const ajouterForme = (forme: typeof FORMES_DISPONIBLES[0]) => {
        if (formesSelectionnees.length >= 3) return;
        if (budget < coutActuel + forme.cout) return;
        setFormesSelectionnees(prev => [...prev, { type: forme.type, valeur1: forme.valeur1, valeur2: forme.valeur2, couleur: forme.couleur }]);
    };

    const retirerForme = (index: number) => setFormesSelectionnees(prev => prev.filter((_, i) => i !== index));

    const placerTourelle = async () => {
        if (caseSelectionnee === null || formesSelectionnees.length === 0) return;
        setLoading(true);
        const nom = `Tourelle ${caseSelectionnee}`;
        await api.ajouterTourelle(partieId, { nom, position: caseSelectionnee, portee: 3, formes: formesSelectionnees });
        annuler();
        await charger();
        setLoading(false);
    };

    const placerMuraille = async () => {
        if (murailleSelectionnee === null) return;
        setLoading(true);
        await api.placerMuraille(partieId, {
            position: murailleSelectionnee,
            largeur: MURAILLE_CONFIG.largeur,
            longueur: MURAILLE_CONFIG.longueur,
        });
        annuler();
        await charger();
        setLoading(false);
    };

    const supprimerTourelle = async (id: number) => {
        await api.supprimerTourelle(partieId, id);
        await charger();
    };

    const supprimerMuraille = async (id: number) => {
        await api.supprimerMuraille(partieId, id);
        await charger();
    };

    const lancerCombat = async () => {
        if (estReprise) {
            router.push(`/parties/${partieId}/combat`);
        } else {
            await api.changerEtat(partieId, 'EN_COURS');
            router.push(`/parties/${partieId}/combat`);
        }
    };

    return (
        <main className="h-screen bg-[#0a0a0f] text-white flex flex-col overflow-hidden p-4 gap-4">

            {/* Header */}
            <div className="flex justify-between items-center border-b border-[#c9a84c]/20 pb-3 flex-shrink-0">
                <div>
                    <h1 className="text-xl font-black text-[#c9a84c] tracking-widest uppercase"
                        style={{ fontFamily: 'var(--font-cinzel)' }}>
                        {estReprise ? '🔨 Phase de Fortification' : '⚔️ Phase de Construction'}
                    </h1>
                    <p className="text-gray-400 text-sm">
                        {partie?.joueurNom} — {partie?.difficulte}
                        {estReprise && (
                            <span className="text-[#c9a84c] ml-2">
                                — Préparez-vous pour la vague {vagueAffichee}
                            </span>
                        )}
                    </p>
                </div>
                <div className="flex items-center gap-4">
                    <div className="text-right">
                        <p className="text-xs text-gray-400 uppercase tracking-widest">Budget</p>
                        <p className="text-2xl font-black text-[#c9a84c]">{budget} 💰</p>
                    </div>
                    {fromLobby ? (
                        <button onClick={() => router.push(`/multi/${lobbyId}?role=DEFENSEUR&partieId=${partieId}`)}
                                className="bg-[#c9a84c] hover:bg-[#e8c96d] text-black font-black px-5 py-3 rounded-lg uppercase tracking-widest transition-all text-sm"
                                style={{ fontFamily: 'var(--font-cinzel)' }}>
                            🛡️ Retour au lobby
                        </button>
                    ) : (
                        <button onClick={lancerCombat}
                                className="bg-[#c9a84c] hover:bg-[#e8c96d] text-black font-black px-5 py-3 rounded-lg uppercase tracking-widest transition-all text-sm"
                                style={{ fontFamily: 'var(--font-cinzel)' }}>
                            {estReprise ? `⚔️ Vague ${vagueAffichee}` : '⚔️ Lancer le combat'}
                        </button>
                    )}
                </div>
            </div>

            {/* Contenu */}
            <div className="flex gap-4 flex-1 min-h-0">

                {/* Carte */}
                <div className="flex flex-col gap-2 flex-1 min-w-0">
                    <div className="flex items-center justify-between flex-shrink-0">
                        <h2 className="text-[#c9a84c] text-xs uppercase tracking-widest" style={{ fontFamily: 'var(--font-cinzel)' }}>
                            Terrain de défense
                        </h2>
                        <p className="text-gray-500 text-xs">
                            {etape === 'carte' ? 'Clique sur une case libre ou un emplacement muraille' :
                                etape === 'muraille' ? `Muraille position ${murailleSelectionnee}` :
                                    `Case ${caseSelectionnee} sélectionnée`}
                        </p>
                    </div>

                    <CarteConstruction
                        tourelles={tourelles}
                        murailles={murailles}
                        caseSelectionnee={caseSelectionnee}
                        murailleSelectionnee={murailleSelectionnee}
                        budget={budget}
                        murailleCout={MURAILLE_CONFIG.cout}
                        onSelectCase={selectCase}
                        onSelectMuraille={selectMuraille}
                    />

                    {/* Légende */}
                    <div className="flex gap-4 text-xs text-gray-500 flex-shrink-0 flex-wrap">
                        <span className="flex items-center gap-1"><span className="w-3 h-3 rounded border border-[#3a3a48] bg-[#1a1a22] inline-block" /> Case tourelle</span>
                        <span className="flex items-center gap-1"><span className="w-3 h-3 rounded border border-[#4a3a1a] bg-[#2a1f0f] inline-block" /> Emplacement muraille</span>
                        <span className="flex items-center gap-1"><span className="w-3 h-3 rounded border border-[#c9a84c] bg-[#c9a84c]/20 inline-block" /> Sélectionnée</span>
                        <span className="flex items-center gap-1"><span className="w-3 h-3 rounded bg-[#2a1f0f] inline-block" /> Chemin</span>
                    </div>
                </div>

                {/* Panneau latéral */}
                <div className="w-96 flex-shrink-0 flex flex-col gap-3 overflow-y-auto">

                    {etape === 'carte' && (
                        <div className="bg-[#2a2a35]/50 border border-[#3a3a48] rounded-lg p-4 text-center">
                            <p className="text-2xl mb-2">{estReprise ? '🔨' : '🗺️'}</p>
                            <p className="text-[#c9a84c] text-sm font-bold" style={{ fontFamily: 'var(--font-cinzel)' }}>
                                {estReprise ? 'Renforcez vos défenses' : 'Sélectionne un emplacement'}
                            </p>
                            <p className="text-gray-400 text-xs mt-1">
                                {estReprise
                                    ? 'Ajoutez des tourelles ou murailles avant la prochaine vague'
                                    : 'Case numérotée pour une tourelle, ou 🧱 pour une muraille sur le chemin'}
                            </p>
                        </div>
                    )}

                    {etape === 'muraille' && murailleSelectionnee !== null && (
                        <PanneauMuraille
                            position={murailleSelectionnee}
                            pvMax={MURAILLE_CONFIG.pvMax}
                            cout={MURAILLE_CONFIG.cout}
                            loading={loading}
                            onPlacer={placerMuraille}
                            onAnnuler={annuler}
                        />
                    )}

                    {etape === 'formes' && caseSelectionnee !== null && (
                        <PanneauFormes
                            caseSelectionnee={caseSelectionnee}
                            formesSelectionnees={formesSelectionnees}
                            formesDisponibles={FORMES_DISPONIBLES}
                            budget={budget}
                            coutActuel={coutActuel}
                            onAjouterForme={ajouterForme}
                            onRetirerForme={retirerForme}
                            onContinuer={placerTourelle}
                            onAnnuler={annuler}
                        />
                    )}

                    <ListeDefenses
                        tourelles={tourelles}
                        murailles={murailles}
                        budget={budget}
                        onSupprimerTourelle={supprimerTourelle}
                        onSupprimerMuraille={supprimerMuraille}
                    />
                </div>
            </div>
        </main>
    );
}