#!/usr/bin/env bash
set -euo pipefail

IMAGE_NAME=waveai-android-builder

echo "Building Docker image $IMAGE_NAME..."
docker build -t "$IMAGE_NAME" .

echo "Running container to build APK (workspace mounted)..."
docker run --rm -v "$(pwd)":/workspace -w /workspace "$IMAGE_NAME" bash -lc \
  "gradle wrapper || true; ./gradlew assembleDebug --no-daemon"

echo "Fertig. APKs (wenn erfolgreich) liegen unter app/build/outputs/apk/."
