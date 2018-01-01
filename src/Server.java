import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {


    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1234);
        Socket sock;
        ReentrantLock lock = new ReentrantLock();
        Overwatch ow = new Overwatch();
        try{
            while ((sock=server.accept()) != null) {
                Condition c = lock.newCondition();
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                PrintWriter out = new PrintWriter(sock.getOutputStream(),true);
                Mensagem m = new Mensagem(lock,c);
                ServerIn sti = new ServerIn(m,in,ow);
                ServerOut sto = new ServerOut(m,out);
                sti.start();
                sto.start();
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

    }
}


