# Script para resolver problemas de Gradle
# Ejecutar como administrador si es necesario

Write-Host "🔧 Solucionando problemas de Gradle..." -ForegroundColor Green

# 1. Limpiar caché de Gradle
Write-Host "1. Limpiando caché de Gradle..." -ForegroundColor Yellow
$gradleHome = "$env:USERPROFILE\.gradle"
if (Test-Path $gradleHome) {
    Remove-Item -Path "$gradleHome\caches" -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -Path "$gradleHome\wrapper\dists\gradle-8.5-bin" -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -Path "$gradleHome\wrapper\dists\gradle-8.4-bin" -Recurse -Force -ErrorAction SilentlyContinue
    Write-Host "   ✅ Caché limpiada" -ForegroundColor Green
} else {
    Write-Host "   ⚠️ Directorio .gradle no encontrado" -ForegroundColor Yellow
}

# 2. Verificar Java
Write-Host "2. Verificando instalación de Java..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "   ✅ Java encontrado: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "   ❌ Java no encontrado en PATH" -ForegroundColor Red
    Write-Host "   💡 Asegúrate de tener Java 17 instalado" -ForegroundColor Cyan
}

# 3. Verificar JAVA_HOME
Write-Host "3. Verificando JAVA_HOME..." -ForegroundColor Yellow
if ($env:JAVA_HOME) {
    Write-Host "   ✅ JAVA_HOME configurado: $env:JAVA_HOME" -ForegroundColor Green
} else {
    Write-Host "   ⚠️ JAVA_HOME no configurado" -ForegroundColor Yellow
    Write-Host "   💡 Configura JAVA_HOME apuntando a Java 17" -ForegroundColor Cyan
}

# 4. Crear directorio .gradle si no existe
Write-Host "4. Verificando directorio .gradle..." -ForegroundColor Yellow
if (-not (Test-Path $gradleHome)) {
    New-Item -ItemType Directory -Path $gradleHome -Force | Out-Null
    Write-Host "   ✅ Directorio .gradle creado" -ForegroundColor Green
} else {
    Write-Host "   ✅ Directorio .gradle existe" -ForegroundColor Green
}

# 5. Configurar propiedades de Gradle
Write-Host "5. Configurando propiedades de Gradle..." -ForegroundColor Yellow
$gradleProps = @"
# Gradle properties optimizadas
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.daemon=true
android.useAndroidX=true
android.nonTransitiveRClass=true
kotlin.code.style=official
"@

$gradleProps | Out-File -FilePath "gradle.properties" -Encoding UTF8
Write-Host "   ✅ gradle.properties actualizado" -ForegroundColor Green

Write-Host "`n🎉 Configuración completada!" -ForegroundColor Green
Write-Host "`n📋 Próximos pasos:" -ForegroundColor Cyan
Write-Host "1. Abre Android Studio" -ForegroundColor White
Write-Host "2. Ve a File > Settings > Build Tools > Gradle" -ForegroundColor White
Write-Host "3. Configura 'Gradle JDK' a Java 17" -ForegroundColor White
Write-Host "4. Haz clic en 'Sync Project with Gradle Files'" -ForegroundColor White
Write-Host "`n💡 Si persisten problemas, reinicia Android Studio" -ForegroundColor Yellow

