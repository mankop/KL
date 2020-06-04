package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.*;
import java.nio.Buffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    //all controlled fxml objects

    @FXML
    private Label out;
    @FXML
    private Label cpm;
    @FXML
    private Label errOut;
    @FXML
    private Label hs;
    @FXML
    private TextArea in;

    //main vars

    private boolean survival=false;
    private int dif=0;
    private int errors=0;
    private String name = "";
    private Double last = 0.0;

    //Decmmail format for formating double or float to 2 decimals

    DecimalFormat f = new DecimalFormat("##.##");

    //getting stapwatch class

    Stopwatch stopwatch = new Stopwatch();

    //initializing window

    public void initialize() throws IOException {

        //setting HS

        setHS();

        //setting listener to changes when writing

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

    //difficulty settings
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

    //setting survival on/off when checkbox is pressed

    public void setSurvival(){
        survival =!survival;
    }

    //start button handelling void

    public void start() throws IOException {

        //vars needed

        List<String> texts = new ArrayList<String>();
        String row;

        //setting input field to clear one

        in.setText("");

        //setting input field to editable

        in.setDisable(false);

        //resetting errors counter

        this.errors = 0;

        //setting text to write from resources based on difficulty

        switch (dif){
            case 0:
                BufferedReader sc = new BufferedReader(new FileReader("resources/ez"));
                while ((row = sc.readLine())!=null)
                    texts.add(row);
                out.setText(texts.get((int) (Math.random()*2)));
                break;

            case 1:
                BufferedReader sc2 = new BufferedReader(new FileReader("resources/hard"));
                while ((row = sc2.readLine())!=null)
                    texts.add(row);
                out.setText(texts.get((int) (Math.random()*2)));
                break;

            case 2:
                BufferedReader sc3 = new BufferedReader(new FileReader("resources/insane"));
                while ((row = sc3.readLine())!=null)
                    texts.add(row);
                out.setText(texts.get((int) (Math.random()*2)));
                break;

        }

        //start counting time

        stopwatch.start();

    }

    //searching for errors when input field is edited

    private void getError() throws IOException {
        if (!in.getText().equals("")){
            if (survival && out.getText().charAt(in.getText().length()-1) != in.getText().charAt(in.getText().length()-1)){
                stopwatch.stop();
                System.out.println(stopwatch.getElapsedTime());
                in.setDisable(true);
            }

            else if(out.getText().charAt(in.getText().length()-1) != in.getText().charAt(in.getText().length()-1)){
                System.out.println("err");
                this.errors+=1;

                //setting border of input field to red

                in.setStyle("-fx-border-color: red");
                errOut.setText(Integer.toString(errors));

            }
            else {

                //setting border of input field to black

                in.setStyle("-fx-border-color: black");
            }
        }

        //testing if text is all written correctly and to the end

        if (out.getText().length() == in.getText().length()){
                in.setDisable(true);
                stopwatch.stop();
                System.out.println(stopwatch.getElapsedTime());

                //stopping timer and calculating CPM

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
                //saving and updating HS if it is HS
            saveSC();
            setHS();

        }

    }

    //set name method used from login controller

    public void settName(String name) {
        this.name = name;
    }

    //set HS method

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

    //save score method

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
