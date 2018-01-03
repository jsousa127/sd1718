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
        if(ow.ready(game))
            ow.setWinner(game);
        else ow.cancelGame(game);

    }


}
