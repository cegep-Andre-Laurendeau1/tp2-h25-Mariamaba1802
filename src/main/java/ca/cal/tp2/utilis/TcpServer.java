package ca.cal.tp2.utilis;

import org.h2.tools.Server;

import java.sql.SQLException;

public class TcpServer {
    public static void createTcpServer() throws SQLException {
        Server tcpServer = Server.createTcpServer(
                "-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
        System.out.println("Tcp server start: " + tcpServer.start());
        System.out.println(tcpServer.getStatus() + " " +
                           tcpServer.getPort());
        System.out.println("jdbc:h2:tcp://localhost:9092/mem:tp2Mariama");
        Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        System.out.println("TCP Server started: " + tcpServer.getStatus());
        System.out.println("Web Console Server started: " + webServer.getStatus());


    }
}
