pipeline {
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
                sh \cp target/ApacheCXF-XML-JSON.war ./
                }
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
