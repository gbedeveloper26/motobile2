# MOTO LAB ID — build lokal Android

Proyek Android lokal ada di **[android-local/](android-local/)**. Folder ini sudah menyertakan kode Java Capacitor dan aset aplikasi; tidak memerlukan npm, Next.js build, atau GitHub Actions untuk membuat APK.

## Xiaomi Pad

Gunakan IDE Android di tablet dengan JDK 17, SDK platform 34, build-tools 34.0.0, dan AAPT2 Android ARM64. Buka folder `android-local/`, lalu dari terminal IDE jalankan:

```bash
bash build-tablet.sh
```

Hasilnya: `android-local/APK/MOTO-LAB-ID-Android11-debug.apk`.

[Petunjuk lengkap, struktur, dan pemecahan error tablet](android-local/README.md).

[Unduhan proyek lokal dan APK](https://github.com/gbedeveloper26/motobile2/releases/tag/android-local-v1.1) — gunakan file `.apk` untuk memasang aplikasi; ZIP proyek untuk edit/build. Android minimum 11/API 30. Pengujian build dilakukan di Linux Java 17; toolchain ARM64 dan pemasangan pada Xiaomi Pad fisik belum diuji.

## Sumber fitur

Kode Next.js/React tetap ada di `src/`. Katalog motor, modifikasi, tampilan 3D, dan pemeriksaan kompatibilitas dipertahankan. Pencahayaan viewer menggunakan lampu lokal sehingga tidak perlu unduh HDR dari internet. Kode Android utama ada di `android-local/app/src/main/java/id/gbedeveloper/motobile2/`.

Untuk mengedit UI di lingkungan Node.js yang mendukung Next.js:

```bash
npm ci
npm run android:sync
```

Perintah ini membangun ulang aset web dan menyalinnya ke proyek Android lokal. Build APK tetap melalui Gradle di folder `android-local`. Jangan menjalankan `cap sync` karena struktur lokal menggunakan runtime Java yang disertakan di `vendor/`.

GitHub Actions bersifat opsional/manual dan tidak lagi dipicu setiap push. Model 3D masih ilustratif, bukan replika CAD pabrikan; hasil pemeriksaan kompatibilitas tidak menggantikan pengukuran part di bengkel.
