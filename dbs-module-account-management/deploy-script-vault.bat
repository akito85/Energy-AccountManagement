@echo off

REM Check the exit code of the PowerShell script
IF %ERRORLEVEL% NEQ 0 (
    echo One or more required tools are missing. Exiting...
    exit /b %ERRORLEVEL%
)
echo All required tools are installed. Continuing with the deployment process...

findstr "vault"  .\src\main\resources\application.yml
if errorlevel 1 (
    @echo Uninstall Process Failed, please change environment to vault!!
) else (
    echo Building dependent modules...
    
    REM Build dbs-common-base
    echo Building dbs-common-base...
    cd ..\dbs-common-base
    call build.bat
    if %errorlevel% neq 0 (
        echo Failed to build dbs-common-base!
        exit /b 1
    )
    cd ..\dbs-module-account-management

    REM Build dbs-common-library
    echo Building dbs-common-library...
    cd ..\dbs-common-library
    call build.bat
    if %errorlevel% neq 0 (
        echo Failed to build dbs-common-library!
        exit /b 1
    )
    cd ..\dbs-module-account-management

    REM Build dbs-database-crm
    echo Building dbs-database-crm...
    cd ..\dbs-database-crm
    call build.bat
    if %errorlevel% neq 0 (
        echo Failed to build dbs-database-crm!
        exit /b 1
    )
    cd ..\dbs-module-account-management
    
    call build-dev.bat || exit /b 1
    timeout /t 5 /nobreak
    
    docker build -t registry.pgn.co.id/billing-dev/dbs-module-account:latest .
    docker push registry.pgn.co.id/billing-dev/dbs-module-account:latest

    @REM set KUBECONFIG="..\config"
    @REM @echo "uninstall account-service"
    @REM helm uninstall dbs-module-account --namespace=crm-development-space > .\helm-output\uninstall_response.txt

    @REM findstr "uninstalled" .\helm-output\uninstall_response.txt
    @REM if errorlevel 1 (
    @REM     @echo Uninstall Process Failed, please run file init-deploy-stag.bat to type in cli like this ".\init-deploy-dev.bat"
    @REM ) else (
    @REM     @echo "re-install account-service"
    @REM     helm install dbs-module-account .\dbs-module-account\ --namespace=crm-development-space > .\helm-output\install_response.txt

    @REM     findstr "STATUS: deployed" .\helm-output\install_response.txt
    @REM     if errorlevel 1 (
    @REM         @echo Install Process Failed !!
    @REM     ) else (
    @REM         @echo Install Process Succeeded !!
    @REM         del .\helm-output\uninstall_response.txt
    @REM         del .\helm-output\install_response.txt
    @REM     )
    @REM )
)
