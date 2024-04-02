package gachicar.gachicarserver;

import gachicar.gachicarserver.service.CarService;
import gachicar.gachicarserver.service.DriveReportService;
import gachicar.gachicarserver.service.UserService;
import gachicar.gachicarserver.socket.SocketServer;
import io.undertow.Undertow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@EnableScheduling
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

	@Bean
	public Undertow undertow() throws IOException {
		Undertow.Builder builder = Undertow.builder()
				.addHttpListener(9090, "localhost"); // HTTP 포트 설정
		log.info("Undertow 실행 = {}", 9090);
		return builder.build();
	}
}
