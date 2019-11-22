def call() {
    pipeline {
        agent {
            label 'bla'
        }
        stages {
            stage('first stage') {
                steps {
                    echo 'My first step'
                }
            }
            stage('build') {
                steps {
                    sh 'gradle build'
                }
            }
            stage('custom') {
                steps {
                    customMethodOne('test')
                }
            }
            stage('test') {
                steps {
                    customMethodTwo('custom action')
                }
            }
        }
        post {
            always {
                echo 'I run demo pipeline'
            }
            success {
                echo 'Yee'
            }
            failure {
                echo 'Boo'
            }
        }
    }
}