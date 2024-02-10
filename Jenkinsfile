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
        stage('Build JAR') {
            steps {
                container('kaniko') {
                    sh 'mvn clean package'  // 또는 gradle build 등 빌드 명령어 사용
                }
            }
        }
        stage('Build and Push Image') {
            steps {
                container('kaniko') {
                    sh '''
                    /kaniko/executor --context . --dockerfile=Dockerfile --destination=renum/moyur:v1.0.0
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
