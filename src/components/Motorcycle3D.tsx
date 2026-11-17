'use client';
import {Canvas} from '@react-three/fiber';
import {OrbitControls, ContactShadows} from '@react-three/drei';
import {Suspense} from 'react';
type Props={body:string;rim:string;seat:string;wheel:number;exhaust:string;mirror:boolean;filter:boolean};
function Scooter({body,rim,seat,wheel,exhaust,mirror,filter}:Props){
 const radius=wheel===12?.60:.70;
 return <group rotation={[0,Math.PI/5,0]}>
  <mesh position={[0,-.78,0]} receiveShadow><boxGeometry args={[5,.08,2]}/><meshStandardMaterial color="#233045"/></mesh>
  {[-1.15,1.15].map((x,i)=><group key={i} position={[x,radius-.75,0]} rotation={[0,0,Math.PI/2]}>
   <mesh><torusGeometry args={[radius,.15,14,42]}/><meshStandardMaterial color="#111219" roughness={.85}/></mesh>
   <mesh><torusGeometry args={[radius*.67,.07,12,30]}/><meshStandardMaterial color={rim} metalness={.8} roughness={.25}/></mesh>
   {[0,1,2,3,4].map(j=><mesh key={j} rotation={[0,j*Math.PI/5,0]}><boxGeometry args={[.07,radius*1.25,.06]}/><meshStandardMaterial color={rim} metalness={.7}/></mesh>)}
   <mesh><cylinderGeometry args={[.16,.16,.22,20]}/><meshStandardMaterial color="#c9d1db" metalness={.8}/></mesh>
  </group>)}
  <mesh position={[0,.19,0]} rotation={[0,0,-.06]}><boxGeometry args={[2.25,.53,.72]}/><meshStandardMaterial color={body} metalness={.32} roughness={.35}/></mesh>
  <mesh position={[-.58,.56,0]}><boxGeometry args={[1.3,.16,.65]}/><meshStandardMaterial color={seat} roughness={.9}/></mesh>
  <mesh position={[.94,.52,0]} rotation={[0,0,-.36]}><boxGeometry args={[.85,1.17,.69]}/><meshStandardMaterial color={body} metalness={.25}/></mesh>
  <mesh position={[1.12,1.03,0]}><boxGeometry args={[.4,.18,.75]}/><meshStandardMaterial color="#bce9ff" emissive="#17426c" emissiveIntensity={.45}/></mesh>
  <mesh position={[.93,1.28,0]} rotation={[0,0,.13]}><cylinderGeometry args={[.045,.045,1.05,10]}/><meshStandardMaterial color="#b9c5d4" metalness={.8}/></mesh>
  <mesh position={[.95,1.78,0]} rotation={[Math.PI/2,0,0]}><cylinderGeometry args={[.065,.065,1.2,12]}/><meshStandardMaterial color="#1a2430"/></mesh>
  {mirror&&[-.5,.5].map((z,i)=><group key={i}><mesh position={[.96,2.02,z]}><cylinderGeometry args={[.025,.025,.54,8]}/><meshStandardMaterial color="#bbc5d3"/></mesh><mesh position={[.98,2.3,z]}><boxGeometry args={[.3,.13,.22]}/><meshStandardMaterial color="#151922"/></mesh></group>)}
  <mesh position={[-.7,-.05,-.48]} rotation={[0,0,-.12]}><boxGeometry args={[1.05,.17,.18]}/><meshStandardMaterial color={exhaust==="Racing"?"#d5a25c":"#2d323a"} metalness={.6}/></mesh>
  {filter&&<mesh position={[-.15,.15,-.49]}><cylinderGeometry args={[.18,.18,.28,12]}/><meshStandardMaterial color="#b23840"/></mesh>}
 </group>;
}
export default function Motorcycle3D(props:Props){return <div className="canvas"><Canvas shadows camera={{position:[4,2.7,6],fov:44}} gl={{antialias:true}}><color attach="background" args={['#131f31']}/><ambientLight intensity={1.8}/><directionalLight position={[3,7,4]} intensity={2.8}/><Suspense fallback={null}><Scooter {...props}/><directionalLight position={[-3,3,-4]} intensity={1.2}/><ContactShadows opacity={.42} scale={10} blur={2} far={5} position={[0,-.76,0]}/></Suspense><OrbitControls enablePan={false} enableZoom minDistance={3.1} maxDistance={12} minPolarAngle={.2} maxPolarAngle={Math.PI-.2}/></Canvas></div>}