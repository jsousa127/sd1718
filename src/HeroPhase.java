public class HeroPhase extends Thread{

    Overwatch ow;
    int game;

    public HeroPhase(Overwatch ow,int game) {
        this.ow=ow;
        this.game=game;
    }

    public void run() {
        try {
            sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String message;
        if(ow.ready(game)) ow.notifyPlayers(game,"Game started",0);
        else ow.cancelGame(game);

    }


}
