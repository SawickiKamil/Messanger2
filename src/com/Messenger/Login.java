package com.Messenger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.UnknownHostException;

public class Login {

    static void startChat(JFrame login, String loginName){
        try {
            ChatClient client = new ChatClient(loginName);
            login.dispose();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame login = new JFrame("Login");
        login.setLayout(new BorderLayout());
        final JPanel panel = new JPanel();
        JTextField loginName = new JTextField(18);
        JButton enterButton = new JButton("Login");

        panel.add(new JLabel(""));
        panel.add(loginName);
        panel.add(enterButton);
        panel.setVisible(true);

        login.setSize(500, 200);
        login.add(panel);
        login.setVisible(true);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.add(enterButton,BorderLayout.SOUTH);
        login.pack();




        loginName.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    startChat(login, loginName.getText());
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }
}
