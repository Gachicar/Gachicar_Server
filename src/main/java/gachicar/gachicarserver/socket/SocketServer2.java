package gachicar.gachicarserver.socket;

import gachicar.gachicarserver.service.CarService;
import gachicar.gachicarserver.service.DriveReportService;
import gachicar.gachicarserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketServer2 {

    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private final Map<Long, Socket> clientSockets = new ConcurrentHashMap<>();
//    private final Map<Long, CarSocketThread> userCarSocketThreads = new ConcurrentHashMap<>();

    private final UserService userService;
    private final DriveReportService driveReportService;
    private final CarService carService;

    @PostConstruct
    public void start() {
        executorService.execute(() -> {
            log.info("Socket server thread is now open: {}", 9595);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected from " + clientSocket.getInetAddress());

                    // 클라이언트로부터 ID를 받음
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    Long clientId = Long.parseLong(in.readLine()); // 클라이언트가 보낸 ID를 읽음
                    System.out.println("Received ID from client: " + clientId);
//                    long clientId = 1L; // 클라이언트 ID 생성 (원하는 방식으로)

                    clientSockets.put(clientId, clientSocket);

                    // 목적지 토큰을 반환하는 스레드 실행
                    TokenSocketThread tokenSocketThread = new TokenSocketThread();
                    executorService.execute(tokenSocketThread);

                    // 안드로이드 클라이언트와의 소켓 연결을 처리하는 스레드 실행
//                    ServerThread serverThread = new ServerThread(clientSocket, carSocketThread, tokenSocketThread,
//                                                                    clientId, userService, driveReportService, carService);
                    ServerThread2 serverThread = new ServerThread2(clientSocket, tokenSocketThread,
                            clientId, userService, driveReportService, carService, executorService);
                    executorService.execute(serverThread);

                    // 해당 사용자에 대한 CarSocketThread 객체 생성 실행
//                    executorService.execute(carSocketThread);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // CarSocketThread 객체에 접근할 수 있는 메서드
//    public CarSocketThread getCarSocketThread(long clientId) {
//        return userCarSocketThreads.get(clientId);
//    }

}
