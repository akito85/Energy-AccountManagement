@echo off

IF EXIST ".\helm-output\install_response.txt" (
    @echo resetting log file
    del .\helm-output\install_response.txt
)
@echo "installing account-service"
set KUBECONFIG="..\pgnbilling-access\pgnbilling-billing.kubeconfig"
helm install dbs-module-account .\dbs-module-account\ --namespace=billing-pgnbilling-staging > .\helm-output\install_response.txt

findstr "STATUS: deployed" .\helm-output\install_response.txt
if errorlevel 1 (
   @echo Install Process Failed !!
) else (
    @echo Install Process Succeeded !!
    del .\helm-output\install_response.txt
)