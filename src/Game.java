import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Game {

	private ArrayList<Player> horde;
	private ArrayList<Player> alliance;
	private int winner;
	private Lock lock;

	public Game(){
			this.horde=new ArrayList<Player>();
			this.alliance=new ArrayList<Player>();
			this.winner=0;
			this.lock=new ReentrantLock();
			}

	public Game(ArrayList<Player> horde,ArrayList<Player> alliance,Lock lock){
			this.horde=horde;
			this.alliance=alliance;
			this.lock=lock;
			winner=0;
			}

	public Game(Game g){
			horde=g.getHorde();
			alliance=g.getAlliance();
			winner=g.getWinner();
			lock=g.getLock();
			}

	public ArrayList<Player> getHorde(){
			return horde;
			}

	public void setHorde(ArrayList<Player> horde){
			this.horde=horde;
			}

	public ArrayList<Player> getAlliance(){
			return alliance;
			}

	public void setAlliance(ArrayList<Player> alliance){
			this.alliance=alliance;
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
}