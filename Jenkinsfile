pipeline {
  environment {
    DOCKERHUB_IMAGE = "budiholan/jenkins-api-poc"
    KUBE_HOST_USER = "rancher"
    KUBE_IP1 = "192.168.160.211"
    KUBE_IP4 = "192.168.160.214"
    KUBE_IP5 = "192.168.160.215"
    KUBE_IP6 = "192.168.160.216"
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
	     sh """
		i=\$BUILD_NUMBER
		shift=1
		result=\$(echo "\$i - \$shift" | bc)
		sed -i "s|<TheOldTag>|\$result|g" delete-old-image-registry.sh
	      """

             sh "scp sample-api-poc.yaml "+KUBE_HOST_USER+"@"+KUBE_IP1+":/home/rancher/"
	     sh "scp deploy-jenkins-rancher.sh "+KUBE_HOST_USER+"@"+KUBE_IP1+":/home/rancher/"
	     sh "scp delete-old-image-registry.sh "+KUBE_HOST_USER+"@"+KUBE_IP4+":/home/rancher/"
	     sh "scp delete-old-image-registry.sh "+KUBE_HOST_USER+"@"+KUBE_IP5+":/home/rancher/"
	     sh "scp delete-old-image-registry.sh "+KUBE_HOST_USER+"@"+KUBE_IP6+":/home/rancher/"
	}
    }
 
    stage('Remote SSH yaml deployment') {
    	steps{
             sshagent(credentials : ['jenkins-rancher']) {
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP1+" chmod +x -R deploy-jenkins-rancher.sh"
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP1+" chmod +x -R delete-old-image-registry.sh"
		     
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP1+" ./deploy-jenkins-rancher.sh"
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP4+" ./delete-old-image-registry.sh"
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP5+" ./delete-old-image-registry.sh"
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP6+" ./delete-old-image-registry.sh"

	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP1+" rm deploy-jenkins-rancher.sh"
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP1+" rm sample-api-poc.yaml"
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP4+" rm delete-old-image-registry.sh"
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP5+" rm delete-old-image-registry.sh"
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP6+" rm delete-old-image-registry.sh"
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
