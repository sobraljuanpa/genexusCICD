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
            serverURL: 'https://sandbox.genexusserver.com/v15'])
    }

}