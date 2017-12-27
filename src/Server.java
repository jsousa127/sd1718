import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1234);

        while (true) {
            Socket sock = server.accept();
            System.out.println("Connected");
            ServerIn st = new ServerIn(sock);
            st.start();
        }
    }
}


