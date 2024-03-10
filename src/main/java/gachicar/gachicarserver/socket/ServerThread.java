package gachicar.gachicarserver.socket;

import gachicar.gachicarserver.domain.Car;
import gachicar.gachicarserver.domain.User;
import gachicar.gachicarserver.service.CarService;
import gachicar.gachicarserver.service.SharingService;
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
    private final SharingService sharingService;
    private final CarService carService;

    private PrintWriter androidClientWriter;

    User user;
    Car car;

    public ServerThread(Socket clientSocket, CarSocketThread carSocketThread, TokenSocketThread tokenSocketThread,
                        Long userId, UserService userService, SharingService sharingService, CarService carService) {
        this.clientSocket = clientSocket;
        this.carSocketThread = carSocketThread;
        this.tokenSocketThread = tokenSocketThread;
        this.userId = userId;
        this.userService = userService;
        this.sharingService = sharingService;
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

                while ((inputLine = ois.readLine()) != null) {
                    // 클라이언트로부터 메시지 수신
                    System.out.println("Received from Android client: " + inputLine);

                    if (inputLine.contains("종료")) {
                        speakToMe("운행을 종료합니다.");
                        carSocketThread.sendToCar("종료");
                        break;
                    } else if (inputLine.contains("안녕")) {
                        speakToMe("안녕하세요. 무엇을 도와드릴까요?");
                    } else if (inputLine.contains("어디")) {
                        // 공유차량의 현재 위치 확인
                        speakToMe("저는 지금 " + car.getCurLoc() + "에 있습니다.");
                    } else if (inputLine.contains("가") || inputLine.contains("와")) {
                        String destination = "";
                        String command = "시작";
                        // 목적지 토큰 추출해주는 서버에 메시지 전달
                        String tokenResponse = sendAndReceiveTokenMessage(inputLine);
                        System.out.println("Received response from Token Server: " + tokenResponse);

                        if (tokenResponse != null) {
                            // 목적지 토큰을 성공적으로 받아왔을 때에만 다음 작업을 진행
                            // tokenResponse를 이용하여 다음 작업 수행
                            System.out.println("토큰 응답 받기 성공. 다음 코드 수행 : " + tokenResponse);
                            destination = tokenResponse;
                        }
//                        if (inputLine.contains("집")) {
//                            destination = "집";
//                        } else if (inputLine.contains("학교")) {
//                            destination = "학교";
//                        }
                        speakToMe("네, 알겠습니다.");
                        checkRC("학교", destination, command);

                        // 메시지를 RC 카로 전달
                        carSocketThread.sendToCar("시작");
                        sharingService.makeReport(user, destination, command);  // 리포트 생성
                    }
//                    else {
//                        carSocketThread.sendToCar("시작");
//                        sharingService.makeReport(user, tokenResponse, "시작");
//                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 클라이언트 소켓과 RC 카 소켓 닫기
            closeClientSocket();
        }
    }

    // 사용자 명령 확인 메시지 생성
    public void checkRC(String departure, String destination, String Command) {
        // 원래는.. 사용자 id로 공유차량 정보 가져오기
        // + 사용자, 출발지, 목적지 => DB에 저장
        sendToAndroidClient("r/- 출발지: " + departure +
                "\n- 목적지: " + destination +
                "\n- 명령어: " + "시작");
    }

    public void speakToMe(String message) {
        sendToAndroidClient("spk/" + message);
    }

    // 안드로이드 클라이언트로 메시지 보내기
    private void sendToAndroidClient(String message) {
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
    private String sendAndReceiveTokenMessage(String message) {
        if (tokenSocketThread != null) {
            return tokenSocketThread.sendAndReceiveFromTokenServer(message);
        } else {
            System.out.println("TokenSocketThread is not available.");
            return null;
        }
    }

}
