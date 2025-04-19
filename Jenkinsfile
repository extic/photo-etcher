pipeline {
  agent {
    docker {
      image '127.0.0.1:5000/jdk23-agent'
    }
  }

  triggers {
    githubPush()
  }

  tools {
    maven 'Maven 3.9.9'
  }

  stages {
    stage('Build (maven)') {
      steps {
        sh '''
            cd server
            mvn -B clean install
        '''
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