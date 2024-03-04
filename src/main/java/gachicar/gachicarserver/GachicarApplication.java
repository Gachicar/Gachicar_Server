package gachicar.gachicarserver;

import gachicar.gachicarserver.socket.SocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class GachicarApplication {

	public static void main(String[] args) {
		SpringApplication.run(GachicarApplication.class, args);
		new SocketServer();	// 소켓 서버 ON
	}

}
