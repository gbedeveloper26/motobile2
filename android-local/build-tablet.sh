#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
if [[ -n "${JAVA_HOME:-}" ]]; then export PATH="$JAVA_HOME/bin:$PATH"; fi
if ! command -v java >/dev/null; then
  echo 'Java belum tersedia. Siapkan JDK 17 melalui toolchain IDE Android Anda.' >&2; exit 1
fi
java_version=$(java -version 2>&1)
if [[ "$java_version" != *'"17.'* ]]; then
  echo "Build ini membutuhkan Java 17. Terdeteksi: $java_version" >&2; exit 1
fi
sdk_dir="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
if [[ -z "$sdk_dir" ]]; then
  for candidate in "$HOME/android-sdk" "$HOME/.androidide/android-sdk"; do
    if [[ -d "$candidate/platforms" ]]; then sdk_dir="$candidate"; break; fi
  done
fi
if [[ -n "$sdk_dir" ]]; then
  export ANDROID_HOME="$sdk_dir" ANDROID_SDK_ROOT="$sdk_dir"
fi
# AndroidIDE normally configures its own native AAPT2. Explicitly pass it when found.
aapt2_path="${MOTO_AAPT2:-}"
if [[ -z "$aapt2_path" && -d "$HOME/.androidide" ]]; then
  while IFS= read -r candidate; do
    if [[ -x "$candidate" ]] && "$candidate" version >/dev/null 2>&1; then
      aapt2_path="$candidate"; break
    fi
  done < <(find "$HOME/.androidide" -type f -name aapt2 2>/dev/null)
fi
args=()
if [[ -n "$aapt2_path" ]]; then
  if ! "$aapt2_path" version >/dev/null 2>&1; then
    echo 'AAPT2 tidak dapat dijalankan. Pilih binary Android ARM64, bukan binary Linux x86_64.' >&2; exit 1
  fi
  args+=("-Pandroid.aapt2FromMavenOverride=$aapt2_path")
fi
if [[ "$(uname -m)" == 'aarch64' && ${#args[@]} -eq 0 ]]; then
  echo 'AAPT2 ARM64 belum ditemukan. Dari terminal IDE, jalankan:' >&2
  echo 'MOTO_AAPT2=/path/ke/aapt2-arm64 bash build-tablet.sh' >&2
  echo 'Jangan memakai AAPT2 x86_64 dari SDK desktop. Gunakan toolchain yang disediakan IDE Android.' >&2
  exit 1
fi
chmod +x gradlew
./gradlew --no-daemon "${args[@]}" assembleDebug
mkdir -p APK
cp app/build/outputs/apk/debug/app-debug.apk APK/MOTO-LAB-ID-Android11-debug.apk
(cd APK && sha256sum MOTO-LAB-ID-Android11-debug.apk > SHA256SUMS.txt)
echo 'SELESAI: APK/MOTO-LAB-ID-Android11-debug.apk'
