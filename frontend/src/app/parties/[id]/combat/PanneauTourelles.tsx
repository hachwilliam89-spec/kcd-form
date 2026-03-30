'use client';

import { Tourelle } from '@/lib/api';
import { PixelCoin } from '@/components/PixelSprites';

const px = { fontFamily: 'var(--font-pixel)' };

interface PanneauTourellesProps {
    tourelles: Tourelle[];
}

export default function PanneauTourelles({ tourelles }: PanneauTourellesProps) {
    return (
        <div className="w-72 flex-shrink-0 flex flex-col gap-3 overflow-y-auto">
            <div className="flex flex-col gap-2 flex-1 p-4"
                 style={{
                     background: 'rgba(26,20,32,0.85)',
                     outline: '3px solid #1a0a00',
                     boxShadow: 'inset 0 3px 0 rgba(220,180,100,0.08), inset 0 -3px 0 rgba(0,0,0,0.3), 0 3px 0 #1a0a00',
                 }}>
                <h2 style={{ ...px, fontSize: '0.4rem', color: '#dcb464', letterSpacing: '0.1em', textTransform: 'uppercase' }}>
                    Défenses ({tourelles.length})
                </h2>
                {tourelles.map(t => (
                    <div key={t.id} className="p-3"
                         style={{ background: 'rgba(10,10,15,0.4)', outline: '2px solid #1a0a00', boxShadow: '0 2px 0 #0a0508' }}>
                        <div className="flex items-center gap-2 mb-2">
                            <span className="text-lg">{t.aoe ? '🪨' : '🏹'}</span>
                            <p style={{ ...px, fontSize: '0.38rem', color: '#d4c8a0', lineHeight: '1.8' }}>{t.nom}</p>
                        </div>
                        <div className="grid grid-cols-3 gap-1 text-center">
                            <div className="p-1" style={{ background: 'rgba(26,20,32,0.6)', outline: '1px solid rgba(26,10,0,0.5)' }}>
                                <p style={{ ...px, fontSize: '0.35rem', color: '#dcb464' }}>{t.dps.toFixed(1)}</p>
                                <p style={{ ...px, fontSize: '0.22rem', color: 'rgba(180,170,150,0.4)', marginTop: '2px', textTransform: 'uppercase' }}>DPS</p>
                            </div>
                            <div className="p-1" style={{ background: 'rgba(26,20,32,0.6)', outline: '1px solid rgba(26,10,0,0.5)' }}>
                                <p style={{ ...px, fontSize: '0.35rem', color: '#3498db' }}>{t.pv}</p>
                                <p style={{ ...px, fontSize: '0.22rem', color: 'rgba(180,170,150,0.4)', marginTop: '2px', textTransform: 'uppercase' }}>PV</p>
                            </div>
                            <div className="p-1" style={{ background: 'rgba(26,20,32,0.6)', outline: '1px solid rgba(26,10,0,0.5)' }}>
                                <p style={{ ...px, fontSize: '0.35rem', color: '#d4c8a0' }}>pos.{t.position}</p>
                                <p style={{ ...px, fontSize: '0.22rem', color: 'rgba(180,170,150,0.4)', marginTop: '2px', textTransform: 'uppercase' }}>Case</p>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}