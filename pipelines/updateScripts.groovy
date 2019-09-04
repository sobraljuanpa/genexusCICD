node {
    
    properties([pipelineTriggers([pollSCM('* * * * *')])])
    
    stage('Clone repository contents') {
        git url: 'https://github.com/sobraljuanpa/genexusCICD.git'
    }
    
    stage('Move scripts to local scripts folder') {
        powershell label: '', script: 'Move-Item -Path .\\scripts\\* -Destination C:\\scripts\\'
    }
    
}