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

        // 목적지로 주행 시작
        car.setCurLoc(requestDto.getDeparture());
        socketClient("시작");

        // 주행 완료 후 현재 위치를 목적지로 설정
        car.setCurLoc(requestDto.getDestination());

    }

    /**
     * [파이썬 소켓 서버와 통신]
     * - RC카 주피터랩 ip 주소: 192.168.0.7
     * - 접속 포트: 9851
     * @param command : RC카에 보낼 명령어
     */
    public void socketClient(String command) {
        String serverAddress = "192.168.0.7"; // 대상 서버의 IP 주소
        int serverPort = 9851; // 대상 서버의 포트 번호

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
