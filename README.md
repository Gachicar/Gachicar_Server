# Gachicar-SERVER
- 가치 있게 같이 쓰는 '가치카' 애플리케이션 백엔드 서버
- 숙명여자대학교 IT공학전공 졸업프로젝트
- 개발 기간: 2023.09 ~ 2024.04
<br/>
<p align="center">
  <img src="https://github.com/Gachicar/Gachicar-SERVER/assets/82032452/82a7ab69-5d6e-40e2-8dc7-b324011fa223" width="200" align="center" />
</p>

<br/>

## 📌 About This Project
그룹을 만들고 개인이 공유할 차량을 등록하여 그룹원들과 편리하게 공유하는 플랫폼입니다. 
음성비서를 사용하여 편리하게 공유 차량을 사용하거나 예약할 수 있으며, 홈 화면 및 메뉴에서 주행 리포트와 예약 리스트, 차량 정보 등을 조회할 수 있습니다.

<br/>

## 📌 Main Features

### 1️⃣ 그룹 및 차량 등록
- 회원가입/로그인 -> 그룹 생성 -> 차량 등록 -> 멤버 초대
- 그룹원들끼리만 해당 그룹의 공유 차량 이용 가능

### 2️⃣ 차량 이용 예약
- 음성 비서를 사용하여 목적지, 사용 일시, 이용 시간을 입력하면, 인공지능 서버에서 자연어 처리를 통해 사용자의 의도에 맞는 응답을 백엔드 서버에 전달
- 백엔드 서버에서는 해당 내용을 기반으로 기존의 예약 내역과 중복되지 않도록 유도하여 예약을 진행
- 공유 차량의 예약 리스트를 조회 가능
- 예약 시간에 사용 시간이 되었다는 알림 전송

### 3️⃣ 음성 인식 기반 공유 차량 이용
- 공유 차량을 사용하고 있는 사용자가 없는 경우, 음성 비서를 통해 목적지를 지정하여 즉시 차량 이용 가능
- 주행 중에는 다른 그룹원들이 해당 차량 이용 불가
- 주행이 종료되면 주행 리포트가 생성되고, 그룹원들은 공유 차량의 상태와 주행 리포트 리스트 조회 가능

<br/>

## 📌 Architecture
<img width="1801" alt="Architecture" src="https://github.com/Gachicar/Gachicar-SERVER/assets/82032452/25a1e334-cb6c-4fd5-9a6a-ab2c9d6da2ca">

<br/>

## ⚙️ Stacks

  ### Environment
  <div>
    <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
    <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
    <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
  </div>
  
  ### Development
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">

<br/>

## 📌 Setup
To run this project, install these requirements locally:

  ### Requirements
  For building and running the server you need:
  - at least JDK 11
  - h2 database
  - Redis Server
  
  #### 🔵 H2
  H2 Database 설치 후 bin 폴더에 있는 h2.sh 파일 실행(경로 주의): 
  ```sh h2.sh```
  > JDBC URL
  > - 처음에만 이 경로 (생성할 때에만): ```jdbc:h2:~/gachicar```
  > - 최초 1회 빼고 그 이후의 JDBC URL: ```jdbc:h2:tcp://localhost/~/gachicar```
  - 추후 개발 계획: AWS RDS 또는 MsSQL로 전환할 예정
  
  #### 🔵 Redis Server
  - 도커 애플리케이션 실행
  - redis 이미지 다운 받기: ```docker pull redis```
  
    ##### MAC
    ```sudo docker run -p 6379:6379 redis```
    ##### Windows
    ```docker run -p 6379:6379 redis (윈도우 버전)```
    
    - docker Terminal 창에서 start 버튼 클릭 후  ```redis-cli``` 명령어 입력하여 redis에 접속

<br/>

### RUN
`GachicarApplication` 파일 Run
