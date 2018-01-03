public class Menu {

    private int option;

    public void setVisible() {
        switch (option) {
            case 1:
                System.out.println("************* MENU ****************\n" +
                        "* 1 - Log In                      *\n" +
                        "* 2 - Sign In                     *\n" +
                        "* 0 - Exit                        *\n" +
                        "***********************************\n");
                break;

            case 2: System.out.println("************* MENU ****************\n"+
                    "* 1 - Search Game                 *\n"+
                    "* 2 - Check Rank                  *\n"+
                    "* 0 - Exit                        *\n"+
                    "***********************************\n");
                break;

            case 3: System.out.println("************* Pick Hero************\n"+
                    "*1-Alejandra                      *\n"+
                    "*2-Ana                            *\n"+
                    "*3-Bastion                        *\n"+
                    "*4-D.Va                           *\n"+
                    "*5-Doomfist                       *\n"+
                    "*6-Genji                          *\n"+
                    "*7-Hakim                          *\n"+
                    "*8-Hanzo                          *\n"+
                    "*9-Junkrat                        *\n"+
                    "*10-Liao                          *\n"+
                    "*11-Lucio                         *\n"+
                    "*12-McCree                        *\n"+
                    "*13-Mei                           *\n"+
                    "*14-Mercy                         *\n"+
                    "*15-Moira                         *\n"+
                    "*16-Orisa                         *\n"+
                    "*17-Pharah                        *\n"+
                    "*18-Reaper                        *\n"+
                    "*19-Reinhardt                     *\n"+
                    "*20-Roadhog                       *\n"+
                    "*21-Soldier:76                    *\n"+
                    "*22-Sombra                        *\n"+
                    "*23-Symmetra                      *\n"+
                    "*24-Sven                          *\n"+
                    "*25-Torbjorn                      *\n"+
                    "*26-Tracer                        *\n"+
                    "*27-Widowmaker                    *\n"+
                    "*28-Winston                       *\n"+
                    "*29-Zarya                         *\n"+
                    "*30-Zenyatta                      *\n");
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