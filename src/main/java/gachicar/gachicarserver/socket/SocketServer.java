package gachicar.gachicarserver.socket;

import gachicar.gachicarserver.service.CarService;
import gachicar.gachicarserver.service.SharingService;
import gachicar.gachicarserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
public class SocketServer {

    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private final Map<Long, Socket> clientSockets = new ConcurrentHashMap<>();
    private final Map<Long, CarSocketThread> userCarSocketThreads = new ConcurrentHashMap<>();


    private final UserService userService;
    private final SharingService sharingService;
    private final CarService carService;

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());

                // 클라이언트의 ID를 설정하고 PrintWriter를 hm 맵에 추가
                long clientId = 1L; // 클라이언트 ID 생성 (원하는 방식으로)
                clientSockets.put(clientId, clientSocket);

                // 해당 사용자에 대한 CarSocketThread 객체 생성
                CarSocketThread carSocketThread = new CarSocketThread(clientId);
                userCarSocketThreads.put(clientId, carSocketThread);

                // 안드로이드 클라이언트와의 소켓 연결을 처리하는 스레드 실행
                ServerThread serverThread = new ServerThread(clientSocket, carSocketThread, clientId, userService, sharingService, carService);
                executorService.execute(serverThread);

                // 해당 사용자에 대한 CarSocketThread 객체 생성 실행
                executorService.execute(carSocketThread);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
