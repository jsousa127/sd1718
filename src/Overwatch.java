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
    private Map<String,Integer> gamers; //Players gaming at the moment;
    private Map<Integer,Game> games;
    private Map<Integer,Queue> queues; //Rank Queues;
    private Map<String, Message> messages; //Player messages;
    private Lock playersLock;
    private Lock gamersLock;
    private Lock gamesLock;
    private Lock queuesLock;
    private Lock messagesLock;
    private int nGames;
    private final int nPlayers=4; // Team length;

    public Overwatch(){
        this.games = new HashMap<>();
        this.players = new HashMap<>();
        this.gamers = new HashMap<>();
        this.queues = new HashMap<>();
        this.messages = new HashMap<>();
        this.playersLock = new ReentrantLock();
        this.gamersLock = new ReentrantLock();
        this.gamesLock = new ReentrantLock();
        this.messagesLock = new ReentrantLock();
        this.queuesLock = new ReentrantLock();
        nGames=0;
        for(int n=0;n<10;n++)
            this.queues.put(n,new Queue());
    }

    /**Log In
     * @return player who just logged in
     * */
    public Player logIn(String username, String password, Message m) throws Exception{
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
        this.messagesLock.lock();
        try{
            if(this.messages.containsKey(username)){
                Message ms = this.messages.get(username);
                String linha;
                while((linha = ms.getMessage())!=null){
                    m.setMessage(linha);
                }
                this.messages.put(username,ms);
            }
        }
        finally{
            this.messagesLock.unlock();
        }
        this.playersLock.lock();
        try{
            return this.players.get(username);
        }
        finally{
            this.playersLock.unlock();
        }
    }

    /**Sign in
     */
    public Player signIn(String username, String password, Message m) throws Exception{
        Player p;
        this.playersLock.lock();
        try {

            if (this.players.containsKey(username)) throw new Exception("Username in use");
            else {
                p = new Player(username, password);

                players.put(p.getUsername(), p);

            }

        } finally {
            this.playersLock.unlock();
        }
        this.messagesLock.lock();
        try {
            this.messages.put(username, m);

        } finally {
            this.messagesLock.unlock();
        }

        return p;
    }

    /**Search for a game
     * @return the game id or 0 if in Queue
     */
    public int searchGame(Player p, Message m){

        int g = play(p);

        if (g!=0) {

            notifyPlayers(g,"GAME FOUND!!!",0);
            HeroPhase hp = new HeroPhase(this,g);
            hp.start();
            return 1;
        }
        else m.setMessage("Waiting on queue...");
        return 0;
    }

    /**Notify players of a certain game
     *
     * @param game is the id of the game
     * @param s is the message to send
     * @param n is the team to send, or 0 to send to both teams
     */
    private void notifyPlayers(int game,String s,int n) {
        Message m=null;
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

                    break;
                case 1:
                    players = g.getTeamMembers(1);
                    break;
                case 2:
                    players = g.getTeamMembers(2);
                    break;
            }
        } finally {
            gLock.unlock();
        }

        messagesLock.lock();
        try{
            for(Player p : players) {

                m = messages.get(p.getUsername());

                m.setMessage(s);
            }

        } finally{
            messagesLock.unlock();
        }
    }

    /** Try to start a game for the given player
     *
     * @param p is the player wanting to play
     * @return the game id, or 0 if no game can be created
     */
    private int play(Player p) {

        int wins=p.getWins(),games=p.getGames(),rank;
        if (wins==0)
            rank=0;
        else {
            if (games<=15)games*=2; // Newbies, get rank reduction by half
            rank = (wins*10)/games;
        }
        if(rank>9) rank=9;
        queuesLock.lock();
        try {

            Queue sameRank = queues.get(rank);
            sameRank.addPlayer(p);
            Queue rankm = queues.getOrDefault(rank-1,null);
            Queue rankM = queues.getOrDefault(rank+1,rankm);

            if (canPlay(sameRank, rankM)) return createGame(sameRank,rankM);
            else return 0;
        } finally {queuesLock.unlock();}



    }

    /** Verify if two queues have enough players to create a game
     *
     */
    private Boolean canPlay(Queue rank,Queue nearRank) {
        int n;
        n=nearRank.length();
        n+=rank.length();
        if(n>=nPlayers)return TRUE;
        return FALSE;
    }

    /** Given two queues with enough players, creates a game

     * @return the new game id
     */
    private int createGame(Queue rank,Queue nearRank) {
        List<Player> players = get10(rank,nearRank);
        int id=0;
        Game game = new Game(players.subList(0,nPlayers/2),players.subList(nPlayers/2,nPlayers));

        gamesLock.lock();
        try{
            nGames++;
            id=nGames;
            games.put(nGames,game);
        } finally {
            gamesLock.unlock();
        }
        gamersLock.lock();
        try {
            players.forEach(p -> gamers.put(p.getUsername(), nGames));
        } finally {
            gamersLock.unlock();
        }

        return id;
    }

    /**Get 10 players from two queues
     *
     * @param rank is the player rank;
     * @param nearRank is +1 or -1 rank;
     * @return 10 players of near rank
     */
    private List<Player> get10(Queue rank,Queue nearRank) {
        List<Player> players = new ArrayList<>();
        Lock rankLock=rank.getLock();
        Lock nearRankLock=nearRank.getLock();

        rankLock.lock();
        try {
            for(int n=0;n<nPlayers;n++) {
                if (rank.length()==0) break;
                Player p = rank.getPlayer(0);
                players.add(p);
                rank.remove(p.getUsername());
            }
        } finally {
            rankLock.unlock();
        }
        int n = players.size();
        if(n<nPlayers) {
            int x = nPlayers - n;
            n = 0;
            nearRankLock.lock();
            try {
                while (n < x) {
                    Player p = nearRank.getPlayer(n);
                    players.add(p);
                    nearRank.remove(p.getUsername());
                    n++;
                }
            } finally {
                nearRankLock.unlock();
            }
        }
        return players;
    }

    /**Check if a game is ready after hero selection
     */
    public synchronized boolean ready(int game) {
        if(!(games.get(game).getHeroes().size()==nPlayers)) return FALSE;
        notifyPlayers(game,"Game started",0);
        return TRUE;
    }

    /** Cancel a game
     *
     * @param g is the game id
     */
    public void cancelGame(int g) {
        notifyPlayers(g,"GAME CANCELED",0);
        removeGamers(g);
    }

    /**Remove a game from list os gaming at the moment
     *
     * @param g is the game id
     */
    private synchronized void removeGamers(int g) {
        while (gamers.values().remove(g));
    }

    /**Get the rank of a player
    */
    public int getRank(Player player) {
        int wins=player.getWins(),games=player.getGames();
        if(wins==0) return 0;
        if(games<=15) games*=2; //Newbies, get rank reduction by half
        int rank = wins*10/games;
        if(rank>9)rank=9;
        return rank;

    }

    /** Set a hero for a player in game, if possible
     *
     * @param player in game who chose the hero
     * @param hero to be picked
     * @param m is the Message of the player
     */
    public void setHero(Player player, String hero, Message m) {
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
                m.setMessage("Hero Selected");
                notifyPlayers(key,username+" picked "+hero,g.getTeam(player));
            }
            else m.setMessage("Already picked by your team");
        } finally {
            gLock.unlock();
        }
    }

    /**Set the winner for a given game
     *
     * @param game
     */
    public void setWinner(int game) {
        Game g;
        gamesLock.lock();
        try{
            g = games.get(game);
        } finally {
            gamesLock.unlock();
        }
        if(Math.random()>0.4)
            g.setWinner(1);
        else g.setWinner(2);
    }
}


