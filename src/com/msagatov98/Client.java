package com.msagatov98;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {

    private String id;
    private Socket socket;
    private PrintWriter out;

    private static Client[] clients = new Client[10];

    private static JButton[] stopAr = new JButton[10];
    private static JButton[] recordAr = new JButton[10];

    private static JButton stopAll;
    private static JButton recordAll;

    private static JTextField[] jTextFieldIp = new JTextField[10];
    private static JTextField[] jTextFieldFileName = new JTextField[10];

    private static void initialize() {

        JFrame jFrame = new JFrame("HyperDeckController");
        jFrame.setLayout(null);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.setSize(640, 480);
        jFrame.getContentPane().setBackground(Color.GRAY);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        stopAll = new JButton("Stop All");
        recordAll = new JButton("Record All");

        jTextFieldIp = new JTextField[10];
        jTextFieldFileName = new JTextField[10];

        stopAll.setBackground(Color.DARK_GRAY);
        recordAll.setBackground(Color.DARK_GRAY);

        stopAll.setForeground(Color.lightGray);
        recordAll.setForeground(Color.lightGray);

        Font font1 = new Font("SansSerif", Font.BOLD, 20);

        stopAll.setBounds(270, 400, 100, 40);
        recordAll.setBounds(390, 400, 100, 40);

        jFrame.getContentPane().add(stopAll);
        jFrame.getContentPane().add(recordAll);


        int a = 0;

        for (int i = 0; i < 10; i++) {
            jTextFieldIp[i] = new JTextField(16);
            jTextFieldFileName[i] = new JTextField(16);

            jTextFieldIp[i].setBounds(250, a, 150, 35);
            jTextFieldFileName[i].setBounds(10, a, 240, 35);

            stopAr[i] = new JButton("Stop");
            recordAr[i] = new JButton("Record");

            stopAr[i].setBounds(410, a, 100, 35);
            recordAr[i].setBounds(510, a, 100, 35);

            jFrame.getContentPane().add(jTextFieldIp[i]);
            jFrame.getContentPane().add(jTextFieldFileName[i]);

            jFrame.getContentPane().add(stopAr[i]);
            jFrame.getContentPane().add(recordAr[i]);

            stopAr[i].setBackground(Color.DARK_GRAY);
            recordAr[i].setBackground(Color.DARK_GRAY);

            stopAr[i].setForeground(Color.lightGray);
            recordAr[i].setForeground(Color.lightGray);

            jTextFieldIp[i].setBackground(Color.lightGray);
            jTextFieldIp[i].setFont(font1);
            jTextFieldFileName[i].setBackground(Color.lightGray);
            jTextFieldFileName[i].setFont(font1);

            a += 37;
        }

        stopAll.addActionListener(e -> {
            for (int i = 0; i < 10; i++) {
                if (clients[i] != null) stopAr[i].doClick();
                else break;
            }
        });

        recordAll.addActionListener(e -> {
            for (int i = 0; i < 10; i++) {
                if (clients[i] != null) recordAr[i].doClick();
                else break;
            }
        });

        for (int i = 0; i < 10; i++) {
            int finalI1 = i;
            recordAr[i].addActionListener(e -> {
                if (clients[finalI1] != null) {
                    clients[finalI1].record(jTextFieldFileName[finalI1].getText());
                    recordAr[finalI1].setForeground(Color.RED);
                }
            });

            stopAr[i].addActionListener( e -> {
                if (clients[finalI1] != null) {
                    clients[finalI1].stop();
                    recordAr[finalI1].setForeground(Color.lightGray);
                }
            });

        }
    }

    public static void main(String[] args) {
        initialize();

        try (BufferedReader reader = new BufferedReader(new FileReader("res/config"))) {
            int i = 0;
            while (reader.ready()) {
                clients[i++] = new Client(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 10; i++) {
            if (clients[i] != null) {
                if (clients[i].isConnected()) {
                    jTextFieldIp[i].setText(clients[i].getId());
                }
            }
        }
    }

    private Client(String id) {
        this.id = id;
        try {
            socket = new Socket(this.id, 9993);
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getId() {
        return id;
    }

    private boolean isConnected() {
        return socket.isConnected();
    }

    private void stop() {
        try {
            out.println("stop");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }


    /*private void info() {
        try {
            InputStream input;
            BufferedReader reader;

            out.println("clips get");

            input = socket.getInputStream();

            reader = new BufferedReader(new InputStreamReader(input));

            String line;

            System.out.println();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println();

            input.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void record(String name) {
        try {
            if (name != null) out.println("record: name: " + name);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
        }
    }
}