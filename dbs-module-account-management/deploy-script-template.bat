@echo off
findstr "based on spring.profiles.active"  .\src\main\resources\application.yml &:: parameter validation for environment

if errorlevel 1 (
    @echo Uninstall Process Failed, please change environment to spring.profiles.active!!
) else (
    docker build -t registry.pgn.co.id/billing-dev/dbs-module-account:latest . &:: build image using docker.
    docker push registry.pgn.co.id/billing-dev/dbs-module-account:latest &:: push image to registry.

    set KUBECONFIG="..\USING-PATH-OF-ENV-FILE-FOR-KUBERNETES-CLUSTER" & :: environment config by set the env file path for kubernetes cluster.

    @echo "uninstall service"
    helm uninstall dbs-module-account --namespace=USING-DESIRED-NAMESPACE > .\helm-output\uninstall_response.txt &:: uninstall helm module.

    findstr "uninstalled" .\helm-output\uninstall_response.txt
    if errorlevel 1 (
        @echo Uninstall Process Failed !!
    ) else (
        @echo "re-install account-service"
        helm install dbs-module-account .\dbs-module-account\ --namespace=USING-DESIRED-NAMESPACE > .\helm-output\install_response.txt &:: install helm module.

        findstr "STATUS: deployed" .\helm-output\install_response.txt &:: validate helm module is installed.
        if errorlevel 1 (
           @echo Install Process Failed !!
        ) else (
           @echo Install Process Succeeded !!
           del .\helm-output\uninstall_response.txt
           del .\helm-output\install_response.txt
        )
    )
)