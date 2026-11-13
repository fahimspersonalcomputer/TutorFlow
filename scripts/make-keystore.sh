#!/usr/bin/env bash
# Creates YOUR release keystore. Run on your own computer. Never commit the .jks or keystore.properties.
# BACK UP the .jks and both passwords: if lost, you can never publish an update to the same app.
set -euo pipefail
cd "$(dirname "$0")/.."
KS=tutorflow-release.jks
[ -e "$KS" ] && { echo "$KS already exists - refusing to overwrite."; exit 1; }
read -rsp "Choose keystore password: " PW; echo
keytool -genkeypair -v -keystore "$KS" -alias tutorflow -keyalg RSA -keysize 4096 -validity 10000 \
  -storepass "$PW" -keypass "$PW" -dname "CN=Abdullah al Fahim, O=TutorFlow, C=BD"
cat > keystore.properties <<EOF
storeFile=$KS
storePassword=$PW
keyAlias=tutorflow
keyPassword=$PW
EOF
echo "Created $KS and keystore.properties (both are git-ignored). Print the SHA-1 needed for Google Drive setup with:"
echo "  keytool -list -v -keystore $KS -alias tutorflow"
