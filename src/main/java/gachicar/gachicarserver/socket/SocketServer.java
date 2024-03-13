package gachicar.gachicarserver.socket;

import gachicar.gachicarserver.service.CarService;
import gachicar.gachicarserver.service.DriveReportService;
import gachicar.gachicarserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketServer {

    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private final Map<Long, Socket> clientSockets = new ConcurrentHashMap<>();
    private final Map<Long, CarSocketThread> userCarSocketThreads = new ConcurrentHashMap<>();

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

                    // 클라이언트의 ID를 설정하고 PrintWriter를 hm 맵에 추가
                    long clientId = 1L; // 클라이언트 ID 생성 (원하는 방식으로)
                    clientSockets.put(clientId, clientSocket);

                    // 해당 사용자에 대한 CarSocketThread 객체 생성
                    CarSocketThread carSocketThread = new CarSocketThread(clientId, driveReportService);
                    userCarSocketThreads.put(clientId, carSocketThread);

                    // 목적지 토큰을 반환하는 스레드 실행
                    TokenSocketThread tokenSocketThread = new TokenSocketThread();
                    executorService.execute(tokenSocketThread);

                    // 안드로이드 클라이언트와의 소켓 연결을 처리하는 스레드 실행
                    ServerThread serverThread = new ServerThread(clientSocket, carSocketThread, tokenSocketThread,
                                                                    clientId, userService, driveReportService, carService);
                    executorService.execute(serverThread);

                    // 해당 사용자에 대한 CarSocketThread 객체 생성 실행
                    executorService.execute(carSocketThread);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // CarSocketThread 객체에 접근할 수 있는 메서드
    public CarSocketThread getCarSocketThread(long clientId) {
        return userCarSocketThreads.get(clientId);
    }

}
