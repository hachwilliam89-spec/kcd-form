const API_URL = process.env.NEXT_PUBLIC_API_URL;

// ============================================================
// Types — alignés sur les DTOs du back
// ============================================================

export interface ApiResponse<T> {
    statut: number;
    message: string;
    donnees: T;
    erreur: string | null;
    horodatage: string;
}

export type Difficulte = 'ECUYER' | 'CHEVALIER' | 'SEIGNEUR';
export type EtatPartie = 'EN_PAUSE' | 'EN_COURS' | 'ENTRE_VAGUES' | 'GAGNE' | 'PERDU';

export interface Joueur {
    id: number;
    nom: string;
    budget: number;
    score: number;
    vies: number;
}

export interface Partie {
    id: number;
    difficulte: Difficulte;
    etat: EtatPartie;
    vagueActuelle: number;
    joueurId: number;
    joueurNom: string;
    scoreFinal: number;
    etoiles: number;
    ennemisElimines: number;
    ennemisTotal: number;
    forteressePvRestants: number;
    forteressePvMax: number;
    orDepense: number;
}

export interface FormeDTO {
    type: 'TRIANGLE' | 'CERCLE' | 'RECTANGLE';
    valeur1: number;
    valeur2: number;
    couleur: string;
}

export interface Tourelle {
    id: number;
    nom: string;
    position: number;
    portee: number;
    nombreTirs: number;
    aoe: boolean;
    rayonAoe: number;
    dps: number;
    pv: number;
    cout: number;
    partieId: number;
}

export interface Muraille {
    id: number;
    position: number;
    pvMax: number;
    pvActuels: number;
    cout: number;
    partieId: number;
}

export interface Vague {
    partieId: number;
    vagueActuelle: number;
    vagueMax: number;
    etatPartie: EtatPartie;
    message: string;
}

// ============================================================
// Helper — fetch + extraction de donnees
// ============================================================

async function fetchApi<T>(url: string, options?: RequestInit): Promise<T> {
    const res = await fetch(url, options);
    const json: ApiResponse<T> = await res.json();

    if (!res.ok || json.erreur) {
        throw new Error(json.erreur || json.message || 'Erreur API');
    }

    return json.donnees;
}

function post<T>(url: string, body?: unknown): Promise<T> {
    return fetchApi<T>(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: body ? JSON.stringify(body) : undefined,
    });
}

function put<T>(url: string): Promise<T> {
    return fetchApi<T>(url, { method: 'PUT' });
}

async function del(url: string): Promise<void> {
    const res = await fetch(url, { method: 'DELETE' });
    if (!res.ok) {
        const json = await res.json();
        throw new Error(json.erreur || 'Erreur suppression');
    }
}

// ============================================================
// API
// ============================================================

export const api = {
    // Joueurs
    getJoueurs: () =>
        fetchApi<Joueur[]>(`${API_URL}/api/joueurs`),

    getJoueur: (id: number) =>
        fetchApi<Joueur>(`${API_URL}/api/joueurs/${id}`),

    creerJoueur: (data: { nom: string; budget: number; vies: number }) =>
        post<Joueur>(`${API_URL}/api/joueurs`, data),

    modifierJoueur: (id: number, data: { nom: string; budget: number; vies: number }) =>
        put<Joueur>(`${API_URL}/api/joueurs/${id}`),

    supprimerJoueur: (id: number) =>
        del(`${API_URL}/api/joueurs/${id}`),

    // Parties
    getParties: () =>
        fetchApi<Partie[]>(`${API_URL}/api/parties`),

    getPartie: (id: number) =>
        fetchApi<Partie>(`${API_URL}/api/parties/${id}`),

    creerPartie: (data: { joueurId: number; difficulte: Difficulte }) =>
        post<Partie>(`${API_URL}/api/parties`, data),

    changerEtat: (id: number, etat: string) =>
        put<Partie>(`${API_URL}/api/parties/${id}/etat?etat=${etat}`),

    supprimerPartie: (id: number) =>
        del(`${API_URL}/api/parties/${id}`),

    // Tourelles
    getTourelles: (partieId: number) =>
        fetchApi<Tourelle[]>(`${API_URL}/api/parties/${partieId}/tourelles`),

    ajouterTourelle: (partieId: number, data: {
        nom: string;
        position: number;
        portee: number;
        formes: FormeDTO[];
    }) =>
        post<Tourelle>(`${API_URL}/api/parties/${partieId}/tourelles`, data),

    supprimerTourelle: (partieId: number, tourelleId: number) =>
        del(`${API_URL}/api/parties/${partieId}/tourelles/${tourelleId}`),

    // Murailles
    getMurailles: (partieId: number) =>
        fetchApi<Muraille[]>(`${API_URL}/api/parties/${partieId}/murailles`),

    placerMuraille: (partieId: number, data: {
        position: number;
        largeur: number;
        longueur: number;
    }) =>
        post<Muraille>(`${API_URL}/api/parties/${partieId}/murailles`, data),

    supprimerMuraille: (partieId: number, murailleId: number) =>
        del(`${API_URL}/api/parties/${partieId}/murailles/${murailleId}`),

    // Vagues
    getVague: (partieId: number) =>
        fetchApi<Vague>(`${API_URL}/api/parties/${partieId}/vague`),

    vagueSuivante: (partieId: number) =>
        post<Vague>(`${API_URL}/api/parties/${partieId}/vague/suivante`),

    // Combat
    demarrerCombat: (partieId: number) =>
        post<void>(`${API_URL}/api/parties/${partieId}/combat/demarrer`),

    reprendreCombat: (partieId: number) =>
        post<void>(`${API_URL}/api/parties/${partieId}/combat/reprendre`),
};