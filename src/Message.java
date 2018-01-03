import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Message {
    private List<String> message;
    private int i;
    private ReentrantLock lock;
    private Condition c;

    public Message(ReentrantLock lock, Condition c){
        this.message = new ArrayList<>();
        this.i=0;
        this.lock = lock;
        this.c = c;
    }

    public String getMessage(){
        this.lock.lock();
        try{
            if(i!=message.size())
                return this.message.get((i++));
            else return null;
        }
        finally{
            this.lock.unlock();
        }
    }

    public void setMessage(String message){
        this.lock.lock();
        try{
            this.message.add(message);
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
