package gachicar.gachicarserver.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class SocketServer {

    int portNumber = 9595;
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    HashMap<String, PrintWriter> hm;
    BufferedReader ois;
    PrintWriter oos;
    String user_id;

    public SocketServer() {
        ServerThread sr;
        Thread t;

        try {
            serverSocket = new ServerSocket(portNumber);

            System.out.println("Server is running on port " + portNumber);

            hm = new HashMap<String, PrintWriter>();

            while (true) {
                try {
                    clientSocket = serverSocket.accept();

                    oos = new PrintWriter(clientSocket.getOutputStream(), true);
                    ois = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    System.out.println("Client connected from " + clientSocket.getInetAddress());

                    user_id = "사용자";
                    sr = new ServerThread(clientSocket, hm, user_id);
                    t = new Thread(sr);
                    t.start();

                    // 클라이언트가 연결을 종료했을 때
                    System.out.println("Client disconnected");

                } catch (IOException e) {
                    System.err.println("Error handling client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            e.printStackTrace();
        }
    }


}
