@echo off
REM Check if Docker is installed
where docker >nul 2>nul
if %errorlevel% neq 0 (
    echo Docker is not installed or not in PATH. Please install Docker first.
    exit /b 1
)

REM Check if Docker service is running
docker info >nul 2>nul
if %errorlevel% neq 0 (
    echo Docker service is not running. Please start Docker service.
    exit /b 1
)

REM Check if we can connect to Docker daemon
docker ps >nul 2>nul
if %errorlevel% neq 0 (
    echo Cannot connect to Docker daemon. Please check Docker service status.
    exit /b 1
)

findstr "staging"  .\src\main\resources\application.yml
if errorlevel 1 (
    @echo Uninstall Process Failed, please change environment to stag!!
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

    echo All dependent modules built successfully. Building account management module...
    call build-stag.bat || exit /b 1
    timeout /t 5 /nobreak
    
    echo Building Docker image...
    docker build -t registry.pgn.co.id/billing-dev/dbs-module-account:latest .
    if %errorlevel% neq 0 (
        echo Docker build failed!
        exit /b 1
    )

    echo Pushing Docker image...
    docker push registry.pgn.co.id/billing-dev/dbs-module-account:latest
    if %errorlevel% neq 0 (
        echo Docker push failed!
        exit /b 1
    )

    set KUBECONFIG="..\pgnbilling-access\pgnbilling-billing.kubeconfig"
    @echo "uninstall account-service"
    helm uninstall dbs-module-account --namespace=billing-pgnbilling-staging > .\helm-output\uninstall_response.txt

    findstr "uninstalled" .\helm-output\uninstall_response.txt
    if errorlevel 1 (
        @echo Uninstall Process Failed, please run file init-deploy-stag.bat to type in cli like this ".\init-deploy-stag.bat"
    ) else (
        @echo "re-install account-service"
        helm install dbs-module-account .\dbs-module-account\ --namespace=billing-pgnbilling-staging > .\helm-output\install_response.txt

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