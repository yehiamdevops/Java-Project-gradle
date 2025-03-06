pipeline {
    agent win                                                                                                                                                                                                                                                                                                                                                                                           
    environment {
        GITHUB_TOKEN = credentials('GITHUB_TOKEN')
        VERSION = "v1.0.${BUILD_NUMBER}" // Generates v1.0.1, v1.0.2, etc.
    }

    stages {
        
        // stage('Checkout') {
        //     steps {
        //         git 'https://your-repo-url.git'
        //     }
        // }

        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        // stage('Test') {
        //     steps {
        //         sh './gradlew test'
        //     }
        // }

        stage('Package') {
            steps {
                sh './gradlew jar'
            }
        }
 


        stage('Publish to GitHub Release') {
            steps {
                script {
                    def version = "v1.0.0"  // Change this dynamically if needed
                    def repo = "yehiamdevops/Java-Project-gradle"

                    // Ensure GitHub CLI is authenticated
                    bat 'echo $GITHUB_TOKEN | gh auth login --with-token'

                    // Create a new GitHub release
                    bat "gh release create ${version} app/build/libs/app.jar --repo ${repo} --title 'Release ${version}' --notes 'Automated release from Jenkins'"
                }
            }
        }

    }
}

