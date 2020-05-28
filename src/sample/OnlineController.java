package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OnlineController extends Thread {

    @FXML
    private Label hs;
    @FXML
    private TextArea in;
    @FXML
    private Label out;
    @FXML
    private Label cpm;
    @FXML
    private Label errOut;

    private int dif=0;
    private int text = 0;
    private int errors=0;
    private int index = 0;
    private String name = "";
    private Double last = 0.0;
    private boolean survival=false;
    private String[] parsedMassage;

    Stopwatch stopwatch = new Stopwatch();
    DecimalFormat f = new DecimalFormat("##.##");



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
    public void getData() throws IOException {
        int port = 8000;
        Scanner sc = new Scanner(System.in);
        DatagramSocket ds = new DatagramSocket();
        UDPEchoReader reader = new UDPEchoReader(ds);
        InetAddress adresa = InetAddress.getByName("127.0.0.1");
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

            parsedMassage = sprava.split(" ");

            index = Integer.parseInt(parsedMassage[0]);
            dif = Integer.parseInt(parsedMassage[1]);
            text = Integer.parseInt(parsedMassage[2]);
        }
    }


    public void initialize() throws IOException {
        setHS();
        getData();
        in.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                System.out.println("changed");
                try {
                    getError();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //dif settings
    public void dif0(){
        this.dif=0;
        System.out.println(dif);

    }
    public void dif1(){
        this.dif=1;
        System.out.println(dif);

    }
    public void dif2(){
        this.dif=2;
        System.out.println(dif);
    }
    public void setSurvival(){
        survival =!survival;
    }

    public void start() {
        List<String> texts = new ArrayList<String>();
        String row = null;

        in.setText("");
        in.setDisable(false);
        this.errors = 0;

        switch (dif){
            case 0:
                BufferedReader sc = null;
                try {
                    sc = new BufferedReader(new FileReader("resources/ez"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        if (!((row = sc.readLine())!=null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    texts.add(row);
                }
                out.setText(texts.get((int) (Math.random()*2)));
                break;

            case 1:
                BufferedReader sc2 = null;
                try {
                    sc2 = new BufferedReader(new FileReader("resources/hard"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        if (!((row = sc2.readLine())!=null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    texts.add(row);
                }
                out.setText(texts.get((int) (Math.random()*2)));
                break;

            case 2:
                BufferedReader sc3 = null;
                try {
                    sc3 = new BufferedReader(new FileReader("resources/insane"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        if (!((row = sc3.readLine())!=null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    texts.add(row);
                }
                out.setText(texts.get((int) (Math.random()*2)));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + dif);
        }

        stopwatch.start();

    }

    private void getError() throws IOException {
        if (!in.getText().equals("")){
            if (survival){
                stopwatch.stop();
                System.out.println(stopwatch.getElapsedTime());
                in.setDisable(true);
            }

            else if(out.getText().charAt(in.getText().length()-1) != in.getText().charAt(in.getText().length()-1)){
                System.out.println("err");
                this.errors+=1;
                in.setStyle("-fx-border-color: red");
                errOut.setText(Integer.toString(errors));

            }
            else {
                in.setStyle("-fx-border-color: black");
            }
        }
        if (out.getText().length() == in.getText().length()){
            in.setDisable(true);
            stopwatch.stop();
            System.out.println(stopwatch.getElapsedTime());

            cpm.setText(
                    f.format(
                            in.getText()
                                    .replace(" ", "")
                                    .length()
                                    *1.0
                                    /(stopwatch.getElapsedTime())
                                    *1000
                    )

            );
            saveSC();
            setHS();

        }

    }
    public void settName(String meno){
        this.name = meno;
    }

    public void setHS() throws IOException {
        File f = new File("resources/scores/" + name);
        if(f.exists() && !f.isDirectory()) {
            BufferedReader sc = new BufferedReader(new FileReader("resources/scores/" + name));
            hs.setText(sc.readLine());
            sc.close();
        }
        else
            hs.setText("0.00");



    }
    private void saveSC() throws IOException {
        if (last < Double.parseDouble(cpm.getText())*100/(errors*3+1)) {
            BufferedWriter w = new BufferedWriter(new FileWriter("resources/scores/" + name));
            w.write(
                    f.format(
                            Double.parseDouble(
                                    cpm.getText())
                                    * 100
                                    * dif + 1.0
                                    / (errors * 3 + 1)
                    )
            );
            w.newLine();
            w.close();
        }
        last = Double.parseDouble(
                cpm.getText())
                * 100
                / (errors * 3 + 1);
    }



}
