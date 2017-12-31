import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Queue {

	private HashMap<String,Player> players;
	private int rank;
	private Lock lock;

	public Queue() {
		players = new HashMap<>();
		rank = 0;
		lock = new ReentrantLock();
	}

	public Queue(HashMap<String,Player> players,int rank,Lock lock) {
		this.players = players;
		this.rank = rank;
		this.lock = lock;
	}

	public Queue(Queue q) {
		players = q.getPlayers();
		rank = q.getRank();
		lock = q.getLock();
	}

	public HashMap<String,Player> getPlayers() {
		return players;
	}

	public Player getPlayer(int index) {
	    return players.entrySet().stream().collect(Collectors.toList()).get(index).getValue();
    }

	public int getRank() {
		return rank;
	}

	public Lock getLock() {
		return lock;
	}

	public void setLock(Lock lock) {
		this.lock = lock;
	}

	public void setPlayers(HashMap<String,Player> players) {
		this.players = players;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int length() {
	    return players.size();
    }

    public void addPlayer(Player p) {
	    players.put(p.getUsername(),p);
    }

    public void remove(Player v) {
	    players.remove(v.getUsername());
    }
}
