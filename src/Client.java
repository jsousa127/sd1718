import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Menu menu = new Menu();
            menu.setOption(1);
            ReentrantLock lock = new ReentrantLock();
            Condition c = lock.newCondition();
            ClientOut clientOut = new ClientOut(socket, menu, lock, c);
            ClientIn clientIn = new ClientIn(in, menu, lock, c);
            clientOut.start();
            clientIn.start();
            clientOut.join();
            clientIn.join();
            in.close();
            System.out.println("Goodbye\n");
            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}