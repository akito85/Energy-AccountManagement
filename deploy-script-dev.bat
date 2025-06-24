@echo off

powershell -ExecutionPolicy Bypass -File "..\tools-checker.ps1"

REM Check the exit code of the PowerShell script
IF %ERRORLEVEL% NEQ 0 (
    echo One or more required tools are missing. Exiting...
    exit /b %ERRORLEVEL%
)
echo All required tools are installed. Continuing with the deployment process...

findstr "dev"  .\src\main\resources\application.yml
if errorlevel 1 (
    @echo Uninstall Process Failed, please change environment to dev!!
) else (
    call build-dev.bat || exit /b 1
    timeout /t 5 /nobreak
    
    docker build -t registry.pgn.co.id/billing-dev/dbs-module-account:latest .
    docker push registry.pgn.co.id/billing-dev/dbs-module-account:latest

    set KUBECONFIG="..\config"
    @echo "uninstall account-service"
    helm uninstall dbs-module-account --namespace=crm-development-space > .\helm-output\uninstall_response.txt

    findstr "uninstalled" .\helm-output\uninstall_response.txt
    if errorlevel 1 (
        @echo Uninstall Process Failed, please run file init-deploy-stag.bat to type in cli like this ".\init-deploy-dev.bat"
    ) else (
        @echo "re-install account-service"
        helm install dbs-module-account .\dbs-module-account\ --namespace=crm-development-space > .\helm-output\install_response.txt

        findstr "STATUS: deployed" .\helm-output\install_response.txt
        if errorlevel 1 (
            @echo Install Process Failed !!
        ) else (
            @echo Install Process Succeeded !!
            del .\helm-output\uninstall_response.txt
            del .\helm-output\install_response.txt
        )
    )
)
