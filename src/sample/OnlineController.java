package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OnlineController extends Thread {

    //all FXML objects initialized

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

    //all vars initialized

    private int dif=0;
    private int text = 0;
    private int errors=0;
    private int index = 0;
    private String name = "online";
    private Double last = 0.0;
    private String[] parsedMassage;
    private FXMLLoader loader;

    //setting stopwatch and format vars

    Stopwatch stopwatch = new Stopwatch();
    DecimalFormat f = new DecimalFormat("##.##");

    //setting udp listener

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

    //requesting data from server

    public void getData() throws IOException {

        int port = 8000;
        String mojaSprava = "name: meno";
        DatagramSocket ds = new DatagramSocket();
        UDPEchoReader reader = new UDPEchoReader(ds);
        InetAddress adresa = InetAddress.getByName("127.0.0.1");

        reader.setDaemon(true);
        reader.start();

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
            parsedMassage = sprava.split(" ");
            index = Integer.parseInt(parsedMassage[0]);
            dif = Integer.parseInt(parsedMassage[1]);
            text = Integer.parseInt(parsedMassage[2]);
            break;
        }
    }


    public void initialize() throws IOException {
        setHS();

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

    public void start() {
        try {
            getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                out.setText(texts.get(text));
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
                out.setText(texts.get(text));
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
                out.setText(texts.get(text));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + dif);
        }

        //starting stapwatch

        stopwatch.start();

    }

    private void getError() throws IOException {

        //checking if input wasnt just reseted

        if (!in.getText().equals("")){

            //checking if mistake was made

            if(out.getText().charAt(in.getText().length()-1) != in.getText().charAt(in.getText().length()-1)){

                //adding 1 to errors, displaying them and setting red border

                System.out.println("err");
                this.errors+=1;
                in.setStyle("-fx-border-color: red");
                errOut.setText(Integer.toString(errors));

            }

            //if no mistake was made setting border to black

            else {
                in.setStyle("-fx-border-color: black");
            }
        }

        //calcing and setting CPM

        if (out.getText().length() == in.getText().length() && out.getText().equals(in.getText())){
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

            //saving and setting HS

            saveSC();
            setHS();


            //sending score to server

            InetAddress adresa = InetAddress.getByName("127.0.0.1");
            int port = 8000;
            byte buffer[] = new byte[1024];
            DatagramPacket dp2 = new DatagramPacket(buffer, buffer.length);
            DatagramSocket ds2 = new DatagramSocket();
            UDPEchoReader reader = new UDPEchoReader(ds2);
            String mojaSprava = "score: 0 " + cpm.getText();
            dp2 = new DatagramPacket(mojaSprava.getBytes(),mojaSprava.length(),adresa, port);
            ds2.send(dp2);

            //starting listener for response

            try {
                ds2 = new DatagramSocket(port+2);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    ds2.receive(dp2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //parsing response

                String prijataSprava = new String(dp2.getData(),0,dp2.getLength());
                System.out.println(prijataSprava);

                //checking if winner or looser

                if (prijataSprava.equals("lose"))
                {

                    //loading next fxml window

                   loader = new FXMLLoader(getClass().getResource("Loser.fxml"));
                    Parent root = loader.load();

                    //Starting new window

                    Stage primaryStage = new Stage();
                    primaryStage.setTitle("KL");
                    primaryStage.setScene(new Scene(root, 600, 400));
                    primaryStage.setResizable(false);

                    //opening new window

                    primaryStage.show();
                    Stage stage = (Stage) hs.getScene().getWindow();
                    stage.close();
                }
                else  {
                    //loading next fxml window

                   loader = new FXMLLoader(getClass().getResource("winner.fxml"));

                }
                Parent root = loader.load();

                //Starting new window

                Stage primaryStage = new Stage();
                primaryStage.setTitle("KL");
                primaryStage.setScene(new Scene(root, 600, 400));
                primaryStage.setResizable(false);

                //opening new window

                primaryStage.show();
                Stage stage = (Stage) hs.getScene().getWindow();
                stage.close();
                break;
            }

        }

    }

    //method for displaying HS


    public void setHS() throws IOException {
        File f = new File("resources/scores/Online" + name);
        if(f.exists() && !f.isDirectory()) {
            BufferedReader sc = new BufferedReader(new FileReader("resources/scores/Online" + name));
            hs.setText(sc.readLine());
            sc.close();
        }
        else
            hs.setText("0.00");



    }

    //method for saving HS

    private void saveSC() throws IOException {
        if (last < Double.parseDouble(cpm.getText())*100/(errors*3+1)) {
            BufferedWriter w = new BufferedWriter(new FileWriter("resources/scores/Online" + name));
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
