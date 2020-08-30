
@Library('secure-deploy') _

//application short name (equals to repo name)
def APP_NAME = 'projectapp'
def ENABLE_DEPLOY = ''

properties([
        disableConcurrentBuilds(),
        [$class: "BuildDiscarderProperty", strategy: [$class: "LogRotator", daysToKeepStr: "30", numToKeepStr: "10"]],
        [$class: "ParametersDefinitionProperty", parameterDefinitions: [
                [$class: "BooleanParameterDefinition", name: "WITH_CHECKS", defaultValue: true]
        ]]
])

def BUILD_ARGS = 'npm install'
def DEPLOY_ENV = ''
def DEPLOY_CREDENTIALS_ID = ''

//derive settings from scm branch name
switch (BRANCH_NAME) {
    case "dev":
        BUILD_ARGS = 'npm install'
        DEPLOY_ENV = 'development'
        // DEPLOY_CREDENTIALS_ID = ''
        ENABLE_DEPLOY = 'true'
        break
    case "master":
        BUILD_ARGS = 'npm install'
        DEPLOY_ENV = 'production'
        // DEPLOY_CREDENTIALS_ID = ''
        ENABLE_DEPLOY = 'true'
        break
}


if (ENABLE_DEPLOY == 'true' ) {
    node('master') {
        stage('Checkout') {
            checkout scm
            // checkoutSecureDeploy()
        }

        def SCM_REVISION = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
        echo "setting SCM_REVISION to '$SCM_REVISION'"

        def BUILD_DATE = sh(returnStdout: true, script: 'date +%Y%m%d-%H%M').trim()
        echo "setting BUILD_DATE to '$BUILD_DATE'"

        def DEPLOY_VERSION = "${BUILD_DATE}-${SCM_REVISION}b${BUILD_NUMBER}".trim()

        def SECURE_TEMP = "/tmp/$BUILD_TAG".trim()

        if (DEPLOY_ENV) {
            stage('Init') {
                setupTempDir tmpDir: SECURE_TEMP,
                        appName: APP_NAME,
                        deployEnv: DEPLOY_ENV,
                        deployVersion: DEPLOY_VERSION,
                        deployCredentialsId: "${DEPLOY_CREDENTIALS_ID}"
            }
        }

        try {
            withEnv(["BUILD_DATE=$BUILD_DATE", "BUILD_NUMBER=$BUILD_NUMBER", "BUILD_ARGS=$BUILD_ARGS"]) {
                stage('Build') {
                    sh "npm install"
                }

                stage('Test') {
                    sh './jenkins/scripts/test.sh'     
                }

                if (BRANCH_NAME == 'dev') {
                    stage('Deliver for development') {
                        secureDeploy tmpDir: SECURE_TEMP,
                                appName: APP_NAME,
                                deployEnv: DEPLOY_ENV,
                                deployVersion: DEPLOY_VERSION
                        sh './jenkins/scripts/deliver-for-development.sh'
                        input message: 'Finished using the web site? (Click "Proceed" to continue)'
                        sh './jenkins/scripts/kill.sh'
                    }
                }
                else if (BRANCH_NAME == 'master') {
                    stage('Deliver for Production') {
                        secureDeploy tmpDir: SECURE_TEMP,
                                appName: APP_NAME,
                                deployEnv: DEPLOY_ENV,
                                deployVersion: DEPLOY_VERSION
                        sh './jenkins/scripts/deliver-for-development.sh'
                        input message: 'Finished using the web site? (Click "Proceed" to continue)'
                        sh './jenkins/scripts/kill.sh'
                    }
                }
                else {
                    echo "Invalid branch"
                }
                
            }
        } catch (e) {
            echo "Build failed: $e"
            currentBuild.result = 'FAILURE'
        } finally {
            if (DEPLOY_ENV) {
                cleanupSecureTemp SECURE_TEMP
            }
        }
    }
} else {
    echo "ENABLE_DEPLOY is not enabled, Branch : ${BRANCH_NAME} is not authorized to perform this build..!"
}