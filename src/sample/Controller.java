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

    private boolean survival=false;
    private int dif=0;
    private int errors=0;
    private String name = "";
    private Double last = 0.0;

    DecimalFormat f = new DecimalFormat("##.##");

    Stopwatch stopwatch = new Stopwatch();


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
    public void setSurvival(){
        survival =!survival;
    }

    public void start() throws IOException {
        List<String> texts = new ArrayList<String>();
        String row;

        in.setText("");
        in.setDisable(false);
        this.errors = 0;

        switch (dif){
            case 0:
                BufferedReader sc = new BufferedReader(new FileReader("resources/ez"));
                while ((row = sc.readLine())!=null)
                    texts.add(row);
                out.setText(texts.get((int) (Math.random()*2)));
                break;

            case 1:
                break;

            case 2:
                break;

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


    public void setName(String name) {
        this.name = name;
    }

    private void setHS() throws IOException {
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
