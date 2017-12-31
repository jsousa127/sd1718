
public class Menu {

    private String menu;
    private int option;

    public void setVisible() {
        switch (option) {
            case 1:
                System.out.println("************* MENU ****************\n" +
                        "* 1 - Iniciar Sessao              *\n" +
                        "* 2 - Registar                    *\n" +
                        "* m - Mostrar o Menu              *\n" +
                        "* 0 - Sair                        *\n" +
                        "***********************************\n");
                break;
        }
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }
}