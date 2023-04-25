pipeline {
    agent {
        node {
            label 'slave'
        }
    }

    stages {
        stage('git-checkout') {
            steps {
                git 'https://github.com/Ram8319/icefaces-archetype.git'
            }
        }
        stage('maven-compiling') {
            steps {
                script {
                 sh 'mvn compile'   
                }
            }
        }
        stage ('maven-test'){
            steps {
                script {
                    sh 'mvn test'
                }
            }
        }
    }
}