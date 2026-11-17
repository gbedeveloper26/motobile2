# MOTO LAB ID — motobile2

Simulator modifikasi motor Indonesia, target Android 11+.

## Build APK via GitHub
Buka tab **Actions** → **Build MOTO LAB ID APK** → jalankan workflow. Saat sukses unduh artifact **MOTO-LAB-ID-Android11** dan ekstrak ZIP untuk memperoleh `app-debug.apk`.

## Dev
`npm ci && npm run dev`

3D viewer dalam versi ini masih ilustrasi prosedural, belum model GLB motor pabrikan. Ukuran ban/velg memiliki validasi diameter; body swap dan filter khusus yang belum terukur mendapat peringatan verifikasi.
