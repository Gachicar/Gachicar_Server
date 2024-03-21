package gachicar.gachicarserver.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.dto.ReportDto;
import gachicar.gachicarserver.dto.requestDto.ReserveRequestDto;
import gachicar.gachicarserver.service.CarService;
import gachicar.gachicarserver.service.DriveReportService;
import gachicar.gachicarserver.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerThread implements Runnable {

    private final Socket clientSocket;
    private final CarSocketThread carSocketThread;
    private final TokenSocketThread tokenSocketThread;

    private final Long userId;
    private final UserService userService;
    private final DriveReportService driveReportService;
    private final CarService carService;

    private PrintWriter androidClientWriter;

    User user;
    Car car;

    public ServerThread(Socket clientSocket, CarSocketThread carSocketThread, TokenSocketThread tokenSocketThread,
                        Long userId, UserService userService, DriveReportService driveReportService, CarService carService) {
        this.clientSocket = clientSocket;
        carSocketThread.setServerThread(this);
        this.carSocketThread = carSocketThread;
        this.tokenSocketThread = tokenSocketThread;
        this.userId = userId;
        this.userService = userService;
        this.driveReportService = driveReportService;
        this.carService = carService;

        try {
            this.androidClientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.user = userService.findUserById(userId);
        this.car = carService.findByUser(user);

    }

    @Override
    public void run() {
        String inputLine;

        try (BufferedReader ois = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            System.out.println("Client connected from " + clientSocket.getInetAddress());
            androidClientWriter.println(user.getName() + "님의 공유차량은 " + car.getCarName() + " 입니다.");
            if (car.getCarStatus() == Boolean.TRUE) {
                androidClientWriter.println("지금은 공유차량을 사용할 수 없습니다.");
            } else {
                // 차량 상태를 사용 중 상태로 변경
                car.setCarStatus(Boolean.TRUE);
                car.setNowUser(userId);

                while ((inputLine = ois.readLine()) != null) {
                    // 클라이언트로부터 메시지 수신
                    System.out.println("Received from Android client: " + inputLine);

                    if (inputLine.contains("종료")) {
                        sendToAndroidClient("운행을 종료합니다.");
                        carSocketThread.sendToCar("종료");
                        break;
                    } else if (inputLine.contains("정지")) {
                        sendToAndroidClient("정지합니다.");
                        carSocketThread.sendToCar("정지");
                    } else if (inputLine.contains("어디")) {
                        // 공유차량의 현재 위치 확인
                        sendToAndroidClient("저는 지금 " + car.getCurLoc() + "에 있습니다.");
                    } else {
                        // 목적지 토큰 추출해주는 서버에 메시지 전달
                        sendAndReceiveTokenMessage(inputLine);
//                        carSocketThread.sendToCar(inputLine);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 클라이언트 소켓과 RC 카 소켓 닫기
            closeClientSocket();
        }
    }

    // 안드로이드 클라이언트로 메시지 보내기
    public void sendToAndroidClient(String message) {
        androidClientWriter.println(message); // PrintWriter를 사용하여 메시지를 안드로이드 클라이언트로 전송
        System.out.println("Sent to Android client: " + message);
    }

    private void closeClientSocket() {
        try {
            clientSocket.close();
            System.out.println("Closed client socket for user: " + userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 사용자 명령에서 목적지 추출하기
     */
    private void sendAndReceiveTokenMessage(String message) throws JsonProcessingException {
        tokenSocketThread.reconnectToTokenServer();

        String response = tokenSocketThread.sendAndReceiveFromTokenServer(message);

        if (response == null) {
            tokenSocketThread.reconnectToTokenServer(); // 소켓 재연결
            response = tokenSocketThread.sendAndReceiveFromTokenServer(message);
        } else {
            System.out.println("Received From Token Server: " + response);

            // test
//            sendToAndroidClient(response);
//            carSocketThread.sendToCar(response);
//            car.setOilStatus(car.getOilStatus()-5);
//            driveReportService.createReport(user, response);

            // ObjectMapper 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();
            // JSON 문자열을 객체로 변환
            ReserveRequestDto reserveRequestDto = objectMapper.readValue(response, ReserveRequestDto.class);
            String intention = reserveRequestDto.getIntention();

            if (intention.equals("주문")) {
                String destination = reserveRequestDto.getDestination();
                if (destination.equals("")) {
                    sendToAndroidClient("다시 말씀해주시겠어요?");
                } else {
                    // 안드로이드 클라이언트에 응답
                    sendToAndroidClient(reserveRequestDto.getResponse());

                    // 메시지를 RC 카로 전달
                    carSocketThread.sendToCar(destination);
                    driveReportService.createReport(user, destination);  // 리포트 생성
                }
            } else if (intention.equals("예약")) {
                ReportDto reserveReport = driveReportService.createReserveReport(user, reserveRequestDto.getDestination(), reserveRequestDto.getTime());

                if (reserveReport == null) {
                    sendToAndroidClient("이미 같은 시간에 예약이 있습니다. 다른 시간을 말씀해주세요.");
                } else {
                    sendToAndroidClient(reserveRequestDto.getResponse());
                }
            } else {
                sendToAndroidClient(reserveRequestDto.getResponse());
            }
        }
    }

}
