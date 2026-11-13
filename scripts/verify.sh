#!/usr/bin/env bash
# Runs the checks that make "compiled" and "tested" real. Needs the Android SDK (Android Studio or CI).
# Phase 1 note: written but never executed - there is no Gradle project yet.
set -euo pipefail
cd "$(dirname "$0")/.."
SRC=app/src/main/java/com/tutorflow/app
fail=0

echo "== Architecture rules =="
if [ -d "$SRC/domain" ] && grep -rnE '^import (android\.|androidx\.|com\.google\.android)' "$SRC/domain"; then
  echo "FAIL: domain layer must be pure Kotlin"; fail=1; fi
net=$(grep -rlE 'java\.net\.|okhttp3' "$SRC" 2>/dev/null | grep -v '/data/backup/drive/' || true)
if [ -n "$net" ]; then echo "FAIL: network code outside data/backup/drive:"; echo "$net"; fail=1; fi
if [ -d app/src/release ] && grep -rniE 'DevSeeder|sample student' app/src/main app/src/release 2>/dev/null; then
  echo "FAIL: seed/sample data referenced in main/release sources"; fail=1; fi

echo "== String key parity (en vs bn) =="
if [ -f app/src/main/res/values/strings.xml ] && [ -f app/src/main/res/values-bn/strings.xml ]; then
  diff <(grep -o 'name="[^"]*"' app/src/main/res/values/strings.xml | sort) \
       <(grep -o 'name="[^"]*"' app/src/main/res/values-bn/strings.xml | sort) || { echo "FAIL: string keys differ"; fail=1; }
fi

[ "$fail" -eq 0 ] || exit 1
echo "== Gradle =="
./gradlew --no-daemon assembleDebug testDebugUnitTest lintDebug
echo "OK: build, unit tests and lint passed. (Instrumented/UI tests need an emulator: ./gradlew connectedDebugAndroidTest)"
