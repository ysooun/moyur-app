pipeline {
    agent {
        kubernetes {
            defaultContainer 'kaniko'
            yaml '''
kind: Pod
spec:
  containers:
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    imagePullPolicy: Always
    command:
    - sleep
    args:
    - 99d
    volumeMounts:
      - name: kaniko-secret
        mountPath: /kaniko/.docker
  volumes:
    - name: kaniko-secret
      secret:
        secretName: regcred
        items:
          - key: .dockerconfigjson
            path: config.json

'''
        }
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    git branch: 'main',
                        credentialsId: 'github-credentials',
                        url: 'https://github.com/ysooun/moyur-app.git'
                }
            }
        }
        stage('Build and Push Image') {
            steps {
                container('kaniko') {
                    sh '''
                    tar czf context.tar.gz -C /Users/yoonsung/eclipse-workspace/project/moyur-project/target moyur-project-0.0.1-SNAPSHOT.jar
                    /kaniko/executor --context=/context.tar.gz --dockerfile=Dockerfile --destination=renum/moyur:v1.0.0
                    '''
                }
            }
        }
    }
    post {
        success {
            echo 'Image build successfully....'
        }
    }
}