import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ClientOut extends Thread {

    private BufferedReader in;
    private PrintWriter writer;
    private Socket socket;
    private Menu menu;
    private Lock lock;
    private Condition c;


    public ClientOut(Socket socket,Menu menu, Lock lock, Condition c) {
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
                        systemIn = "1";
                    } else if (systemIn.equals("2")) {
                        writer.println("signin");
                        System.out.print("Username: ");
                        systemIn = in.readLine();
                        writer.println(systemIn);

                        System.out.print("Password: ");
                        systemIn = in.readLine();
                        writer.println(systemIn);
                        systemIn = "2";
                    }
                    if(systemIn.equals("1") || systemIn.equals("2") || systemIn.equals("3") || systemIn.equals("0") || systemIn.equals("m")){
                        space();
                        menu.setVisible();
                    }
                    else System.out.println("Opção inválida.");
                }
            }
            socket.shutdownOutput();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }



    }

    private void space(){
        for(int i = 0;i<40;i++)
            System.out.println();
    }
}