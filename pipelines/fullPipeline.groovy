node {

    properties([pipelineTriggers([pollSCM('* * * * *')])])

    stage('Update KB') {
        checkout(
            [$class: 'GeneXusServerSCM',
            credentialsId: 'GXServerCredentials',
            gxInstallationId: 'genexus',
            kbDbCredentialsId: '',
            kbDbInSameFolder: true,
            kbDbName: '',
            kbDbServerInstance: '',
            kbName: 'GXTestSample16',
            kbVersion: '',
            localKbPath: '',
            localKbVersion: '',
            serverURL: 'http://172.40.11.254/genexusserverbeta_2'])
    }

    stage('Analize code quality') {
        bat script: 'MSBuild.exe C:\\scripts\\analizeCode.msbuild /t:ReviewObjects /p:KBPath=C:\\Models\\GXTestSample16 > C:\\results\\KBDoctorAnalysis.txt'
    }

    stage('BuildAll and run unit tests') {
        bat script: 'MSBuild.exe C:\\scripts\\runUnitTests.msbuild /p:KBPath=C:\\Models\\GxTestSample16;GXServerUser=local\\admin;GXServerPass=admin123;JUnitTestFilePath=C:\\results'
    }

    stage('Build Artifact') { // The last parameter must be the name of the main object in the KB
        bat script:'C:\\scripts\\buildWar.bat C:\\Models\\GXTestSample16 JavaModel SampleApp SampleApp Home'
    }

    stage('Deploy Artifact') {

    }

    stage('Run UI Tests') {
        bat script:'MSBuild.exe C:\\scripts\\runUITests.msbuild /p:KBPath=C:\\Models\\GxTestSample16;GXServerUser=local\\admin;GXServerPass=admin123;JUnitTestFilePath=C:\\results'
    }

}