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
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentMap;


public class ServerThread implements Runnable {

    private final Socket clientSocket;
    private BufferedReader ois;
    private PrintWriter oos;
    private final ConcurrentMap<Long, PrintWriter> hm;
    InetAddress ip;
    Long userId;
    User user;
    Car car;

    private final UserService userService;
    private final SharingService sharingService;
    private final CarService carService;

    public ServerThread(Socket s, ConcurrentMap<Long, PrintWriter> hm, Long userId, UserService userService, SharingService sharingService, CarService carService) throws IOException {

        this.clientSocket = s;
        this.hm = hm;
        this.userId = userId;
        this.userService = userService;
        this.sharingService = sharingService;
        this.carService = carService;

        this.ois = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.oos = new PrintWriter(clientSocket.getOutputStream(), true);

        this.ip = clientSocket.getInetAddress();

        // 클라이언트에서 사용자 아이디 보내고 시작
        this.user = userService.findUserById(userId); // Assuming userService is properly initialized
        System.out.println(user.getName());
        this.car = carService.findByUser(user); // Assuming carService is properly initialized
        System.out.println(car.getCarName());
//
//            String userCarName = car.getCarName();
//            oos.println("사용자의 공유차량: " + userCarName);


    }

    @Override
    public void run() {
        String inputLine;

        try {
            System.out.println(ip + "로부터 " + user.getName() + "님이 접속하였습니다.");

            while ((inputLine = ois.readLine()) != null) {
                // TODO : RC카 주행이 끝나면 자동으로 리포트를 보냄.
                if (inputLine.equals("/quit")) {
                    // 주행 기록 요약
                    report(car);

                    synchronized(hm) {
                        hm.remove(userId);
                    }
                    break; // 클라이언트와의 연결 종료
                } else if (inputLine.contains("안녕")) {
                    speakToMe("안녕하세요. 무엇을 도와드릴까요?");
                } else if (inputLine.contains("어디")) {
                    speakToMe("저는 지금 학교에 있습니다.");
                } else if (inputLine.contains("가") || inputLine.contains("와")) {
                    String destination = "";
                    String command = "시작";
                    if (inputLine.contains("집")) {
                        destination = "집";
                    } else if (inputLine.contains("학교")) {
                        destination = "학교";
                    }
                    speakToMe("네, 알겠습니다.");
                    checkRC("학교", destination, command);

                    sharingService.startDrive(user, destination, command);

                }
                else {
//                    extractDest(inputLine);
                    System.out.println("Received from client: " + inputLine);
                    oos.println("Server received: " + inputLine);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ois.close();
                oos.close();
                clientSocket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // 클라이언트 메시지에 대한 서버 응답 보내기
    public void sendMsgToMe(String message){
        hm.forEach((key, value) -> {
            if (key.equals(userId)) {
                PrintWriter oos = hm.get(key);
                System.out.println(message);
                oos.println(message);
            }
        });
    }

    // 다른 클라이언트에 메시지 보내기
    public void sendMsgToOther(String message){
        hm.forEach((key, value) -> {
            if (!key.equals(userId)) {
                PrintWriter oos = hm.get(key);
                oos.println(message);
            }
        });
    }

    // 사용자 명령 확인 메시지 생성
    public void checkRC(String departure, String destination, String Command) {
        // 원래는.. 사용자 id로 공유차량 정보 가져오기
        // + 사용자, 출발지, 목적지 => DB에 저장
        sendMsgToMe("r/- 출발지: " + departure +
                "\n- 목적지: " + destination +
                "\n- 명령어: " + "시작");
    }

    public void speakToMe(String message) {
        sendMsgToMe("spk/" + message);
    }

    public void report(Car car) {

        String msg = "[차량 주행 기록]" +
                "\n주행 거리: 10km" +
                "\n주유 상태: 보통" +
                "\n주행 시간: 30분";
        sendMsgToMe("report/" + msg);
    }

    /**
     * 사용자 명령에서 목적지 추출하기
     */
    public String extractDest(String msg) {
        String pythonServerAddress = "192.168.0.8";
        int pythonServerPort = 9999;

        try (
            Socket socket = new Socket(pythonServerAddress, pythonServerPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // 파이썬 서버에 메시지 전송
            out.println(msg);

            // 서버로부터 응답 받기
            String response = in.readLine();
            System.out.println("Received from server: " + response);

            return response;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
