import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Queue {

	private HashMap<String,Player> players;
	private int nPlayers;
	private Lock lock;

	public Queue() {
		this.players = new HashMap<>();;
		this.lock = new ReentrantLock();
		nPlayers = 0;
	}

	public Player getPlayer(int index) {
	    return players.entrySet().stream().collect(Collectors.toList()).get(index).getValue();
    }

	public Lock getLock() {
		return lock;
	}

	public int length() {
	    return nPlayers;
    }

    public void addPlayer(Player p) {
	    players.put(p.getUsername(),p);
	    nPlayers++;
    }

    public void remove(String v) {
	    players.remove(v);
	    nPlayers--;

    }
}
