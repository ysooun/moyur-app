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
                    script {
	                    // 도커파일 위치 및 JAR 파일 위치 설정
	                    def dockerfilePath = '/Users/yoonsung/eclipse-workspace/project/moyur-project/Dockerfile'
	                    def jarFilePath = '/Users/yoonsung/eclipse-workspace/project/moyur-project/target/moyur-project-0.0.1-SNAPSHOT.jar'
	                    
	                    // 카니코 실행 명령어
	                    sh "/kaniko/executor --context /Users/yoonsung/eclipse-workspace/project/moyur-project --dockerfile=${dockerfilePath} --destination=renum/moyur:v1.0.0"
	                    
	                    // 만약 도커파일 내에서 JAR 파일을 복사해야 한다면
	                    // sh "/kaniko/executor --context /Users/yoonsung/eclipse-workspace/project/moyur-project --dockerfile=${dockerfilePath} --destination=renum/moyur:v1.0.0 --build-arg JAR_FILE=${jarFilePath}"
	                    
	                    // Docker Hub 또는 다른 레지스트리에 이미지 푸시 명령어 실행 (필요에 따라)
	                    // sh "docker push renum/moyur:v1.0.0"
	                }
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