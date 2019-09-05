node {

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
            serverURL: 'http://172.40.11.254/genexusserverbeta'])
    }

    stage('BuildAll and run unit tests') {
        bat script: 'MSBuild.exe C:\\scripts\\runUnitTests.msbuild /p:KBPath=C:\\Models\\GxTestSample16;GXServerUser=local\\admin;GXServerPass=admin123;JUnitTestFilePath=C:\\results'
    }

    stage('Build Artifact') {
        bat script:'C:\\scripts\\buildWar.bat C:\\Models\\GXTestSample16 JavaModel SampleApp SampleApp BuiltSampleApp'
    }

}