

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Overwatch {
    private Map<String,Player> players;
    private Map<String,Game> games;
    private Map<Integer,Queue> queues;
    private Map<String,Mensagem> mensagens;
    private Lock playersLock;
    private Lock gamesLock;
    private Lock queuesLock;
    private Lock mensagensLock;

    public Overwatch(){
        this.games = new HashMap<>();
        this.queues = new HashMap<>();
        this.mensagens = new HashMap<>();
        this.gamesLock = new ReentrantLock();
        this.mensagensLock = new ReentrantLock();
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
                Player p = new Player(username, password, 0, 0);

                players.put(p.getUsername(), p);
                this.mensagensLock.lock();
                try {
                    this.mensagens.put(username, m);
                } finally {
                    this.mensagensLock.unlock();
                }
            }
        } finally {
            this.playersLock.unlock();
        }

    }

    public Game play(Player p) {
        int rank = p.getVitorias()/p.getJogos();
        queuesLock.lock();
        try {
            Queue sameRank = queues.get(rank);
            List<Queue> nearRank = new ArrayList<>();
            queues.forEach((k, v) -> {
                if ((k == rank - 1 || k == rank + 1) && v.length() > 0) nearRank.add(v);
            });
            if (canPlay(sameRank, nearRank)) return startGame(sameRank, nearRank);
            else sameRank.addPlayer(p);
        } finally {queuesLock.unlock();}
        return null;
    }

    public Boolean canPlay(Queue rank,List<Queue> list) {
        int n=0;
        n=list.stream().map(q->q.length()).mapToInt(Integer::intValue).sum();
        n+=rank.length();
        if(n>9)return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public Game startGame(Queue rank,List<Queue> nearRank) {

        List<Player> players = get10(rank,nearRank);
        Game game = new Game(players.subList(0,4),players.subList(5,9));
        return game;


    }

    public List<Player> get10(Queue rank,List<Queue> nearRank) {
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


