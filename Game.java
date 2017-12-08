public class Game() {
	
	private ArrayList<Jogador> equipa1;
	private ArrayList<Jogador> equipa2;
	private int vencedor;
	private ReentrantLock lock;

	public Game() {
		this.equipa1 = new ArrayList<>();
		this.equipa2 = new ArrayList<>();
		this.vencedor = 0;
		this.lock = new ReentrantLock(); 
	}

	public Game(ArrayList<Jogador> equipa1, ArrayList<Jogador> equipa2) {
		this.equipa1 = equipa1;
		this.equipa2 = equipa2;
		vencedor = 0;
		lock = new ReentrantLock();
	}

	public Game(Game g) {
		equipa1 = g.getEquipa1();
		equipa2 = g.getEquipa2();
		vencedor = g.getVencedor();
		lock = new ReentrantLock(); 
	}

	public ArrayList<Jogador> getEquipa1() {
        return equipa1;
    }

    public void setEquipa1(ArrayList<Jogador> equipa1) {
        this.equipa1 = equipa1;
    }

    public ArrayList<Jogador> getEquipa2() {
        return equipa2;
    }

    public void setEquipa2(ArrayList<Jogador> equipa2) {
        this.equipa2 = equipa2;
    }

    public int getVencedor() {
        return vencedor;
    }

    public void setVencedor(int vencedor) {
        this.vencedor = vencedor;
    }

    public ReentrantLock getLock() {
        return lock;
    }

