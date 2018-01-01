import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Mensagem {
    private List<String> mensagem;
    private int i;
    private ReentrantLock lock;
    private Condition c;

    public Mensagem(ReentrantLock lock,Condition c){
        this.mensagem = new ArrayList<>();
        this.i=0;
        this.lock = lock;
        this.c = c;
    }

    public String getMensagem(){
        this.lock.lock();
        try{
            if(i!=mensagem.size())
                return this.mensagem.get((i++));
            else return null;
        }
        finally{
            this.lock.unlock();
        }
    }

    public void setMensagem(String mensagem){
        this.lock.lock();
        try{
            this.mensagem.add(mensagem);
            //for(String m : mensagem) this.mensagem.add(m);
            c.signal();
        } finally {
            this.lock.unlock();
        }
    }

    public ReentrantLock getLock() {
        return this.lock;
    }

    public Condition getCondition() {
        return this.c;
    }

}
