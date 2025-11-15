@echo off
REM Batch script to clean the build directory and stop Gradle daemons
REM This helps resolve Windows file deletion issues

echo Stopping Gradle daemons...
call gradlew.bat --stop >nul 2>&1

echo Waiting for processes to release file locks...
timeout /t 2 /nobreak >nul

echo Cleaning build directories...
if exist "app\build" (
    rmdir /s /q "app\build" 2>nul
    echo Deleted app\build directory
)

if exist "build" (
    rmdir /s /q "build" 2>nul
    echo Deleted build directory
)

echo.
echo Cleanup complete! You can now rebuild your project in Android Studio.
pause




