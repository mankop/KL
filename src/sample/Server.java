package sample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{

    //all vars needed

    public static List<Player> object =  new ArrayList<Player>();
    public static String  send;
    public String address="";
    static int dif;
    static int text;

    //main void listening for data and responsing to hosts

    public static void main(String[] args) throws IOException {
        int port = 8000;
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("Kanál na porte " + port + " otvorený");
        byte buffer[] = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buffer,buffer.length);

        //listening fot messages


        while (true){
            try {
                ds.receive(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String sprava = new String(dp.getData(),0, dp.getLength());
            System.out.println(sprava);
            String[] what = sprava.split(" ");

            //recognnizing type of message

            switch (what[0]){

                case "name:":
                    //parsing players score and ip Add, sending them set diff and text to write with unique index so they can be identified
                    Player a = new Player(what[1], dp.getAddress().toString().substring(1));
                    object.add(a);
                    send = Integer.toString(object.size()-1);
                    DatagramPacket dps = new DatagramPacket(send.getBytes(),send.length(),dp.getAddress(), port+1);
                    if (object.size() >=1){
                        dif = getRandom(3);
                        System.out.println(dif);
                        text = getRandom(2);
                        System.out.println(text);
                        send += " " + Integer.toString(dif) + " " + Integer.toString(text);
                        dps = new DatagramPacket(send.getBytes(),send.length(),dp.getAddress(), port+1);
                        ds.send(dps);
                        /*dps = new DatagramPacket(send.getBytes(),send.length(), InetAddress.getByName(object.get(1).getIp()), port);
                         ds.send(dps);*/
                    }
                    break;

                case "score:":
                    //parsing players score
                    object.get(Integer.parseInt(what[1])).setScore(what[2]);
                    if (object.size()>=1){
                        DatagramPacket dpsc = new DatagramPacket(send.getBytes(),send.length(),dp.getAddress(), port+1);
                        if (Integer.parseInt(object.get(0).getScore()) < Integer.parseInt(object.get(1).getScore())){
                            dpsc = new DatagramPacket(send.getBytes(),send.length(), InetAddress.getByName(object.get(0).getIp()), port+1);
                            send = "lose";
                            ds.send(dpsc);
                            dpsc = new DatagramPacket(send.getBytes(),send.length(), InetAddress.getByName(object.get(1).getIp()), port+1);
                            send = "win";
                            ds.send(dpsc);
                        }
                        else {
                            dpsc = new DatagramPacket(send.getBytes(),send.length(), InetAddress.getByName(object.get(0).getIp()), port+1);
                            send = "win";
                            ds.send(dpsc);
                            dpsc = new DatagramPacket(send.getBytes(),send.length(), InetAddress.getByName(object.get(1).getIp()), port+1);
                            send = "lose";
                            ds.send(dpsc);
                        }
                       /* send = "lose";
                        dpsc = new DatagramPacket(send.getBytes(),send.length(), InetAddress.getByName(object.get(0).getIp()), port+2);
                        ds.send(dpsc);
                        System.out.println("lose");*/
                       /* DatagramPacket dpsc = new DatagramPacket(send.getBytes(),send.length(),dp.getAddress(), port+1);
                        send = object.get(0).toString();
                        ds.send(dpsc);
                        dpsc = new DatagramPacket(send.getBytes(),send.length(),dp.getAddress(), port+1);
                        send = object.get(1).toString();
                        ds.send(dpsc);*/
                    }
                    break;

                    //resseting servers data

                case "reset":
                    object.clear();
                    break;
            }

        }

    }
    public static int getRandom(int max){
        return (int) (Math.random()*max);
    }
}
