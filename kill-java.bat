@echo off
echo Killing all Java processes...
for /f "tokens=2" %%i in ('tasklist /fi "imagename eq java.exe" /fo csv ^| findstr java') do (
    echo Killing Java process with PID %%i
    taskkill /F /PID %%i >nul 2>&1
)
echo All Java processes terminated.
pause

