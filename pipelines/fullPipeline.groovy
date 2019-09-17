node {

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
        powershell lable: '', script: '''
        Stop-Service -Name "Apache Tomcat 8.5 Tomcat8"
        Remove-Item -Recurse -Force "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\SampleApp"
        Remove-Item -Path "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\SampleApp.war"
        Copy-Item -Path "C:\\Models\\GXTestSample16\\JavaModel\\Deploy\\Local\\SampleApp.war" -Destination "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps"
        Start-Service -Name "Apache Tomcat 8.5 Tomcat8"
        '''
    }

    stage('Run UI Tests') {
    //    bat script:'MSBuild.exe C:\\scripts\\runUITests.msbuild /p:KBPath=C:\\Models\\GxTestSample16;GXServerUser=local\\admin;GXServerPass=admin123;JUnitTestFilePath=C:\\results'
    }
    
    stage('Copy test results to workspace'){
        powershell label: '', script: 'Copy-Item -Path \'C:\\results\\*.xml\' -Destination $WORKSPACE'
    }

    stage('Generate Test report'){
        junit '*.xml'
    }

}