'use client';

import { Tourelle } from '@/lib/api';

interface PanneauTourellesProps {
    tourelles: Tourelle[];
}

export default function PanneauTourelles({ tourelles }: PanneauTourellesProps) {
    return (
        <div className="w-72 flex-shrink-0 flex flex-col gap-3 overflow-y-auto">
            <div className="bg-[#1a1a22] border border-[#3a3a48] rounded-lg p-4 flex flex-col gap-2 flex-1">
                <h2 className="text-[#c9a84c] text-xs uppercase tracking-widest mb-2"
                    style={{ fontFamily: 'var(--font-cinzel)' }}>
                    Défenses ({tourelles.length})
                </h2>
                {tourelles.map(t => (
                    <div key={t.id} className="bg-[#2a2a35] border border-[#3a3a48] rounded-lg p-3">
                        <div className="flex items-center gap-2 mb-2">
                            <span className="text-lg">{t.aoe ? '🪨' : '🏹'}</span>
                            <p className="font-bold text-sm">{t.nom}</p>
                        </div>
                        <div className="grid grid-cols-3 gap-1 text-center">
                            <div className="bg-[#1a1a22] rounded p-1">
                                <p className="text-[#c9a84c] text-xs font-bold">{t.dps.toFixed(1)}</p>
                                <p className="text-[9px] text-gray-500">DPS</p>
                            </div>
                            <div className="bg-[#1a1a22] rounded p-1">
                                <p className="text-blue-400 text-xs font-bold">{t.pv}</p>
                                <p className="text-[9px] text-gray-500">PV</p>
                            </div>
                            <div className="bg-[#1a1a22] rounded p-1">
                                <p className="text-gray-300 text-xs font-bold">pos.{t.position}</p>
                                <p className="text-[9px] text-gray-500">Case</p>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}