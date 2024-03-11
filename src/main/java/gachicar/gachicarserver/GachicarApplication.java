package gachicar.gachicarserver;

import gachicar.gachicarserver.service.CarService;
import gachicar.gachicarserver.service.DriveReportService;
import gachicar.gachicarserver.service.UserService;
import gachicar.gachicarserver.socket.SocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class GachicarApplication {

	public static void main(String[] args) {
		SpringApplication.run(GachicarApplication.class, args);
	}

	@Bean
	public ExecutorService executorService() {
		return Executors.newCachedThreadPool();
	}

	@Bean
	public ServerSocket serverSocket() throws IOException {
		return new ServerSocket(9595);
	}

	@Bean
	public SocketServer customSocketServer(ServerSocket serverSocket, ExecutorService executorService, UserService userService, DriveReportService driveReportService, CarService carService) {
		SocketServer socketServer = new SocketServer(serverSocket, executorService, userService, driveReportService, carService);
		socketServer.start();
		return socketServer;
	}

}
