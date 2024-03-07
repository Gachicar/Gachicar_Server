package gachicar.gachicarserver.socket;

import gachicar.gachicarserver.service.CarService;
import gachicar.gachicarserver.service.SharingService;
import gachicar.gachicarserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
public class SocketServer {

    private final ServerSocket serverSocket;
    private final ExecutorService executorService;
    private final ConcurrentMap<Long, PrintWriter> clients = new ConcurrentHashMap<>();

    private final UserService userService;
    private final SharingService sharingService;
    private final CarService carService;

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());

                // 클라이언트의 PrintWriter 가져오기
                PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);

                // 클라이언트의 ID를 설정하고 PrintWriter를 hm 맵에 추가
                long clientId = 1L; // 클라이언트 ID 생성 (원하는 방식으로)
                clients.put(clientId, clientWriter);

                executorService.execute(new ServerThread(clientSocket, clients, clientId, userService, sharingService, carService));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
