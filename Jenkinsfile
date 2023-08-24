pipeline {
  environment {
    DOCKERHUB_IMAGE = "budiholan/jenkins-api-poc"
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

    stage('Build image') {
	steps {
	  script {
	    dockerImage = docker.build DOCKERHUB_IMAGE+":$BUILD_NUMBER"
          }
	}
    }
    
    stage('Push image') {
	steps {
	  script {
	    docker.withRegistry('', 'dockerhub') {
	    dockerImage.push()
            }
          }
	}
    }

    stage('Cleaning up') {
        steps{
            sh "docker rmi "+DOCKERHUB_IMAGE+":$BUILD_NUMBER"
        }
    }

  }
/*  post {
    always {
      sh 'docker logout'
    }
  }*/
}
