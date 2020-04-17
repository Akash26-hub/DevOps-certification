pipeline {
    agent none
    stages {
        
        stage('install puppet on slave') {
            agent { label 'slave'}
            steps {
                echo 'Install Puppet'
                sh "wget -N -O 'puppet.deb' https://apt.puppetlabs.com/puppet6-release-bionic.deb"
                sh "chmod 755 puppet.deb"
                sh "sudo dpkg -i puppet.deb"
                sh "sudo apt update"
                sh "sudo apt install -y puppet-agent"
            }
        }

        stage('configure and start puppet') {
            agent { label 'slave'}
            steps {
                echo 'configure puppet'
                sh "mkdir -p /etc/puppetlabs/puppet"
                sh "if [ -f /etc/puppetlabs/puppet/puppet.conf ]; then sudo rm -f /etc/puppetlabs/puppet.conf; fi"
                sh "echo '[main]\ncertname = node1.local\nserver = puppet' >> ~/puppet.conf"
                sh "sudo mv ~/puppet.conf /etc/puppetlabs/puppet/puppet.conf"
                echo 'start puppet'
                sh "sudo systemctl start puppet"
                sh "sudo systemctl enable puppet"
            }
        }

        stage('Install Docker on slave through puppet') {
            agent{ label 'slave'}
            steps {
                sh "sudo /opt/puppetlabs/bin/puppet module install garethr-docker"
                sh "sudo /opt/puppetlabs/bin/puppet apply /home/jenkins/jenkins_slave/workspace/Certification/dockerce.pp"
            }
        }

        stage('Git Checkout') {
            agent{ label 'slave'}
            steps {
                sh "if [ ! -d '/home/jenkins/jenkins_slave/workspace/Certification' ]; then git clone https://github.com/Akash26-hub/DevOps-certification.git /home/jenkins/jenkins_slave/workspace/Certification ; fi"
                sh "cd /home/jenkins/jenkins_slave/workspace/Certification && sudo git checkout master"
            }
        }
        
        stage('Docker Build and Run') {
            agent{ label 'slave'}
            steps {
                sh "sudo docker rm -f webapp || true"
                sh "cd /home/jenkins/jenkins_slave/workspace/Certification && sudo docker build -t test ."
                sh "sudo docker run -it -d --name webapp -p 1998:80 test"
            }
        }

		stage('Setting Prerequisite for Selenium') {
            agent{ label 'slave'}
            steps {
                sh "wget -N -O 'firefox-57.0.tar.bz2' http://ftp.mozilla.org/pub/firefox/releases/57.0/linux-x86_64/en-US/firefox-57.0.tar.bz2"
				sh "tar -xjf firefox-57.0.tar.bz2"
				sh "rm -rf /opt/firefox"
				sh "sudo mv firefox /opt/"
				sh "sudo mv /usr/bin/firefox /usr/bin/firefox_old"
				sh "sudo ln -s /opt/firefox/firefox /usr/bin/firefox"
            }
        }

        stage('Check if selenium test run') {
            agent{ label 'slave'}
            steps {
		sh "cd /home/jenkins/jenkins_slave/workspace/Certification/"
		sh "java -jar certification-project-1.0-SNAPSHOT-jar-with-dependencies.jar --headless"
            	}
            post {
                failure {
                    sh "echo Failure"
					sh "sudo docker rm -f webapp"
                }
			}
		}
	}
}
