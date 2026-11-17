'use client';
import dynamic from 'next/dynamic';
import {useState,useEffect} from 'react';
const Motorcycle3D=dynamic(()=>import('../components/Motorcycle3D'),{ssr:false});
const models=['Honda BeAT Street 2019','Honda BeAT Street 2020','Honda BeAT Street 2026','Honda BeAT 2024','Honda BeAT 2026','Honda Vario 125','Honda Vario 160','Honda Scoopy','Honda PCX 160','Yamaha NMAX','Yamaha Aerox','Yamaha Fazzio'];
type State={model:string;wheel:number;front:string;rear:string;body:string;rim:string;seat:string;exhaust:string;filter:boolean;mirror:boolean;swap:string};
const defaults:State={model:models[1],wheel:14,front:'80/90-14',rear:'90/90-14',body:'#2674d9',rim:'#c9d2df',seat:'#17191e',exhaust:'Standar',filter:false,mirror:true,swap:'Original'};
const sizes=['70/90','80/90','90/90','90/100','100/90','110/90','120/80'];
const getDefaults=(model:string):State=>({...defaults,model,wheel:model.includes('2019')||model.includes('2020')?14:12,front:model.includes('2019')||model.includes('2020')?'80/90-14':'100/90-12',rear:model.includes('2019')||model.includes('2020')?'90/90-14':'110/90-12'});
export default function Page(){
 const [s,setS]=useState<State>(defaults);
 const [mode,setMode]=useState<'motor'|'parts'>('motor');
 const set=<K extends keyof State>(key:K,value:State[K])=>setS(prev=>({...prev,[key]:value}));
 useEffect(()=>{try{const x=localStorage.getItem('motobile2');if(x)setS({...defaults,...JSON.parse(x)})}catch{}},[]);
 useEffect(()=>{localStorage.setItem('motobile2',JSON.stringify(s))},[s]);
 const incompatible=!s.front.endsWith('-'+s.wheel)||!s.rear.endsWith('-'+s.wheel);
 const needsCheck=s.swap!=='Original'||s.filter||s.wheel!==(s.model.includes('2019')||s.model.includes('2020')?14:12);
 const cost=(s.exhaust==='Racing'?1250000:0)+(s.filter?325000:0)+(s.swap!=='Original'?1200000:0)+(s.wheel===12?1600000:1800000);
 return <main className="app">
  <header className="header"><div><div className="brand">MOTO LAB <span style={{color:'#54aeff'}}>ID</span></div><div className="hint">Studio modifikasi motor Indonesia • Android 11+</div></div><button onClick={()=>{const a=document.createElement('a');a.href=URL.createObjectURL(new Blob([JSON.stringify(s,null,2)],{type:'application/json'}));a.download='motobile-design.json';a.click()}}>Simpan desain</button></header>
  <section className="workspace">
   <aside className="panel scroll"><h3>Pilih Motor</h3><div className="catalog">{models.map(m=><button key={m} className={s.model===m?'active':''} onClick={()=>{setS(getDefaults(m));setMode('parts')}}>{m}</button>)}</div></aside>
   <section className="viewer"><div className="viewerbar"><strong>{s.model}</strong><span className="small">Geser 360° • Cubit untuk zoom</span></div><Motorcycle3D body={s.body} rim={s.rim} seat={s.seat} wheel={s.wheel} exhaust={s.exhaust} mirror={s.mirror} filter={s.filter}/><div className="viewerbar"><button onClick={()=>setMode('motor')} className={mode==='motor'?'active':''}>Katalog</button><button onClick={()=>setMode('parts')} className={mode==='parts'?'active':''}>Modifikasi</button><button onClick={()=>setS(getDefaults(s.model))}>Reset</button></div><p className="small">Visual 3D adalah placeholder ilustratif. Bentuk motor pabrikan dan sparepart belum direplikasi secara akurat.</p></section>
   <aside className="panel scroll"><h3>Modifikasi</h3>
    <div className="field"><label>Warna body</label><input aria-label="Warna body" className="color" type="color" value={s.body} onChange={e=>set('body',e.target.value)}/></div>
    <div className="field"><label>Warna velg</label><input aria-label="Warna velg" className="color" type="color" value={s.rim} onChange={e=>set('rim',e.target.value)}/></div>
    <div className="field"><label>Ukuran velg</label><select value={s.wheel} onChange={e=>set('wheel',Number(e.target.value))}><option value={12}>12 inci</option><option value={14}>14 inci</option></select></div>
    {(['front','rear'] as const).map(key=><div key={key} className="field"><label>Ban {key==='front'?'depan':'belakang'}</label><select value={s[key]} onChange={e=>set(key,e.target.value)}>{[12,14].flatMap(w=>sizes.map(sz=><option key={sz+'-'+w} value={sz+'-'+w}>{sz}-{w}</option>))}</select></div>)}
    <div className="field"><label>Knalpot</label><select value={s.exhaust} onChange={e=>set('exhaust',e.target.value)}><option>Standar</option><option>Racing</option></select></div>
    <div className="field"><label>Swap body</label><select value={s.swap} onChange={e=>set('swap',e.target.value)}>{['Original','BeAT Street 2019','BeAT Street 2020','BeAT Street 2026'].map(x=><option key={x}>{x}</option>)}</select></div>
    <div className="field"><label><input type="checkbox" checked={s.filter} onChange={e=>set('filter',e.target.checked)}/> Filter udara Muttaru Custom</label></div>
    <div className="field"><label><input type="checkbox" checked={s.mirror} onChange={e=>set('mirror',e.target.checked)}/> Spion</label></div>
    <div className="field"><label>Jok</label><select value={s.seat} onChange={e=>set('seat',e.target.value)}><option value="#17191e">Standar hitam</option><option value="#774b34">Coklat custom</option></select></div>
    <div className={'status '+(incompatible?'bad':needsCheck?'warn':'')}>{incompatible?'Tidak kompatibel: diameter ban dan velg harus sama.':needsCheck?'Perlu verifikasi bengkel: swap body, filter custom, atau perubahan diameter velg wajib diperiksa berdasarkan dudukan, ruang bebas, dan ukuran aktual.':'Ukuran ban dan velg sesuai. Periksa spesifikasi pabrikan sebelum pemasangan fisik.'}</div>
    <p className="small">Estimasi ilustratif (belum termasuk seluruh biaya pemasangan)</p><div className="cost">Rp {cost.toLocaleString('id-ID')}</div>
   </aside>
  </section></main>
}