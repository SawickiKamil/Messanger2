package com.Messenger;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ChatServer {
    List<String> loginNames = new ArrayList<>();
    List<Socket> clientSocket = new ArrayList<>();

    ChatServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(5217);
        loginNames = new ArrayList<String>();
        clientSocket = new ArrayList<Socket>();
        while (true) {
            Socket clientSocket = serverSocket.accept();
            Client client1 = new Client(clientSocket);
        }
    }

    class Client extends Thread {

        Socket clientSocked;
        DataInputStream inputStream;
        DataOutput output;

        int position = 0;
        int i = 0;


        Client(Socket client) throws IOException {
            clientSocked = client;
            inputStream = new DataInputStream(clientSocked.getInputStream());
            output = new DataOutputStream(clientSocked.getOutputStream());

            String loginName = inputStream.readUTF();
            System.out.println("login name: " + loginName);

            loginNames.add(loginName);
            clientSocket.add(clientSocked);

            start();
        }

        public void run() {
            while (true) {
                try {
                    String msgFromClient = inputStream.readUTF();

                    StringTokenizer msgParts = new StringTokenizer(msgFromClient);

                    String name = msgParts.nextToken();
                    String msgType = msgParts.nextToken();

                    StringBuffer stringBuffer = new StringBuffer();

                    while (msgParts.hasMoreTokens()) {
                        stringBuffer.append(" " + msgParts.nextToken());
                    }

                    final String message = stringBuffer.toString();

                    switch (msgType){
                        case "LOGIN":
                            clientSocket.forEach(socket -> {
                                notifyLogin(socket, name);
                            });
                            break;
                        case "LOGOUT":
                            clientSocket.forEach(socket -> {
                                if (name.equals(loginNames.get(i++)))
                                    position = i-1;

                                performLogout(socket, name);
                            });

                            loginNames.remove(position);
                            clientSocket.remove(position);
                            break;

                        default:
                            clientSocket.forEach(socket -> {
                                notifyMessage(socket, name, message);
                            });

                            if (msgType.equals("LOGOUT")) {
                                break;
                            }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    private void performLogout(Socket socket, String name) {
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(name + " has logged out");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyMessage(Socket socket, String name, String message) {
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(name + ": " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyLogin(Socket socket, String name) {
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(name + " has logged in");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        new ChatServer();

    }
}
