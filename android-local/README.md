# MOTO LAB ID — proyek Android lokal untuk Xiaomi Pad

Proyek Android Java/Gradle dengan aset aplikasi disertakan. Viewer 3D, konfigurator modifikasi, dan pemeriksaan kompatibilitas tetap memakai kode aplikasi asli. Shell Android memakai Capacitor 6.2.3 yang sumbernya disertakan di `vendor/`; tidak mengambil kode dari `node_modules` saat build APK. Next.js sudah di-build menjadi aset lokal di `app/src/main/assets/public/`.

**Tidak perlu GitHub Actions, server, npm, atau build Next.js untuk membuat APK dari paket ini.** Unduhan dependensi Gradle/SDK tetap membutuhkan internet pada build pertama. Ini proyek Android dengan UI web lokal, bukan penulisan ulang UI ke XML/Compose. Belum diuji pada Xiaomi Pad fisik.

## Cara paling singkat: pasang APK

Bila hanya ingin memakai aplikasi, instal file `MOTO-LAB-ID-Android11-debug.apk` dari folder `APK/` di ZIP proyek atau unduhan APK terpisah. ZIP proyek ini adalah source code untuk build/edit, bukan file instalasi. Minimum Android 11/API 30.

## Build langsung di Xiaomi Pad

Gunakan IDE Android yang menyediakan Gradle, **JDK 17**, SDK platform **34**, build-tools **34.0.0**, dan **AAPT2 Android ARM64**, misalnya lingkungan AndroidIDE yang sudah dikonfigurasi. Android Studio desktop tidak dapat dipasang langsung di Xiaomi Pad. File Manager saja juga tidak dapat mengompilasi proyek. Dukungan/distribusi toolchain AndroidIDE dapat berubah; paket proyek tidak menyertakan installer IDE maupun toolchain.

1. Pasang/siapkan IDE Android dan toolchain di tablet. Pilih JDK 17 dan SDK 34. Jangan memakai AAPT2 Linux x86_64 pada Android ARM64.
2. Ekstrak ZIP proyek. Pindahkan ke folder proyek milik IDE bila IDE tidak bisa membaca folder Downloads. Hindari folder SD card/external untuk menjalankan Gradle karena beberapa filesystem melarang eksekusi.
3. Buka folder **MOTO-LAB-ID-Android-Lokal** yang langsung berisi `settings.gradle`, `build.gradle`, `gradlew`, dan `app/`. Jangan membuka `web-source/`.
4. Di terminal IDE, periksa `java -version`: harus Java 17.
5. Pastikan SDK terpasang. Bila SDK belum lengkap dan `sdkmanager` tersedia, jalankan:

   ```bash
   sdkmanager 'platforms;android-34' 'build-tools;34.0.0' 'platform-tools'
   sdkmanager --licenses
   ```

6. Dari folder proyek jalankan:

   ```bash
   bash build-tablet.sh
   ```

   Script mencoba mendeteksi SDK dan AAPT2 di `$HOME/.androidide`. Jika AAPT2 tidak ditemukan, tentukan path AAPT2 ARM64 yang disediakan IDE:

   ```bash
   MOTO_AAPT2=/path/yang-benar/ke/aapt2 bash build-tablet.sh
   ```

   `/path/yang-benar/ke/aapt2` harus diganti dengan lokasi binary di tablet Anda; jangan disalin mentah. Bila memakai tombol Build IDE, gunakan pengaturan AAPT2 bawaan IDE atau tambahkan `android.aapt2FromMavenOverride=/lokasi/aapt2` pada `gradle.properties` lokal Anda.

7. Setelah `BUILD SUCCESSFUL`, APK berada di:

   ```text
   APK/MOTO-LAB-ID-Android11-debug.apk
   ```

   Output Gradle asli juga ada di `app/build/outputs/apk/debug/app-debug.apk`. Buka dari File Manager, izinkan instalasi dari sumber tersebut bila Android meminta, lalu instal.

## Struktur

```text
MOTO-LAB-ID-Android-Lokal/
├── settings.gradle
├── build.gradle
├── variables.gradle              # minimum SDK 30, compile/target 34
├── gradlew / gradlew.bat
├── gradle/wrapper/
├── build-tablet.sh
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/id/gbedeveloper/motobile2/MainActivity.java
│       ├── assets/public/         # UI dan 3D lokal, sudah dibangun
│       └── res/
├── vendor/capacitor-android/      # Java runtime + lisensi
└── web-source/                   # sumber Next.js asli untuk edit fitur
```

## Error yang umum di tablet

- **AAPT2 cannot execute / Exec format error**: binary desktop x86_64 sedang digunakan. Pilih AAPT2 Android ARM64 dari IDE melalui `MOTO_AAPT2`.
- **SDK location not found**: set `ANDROID_HOME` sesuai lokasi SDK atau buat `local.properties` dengan `sdk.dir=/lokasi/sdk`.
- **Permission denied**: jalankan `bash build-tablet.sh` dari folder internal proyek IDE; pindahkan dari external storage bila perlu.
- **Java berbeda**: pilih JDK 17, bukan 11 atau 21, pada terminal/toolchain IDE.
- **Gradle kehabisan RAM**: tutup aplikasi lain; workers sudah dibatasi 2.
- **APK tidak dapat diperbarui**: debug key dari komputer/runner lain dapat berbeda. Ekspor desain penting terlebih dahulu sebelum menghapus instalasi lama.

## Edit fitur web (opsional)

Dalam ZIP standalone, sumber UI disimpan di `web-source/`. Dalam repository, sumber UI berada di folder `src/` pada root repository. Perubahan Java dan resource Android dapat dibuild langsung lewat Gradle. Perubahan TypeScript/React memerlukan build Next.js ulang dengan Node.js yang sesuai; aset lama tidak berubah hanya dengan mengedit `.tsx`. Untuk memperbarui aset dari lingkungan Node.js yang mendukung Next.js:

```bash
cd web-source
npm ci
npm run build
```

Lalu salin **isi** `web-source/out/` ke `app/src/main/assets/public/` dengan mengganti aset lama. Build APK lagi dari root proyek. Jangan menjalankan `cap sync` pada paket lokal ini karena dapat membuat ulang struktur berbasis node_modules.

Lisensi Capacitor ada di `vendor/CAPACITOR-LICENSE`. Viewer memakai aset dan pustaka web dari proyek sebelumnya. Model tetap ilustratif, bukan CAD pabrikan. Aturan kompatibilitas tetap berupa pemeriksaan referensi; lihat `web-source/FITMENT.md`.
