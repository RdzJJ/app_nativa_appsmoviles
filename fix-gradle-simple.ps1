# Script simple para resolver problemas de Gradle
Write-Host "Solucionando problemas de Gradle..." -ForegroundColor Green

# Limpiar caché de Gradle
Write-Host "Limpiando caché de Gradle..." -ForegroundColor Yellow
$gradleHome = "$env:USERPROFILE\.gradle"
if (Test-Path $gradleHome) {
    Remove-Item -Path "$gradleHome\caches" -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -Path "$gradleHome\wrapper\dists\gradle-8.5-bin" -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -Path "$gradleHome\wrapper\dists\gradle-8.4-bin" -Recurse -Force -ErrorAction SilentlyContinue
    Write-Host "Caché limpiada" -ForegroundColor Green
}

# Verificar Java
Write-Host "Verificando Java..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "Java encontrado: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "Java no encontrado en PATH" -ForegroundColor Red
}

Write-Host "Configuración completada!" -ForegroundColor Green
Write-Host "Ahora abre Android Studio y sincroniza el proyecto" -ForegroundColor Cyan

