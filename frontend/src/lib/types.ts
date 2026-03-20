// ============================================================
// Types frontend — WebSocket & UI
// (Les types API/DTO sont dans api.ts)
// ============================================================

export interface EnnemiEtat {
    id: number;
    nom: string;
    position: number;
    pvActuels: number;
    pvMax: number;
    vivant: boolean;
    type: string;
}

export interface MurailleEtat {
    position: number;
    pvActuels: number;
    pvMax: number;
    detruite: boolean;
}

export interface CombatEtat {
    vagueNumero: number;
    etat: string;
    forteressePvActuels: number;
    forteressePvMax: number;
    score: number;
    ennemisVivants: number;
    ennemisTotal: number;
    tempsEcoule: number;
    dureeSecondes: number;
    derniereVague: boolean;
    ennemis: EnnemiEtat[];
    murailles: MurailleEtat[];
}