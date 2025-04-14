pipeline {
  agent any

  triggers {
    githubPush()
  }

  stages {
    stage('Build (maven)') {
      steps {
        sh 'cd server'
        sh 'mvn clean install'

        stash name: 'maven-build', includes: '**/*'
      }
    }

    stage('Build (npm)') {
      agent {
        docker { image 'node:latest' }
      }
      steps {
        sh 'node -v'
        sh 'npm -v'

        sh 'npm install'
        sh 'npm run build'
        stash name: 'node-build', includes: '**/*'
      }
    }

    stage('Build (docker)') {
      steps {
        unstash 'node-build'
        sh 'docker build -t web.local:5000/resume:latest .'
      }
    }

    stage('Docker Push') {
      steps {
        sh 'docker push web.local:5000/resume:latest'
      }
    }
  }
}