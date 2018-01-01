

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
    private Map<Integer,Game> games;
    private Map<Integer,Queue> queues;
    private Map<String,Mensagem> mensagens;
    private Lock playersLock;
    private Lock gamesLock;
    private Lock queuesLock;
    private Lock mensagensLock;

    public Overwatch(){
        this.games = new HashMap<>();
        this.players = new HashMap<>();
        this.queues = new HashMap<>();
        this.mensagens = new HashMap<>();
        this.playersLock = new ReentrantLock();
        this.gamesLock = new ReentrantLock();
        this.mensagensLock = new Reen,[trantLock();
        this.queuesLock = new ReentrantLock();
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
        Game g = play(p);
        if (g!=null) {
            notifyPlayers(g,"GAME FOUND!!!");
            selectHeroes(g);
        }
        else m.setMensagem("Waiting on queue...");
        return 0;
    }

    private void notifyPlayers(Game g,String s) {
        Mensagem m=null;
        int n=0;
        mensagensLock.lock();
        try{
            for(Player p : g.getPlayers()) {
                m = mensagens.get(p.getUsername());
                m.setMensagem(s);
            }
        } finally{
            mensagensLock.unlock();
        }
    }

    private boolean selectHeroes(Game g) {
        long endTime = System.currentTimeMillis() + 30000;
        Mensagem m=null;
        while (System.currentTimeMillis() < endTime) {
            for(Player p : g.getPlayers()) {
                mensagensLock.lock();
                try {
                    m = mensagens.get(p);
                }finally {
                    mensagensLock.unlock();
                }
                if (m.getMensagem() != null)
                    if(!g.addHero(p.getUsername(),m.getMensagem()))m.setMensagem("Your team has already chosen this hero.");
            }
        }
        if (g.getHeroes().size()!=10) cancelGame(g);
        else return TRUE;
        return FALSE;
    }

    private void cancelGame(Game g) {
        notifyPlayers(g,"GAME CANCELED");
        gamesLock.lock();
        try{
            games.remove(g.getId());
        } finally {
            gamesLock.unlock();
        }
        g=null;
    }


    public Game play(Player p) {
        int rank = p.getVitorias()/p.getJogos();
        queuesLock.lock();
        try {
            Queue sameRank = queues.get(rank);
            List<Queue> rankm = new ArrayList<>();
            List<Queue> rankM = new ArrayList<>();
            queues.forEach((k, v) -> {
                if (k == rank - 1 && v.length() > 0) rankm.add(v);
                if (k == rank + 1 && v.length() > 0) rankM.add(v);
            });
            if (canPlay(sameRank, rankm)) return createGame(sameRank, rankm);
            else if (canPlay(sameRank, rankM)) return createGame(sameRank,rankM);
            else sameRank.addPlayer(p);
        } finally {queuesLock.unlock();}
        return null;
    }

    private Boolean canPlay(Queue rank,List<Queue> list) {
        int n=0;
        n=list.stream().map(q->q.length()).mapToInt(Integer::intValue).sum();
        n+=rank.length();
        if(n>9)return TRUE;
        return Boolean.FALSE;
    }

    private Game createGame(Queue rank,List<Queue> nearRank) {

        List<Player> players = get10(rank,nearRank);
        Game game = new Game(players.subList(0,4),players.subList(5,9));
        return game;


    }

    private List<Player> get10(Queue rank,List<Queue> nearRank) {
        List<Player> players = new ArrayList<>();
        rank.getPlayers().forEach((k,v)->{players.add(v);rank.remove(v);});
        int n = players.size();
        int x = 10-n;
        n=0;
        if(x==0);
        if(nearRank.size()==1||x==1) {
            while(n<x) {
                Player p = nearRank.get(0).getPlayer(n);
                players.add(p);
                nearRank.get(0).remove(p);
                n++;
            }
        }
        else {
            while(n<x){
                Player p1 = nearRank.get(0).getPlayer(n);
                Player p2 = nearRank.get(1).getPlayer(n);
                players.add(p1);
                nearRank.get(0).remove(p1);
                if(n==x-2) break;
                n++;
                players.add(p2);
                nearRank.get(1).remove(p2);
                n++;
            }

        }
        return players;
    }


}


