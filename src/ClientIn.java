import java.io.BufferedReader;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientIn extends Thread {


    private BufferedReader in;
    private Menu menu;
    private ReentrantLock lock;
    private Condition c;

    public ClientIn(BufferedReader in, Menu menu, ReentrantLock lock, Condition c) {
        this.in = in;
        this.menu = menu;
        this.lock = lock;
        this.c = c;
    }

    public void run() {
        try {
            String serverIn;
            while ((serverIn = in.readLine()) != null) {
                if (serverIn.equals("Logged In")) {
                    menu.setOption(2);
                    this.lock.lock();
                    c.signal();
                    this.lock.unlock();
                } else if (serverIn.equals("Logged Out") || serverIn.equals("Not an account") || serverIn.equals("Wrong password")) {
                    menu.setOption(1);
                    this.lock.lock();
                    c.signal();
                    this.lock.unlock();
                }   else if (serverIn.equals("GAME FOUND!!!")) {
                    menu.setOption(3);
                    this.lock.lock();
                    c.signal();
                    this.lock.unlock();
                }
                System.out.println("\n"+serverIn+"\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}



