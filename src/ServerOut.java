import java.io.PrintWriter;

public class ServerOut extends Thread{

    private final Player player;
    private final PrintWriter out;

    public ServerOut(Player player,PrintWriter out) {
        this.player = player;
        this.out = out;
    }

    public void run() {}

}
