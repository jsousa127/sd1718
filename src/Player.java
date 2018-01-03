
public class Player {

	private String username;
	private String password;
	private int wins;
	private int games;

	public Player(String username, String password) {
		this.username = username;
		this.password = password;
		this.wins = 0;
		this.games = 0;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public int getWins() {
		return this.wins;
	}

	public int getGames() {
		return this.games;
	}

	public void addWin() {
		wins++;
		games++;
	}

	public void addLoss() {
		games++;
	}
}