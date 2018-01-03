import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Game {

	private List<Player> team1;
	private List<Player> team2;
	private Map<String,String> heroes;
	private Lock lock;

	public Game(List<Player> team1,List<Player> team2){
			this.team1=team1;
			this.team2=team2;
			this.heroes =new HashMap<>();
			this.lock=new ReentrantLock();
			}

	public Map<String, String> getHeroes() {
		return heroes;
	}

	public Lock getLock(){
		return lock;
	}

	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<>();
		players.addAll(team1);
		players.addAll(team2);
		return players;
	}

	public int getTeam(Player p){
		if (team1.contains(p)) return 1;
		else return 2;
	}

	public void setWinner(int winner){
		List<Player> w,l;
		if(winner==1){
			w=team1;
			l=team2;
		}
		else {
			w=team2;
			l=team1;
		}
		for(Player p: w)
			p.addWin();
		for(Player p: l){
			p.addLoss();
		}

	}

	public List<Player> getTeamMembers(int team){
		if(team==1) return team1;
		else return team2;
	}

	public boolean addHero(String username, String h) {
		if(heroes.containsValue(h)){
			List<String> players = heroes.entrySet().stream().filter(e->e.getValue().equals(h)).map(e->e.getKey()).collect(Collectors.toList());
			if(players.size()>1) return FALSE;
			else if (sameTeam(players.get(0),username)) return FALSE;
		}

		lock.lock();
		try {
			heroes.put(username, h);
		} finally {
			lock.unlock();
		}
		return TRUE;
	}

	private boolean sameTeam(String player1, String player2) {
		int n=0;
		for(Player p:team1){
			if (p.getUsername().equals(player1))n++;
			else if (p.getUsername().equals(player2))n++;
		}
		for(Player p:team1){
			if (p.getUsername().equals(player1))n++;
			else if (p.getUsername().equals(player2))n++;
		}
		if (n>1) return FALSE;
		else return TRUE;
	}
}