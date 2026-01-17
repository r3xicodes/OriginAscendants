# OriginAscendants Build Script
$projectRoot = "c:\Users\Cosmo\OriginAscendants"
$srcDir = "$projectRoot\src\main\java"
$classesDir = "$projectRoot\build\classes\java\main"
$jarDir = "$projectRoot\build\libs"
$resourcesDir = "$projectRoot\src\main\resources"

# Ensure directories exist
if (-not (Test-Path $classesDir)) {
    New-Item -ItemType Directory -Path $classesDir -Force | Out-Null
}
if (-not (Test-Path $jarDir)) {
    New-Item -ItemType Directory -Path $jarDir -Force | Out-Null
}

Write-Host "=== OriginAscendants Build Script ===" -ForegroundColor Green
Write-Host "Project Root: $projectRoot"
Write-Host "Source Dir: $srcDir"
Write-Host "Classes Dir: $classesDir"

# Check Java version
Write-Host "`nChecking Java..."
$javaVersion = java -version 2>&1 | Select-String "version"
if ($javaVersion) {
    Write-Host "Java found: $javaVersion"
} else {
    Write-Host "ERROR: Java not found!" -ForegroundColor Red
    exit 1
}

# Find Paper API JAR directly from known location
Write-Host "`nLocating Paper API JAR..."
$paperJarPath = "C:\Users\Cosmo\.gradle\caches\modules-2\files-2.1\io.papermc.paper\paper-api\1.21.11-R0.1-SNAPSHOT\561e40c9ffeeb4114f82c5f01364a0d1f0487812\paper-api-1.21.11-R0.1-SNAPSHOT.jar"

if (Test-Path $paperJarPath) {
    Write-Host "Paper API JAR found: $paperJarPath"
} else {
    # Fallback: search for it
    Write-Host "Paper API not found at expected location. Searching..."
    $paperJarPath = Get-ChildItem -Path "C:\Users\Cosmo\.gradle\caches" -Filter "paper-api*.jar" -Recurse -ErrorAction SilentlyContinue | 
        Where-Object { $_.Name -notlike "*sources*" -and $_.Name -notlike "*javadoc*" } | 
        Select-Object -First 1 -ExpandProperty FullName
    
    if ($paperJarPath) {
        Write-Host "Paper API JAR found: $paperJarPath"
    } else {
        Write-Host "ERROR: Paper API JAR not found!" -ForegroundColor Red
        exit 1
    }
}

$classpath = $paperJarPath

# Get all Java files
Write-Host "`nScanning for Java files..."
$javaFiles = Get-ChildItem -Path $srcDir -Filter "*.java" -Recurse
$fileCount = $javaFiles.Count
Write-Host "Found $fileCount Java files to compile"

# Compile with Paper API on classpath
Write-Host "`nCompiling Java sources..."
Write-Host "Running: javac -encoding UTF-8 -d `"$classesDir`" -cp `"$classpath`" [all java files]"

# Execute compilation
& javac -encoding UTF-8 -d $classesDir -cp $classpath @($javaFiles.FullName)

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Compilation failed with exit code $LASTEXITCODE" -ForegroundColor Red
    exit 1
}

Write-Host "Compilation successful!" -ForegroundColor Green

# Copy resources
Write-Host "`nCopying resources..."
if (Test-Path $resourcesDir) {
    Get-ChildItem -Path $resourcesDir | Copy-Item -Destination $classesDir -Force -Recurse
    Write-Host "Resources copied"
}

# Create JAR
Write-Host "`nCreating JAR file..."
$jarPath = "$jarDir\originascendants.jar"

try {
    # Use jar command from JDK
    jar cfm $jarPath <(Write-Output "Manifest-Version: 1.0`nMain-Class: org.originsascendants.originAscendants.OriginAscendants`nClass-Path: .") -C $classesDir .
    
    if (Test-Path $jarPath) {
        $jarSize = (Get-Item $jarPath).Length / 1MB
        Write-Host "JAR created successfully: $jarPath" -ForegroundColor Green
        Write-Host "JAR size: $([math]::Round($jarSize, 2)) MB"
    } else {
        Write-Host "ERROR: JAR file was not created" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "ERROR: Failed to create JAR: $_" -ForegroundColor Red
    exit 1
}

Write-Host "`n=== Build Complete ===" -ForegroundColor Green
Write-Host "JAR location: $jarPath"
    exit 1
}

Write-Host "`n=== Build Complete ===" -ForegroundColor Cyan
