#!/usr/bin/env bash
# Builds and verifies the signed release APK. Requires keystore.properties (see make-keystore.sh).
set -euo pipefail
cd "$(dirname "$0")/.."
[ -f keystore.properties ] || { echo "Missing keystore.properties - run scripts/make-keystore.sh first."; exit 1; }
./gradlew --no-daemon clean assembleRelease
APK=$(ls app/build/outputs/apk/release/*.apk | head -1)
"$ANDROID_HOME"/build-tools/*/apksigner verify --verbose "$APK"
echo "Signed APK verified: $APK  (still install and test it on a real phone)"
