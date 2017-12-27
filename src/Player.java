
public class Player {

	private String username;
	private String password;
	private int vitorias;
	private int jogos;

	public Player() {
		username=null;
		password=null;
		vitorias=0;
		jogos=0;
	}

	public Player(String username, String password, int vitorias, int jogos) {
		this.username = username;
		this.password = password;
		this.vitorias = vitorias;
		this.jogos = jogos;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public int getVitorias() {
		return this.vitorias;
	}

	public int getJogos() {
		return this.jogos;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVitorias(int vitorias) {
		this.vitorias = vitorias;
	}

	public void setJogos(int jogos) {this.jogos = jogos;}

}