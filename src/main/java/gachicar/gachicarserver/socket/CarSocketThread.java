package gachicar.gachicarserver.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * [파이썬 소켓 서버와 통신]
 * - RC카 주피터랩 ip 주소: 192.168.0.7
 * - 접속 포트: 9851
 */
public class CarSocketThread implements Runnable {

    private final String carIpAddress = "localhost";    // RC카의 IP 주소
    private final int carPort = 9851;                   // RC카의 포트 번호

    private final long userId;                          // 사용자 ID
    private Socket carSocket;                     // RC카와의 소켓 연결
    private PrintWriter carWriter;

    public CarSocketThread(long userId) {
        this.userId = userId;
    }

    private void connectToCar() {
        try {
            carSocket = new Socket(carIpAddress, carPort);
            carWriter = new PrintWriter(carSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToCar(String message) {
        if (carWriter != null) {
            carWriter.println(message);
            System.out.println("Sent to RC car: " + message);
        } else {
            System.out.println("Failed to send message to RC car. Car socket is not available.");
        }
    }

    @Override
    public void run() {
        connectToCar(); // RC카와의 연결 설정
        // RC카와의 소켓 연결을 유지하는 코드를 작성합니다.
    }

    public void closeCarSocket() {
        if (carSocket != null && !carSocket.isClosed()) {
            try {
                carSocket.close();
                System.out.println("Closed RC car socket for user: " + userId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
