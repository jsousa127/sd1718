import java.io.BufferedReader;
import java.io.IOException;

public class ServerIn extends Thread {
    private Player player;
    private Mensagem m;
    private BufferedReader in;
    private Overwatch ow;

    public ServerIn(Mensagem m, BufferedReader in, Overwatch ow) throws IOException {
        this.m = m;
        this.in = in;
        this.ow = ow;
        player = null;
    }

    public void run() {
        try {
            String systemIn;
            while ((systemIn = in.readLine()) != null) {
                if (systemIn.equals("login")) {
                    String username, password;
                    username = in.readLine();
                    password = in.readLine();
                    try {
                        this.player = ow.logIn(username, password, m);
                        m.setMensagem("Logged In");
                    } catch (Exception e) {
                        m.setMensagem(e.getMessage());
                    }
                } else if (systemIn.equals("signin")) {
                    String user, pass;
                    user = in.readLine();
                    pass = in.readLine();
                    try {
                        ow.signIn(user, pass, m);
                        m.setMensagem("Signed in");
                    } catch (Exception e) {
                        m.setMensagem(e.getMessage());
                    }
                } else if (systemIn.equals("searchgame")) {
                    try {
                        ow.searchGame(player, m);
                    } catch (Exception e) {
                        m.setMensagem(e.getMessage());
                    }
                } else if (systemIn.equals("getrank")) {
                    int n = ow.getRank(player);
                    m.setMensagem("Rank:"+n);
                }
                else if (systemIn.startsWith("H")) {
                    try {
                        ow.setHero(player, systemIn,m);
                    } catch (Exception e) {
                        m.setMensagem(e.getMessage());
                    }

                }
            }
            in.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}