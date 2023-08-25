def time = "10"
pipeline {
  environment {
    DOCKERHUB_IMAGE = "budiholan/jenkins-api-poc"
    KUBE_HOST_USER = "rancher"
    KUBE_IP = "192.168.160.211"
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
    stage('Build Source') {
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
	    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub') {
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

    stage('Remote SCP') {
	steps {
             sh "sed -i 's|<TheTag>|$BUILD_NUMBER|g' sample-api-poc.yaml"
	     sh "sed -i 's|<TheTag>|$BUILD_NUMBER-1|g' delete-old-image-registry.sh"
             sh "scp sample-api-poc.yaml "+KUBE_HOST_USER+"@"+KUBE_IP+":/home/rancher/"
	     sh "scp deploy-jenkins-rancher.sh "+KUBE_HOST_USER+"@"+KUBE_IP+":/home/rancher/"
	     sh "scp delete-old-image-registry.sh "+KUBE_HOST_USER+"@"+KUBE_IP+":/home/rancher/"
	}
    }
 
    stage('Remote SSH yaml deployment') {
    	steps{
             sshagent(credentials : ['jenkins-rancher']) {
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP+" chmod +x -R deploy-jenkins-rancher.sh"
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP+" ./deploy-jenkins-rancher.sh"
             }
    	}
    }

    stage ('wait_prior_starting_smoke_testing') {
	steps{
    	     echo "Waiting 10 seconds for deployment to complete"
    	     sleep time.toInteger() // seconds
	}
    }

    stage('Remote SSH rmi') {
    	steps{
             sshagent(credentials : ['jenkins-rancher']) {
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP+" chmod +x -R delete-old-image-registry.sh"
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP+" ./delete-old-image-registry.sh"
             }
    	}
    }


  }
  post {
    always {
      sh 'docker logout'
    }
  }
}
