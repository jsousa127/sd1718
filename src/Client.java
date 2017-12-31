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
            Lock lock = new ReentrantLock();
            Condition c = lock.newCondition();
            ClientIn clientIn = new ClientIn(in, menu, lock, c);
            ClientOut clientOut = new ClientOut(socket, menu, lock, c);
            clientIn.start();
            clientOut.start();
            clientIn.join();
            clientOut.join();
            in.close();
            System.out.println("Até uma próxima!\n");
            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}