public class Queue {

	private HashSet<Player> players;
	private ReentrantLock lock;

	public Queue() {
		players = new HashSet<>();
		lock = new ReentrantLock();
	}

	public Queue(HashSet<Player> p) {
		players = p;
		lock = new ReentrantLock();
	}

	public Queue(Queue q) {
		players = q.getPlayers();
		lock = new ReentrantLock();
	}

	public getPlayers() {
		return this.players;
		lock = new ReentrantLock();
	}
}
