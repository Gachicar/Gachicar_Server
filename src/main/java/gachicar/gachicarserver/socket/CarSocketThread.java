package gachicar.gachicarserver.socket;

import gachicar.gachicarserver.service.DriveReportService;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * [파이썬 소켓 서버와 통신]
 * - RC카 주피터랩 ip 주소: 192.168.0.7
 * - 접속 포트: 9851
 */
public class CarSocketThread implements Runnable {

    private final long userId;                          // 사용자 ID
    private final DriveReportService driveReportService;
    private Socket carSocket;                     // RC카와의 소켓 연결
    private PrintWriter carWriter;

    public CarSocketThread(long userId, DriveReportService driveReportService) {
        this.userId = userId;
        this.driveReportService = driveReportService;
    }

    private void connectToCar() {
        try {
            // RC카의 IP 주소
            String carIpAddress = "localhost";    // 192.168.0.7
            // RC카의 포트 번호
            int carPort = 9851;
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

        try (DataInputStream carReader = new DataInputStream(carSocket.getInputStream())) {
            while (true) {
                if (carReader.available() > 0) { // 읽을 데이터가 있는지 확인
                    // 정수로 입력을 직접 읽음
                    int inputInt = carReader.readInt();
                    System.out.println("Received From RC: " + inputInt);

                    // 여기에 RC카로부터 받은 정수에 대한 작업을 추가합니다.
                    // TODO : RC카 주행 상태에 따라 클라이언트에게 메시지 전달
                    if (inputInt == 0) {
                        driveReportService.updateReport(userId);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeCarSocket(); // 소켓 닫기
        }
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
