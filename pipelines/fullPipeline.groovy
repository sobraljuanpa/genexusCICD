node {

    try{

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
            bat script:'MSBuild.exe C:\\scripts\\runUITests.msbuild /p:KBPath=C:\\Models\\GxTestSample16;GXServerUser=local\\admin;GXServerPass=admin123;JUnitTestFilePath=C:\\results'
        }

    }catch (e) {
        
        throw e

    } finally {

        //#$TestResults = Get-ChildItem -Path "C:\\results\\" -Name *.xml | Sort-Object LastAccessTime -Ascending | Select-Object -First 1
        //#$latestUnit = $TestResults
        //#$latestUI = $TestResults[1]
        //#Copy-Item -Path "C:\\results\\$latestUnit" -Destination $WORKSPACE
        //#Copy-Item -Path "C:\\results\\$latestUI" -Destination $WORKSPACE

        stage('Copy test results to workspace'){
            powershell label: '', script: '''
            Copy-Item -Path "C:\\results\\*xml" -Destination $WORKSPACE
            Copy-Item -Path "C:\\results\\*.txt" -Destination $WORKSPACE
            '''
        }

        stage('Generate Test report'){
            junit '*.xml'
        }

        stage('Notify via email'){
            emailext attachLog: true, attachmentsPattern: '*.txt', body: 'Job ${JOB_NAME} build ${BUILD_NUMBER}\n More info at: ${BUILD_URL}', subject: '${PROJECT_NAME} - Build # ${BUILD_NUMBER} - ${BUILD_STATUS}', to: 'juan.sobral@abstracta.com.uy'
        }

    }

}