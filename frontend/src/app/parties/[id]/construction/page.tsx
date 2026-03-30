'use client';

import { useState, useEffect } from 'react';
import { api, Tourelle, Muraille, Partie } from '@/lib/api';
import CarteConstruction from './CarteConstruction';
import PanneauFormes from './PanneauFormes';
import PanneauMuraille from './PanneauMuraille';
import ListeDefenses from './ListeDefenses';
import { useRouter, useParams, useSearchParams } from 'next/navigation';
import { PixelBorder, PixelCoin, PixelTourelle, PixelRectangle, PixelShield } from '@/components/PixelSprites';

const px = { fontFamily: 'var(--font-pixel)' };

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

const MURAILLE_CONFIG = { largeur: 5.0, longueur: 3.0, cout: 33, pvMax: 150 };
const BUDGET_INITIAL: Record<string, number> = { ECUYER: 1000, CHEVALIER: 700, SEIGNEUR: 400 };

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

    const supprimerTourelle = async (id: number) => { await api.supprimerTourelle(partieId, id); await charger(); };
    const supprimerMuraille = async (id: number) => { await api.supprimerMuraille(partieId, id); await charger(); };

    const lancerCombat = async () => {
        if (estReprise) {
            router.push(`/parties/${partieId}/combat`);
        } else {
            await api.changerEtat(partieId, 'EN_COURS');
            router.push(`/parties/${partieId}/combat`);
        }
    };

    return (
        <main className="h-screen bg-medieval-construction text-white flex flex-col overflow-hidden p-4 gap-3">

            {/* ════════ Header ════════ */}
            <div className="flex justify-between items-center pb-2 flex-shrink-0 relative">
                <div className="flex items-center gap-3">
                    <PixelTourelle size={28} />
                    <div>
                        <h1 style={{
                            ...px,
                            fontSize: '0.6rem',
                            color: '#dcb464',
                            textShadow: '0 2px 4px rgba(0,0,0,0.6)',
                            lineHeight: '1.8',
                            letterSpacing: '0.1em',
                            textTransform: 'uppercase' as const,
                        }}>
                            {estReprise ? 'Phase de Fortification' : 'Phase de Construction'}
                        </h1>
                        <p style={{
                            ...px,
                            fontSize: '0.35rem',
                            color: 'rgba(212,200,160,0.7)',
                            textShadow: '0 1px 2px rgba(0,0,0,0.5)',
                            lineHeight: '1.8',
                        }}>
                            {partie?.joueurNom} — {partie?.difficulte}
                            {estReprise && (
                                <span style={{ color: '#dcb464', marginLeft: '8px' }}>
                                    — Préparez-vous pour la vague {vagueAffichee}
                                </span>
                            )}
                        </p>
                    </div>
                </div>
                <div className="flex items-center gap-4">
                    {/* Budget */}
                    <div className="flex items-center gap-2 px-4 py-2"
                         style={{
                             background: 'rgba(26,20,32,0.8)',
                             outline: '2px solid #1a0a00',
                             boxShadow: 'inset 0 2px 0 rgba(220,180,100,0.1), inset 0 -2px 0 rgba(0,0,0,0.3), 0 2px 0 #1a0a00',
                         }}>
                        <PixelCoin size={18} />
                        <span style={{ ...px, fontSize: '0.8rem', color: '#dcb464' }}>{budget}</span>
                    </div>

                    {/* Bouton action */}
                    {fromLobby ? (
                        <button onClick={() => router.push(`/multi/${lobbyId}?role=DEFENSEUR&partieId=${partieId}`)}
                                className="btn-gold py-3 px-5 flex items-center gap-2"
                                style={{ ...px, fontSize: '0.45rem' }}>
                            <PixelShield size={16} />
                            Retour au lobby
                        </button>
                    ) : (
                        <button onClick={lancerCombat}
                                className="btn-blood py-3 px-5 flex items-center gap-2"
                                style={{ ...px, fontSize: '0.45rem' }}>
                            {estReprise ? `⚔️ Vague ${vagueAffichee}` : '⚔️ Lancer le combat'}
                        </button>
                    )}
                </div>
                <PixelBorder className="absolute bottom-0 left-0 right-0" />
            </div>

            {/* ════════ Contenu ════════ */}
            <div className="flex gap-4 flex-1 min-h-0">

                {/* Carte */}
                <div className="flex flex-col gap-2 flex-1 min-w-0">
                    <div className="flex items-center justify-between flex-shrink-0">
                        <h2 style={{
                            ...px,
                            fontSize: '0.4rem',
                            color: '#dcb464',
                            textShadow: '0 1px 2px rgba(0,0,0,0.5)',
                            letterSpacing: '0.1em',
                            textTransform: 'uppercase' as const,
                        }}>
                            Terrain de défense
                        </h2>
                        <p style={{
                            ...px,
                            fontSize: '0.32rem',
                            color: 'rgba(212,200,160,0.5)',
                            textShadow: '0 1px 2px rgba(0,0,0,0.5)',
                        }}>
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
                    <div className="flex gap-4 flex-shrink-0 flex-wrap px-1">
                        {[
                            { label: 'Tourelle', bg: 'rgba(26,20,32,0.5)', border: 'rgba(220,180,100,0.3)' },
                            { label: 'Muraille', bg: 'rgba(42,31,15,0.5)', border: 'rgba(139,105,20,0.3)' },
                            { label: 'Sélectionnée', bg: 'rgba(220,180,100,0.15)', border: 'rgba(220,180,100,0.6)' },
                            { label: 'Chemin', bg: 'rgba(42,31,15,0.4)', border: 'transparent' },
                        ].map((item, i) => (
                            <span key={i} className="flex items-center gap-1"
                                  style={{ ...px, fontSize: '0.3rem', color: 'rgba(212,200,160,0.5)' }}>
                                <span className="w-3 h-3 inline-block"
                                      style={{ background: item.bg, outline: `1px solid ${item.border}` }} />
                                {item.label}
                            </span>
                        ))}
                    </div>
                </div>

                {/* Panneau latéral */}
                <div className="w-96 flex-shrink-0 flex flex-col gap-3 overflow-y-auto">

                    {etape === 'carte' && (
                        <div className="p-4 text-center relative"
                             style={{
                                 background: 'rgba(26,20,32,0.85)',
                                 outline: '3px solid #1a0a00',
                                 boxShadow: 'inset 0 3px 0 rgba(220,180,100,0.08), inset 0 -3px 0 rgba(0,0,0,0.3), 0 3px 0 #1a0a00',
                             }}>
                            <PixelBorder className="absolute top-0 left-0 right-0" />
                            <div className="pt-3">
                                {estReprise ? <PixelTourelle size={32} className="mx-auto mb-2" /> : <PixelShield size={32} className="mx-auto mb-2" />}
                                <p style={{ ...px, fontSize: '0.5rem', color: '#dcb464', lineHeight: '1.8' }}>
                                    {estReprise ? 'Renforcez vos défenses' : 'Sélectionne un emplacement'}
                                </p>
                                <p style={{ ...px, fontSize: '0.32rem', color: 'rgba(212,200,160,0.6)', lineHeight: '2', marginTop: '4px' }}>
                                    {estReprise
                                        ? 'Ajoutez des tourelles ou murailles avant la prochaine vague'
                                        : 'Case numérotée pour une tourelle, ou 🧱 pour une muraille sur le chemin'}
                                </p>
                            </div>
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