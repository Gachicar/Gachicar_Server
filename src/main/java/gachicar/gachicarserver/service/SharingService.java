package gachicar.gachicarserver.service;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.dto.CurLocationDto;
import gachicar.gachicarserver.dto.requestDto.StartDriveRequestDto;
import gachicar.gachicarserver.exception.ApiErrorException;
import gachicar.gachicarserver.exception.ApiErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 카셰어링 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SharingService {

    public CurLocationDto getCarLoc(Car car) {
        String loc = car.getCurLoc();
        if (loc == null) {
            throw new ApiErrorException(ApiErrorStatus.NOT_EXIST);
        } else {
            return new CurLocationDto(loc);
        }
    }

    public void startDrive(Car car, StartDriveRequestDto requestDto) throws IOException {

        // 공유차량의 현재 위치 확인
        String curLoc = car.getCurLoc();

        // 차량 상태를 사용 중 상태로 변경
        car.setCarStatus(Boolean.TRUE);

        // RC카에 연결
        Socket socket = connectCar(car);

        // 목적지로 주행 시작
        car.setCurLoc(requestDto.getDeparture());
        OutputStream outputStream = drive(socket, "주행시작");

        // 주행 완료 후 현재 위치를 목적지로 설정
        car.setCurLoc(requestDto.getDestination());

        // RC카 연결 해제
        disconnectCar(socket, outputStream);
    }

    // 주행
    public OutputStream drive(Socket socket, String message) {
        try {
            // RC카로 명령 보내기
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(message.getBytes());
            outputStream.flush();
            log.info("RC카 제어 요청이 성공적으로 전송되었습니다.");

            return outputStream;

        } catch (IOException e) {
            e.printStackTrace();
            log.info("RC카 제어 요청 전송 중 오류가 발생했습니다.");
            throw new ApiErrorException(ApiErrorStatus.SOCKET_ERROR);
        }
    }

    // 공유차량 연결
    public Socket connectCar(Car car) {
        // RC카의 IP 주소와 포트 번호
        String carIP = "localhost";     // RC_CAR_IP_ADDRESS
        int carPort = 9091;     // RC_CAR_PORT_NUMBER

        try {
            // 서버 소켓 생성 및 포트 바인딩
            ServerSocket serverSocket = new ServerSocket(carPort);

            // 클라이언트 연결 대기
            Socket clientSocket = serverSocket.accept();

            return new Socket(carIP, carPort);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiErrorException(ApiErrorStatus.CONNECT_FAILED);
        }
    }

    // 공유차량 연결 해제
    public void disconnectCar(Socket socket, OutputStream outputStream) {
        try {
            // 출력 스트림 닫기
            outputStream.close();
        } catch (IOException e) {
            log.error("출력 스트림을 닫는 중 오류가 발생했습니다.", e);
            throw new ApiErrorException(ApiErrorStatus.DISCONNECT_FAILED);
        }

        try {
            // 소켓 닫기
            socket.close();
        } catch (IOException e) {
            log.error("소켓을 닫는 중 오류가 발생했습니다.", e);
            throw new ApiErrorException(ApiErrorStatus.DISCONNECT_FAILED);
        }
    }

    public void sendMessage(){
        int portNumber = 9999;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server is running on port " + portNumber);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    System.out.println("Client connected from " + clientSocket.getInetAddress());

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Received from client: " + inputLine);
                        out.println("Echo: " + inputLine);
                    }
                } catch (IOException e) {
                    System.err.println("Error handling client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            e.printStackTrace();
        }
    }

    public void socketClient(String command) {
        String serverAddress = "localhost"; // 대상 서버의 IP 주소
        int serverPort = 9999; // 대상 서버의 포트 번호

        try (
                Socket socket = new Socket(serverAddress, serverPort);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // 서버로 메시지 전송
            out.println(command);

            // 서버로부터 응답 받기
            String response = in.readLine();
            System.out.println("Received from server: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
