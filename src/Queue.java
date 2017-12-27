import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Queue {

	private HashSet<Player> players;
	private int rank;
	private Lock lock;

	public Queue() {
		players = new HashSet<>();
		rank = 0;
		lock = new ReentrantLock();
	}

	public Queue(HashSet<Player> players,int rank,Lock lock) {
		this.players = players;
		this.rank = rank;
		this.lock = lock;
	}

	public Queue(Queue q) {
		players = q.getPlayers();
		rank = q.getRank();
		lock = q.getLock();
	}

	public HashSet<Player> getPlayers() {
		return players;
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

	public void setPlayers(HashSet<Player> players) {
		this.players = players;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}
