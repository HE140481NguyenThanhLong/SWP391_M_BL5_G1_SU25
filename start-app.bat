@echo off
echo Starting Smart Shop Application...
echo.

echo Step 1: Killing existing Java processes...
for /f "tokens=2" %%i in ('tasklist /fi "imagename eq java.exe" /fo csv ^| findstr java') do (
    echo   Killing Java process PID %%i
    taskkill /F /PID %%i >nul 2>&1
)

echo Step 2: Checking if port 8081 is free...
netstat -ano | findstr :8081 >nul
if %errorlevel% == 0 (
    echo   Port 8081 is still in use, waiting 3 seconds...
    timeout /t 3 >nul
) else (
    echo   Port 8081 is free
)

echo Step 3: Starting Spring Boot application...
echo.
mvnw.cmd spring-boot:run

pause
