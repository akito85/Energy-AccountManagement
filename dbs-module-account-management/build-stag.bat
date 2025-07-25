@echo off
echo Build Java in staging environment
mvn clean install || exit /b 1