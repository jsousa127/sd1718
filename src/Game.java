import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Game {

	private List<Player> equipa1;
	private List<Player> equipa2;
	private int winner;
	private Lock lock;

	public Game(){
			this.equipa1=new ArrayList<>();
			this.equipa2=new ArrayList<>();
			this.winner=0;
			this.lock=new ReentrantLock();
			}

	public Game(List<Player> equipa1,List<Player> equipa2){
			this.equipa1=equipa1;
			this.equipa2=equipa2;
			winner=0;
			}

	public Game(Game g){
			equipa1=g.getequipa1();
			equipa2=g.getequipa2();
			winner=g.getWinner();
			lock=g.getLock();
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
}