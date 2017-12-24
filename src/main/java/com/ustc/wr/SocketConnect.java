package com.ustc.wr;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketConnect implements Runnable{
    Socket socket;
    String adress;
    Server server;
    int adressPort;
    DataInputStream in;
    public SocketConnect(Server server,String address,int adressPort){
        this.server = server;
        this.adressPort = adressPort;
        this.adress = address;
    }
    public void run() {
        try {
            socket = new Socket(adress,adressPort);
            in = new DataInputStream(socket.getInputStream());
            while (true) {
                try {
                    byte[] a = new byte[1000];
                    int b = in.read(a, 0, 1000);
                    String ss = new String(a, 0, b);
                    server.insertMessage("Client"+adressPort+": "+ss+"\n");
                }
                catch (Exception ee) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
