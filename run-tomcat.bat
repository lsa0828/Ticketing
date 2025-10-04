@echo off
REM 1. Maven 빌드
echo === Maven Clean Package ===
call mvn clean package
IF %ERRORLEVEL% NEQ 0 (
    echo Maven build failed!
    exit /b %ERRORLEVEL%
)

REM 2. Tomcat 배포 경로
SET WAR_FILE=target\ticketing-system.war
SET DEPLOY_PATH=%TOMCAT_HOME%\webapps\ticketing-system.war

REM 3. 기존 WAR 제거
IF EXIST "%DEPLOY_PATH%" (
    echo Deleting old WAR...
    DEL /Q "%DEPLOY_PATH%"
    RMDIR /S /Q "%TOMCAT_HOME%\webapps\ticketing-system"
)

REM 4. 새 WAR 복사
echo Copying WAR...
COPY "%WAR_FILE%" "%DEPLOY_PATH%"

REM 5. Tomcat 실행
echo Starting Tomcat...
START "" "%TOMCAT_HOME%\bin\startup.bat"

echo === Done! Visit http://localhost:8090/ticketing-system ===
pause
