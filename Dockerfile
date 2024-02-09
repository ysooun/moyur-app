FROM openjdk:23-slim

# 환경 변수 선언
ENV STAGE=stage
ENV DB_HOST=
ENV DB_PORT=
ENV DB_USER=
ENV DB_PASSWORD=

ENV AWS_ACCESS_KEY_ID=
ENV AWS_SECRET_ACCESS_KEY=
ENV S3_BUCKET_NAME=my-moyur-image
ENV AWS_REGION=ap-northeast-2

# 애플리케이션 파일을 컨테이너에 복사
COPY /Users/yoonsung/eclipse-workspace/project/moyur-project/target/moyur-project-0.0.1-SNAPSHOT.jar /app.jar

# 실행할 명령 지정
ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080