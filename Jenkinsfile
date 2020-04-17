pipeline {
    agent none
    stages {
        
        stage('install puppet on slave') {
            agent { label 'slave'}
            steps {
                echo 'Install Puppet'
                sh "wget https://apt.puppetlabs.com/puppet6-release-bionic.deb"
                sh "chmod 755 puppet6-release-bionic.deb"
                sh "sudo dpkg -i puppet6-release-bionic.deb"
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


        stage('Sign in puppet certificate') {
            agent{ label 'slave'}
            steps {
              catchError {
                sh "sudo /opt/puppetlabs/bin/puppet cert sign node1.local"
              }
            }
        }


        stage('Install Docker-CE on slave through puppet') {
            agent{ label 'slave'}
            steps {
                sh "sudo /opt/puppetlabs/bin/puppet module install garethr-docker"
                sh "sudo /opt/puppetlabs/bin/puppet apply /home/jenkins/jenkins_slave/workspace/Certification/dockerce.pp"
            }
        }

        stage('Git Checkout') {
            agent{ label 'slave'}
            steps {
                sh "if [ ! -d '/home/jenkins/jenkins_slave/workspace/Certification' ]; then git clone https://github.com/Ad013/Certification.git /home/jenkins/jenkins_slave/workspace/Certification ; fi"
                sh "cd /home/jenkins/jenkins_slave/workspace/Certification && git checkout master"
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

        stage('Install Chrome Driver'){
            agent{ label 'slave'}
            steps {
             sh "sudo apt-get update"
             sh "sudo apt-get install -y unzip xvfb libxi6 libgconf-2-4"
             sh "sudo apt-get install default-jdk"
             sh "wget https://chromedriver.storage.googleapis.com/2.41/chromedriver_linux64.zip"
             sh "unzip chromedriver_linux64.zip"
             sh "sudo mv chromedriver /usr/bin/chromedriver"
             sh "sudo chown root:root /usr/bin/chromedriver"
             sh "sudo chmod +x /usr/bin/chromedriver"
             sh "wget https://selenium-release.storage.googleapis.com/3.141/selenium-server-standalone-3.141.59.jar"
             sh "wget http://www.java2s.com/Code/JarDownload/testng/testng-6.8.7.jar.zip"
             sh "unzip -u testng-6.8.7.jar.zip"
             sh "xvfb-run java -Dwebdriver.chrome.driver=/usr/bin/chromedriver -jar /home/jenkins/jenkins_slave/workspace/Certification/selenium-server-standalone-3.141.59.jar"
            }
        }
        stage('Check if selenium test run') {
            agent{ label 'slave'}
            steps {
                sh "cd /home/jenkins/jenkins_slave/workspace/Certification/src/main/java/com/edureka"
                sh "export CLASSPATH='/$HOME/Desktop.:selenium-server-standalone-3.141.59.jar:testng-6.8.7.jar' "
                sh "javac Main.java"
                sh "java Main"
            }
            post {
                failure {
                    sh "sudo docker rm -f webapp"
                }
            }
        }
    }
}
