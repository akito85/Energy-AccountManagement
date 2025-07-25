@echo off
echo Build Java in development environment
mvn clean install || exit /b 1