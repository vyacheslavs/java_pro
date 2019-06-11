package Lesson_2.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;

public class ClientHandler {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private Server server;
    public Auth.Credentials credentials;
    HashSet<String> blockList;
    Long lastActivity;
    boolean sessionEnded = false;

    private void updateActivity() {
        lastActivity = System.currentTimeMillis();
    }

    private void tryAuthenticate(String str) {

        Auth.Credentials _credentials = server.auth.chatAuthenticate(str);
        if (_credentials.isValid() && !server.alreadyLoggedIn(_credentials.nickname)) {
            credentials = _credentials;

            System.out.println("client successfully authentificated");
            sendMsg("/authok "+credentials.nickname);
            server.broadcastMembers();
            blockList = server.auth.getBlocks(credentials.db_id);
        } else {
            sendMsg("/authfail");
        }
    }

    private void block(String str) {
        String blockNick = str.substring("/block ".length());
        if (blockNick.length()>0 && credentials.isValid()) {
            server.addBlock(credentials.db_id, blockNick);
            blockList = server.auth.getBlocks(credentials.db_id);
        }
    }

    private void unblock(String str) {
        String blockNick = str.substring("/unblock ".length());
        if (blockNick.length()>0 && credentials.isValid()) {
            server.removeBlock(credentials.db_id, blockNick);
            blockList = server.auth.getBlocks(credentials.db_id);
        }
    }

    public boolean canReceiveMessagesFrom(String nick) {
        return !blockList.contains(nick);
    }

    void end_session() {

        sessionEnded = true;
        server.unsubscribe(ClientHandler.this);
        server.broadcastMembers();

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientHandler(Socket socket, final Server server) {
        try {
            this.socket = socket;
            this.server = server;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            lastActivity = 0L;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (!sessionEnded) {
                            String str = in.readUTF();
                            if(str.equals("/end")) {
                                System.out.println("client exits...");
                                break;
                            }

                            if (str.startsWith("/auth ")) {
                                tryAuthenticate(str);
                                updateActivity();
                                continue;
                            }

                            if (str.startsWith("/block ")) {
                                block(str);
                                updateActivity();
                                continue;
                            }

                            if (str.startsWith("/unblock ")) {
                                unblock(str);
                                updateActivity();
                                continue;
                            }

                            if (str.startsWith("/w ") && credentials.isValid() ) {
                                String[] cmd = str.split("\\s");
                                if (cmd.length>=3) {
                                    String _n = cmd[1];
                                    int p = str.indexOf(_n);
                                    String trueMsg = str.substring(p+_n.length()).trim();
                                    server.unicast(credentials.nickname, credentials.nickname, credentials.nickname+": "+trueMsg);
                                    server.unicast(credentials.nickname, _n, credentials.nickname+": "+trueMsg);
                                    updateActivity();
                                    continue;
                                }
                            }


                            if (credentials.isValid()) {
                                server.broadcastMsg(credentials.nickname, credentials.nickname + ": " + str);
                                updateActivity();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        end_session();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
