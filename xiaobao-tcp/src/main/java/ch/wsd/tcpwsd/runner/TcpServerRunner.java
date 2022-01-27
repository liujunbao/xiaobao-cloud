package ch.wsd.tcpwsd.runner;

import ch.wsd.tcpwsd.tcpserver.TcpServer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TcpServerRunner implements CommandLineRunner {

    private final TcpServer tcpServer;

    @Override
    public void run(String... args) throws Exception {
        tcpServer.run();
    }
}
