package Developing;

import java.awt.geom.Area;
import java.io.IOException;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

class klient extends Thread{

    public static class UDPEchoReader extends Thread {
        public boolean active;
        public DatagramSocket ds;

        public UDPEchoReader(DatagramSocket ds) {
            this.ds = ds;
            active = true;
        }

        @Override
        public void run() {
            byte buffer[] = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            while (active) {
                try {
                    ds.receive(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                    active = false;
                }
                String prijataSprava = new String(dp.getData(),dp.getLength());
                System.out.println("prijate zo servera:" + prijataSprava);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 8000;
        Scanner sc = new Scanner(System.in);
        InetAddress adresa = InetAddress.getByName("127.0.0.1");
        DatagramSocket ds = new DatagramSocket();
        UDPEchoReader reader = new UDPEchoReader(ds);
        reader.setDaemon(true);
        reader.start();
       /* String mojaSprava = "test";
        DatagramPacket dp = new DatagramPacket(mojaSprava.getBytes(),mojaSprava.length(),adresa, port);
        ds.send(dp);*/

        String mojaSprava = "name: a";
        DatagramPacket dp = new DatagramPacket(mojaSprava.getBytes(),mojaSprava.length(),adresa, port);
        ds.send(dp);
        try {
            ds = new DatagramSocket(port+1);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                ds.receive(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String sprava = new String(dp.getData(),0, dp.getLength());
            System.out.println(sprava);
            System.out.println("a");
        }
    }

}