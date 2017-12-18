import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Game {

	private List<Player> equipa1;
	private List<Player> equipa2;
	private Map<String,String> heroes;



	public Map<String, String> getHeroes() {
		return heroes;
	}

	public void setHeroes(Map<String, String> heroes) {
		this.heroes = heroes;
	}

	private int winner;
	private Lock lock;

	public Game(){
			this.equipa1=new ArrayList<>();
			this.equipa2=new ArrayList<>();
			this.heroes =new HashMap<>();
			this.winner=0;
			this.lock=new ReentrantLock();
			}

	public Game(List<Player> equipa1,List<Player> equipa2){
			this.equipa1=equipa1;
			this.equipa2=equipa2;
			this.heroes =new HashMap<>();
			this.winner=0;
			this.lock=new ReentrantLock();
			}

	public Game(Game g){
			equipa1=g.getequipa1();
			equipa2=g.getequipa2();
			winner=g.getWinner();
			lock=g.getLock();
			heroes=g.getHeroes();
			}

	public List<Player> getequipa1(){
			return equipa1;
			}

	public void setequipa1(ArrayList<Player> equipa1){
			this.equipa1=equipa1;
			}

	public List<Player> getequipa2(){
			return equipa2;
			}

	public void setequipa2(List<Player> equipa2){
			this.equipa2=equipa2;
			}

	public void setLock(Lock lock) {
			this.lock = lock;
		}

	public int getWinner(){
			return winner;

			}

	public void setWinner(int winner){
			this.winner=winner;
			}

	public Lock getLock(){
			return lock;
			}

	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<>();
		players.addAll(equipa1);
		players.addAll(equipa2);
		return players;
	}

	public int getTeam(Player p){
		if (equipa1.contains(p)) return 1;
		else return 2;
	}

	public boolean addHero(String username, String h) {
		if(equipa1.contains(username)) {
			for (Player p : equipa1) {
				if (heroes.containsKey(p.getUsername()))
					if (heroes.get(p.getUsername()).equals(h)) return FALSE;
			}
		} else if(equipa2.contains(username)) {
			for (Player p : equipa2) {
				if (heroes.containsKey(p.getUsername()))
					if (heroes.get(p.getUsername()).equals(h)) return FALSE;
			}
		}
		lock.lock();

		try {
			heroes.put(username, h);
		} finally {
			lock.unlock();
		}
		return TRUE;
	}
}