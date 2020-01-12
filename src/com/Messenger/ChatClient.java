package com.Messenger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class ChatClient extends JFrame implements Runnable {

    String loginName;

    JTextArea messages;
    JTextField sendMessage;

    JButton send;
    JButton logout;

    DataInputStream in;
    DataOutput output;

    private void logout() {
        try {
            output.writeUTF(loginName + " LOGOUT");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    private void send() {

        try{
            if(sendMessage.getText().length() > 0) {
                output.writeUTF(loginName + " DATA " + sendMessage.getText());
            }
            sendMessage.setText("");

        } catch (IOException e1){
            e1.printStackTrace();
        }
    }

    public ChatClient(String loginName) throws UnknownHostException, IOException {

        super(loginName);
        this.loginName = loginName;



        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                logout();
            }
        });

        messages = new JTextArea(18,50);
        messages.setEditable(false);
        sendMessage = new JTextField(50);

        sendMessage.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    send();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        send = new JButton("Send");
        logout = new JButton("Logout");

        send.addActionListener(e -> {
            send();
        });

        logout.addActionListener(e -> {
            logout();
        });

        Socket socket = new Socket("127.0.0.1" , 5217);

        in = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());

        output.writeUTF(loginName);
        output.writeUTF(loginName + " LOGIN");

        setup();

    }

    private void setup() {
        setSize(600, 400);
        JPanel panel = new JPanel();

        panel.add(new JScrollPane(messages));
        panel.add(sendMessage);
        panel.add(send);
        panel.add(logout);

        add(panel);
        new Thread(this).start();
        setVisible(true);
    }

    @Override
    public void run() {
        while(true) {
            try {
                messages.append("\n" + in.readUTF());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
