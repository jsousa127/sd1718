import java.net.Socket;

public class Client {

    public void main(String[] args) throws Exception{

        Socket client = new Socket(args[0],7777);
        ClientIn clientIn = new ClientIn(client);
        ClientOut clientOut = new ClientOut(client);
        clientIn.start();
        clientOut.start();

    }

}
