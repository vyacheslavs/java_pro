package Lesson_2.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class Server {

    private static final Logger LOGGER = Logger.getLogger( Server.class.getName() );

    History history = new History();
    Auth auth = new Auth();
    private Vector<ClientHandler> clients;

    public Server() {
        final int PORT = 10000;
        final long MAX_DELAY = 20000L;

        ServerSocket server = null;
        Socket socket = null;
        clients = new Vector<>();

        try {
            auth.connect();
            history.connect();
            server = new ServerSocket(PORT);
            LOGGER.info("Server started");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted()) {

                        try {
                            for (ClientHandler o : clients) {
                                Long now = System.currentTimeMillis();
                                if (o.lastActivity>0 && now - o.lastActivity > MAX_DELAY) {
                                    o.sendMsg("session timeout, you've been kicked out... ");
                                    o.end_session();
                                    break;
                                }
                            }
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "error", e);
                        }
                    }
                }
            }).start();

            while (true) {
                socket = server.accept();
                LOGGER.info("new client accepted");
                subscribe(new ClientHandler(socket, this));
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "error", e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "error", e);
            }

            try {
                server.close();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "error", e);
            }

            auth.disconnect();
        }
    }

    public void subscribe(ClientHandler client) {
        clients.add(client);
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client);
    }

    public void archivate(int id, String msg) {
        history.archivate(id, msg);
    }

    public void broadcastMsg(String from_nick, String msg) {
        for (ClientHandler o: clients) {
            if (o.canReceiveMessagesFrom(from_nick))
                o.sendMsg(msg);
        }
    }

    public void unicast(String from_nick, String to_nick, String msg) {

        // тут лучше всего использовать конечно какой нибудь
        // другой контейнер чтобы не искать клиента линейно
        // но Vector синхронизирован и просто так сейчас заменить его
        // накладно. Времени пока нет

        for (ClientHandler o: clients) {
            if (o.credentials.isValid() && o.canReceiveMessagesFrom(from_nick)) {
                if (o.credentials.nickname.equals(to_nick)) {
                    o.sendMsg(msg);
                }
            }
        }
    }

    public void broadcastMembers() {
        StringBuilder s = new StringBuilder();
        s.append("/members ");

        for (ClientHandler cl : clients) {
            if (cl.credentials!=null && cl.credentials.isValid()) {
                s.append(cl.credentials.nickname);
                s.append(",");
            }
        }

        String msg = s.toString();

        for (ClientHandler o: clients) {
            o.sendMsg(msg);
        }
    }

    public void loadHistory(final String nickname) {
        history.iterateHistory(new HistoryWalking() {
            @Override
            public void messageUpdate(int db_id, String msg) {
                for (ClientHandler cl : clients) {
                    if (cl!=null && cl.credentials!=null && cl.credentials.db_id == db_id) {
                        broadcastMsg(nickname, cl.credentials.nickname + ": " + msg);
                        break;
                    }
                }
            }
        });

    }

    public void addBlock(int whosBlocking, String whomNick) {
        for (ClientHandler o : clients) {
            if (o.credentials.isValid() && o.credentials.nickname.equals(whomNick)) {
                auth.addBlock(whosBlocking, o.credentials.db_id);
                break;
            }
        }
    }

    void removeBlock(int whosUnblocking, String whomNick) {
        for (ClientHandler o : clients) {
            if (o.credentials.isValid() && o.credentials.nickname.equals(whomNick)) {
                auth.removeBlock(whosUnblocking, o.credentials.db_id);
                break;
            }
        }
    }

    public boolean alreadyLoggedIn(String nick) {
        for (ClientHandler o: clients) {
            if (o.credentials!=null && o.credentials.isValid() && o.credentials.nickname.equals(nick)) {
                return true;
            }
        }
        return false;
    }
}
