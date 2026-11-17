# MOTO LAB ID — Native Android
Aplikasi Android **lokal/native Kotlin** (bukan Next.js, Capacitor, atau WebView). Minimum Android 11 (API 30).

## Buka di Android Studio
Clone repo → Open folder root → tunggu Gradle sync → Build > Build APK(s). APK debug ada di `app/build/outputs/apk/debug/app-debug.apk`.

Bisa juga dari tab GitHub **Actions**: pilih **Android native APK**, lalu download artifact bila build berhasil.

## Fitur MVP
- Satu motor saja di layar, pilihan Honda BeAT Street 2019/2020/2026, BeAT, Vario, Yamaha dan lainnya.
- Geser kiri/kanan untuk memutar ilustrasi motor dari berbagai sudut; pinch untuk zoom.
- Ban ukuran 70/90, 80/90, 90/90, 90/100, 100/90, 110/90, 120/80, velg 12 atau 14 inci.
- Filter Muttaru, spion, knalpot, body swap, aksesoris; simpan konfigurasi lokal.
- Cek ukuran ban dan velg; perubahan dudukan/mounting yang belum diverifikasi diberi status perlu inspeksi bengkel.

**Batasan penting:** Canvas drawing adalah ilustrasi rotasi 2.5D, **bukan model 3D asli motor**. Untuk detail yang benar-benar identik, perlu aset 3D GLB tiap tipe/tahun, dan renderer native seperti Filament. Fitur yang belum punya model aktual hanya mengubah konfigurasi/label, bukan geometrinya. Pemeriksaan ukuran tidak menggantikan pengukuran fisik dan rekomendasi pabrikan.
