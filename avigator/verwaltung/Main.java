package avigator.verwaltung;

/**
 * Startet die Avigator-Anwendung und initialisiert die gespeicherten Anwendungsdaten.
 *
 * @author Lars Pfeiffer
 */
public class Main {

    /**
     * Erzeugt eine neue Instanz der Main-Klasse.
     */
    public Main() {

    }

    /**
     * Einstiegspunkt der Anwendung.
     *
     * @param args die Kommandozeilenargumente, die von der Anwendung nicht ausgewertet werden
     */
    @SuppressWarnings({"UnnecessaryModifier", "unused"})
    public static void main(String[] args) {

        druckeAvigatorLogo();

        DatenHandler datenHandler = new DatenHandler();

        Anwendungsdaten anwendungsdaten = datenHandler.initialisiereAnwendungsdaten();

        Manager manager = new Manager(datenHandler, anwendungsdaten);

        manager.start();
    }

    /**
     * Gibt das Avigator-Logo auf der Konsole aus.
     */
    private static void druckeAvigatorLogo() {

        System.out.println(
                "                                            =---=======================================--=++");
        System.out.println(
                "                                        +-=+#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*=-+");
        System.out.println(
                "                                      #-=#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%+-#");
        System.out.println(
                "                                     ==#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%++");
        System.out.println(
                "                                    *-%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%@@%+*");
        System.out.println(
                "                                   +-%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%@@@@@@%+*");
        System.out.println(
                "                                   =#%%%%%%%%%%%%%%%%%%%%%%%%%+**********@@%%%%%%%@@@@@@@@@@@@@@@@%*%");
        System.out.println(
                "                                  #+#%%%%%%%%%%%%%%%%%%%%%%%%************#@@@@@@@@@@@@@@@@@@@@@@@@@##");
        System.out.println(
                "                                  #+%%%%%%%%%%%%%%%%%%%%%%@@**************%@@@@@@@@@@@@@@@%*#@@@@@@#*");
        System.out.println(
                "                                  #+%%%%%%%%%%%%%%%%%%@@@@@****************%@@@@@@@##%%%%**%@@@@@@@#*");
        System.out.println(
                "                                  *+%%%%%%%%%%%%%@@@@@@@@@#**************##*%@@@@@@@@%#**#%@@@@@@@@#*");
        System.out.println(
                "                                  *+%%%%%%%%@@@@@@@@@@@@@#+************####*#@@@@@@@%@*#%#%@@@@@@@@#*");
        System.out.println(
                "                                  #+%@@@@@@@@@@@@@@@@@@@#***********########*#@@@@@@@##@@@#@@@@@@@@#*");
        System.out.println(
                "                                  #+%@@@@@@@@@@@@@@@@@@#*******##*#%*##########@@@@@#@@@@@@@@@@@@@@#*");
        System.out.println(
                "                                  #*%@@@@@@@@@@@@@@@@@%+****####*#@@###########%@@##@@@@@@@@@@@@@@@#*");
        System.out.println(
                "                                  **%@@@@@@@@@@@@@@@@%+***#######@@@@#########%@@*%@@@@@@@@@@@@@@@@#*");
        System.out.println(
                "                                  #*%@@@@@@@@@@@@@@@%***########@@@@@@#####%@@%*#@@@@@@@@@@@@@@@@@@#*");
        System.out.println(
                "                                  #*%@@@@@@@@@@@@@@@***########@@@@@@@@##%@@%*#%@@@@@@@@@@@@@@@@@@@#*");
        System.out.println(
                "                                  #*%@@@@@@@@@@@@@@+*#########@@@@@@@@@@@%#*#%@@%*#@@@@@@@@@@@@@@@@#*");
        System.out.println(
                "                                  #*%@@@@@@@@@@@@@**#########@@@@@@@@@%***#%@@%**#*#@@@@@@@@@@@@@@@#*");
        System.out.println(
                "                                  #*%@@@@@@@@@@@@#*#########@@@@@@%#****#@@@%**####*%@@@@@@@@@@@@@@#*");
        System.out.println(
                "                                  #*%@@@@@@@@@@@#*#########@@@#*****#*%@@@#**########%@@@@@@@@@@@@@#*");
        System.out.println(
                "                                  ##@@@@@@@@@@@###########**########@@@@@@%*##########@@@@@@@@@@@@@#*");
        System.out.println(
                "                                  ##%@@@@@@@@@%*#################@@@@@@@@@@%##########*@@@@@@@@@@@@#*");
        System.out.println(
                "                                  ##@@@@@@@@@%*###############@@@@@@@@@@@@@@############@@@@@@@@@@@#*");
        System.out.println(
                "                                  ##@@@@@@@@%*############%@@@@@@@@@@@@@@@@@@###########%@@@@@@@@@@#*");
        System.out.println(
                "                                  #*%@@@@@@%*#########@@@@@@@@@@@@@@@@@@@@@@@@###########%@@@@@@@@@*#");
        System.out.println(
                "                                   ##@@@@@%*###%%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@###########%@@@@@@@#*");
        System.out.println(
                "                                   %*%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%*%");
        System.out.println(
                "                                    %*#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%*%");
        System.out.println(
                "                                      #*%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%**%");
        System.out.println(
                "                                       %##%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@#**%");
        System.out.println(
                "                                          #############################################****##");
        System.out.println();
        System.out.println();
        System.out.println("                         ******                    +...=");
        System.out.println("                        *******#                    ==+#                              *...*");
        System.out.println(
                "                       ***#%#*#*#   ---+      =---*+---     ------+----    -------    *:::+---    -------     =--==---+");
        System.out.println(
                "                      #*##%@%##**#  -..-*    +:..+ +.::#  ::.:::::::.:-  -:::::::::-  *::::::-  -::::::::::*  -::-::-:+");
        System.out.println(
                "                     #*###@  %###*%  -::-*   :::*  +:::# -::=%    +:::-   *%    #-::- *:::#   +:::+%   %+:::+ =---+%");
        System.out.println(
                "                    **####*****###*% =-::= =::-+   +::-#=::=%      -::=  =:::::---:--#*:-:#   -:-=       ---= =--=%");
        System.out.println(
                "                    *#####%%%%%%###*  +:-:+-:-=%   +:--#*:-:*     ----= -:--*%###+--=#*---#   ---=      +---= =-=+");
        System.out.println(
                "                  #*###%        #####  *-----+%    +:--# +--::::--:---= =---=**=----=%#---==-=#=----===----+# ===+");
        System.out.println(
                "                 #*###%          ###*#  =---+#     +---%   *+--:-+#---+  +------+*-==% +-==--=# #+-======*#   ===+");
        System.out.println(
                "                                                          --=+   =---=#     %%%           %%        %@@");
        System.out.println("                                                         *=---:::::-*");
        System.out.println("                                                             %#*#%%");
        System.out.println();
        System.out.println();
    }
}
