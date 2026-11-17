const fs = require('node:fs');
const path = require('node:path');
const base = path.resolve(__dirname, '..');
const source = path.join(base, 'out');
if (!fs.existsSync(path.join(source, 'index.html'))) throw new Error('Jalankan npm run build terlebih dahulu.');
const destination = fs.existsSync(path.join(base, 'android-local'))
  ? path.join(base, 'android-local/app/src/main/assets/public')
  : path.resolve(base, '../app/src/main/assets/public');
fs.rmSync(destination, {recursive:true, force:true});
fs.cpSync(source, destination, {recursive:true});
console.log('Aset lokal diperbarui:', destination);
