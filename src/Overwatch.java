

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Overwatch {
    private Map<String,Player> players;
    private Map<String,Integer> gamers;
    private Map<Integer,Game> games;
    private int nGames;
    private Map<Integer,Queue> queues;
    private Map<String,Mensagem> mensagens;
    private Lock playersLock;
    private Lock gamersLock;
    private Lock gamesLock;
    private Lock queuesLock;
    private Lock mensagensLock;

    public Overwatch(){
        this.games = new HashMap<>();
        this.players = new HashMap<>();
        this.gamers = new HashMap<>();
        this.queues = new HashMap<>();
        this.mensagens = new HashMap<>();
        this.playersLock = new ReentrantLock();
        this.gamersLock = new ReentrantLock();
        this.gamesLock = new ReentrantLock();
        this.mensagensLock = new ReentrantLock();
        this.queuesLock = new ReentrantLock();
        nGames=0;
    }

    public Player logIn(String username, String password,Mensagem m) throws Exception{
        this.playersLock.lock();
        try{
            if(players.containsKey(username)){
                if(!players.get(username).getPassword().equals(password)) throw new Exception("Wrong password");
            }
            else throw new Exception("Not an account");
        }
        finally{
            this.playersLock.unlock();
        }
        this.mensagensLock.lock();
        try{
            if(this.mensagens.containsKey(username)){
                Mensagem ms = this.mensagens.get(username);
                String linha;
                while((linha = ms.getMensagem())!=null){
                    m.setMensagem(linha);
                }
                this.mensagens.put(username,ms);
            }
        }
        finally{
            this.mensagensLock.unlock();
        }
        this.playersLock.lock();
        try{
            return this.players.get(username);
        }
        finally{
            this.playersLock.unlock();
        }
    }

    public void signIn(String username, String password,Mensagem m) throws Exception{

        this.playersLock.lock();
        try {

            if (this.players.containsKey(username)) throw new Exception("Username já registado");
            else {
                System.out.println("qwqw");
                Player p = new Player(username, password, 0, 0);

                players.put(p.getUsername(), p);

            }

        } finally {
            this.playersLock.unlock();
        }
        this.mensagensLock.lock();
        try {
            this.mensagens.put(username, m);

        } finally {
            this.mensagensLock.unlock();
        }


    }

    public int searchGame(Player p,Mensagem m){
        int g = play(p);
        if (g!=0) {
            notifyPlayers(g,"GAME FOUND!!!",0);
            HeroPhase hp = new HeroPhase(this,g);
            hp.start();
            return 1;
        }
        else m.setMensagem("Waiting on queue...");
        return 0;
    }

    public void notifyPlayers(int game,String s,int n) {
        Mensagem m=null;
        List<Player> players=null;
        gamesLock.lock();
        Game g=null;
        try{
            g=games.get(game);
        } finally {
            gamesLock.unlock();
        }
        Lock gLock = g.getLock();
        gLock.lock();
        try {
            switch (n) {
                case 0:
                    players = g.getPlayers();
                case 1:
                    players = g.getequipa1();
                case 2:
                    players = g.getequipa2();
            }
        } finally {
            gLock.unlock();
        }
        mensagensLock.lock();
        try{
            for(Player p : players) {
                m = mensagens.get(p.getUsername());
                m.setMensagem(s);
            }
        } finally{
            mensagensLock.unlock();
        }
    }

    public void cancelGame(int g) {
        notifyPlayers(g,"GAME CANCELED",0);
        gamersLock.lock();
        try{
            removeGamers(g);
        } finally {
            gamesLock.unlock();
        }
    }

    private void removeGamers(int g) {
        gamers.forEach((k,v)->{
            if(v==g)gamers.remove(k);
        });
    }


    public int play(Player p) {
        int rank = p.getVitorias()*10/p.getJogos();
        queuesLock.lock();
        try {
            Queue sameRank = queues.get(rank);
            Queue rankm = queues.get(rank-1);
            Queue rankM = queues.get(rank+1);
            if (canPlay(sameRank, rankm)) return createGame(sameRank, rankm);
            else if (canPlay(sameRank, rankM)) return createGame(sameRank,rankM);
            else sameRank.addPlayer(p);
        } finally {queuesLock.unlock();}
        return 0;
    }

    private Boolean canPlay(Queue rank,Queue nearRank) {
        int n=0;
        n=nearRank.length();
        n+=rank.length();
        if(n>1)return TRUE;
        return FALSE;
    }

    private int createGame(Queue rank,Queue nearRank) {

        List<Player> players = get10(rank,nearRank);
        int id=0;
        Game game = new Game(players.subList(0,4),players.subList(5,9));
        gamesLock.lock();
        try{
            nGames++;
            id=nGames;
            games.put(nGames,game);
        } finally {
            gamesLock.unlock();
        }
        players.forEach(p->gamers.put(p.getUsername(),nGames));
        return id;


    }

    private List<Player> get10(Queue rank,Queue nearRank) {
        List<Player> players = new ArrayList<>();
        Lock rankLock=rank.getLock();
        Lock nearRankLock=nearRank.getLock();
        rankLock.lock();
        try {
            rank.getPlayers().forEach((k, v) -> {
                if (players.size() < 2) {
                    players.add(v);
                    rank.remove(v);
                }
            });
        } finally {
            rankLock.unlock();
        }
        int n = players.size();
        int x = 2-n;
        n=0;
        nearRankLock.lock();
        try {
            while (n < x) {
                Player p = nearRank.getPlayer(n);
                players.add(p);
                nearRank.remove(p);
                n++;
            }
        } finally {
            nearRankLock.unlock();
        }

        return players;
    }


    public boolean ready(int game) {
        gamesLock.lock();
        try{
            if(games.get(game).getHeroes().size()==2) return TRUE;
        } finally {
            gamesLock.unlock();
        }
        return FALSE;
    }

    public int getRank(Player player) {
        return player.getVitorias()*10/player.getJogos();

    }

    public void setHero(Player player, String hero, Mensagem m) {
        int key;
        String username=player.getUsername();
        Game g;
        gamersLock.lock();
        try {
            key=gamers.get(username);
        } finally {
            gamersLock.unlock();
        }
        gamesLock.lock();
        try{
            g=games.get(key);
        } finally {
            gamesLock.unlock();
        }
        Lock gLock = g.getLock();
        gLock.lock();
        try{
            if(g.addHero(username,hero)) {
                m.setMensagem("Hero Selected");
                notifyPlayers(key,username+"picked"+hero,g.getTeam(player));
            }
            else m.setMensagem("Already picked by your team");
        } finally {
            gLock.unlock();
        }
    }
}


