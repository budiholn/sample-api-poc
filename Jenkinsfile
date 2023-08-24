pipeline {
environment {
registry = "budiholan/openai-cicd"
registryCredential = 'docker-hub'
dockerImage = ''
}
    agent any
    tools {
        maven "maven"
        jdk "jdk8"
    }
    stages {
        stage('Initialize'){
            steps{
                echo "PATH = ${M2_HOME}/bin:${PATH}"
                echo "M2_HOME = /opt/maven"
            }
        }
        stage('Build') {
            steps {
                dir("/var/lib/jenkins/workspace/sample-api-poc") {
                sh 'mvn -B -DskipTests clean package'
                }
            }
        }

stage('Building our image') {
steps{
script {
dockerImage = sudo docker.build registry + ":$BUILD_NUMBER"
}
}
}
stage('Deploy our image') {
steps{
script {
docker.withRegistry( '', registryCredential ) {
dockerImage.push()
}
}
}
}
stage('Cleaning up') {
steps{
sh "docker rmi $registry:$BUILD_NUMBER"
}
}
     }
    post {
       always {
          junit(
        allowEmptyResults: true,
        testResults: '*/test-reports/.xml'
      )
      }
   } 
}
