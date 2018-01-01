import java.io.PrintWriter;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerOut extends Thread{

    private final PrintWriter out;
    private Mensagem  m;
    private ReentrantLock lock;
    private Condition c;

    public ServerOut(Mensagem m,PrintWriter out) {
        this.m = m;
        this.out = out;
        this.c = m.getCondition();
        this.lock = m.getLock();
    }

    public void run() {
        this.lock.lock();
        try{
            String linha;
            while(true){
                while((linha = m.getMensagem())==null) c.await();
                this.out.println(linha);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            this.lock.unlock();
        }
    }


}
