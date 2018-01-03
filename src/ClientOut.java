import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientOut extends Thread {

    private BufferedReader in;
    private PrintWriter writer;
    private Socket socket;
    private Menu menu;
    private ReentrantLock lock;
    private Condition c;

    public ClientOut(Socket socket, Menu menu, ReentrantLock lock, Condition c) {
        try {
            this.socket = socket;
            this.menu = menu;
            this.in = new BufferedReader(new InputStreamReader(System.in));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.lock = lock;
            this.c = c;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void run() {
        String systemIn;
        try{
            menu.setVisible();
            while((systemIn = in.readLine())!= null){
                if(menu.getOption()==1) {
                    if (systemIn.equals("1")) {
                        writer.println("login");
                        System.out.print("Username:");
                        systemIn = in.readLine();
                        writer.println(systemIn);
                        System.out.print("Password:");
                        systemIn = in.readLine();
                        writer.println(systemIn);
                        this.lock.lock();
                        c.await();
                        this.lock.unlock();
                        systemIn="1";
                    } else if (systemIn.equals("2")) {
                        writer.println("signin");
                        System.out.print("Username: ");
                        systemIn = in.readLine();
                        writer.println(systemIn);
                        System.out.print("Password: ");
                        systemIn = in.readLine();
                        writer.println(systemIn);
                        this.lock.lock();
                        c.await();
                        this.lock.unlock();
                        systemIn="2";
                    }
                    if(systemIn.equals("1")||systemIn.equals("2") || systemIn.equals("3") || systemIn.equals("m")){
                        System.out.println("\n\n\n\n\n");
                        menu.setVisible();
                    }
                }
                else if(menu.getOption()==2) {
                    if (systemIn.equals("1"))
                        writer.println("searchgame");
                    else if (systemIn.equals("2"))
                        writer.println("getrank");
                    else if(systemIn.equals("0"))
                        break;
                    if (systemIn.equals("2") || systemIn.equals("m")){
                        System.out.println("\n\n\n\n\n");
                        menu.setVisible();
                    }

                }
                else if(menu.getOption()==3) {
                    writer.println("H"+systemIn);
                }
            }
            socket.shutdownOutput();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }



    }
}