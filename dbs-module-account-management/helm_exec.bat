@REM helm template dbs-module-account .\dbs-module-account\ --version 0.1.0 ^
@REM --namespace=energy-development ^
@REM --set resources.limits.cpu="0.25" ^
@REM --set resources.limits.memory="1Gi" ^
@REM --set resources.requests.cpu="0.25" ^
@REM --set resources.requests.memory="512Mi" ^
@REM --set serviceAccount.name="energy-vault" ^
@REM --set podAnnotations.vault.authPath="auth/energy-development-kubernetes" ^
@REM --set podAnnotations.vault.secretPath="energy-development/data/db_access" ^
@REM --set imagePullSecrets.name="pgnregcred" ^
@REM --set podAnnotations.releaseNamespace="energy-development" ^
@REM --set image.registry_url="registry.pgn.co.id" ^
@REM --set image.name="registry.pgn.co.id/billing-dev/dbs-module-account" ^
@REM --set podAnnotations.vault.role="energy" > deployment.yaml

@REM helm template dbs-module-account .\dbs-module-account\ --version 0.1.0 ^
@REM --namespace=energy-development ^
@REM --set resources.limits.cpu="0.25" ^
@REM --set resources.limits.memory="1Gi" ^
@REM --set resources.requests.cpu="0.25" ^
@REM --set resources.requests.memory="512Mi" ^
@REM --set serviceAccount.name="energy-vault" ^
@REM --set podAnnotations.vault.authPath="auth/energy-development-kubernetes" ^
@REM --set podAnnotations.vault.secretPath="energy-development/data/db_access" ^
@REM --set imagePullSecrets.name="pgnregcred" ^
@REM --set podAnnotations.releaseNamespace="energy-development" ^
@REM --set image.registry_url="registry.pgn.co.id" ^
@REM --set image.image_path="/billing-dev/dbs-module-account" ^
@REM --set image.tag="latest" > .yaml

@REM Secrets:
@REM REGISTRY_USERNAME (provide by pgn) 
@REM REGISTRY_PASSWORD (provide by pgn)
@REM KUBE_CONFIG (in User Management has been configured)

@REM Variables:
@REM APPLICATION_DNS=https://dev-energy.pgn.co.id
@REM REGISTRY_URL=https://registry.pgn.co.id
@REM IMAGE_PULL_SECRET=pgnregcred
@REM KUBE_NAMESPACE=energy-development

@REM INGRESS_TLS_SECRET belum perlu
@REM VAULT_URL--belum perlu
@REM VAULT_SERVICE_ACCOUNT=energy-vault
@REM VAULT_AUTH_PATH=auth/energy-development-kubernetes
@REM VAULT_CONFIG_PATH=energy-development/data/db_access
@REM CPU_LIMIT=0.25
@REM CPU_REQUEST=0.25
@REM MEMORY_LIMIT=1Gi
@REM MEMORY_REQUEST=512Mi
@REM IMAGE_PATH="/energy/dbs-module-account"
@REM IMAGE_TAG="latest"

@REM helm template dbs-module-account .\dbs-module-account\ --version 0.1.0 ^
@REM --namespace=energoutput_accy-development ^
@REM --set service.enabled=true ^
@REM --set deployment.enabled=false ^
@REM --set podAnnotations.releaseNamespace="energy-development" ^
@REM --set podLabels.version="green"


@REM --namespace=billing-pgnbilling-staging

helm package .\dbs-module-account\
helm push .\dbs-module-account-0.1.0.tgz oci://registry.pgn.co.id/billing-dev/helm/dbs-module-account