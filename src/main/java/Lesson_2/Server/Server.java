package Lesson_2.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import static java.lang.Thread.sleep;

public class Server {

    public Auth auth = new Auth();
    private Vector<ClientHandler> clients;
    final int PORT = 10000;
    final Long MAX_DELAY = 20000L;

    public Server() {
        ServerSocket server = null;
        Socket socket = null;
        clients = new Vector<>();

        try {
            auth.connect();
            server = new ServerSocket(PORT);
            System.out.println("Server started");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted()) {

                        try {
                            for (ClientHandler o : clients) {
                                Long now = System.currentTimeMillis();
                                if (o.lastActivity>0)
                                    System.out.println(now-o.lastActivity);
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
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            while (true) {
                socket = server.accept();
                System.out.println("new client accepted");
                subscribe(new ClientHandler(socket, this));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
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
