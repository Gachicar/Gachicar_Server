# Gachicar-SERVER
가치 있게 같이 쓰는 '가치카' 애플리케이션 백엔드 서버
<br/>
<p align="center">
  <img src="https://github.com/Gachicar/Gachicar-SERVER/assets/82032452/82a7ab69-5d6e-40e2-8dc7-b324011fa223" width="200" align="center" />
</p>

## ⚙️ Stacks

### Environment
<div>
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
</div>

### Development
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">

## How to Start
### Requirements
For building and running the server you need:
- at least JDK 11
- h2 database
- Redis Server

#### H2
H2 Database 설치 후 bin 폴더에 있는 h2.sh 파일 실행(경로 주의): 
```sh h2.sh```
> JDBC URL
> - 처음에만 이 경로 (생성할 때에만): ```jdbc:h2:~/gachicar```
> - 최초 1회 빼고 그 이후의 JDBC URL: ```jdbc:h2:tcp://localhost/~/gachicar```
- 추후 개발 계획: AWS RDS 또는 MsSQL로 전환할 예정

#### Redis Server
- 도커 애플리케이션 실행
- redis 이미지 다운 받기: ```docker pull redis```

##### MAC
```sudo docker run -p 6379:6379 redis```
##### Windows
```docker run -p 6379:6379 redis (윈도우 버전)```

- docker Terminal 창에서 start 버튼 클릭 후  ```redis-cli``` 명령어 입력하여 redis에 접속
