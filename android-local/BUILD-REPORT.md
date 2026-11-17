# Hasil verifikasi paket lokal

17 November 2026.

- Sumber UI memakai versi motobile2; kontrol modifikasi dan pemeriksaan kompatibilitas pada `src/app/page.tsx` dipertahankan tanpa perubahan.
- Viewer memakai pencahayaan lokal, menggantikan HDR preset yang mengunduh file dari internet.
- TypeScript dan static export Next.js 15.5.29 berhasil.
- Sumber Java Capacitor 6.2.3 dibundel di vendor; proyek Gradle tidak menunjuk node_modules.
- Gradle 8.2.1 / Android Gradle Plugin 8.2.1 / Java 17: BUILD SUCCESSFUL, 58 task.
- Build paket final melalui build-tablet.sh dengan opsi MOTO_AAPT2 berhasil pada Linux x86_64.
- APK: id.gbedeveloper.motobile2, versionCode 2, versionName 1.1-local; min SDK 30, target/compile SDK 34.
- Signature APK v2 terverifikasi dengan apksigner; checksum SHA-256 sesuai.
- Seluruh aset web berada di app/src/main/assets/public, tanpa server aplikasi eksternal.

Pengujian di atas membuktikan struktur Android dan mekanisme override AAPT2 dapat dibuild dengan toolchain Linux yang tersedia. Tidak membuktikan ketersediaan toolchain di tablet: build ARM64, kompatibilitas IDE Android, viewer runtime, unduhan browser dan instalasi pada Xiaomi Pad fisik belum diuji. Build tablet memerlukan JDK 17, SDK 34 dan AAPT2 Android ARM64 dari IDE yang mendukungnya. Internet diperlukan untuk dependensi build pertama.
