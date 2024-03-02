package gachicar.gachicarserver.config;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class UndertowWebsocketCustomizer implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    private final ServerProperties.Undertow undertow;

    public UndertowWebsocketCustomizer(final ServerProperties serverProperties) {
        this.undertow = serverProperties.getUndertow();
    }

    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            boolean direct = this.undertow.getDirectBuffers() != null && this.undertow.getDirectBuffers();
            int bufferSize = this.undertow.getBufferSize() != null ? (int) this.undertow.getBufferSize().toBytes() : 1024;
            WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
            webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(direct, bufferSize));
            deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", webSocketDeploymentInfo);
        });
    }
}
