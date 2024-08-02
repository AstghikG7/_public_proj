pipeline {
    agent any 
    tools {
        jdk 'jdk-11'
        maven 'maven-3.6'
    }
     
    environment {
        GITHUB_CREDS = credentials('github-credentials')
        DOCKER_CREDS = credentials('docker-credentials')
        MAVEN_GH = "-Dgithub.username=${GITHUB_CREDS_USR} -Dgithub.usertoken=${GITHUB_CREDS_PSW}"
        MAVEN_DGH = "-Ddocker.username=${GITHUB_CREDS_USR} -Ddocker.password=${GITHUB_CREDS_PSW}"     
    }
    stages {
        stage('showVars') {
        	steps {
            	echo "MAVEN_GH=${MAVEN_GH}"
            	echo "MAVEN_DH=${MAVEN_DGH}"
            }
		}    
        stage('Clean') {
            steps {
                sh "mvn -B clean"
            }
        }
        stage('Build') {
            steps {
                sh "mvn -B -s 'settings.xml' $MAVEN_GH package"
            }
        }
        stage('Tests') {
            steps {
                script {
                    try {
                        sh "mvn -B -s 'settings.xml' $MAVEN_GH test -Dskip.unit.tests=false -Dskip.integration.tests=true -Dmaven.test.failure.ignore=false"
                    }
                    catch (e) {
                        throw e
                    }
                    finally {
                        findText(textFinders: [textFinder(regexp: 'There are test failures', alsoCheckConsoleOutput: true, buildResult: 'FAILURE')])
                        if (currentBuild.getCurrentResult() == 'FAILURE') {
                            unstable('Tests failed!')                            
                        }
                    }
                }
            }
        }
        stage('Integration Tests') {
            steps {
                script {
                    try {
                        sh "mvn -B -s 'settings.xml' $MAVEN_GH verify -Dskip.integration.tests=false -Dskip.unit.tests=true -Dskip.owasp.analysis=true"
                    }
                    catch (e) {
                        throw e
                    }
                    finally {
//                        findText(textFinders: [textFinder(regexp: 'There are test failures', alsoCheckConsoleOutput: true, buildResult: 'FAILURE')])
//                        if (currentBuild.getCurrentResult() == 'FAILURE') {
//                            unstable('Tests failed!')                            
//                        }
// I comment this lines because they raises an error because of the previous test validation in Tests stage. Try to find a specific error in Integration
                    }
                }
            }
        }
        stage('dockerBuild') {
            steps {
                sh "mvn -B docker:build"
            }
        }
        stage('dockerPush') {
            steps {
                sh "mvn -B -s 'settings.xml' $MAVEN_DGH docker:push"
            }
        }
        stage('Code Quality') {
            steps {
                withSonarQubeEnv ('bolzano')
                {
                    sh "mvn -B -s 'settings.xml' $MAVEN_GH verify sonar:sonar -Dsonar.login=d7e6c1e7381adc76799d9a2bb2981df79103efaa"
                }
            }
        }
        stage('OWASP Analysis') {
            steps {
                script {
                    sh "mvn -B -s 'settings.xml' $MAVEN_GH verify -Dskip.unit.tests=true -Dskip.integration.tests=true -Dskip.owasp.analysis=false"
                }
            }
        }

    }
    post {
        always {
            cleanWs()
        }
    }

}
