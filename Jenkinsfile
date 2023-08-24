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

    stage('Deploy image') {
	steps {
             sh "sed -i 's|<TheTag>|$BUILD_NUMBER|g' sample-api-poc.yaml"
             sh "scp sample-api-poc.yaml "+KUBE_HOST_USER+"@"+KUBE_IP+":/home/rancher/"
             //sh 'ssh '+KUBE_HOST_USER+'@'+KUBE_IP+' export KUBECONFIG=kube_config_cluster.yml && kubectl apply -f sample-api-poc.yaml'
	     //sh "ssh -tt "+KUBE_HOST_USER+"@"+KUBE_IP+" 'export KUBECONFIG=kube_config_cluster.yml';'kubectl apply -f sample-api-poc.yaml'"
	}
    }

        /*stage('SSH to Remote Server') {
            steps {
                script {
                    // Define the remote server configuration
                    def remote = [:]
                    remote.name = 'RemoteServer'
                    remote.host = KUBE_IP
                    remote.user = KUBE_HOST_USER
                    remote.port = 22
                    remote.allowAnyHosts = true
                    remote.identityFile = credentials('jenkins-rancher')
                    
                    // Define commands to execute on the remote server
                    def commands = """
                        echo 'Hello from Jenkins!'
                        ls -al
			export KUBECONFIG=kube_config_cluster.yml
                        kubectl apply -f sample-api-poc.yaml
                    """
                    
                    // Execute commands on the remote server
                    sshCommand remote: remote, command: commands
                }
            }
        }*/
	  
    stage('Remote SSH') {
    	steps{
             sshagent(credentials : ['jenkins-rancher']) {
             //sh 'ssh '+KUBE_HOST_USER+'@'+KUBE_IP+' export KUBECONFIG=kube_config_cluster.yml && kubectl apply -f sample-api-poc.yaml'
	     sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP+" 'export KUBECONFIG=kube_config_cluster.yml' && 'kubectl apply -f sample-api-poc.yaml'"
	     //sh "ssh "+KUBE_HOST_USER+"@"+KUBE_IP+" 'kubectl apply -f sample-api-poc.yaml'"
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
