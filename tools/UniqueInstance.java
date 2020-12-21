package tools;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Cette classe permet d'assurer l'unicité de l'instance de l'application. Deux applications ne peuvent pas être lancées
 * simultanément. Voici un exemple typique d'utilisation :
 * 
 * <pre>
 * // Port à utiliser pour communiquer avec l'instance de l'application lancée.
 * final int PORT = 32145;
 * // Message à envoyer à l'application lancée lorsqu'une autre instance essaye de démarrer.
 * final String MESSAGE = &quot;nomDeMonApplication&quot;;
 * // Actions à effectuer lorsqu'une autre instance essaye de démarrer.
 * final Runnable RUN_ON_RECEIVE = new Runnable() {
 *     public void run() {
 *         if(mainFrame != null) {
 *             // Si la fenêtre n'est pas visible (uniquement dans le systray par exemple), on la rend visible.
 *             if(!mainFrame.isVisible())
 *                 mainFrame.setVisible(true);
 *             // On demande à la mettre au premier plan.
 *             mainFrame.toFront();
 *         }
 *     }                   
 * });
 *                 
 * UniqueInstance uniqueInstance = new UniqueInstance(PORT, MESSAGE, RUN_ON_RECEIVE);
 * // Si aucune autre instance n'est lancée...
 * if(uniqueInstance.launch()) {
 *     // On démarre l'application.
 *     new MonApplication();
 * }
 * </pre>
 * 
 * @author rom1v
 */
public class UniqueInstance {

	public static final int defaultPort = 39204;
	public static final String defaultMessage = "Message"; //$NON-NLS-1$
	
    /** Port d'écoute utilisé pour l'unique instance de l'application. */
    private final int port;

    /** Message à envoyer à l'éventuelle application déjà lancée. */
    private final String message;

    /** Actions à effectuer lorsqu'une autre instance de l'application a indiqué qu'elle avait essayé de démarrer. */
    private final Runnable runOnReceive;

    /**
     * Créer un gestionnaire d'instance unique de l'application.
     * 
     * @param port
     *            Port d'écoute utilisé pour l'unique instance de l'application.
     * @param message
     *            Message à envoyer à l'éventuelle application déjà lancée, {@code null} si aucune action.
     * @param runOnReceive
     *            Actions à effectuer lorsqu'une autre instance de l'application a indiqué qu'elle avait essayé de
     *            démarrer, {@code null} pour aucune action.
     * @param runOnReceive
     *            Actions à effectuer lorsqu'une autre instance de l'application a indiqué qu'elle
     *            avait essayé de démarrer, {@code null} pour aucune action.
     * @throws IllegalArgumentException
     *             si le port n'est pas compris entre 1 et 65535, ou si
     *             {@code runOnReceive != null && message == null} (s'il y a des actions à
     *             effectuer, le message ne doit pas être {@code null}.
     */
    
    public UniqueInstance(int port, String message, Runnable runOnReceive) {
        if (port == 0 || (port & 0xffff0000) != 0)
            throw new IllegalArgumentException("Le port doit être compris entre 1 et 65535 : " + port + "."); //$NON-NLS-1$ //$NON-NLS-2$
        if (runOnReceive != null && message == null)
            throw new IllegalArgumentException("runOnReceive != null ==> message == null."); //$NON-NLS-1$

        this.port = port;
        this.message = message;
        this.runOnReceive = runOnReceive;
    }

    /**
     * Créer un gestionnaire d'instance unique de l'application. Ce constructeur désactive la communication entre
     * l'instance déjà lancée et l'instance qui essaye de démarrer.
     * 
     * @param port
     *            Port d'écoute utilisé pour l'unique instance de l'application.
     */
    public UniqueInstance(int port) {
        this(port, null, null);
    }

