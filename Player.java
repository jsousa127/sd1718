
public class Player {

	private String username;
	private String password;
	private int rank;

	public Player() {
		username=null;
		password=null;
		rank=0;
	}

	public Player(String username, String password, int rank) {
		this.username = username;
		this.password = password;
		this.rank = rank;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public int getRank() {
		return this.rank;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
}