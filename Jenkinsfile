pipeline {
    agent any
    environment {
             VERSION                = 'latest'
             PROJECT                = 'mhmarket'
             DOCKERHUB_CREDENTIALS  = credentials('dockerhub')
             ENVIRONMENT            = 'dev'
             SERVICE                = 'account'
    }
    stages {
        stage("Cloud Config") {
                steps {
                    sh 'apt-get update && apt-get install -y postgresql-client'
                    sh 'chmod 700 run_dbscript_${ENVIRONMENT}.sh'
                    sh './run_dbscript_${ENVIRONMENT}.sh'
                }
        }
        stage("Maven build") {
                    agent {
                        docker {
                            image 'maven:3.6.3-jdk-11'
                            args '-v /home/jenkins/.m2:/root/.m2 --network=host'
                            reuseNode true
                        }
                    }
                    steps {
                        sh 'mvn -s /root/.m2/settings.xml -q -U clean install -Dmaven.test.skip=true -P server'
                    }
        }
         stage("Docker build") {
                    steps {
                        sh "docker build --network=host --tag 211020/${PROJECT}-${SERVICE}:${VERSION} ."
                    }
         }
         stage("Docker Push") {
                    steps {
                        sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                        sh "docker push 211020/${PROJECT}-${SERVICE}:${VERSION}"
                    }
         }
         stage("Deploy") {
                    steps {
                        sh "docker-compose pull"
                        sh "docker-compose down | echo IGNORE"
                        sh "docker-compose up -d"
                    }
         }
    }

}