    /**
     * Essaye de démarrer le gestionnaire d'instance unique. Si l'initialisation a réussi, c'est que l'instance est
     * unique. Sinon, c'est qu'une autre instance de l'application est déjà lancée. L'appel de cette méthode prévient
     * l'application déjà lancée qu'une autre vient d'essayer de se connecter.
     * 
     * @return {@code true} si l'instance de l'application est unique.
     */
    public boolean launch() {
        /* Indique si l'instance du programme est unique. */
        boolean unique;

        try {
            /* On crée une socket sur le port défini. */
            final ServerSocket server = new ServerSocket(port);

            /* Si la création de la socket réussit, c'est que l'instance du programme est unique, aucune autre n'existe. */
            unique = true;

            /* S'il y a des actions à faire lorsqu'une autre instance essaye de démarrer... */
            if(runOnReceive != null) {

                /* On lance un Thread d'écoute sur ce port. */
                Thread portListenerThread = new Thread("UniqueInstance-PortListenerThread") { //$NON-NLS-1$

                    {
                        setDaemon(true);
                    }

                    @Override public void run() {
                        /* Tant que l'application est lancée... */
                        while(true) {
                            try {
                                /* On attend qu'une socket se connecte sur le serveur. */
                                final Socket socket = server.accept();

                                /* Si une socket est connectée, on écoute le message envoyé dans un nouveau Thread. */
                                new Thread("UniqueInstance-SocketReceiver") { //$NON-NLS-1$

                                    {
                                        setDaemon(true);
                                    }

                                    @Override public void run() {
                                        receive(socket);
                                    }
                                }.start();
                            } catch(IOException e) {
                                Logger.getLogger("UniqueInstance").warning("Attente de connexion de socket échouée."); //$NON-NLS-1$ //$NON-NLS-2$
                            }
                        }
                    }
                };

                /* On démarre le Thread. */
                portListenerThread.start();
            }
        } catch(IOException e) {
            /* Si la création de la socket échoue, c'est que l'instance n'est pas unique, une autre n'existe. */
        	e.printStackTrace();
            unique = false;

            /* Si des actions sont prévues par l'instance déjà lancée... */
            if(runOnReceive != null) {
                /*
                 * Dans ce cas, on envoie un message à l'autre instance de l'application pour lui demander d'avoir le
                 * focus (par exemple).
                 */
                send();
            }
        }
        return unique;
    }

    /**
     * Envoie un message à l'instance de l'application déjà ouverte.
     */
    private void send() {
        PrintWriter pw = null;
        try {
            /* On se connecte sur la machine locale. */
            Socket socket = new Socket("localhost", port); //$NON-NLS-1$

            /* On définit un PrintWriter pour écrire sur la sortie de la socket. */
            pw = new PrintWriter(socket.getOutputStream());

            /* On écrit le message sur la socket. */
            pw.write(message);
        } catch(IOException e) {
            Logger.getLogger("UniqueInstance").warning("Écriture sur flux de sortie de la socket échouée."); //$NON-NLS-1$ //$NON-NLS-2$
        } finally {
            if(pw != null)
                pw.close();
        }
    }

    /**
     * Reçoit un message d'une socket s'étant connectée au serveur d'écoute. Si ce message est le message de l'instance
     * unique, l'application demande le focus.
     * 
     * @param socket
     *            Socket connectée au serveur d'écoute.
     */
    private synchronized void receive(Socket socket) {
        Scanner sc = null;

        try {
            /* On n'écoute que 5 secondes, si aucun message n'est reçu, tant pis... */
            socket.setSoTimeout(5000);

            /* On définit un Scanner pour lire sur l'entrée de la socket. */
            sc = new Scanner(socket.getInputStream());

            /* On ne lit qu'une ligne. */
            String s = sc.nextLine();

            /* Si cette ligne est le message de l'instance unique... */
            if(message.equals(s)) {
                /* On exécute le code demandé. */
                runOnReceive.run();
            }
        } catch(IOException e) {
            Logger.getLogger("UniqueInstance").warning("Lecture du flux d'entrée de la socket échoué."); //$NON-NLS-1$ //$NON-NLS-2$
        } finally {
            if(sc != null)
                sc.close();
        }

    }
}
