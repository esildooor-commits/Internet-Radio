Param()

$imageName = 'waveai-android-builder'
Write-Host "Building Docker image $imageName..."
docker build -t $imageName .

Write-Host "Running container to build APK (workspace mounted)..."
docker run --rm -v "${PWD}:/workspace" -w /workspace $imageName bash -lc "gradle wrapper || true; ./gradlew assembleDebug --no-daemon"

Write-Host "Fertig. APKs (wenn erfolgreich) liegen unter app/build/outputs/apk/."
