package controllers;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

public class Controller extends Stage implements Initializable {
    public static Status status;
    static byte hour;
    static boolean currentTime;
    static byte hourStart;
    static byte hourEnd;
    @FXML
    private Label labelStatus;

    @FXML
    private Label labelClients;

    @FXML
    private Label labelOperators;

    @FXML
    private Label labelDrivers;

    @FXML
    private Label labelOperatorsVE;

    @FXML
    private Label clientNum;

    @FXML
    private Label operClientNum;

    @FXML
    private Label driverNum;

    @FXML
    private Label operDriverNum;

    public Controller() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hourStart = properties.Props.hourStart;
        hourEnd = properties.Props.hourEnd;
        currentTime = true;
    }

    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/queue.fxml"));
            fxmlLoader.setController(this);
            primaryStage.setScene(new Scene(fxmlLoader.load()));
            primaryStage.setTitle("Монитор звонков");
            primaryStage.setResizable(false);
            primaryStage.setFullScreen(true);

            StatusService service = new StatusService();
            service.valueProperty().addListener((ObservableValue<? extends Status> obs, Status oldValue, Status newValue) -> {

                if (currentTime) {
                    labelStatus.setTextFill(Color.RED);
                    labelClients.setTextFill(Color.RED);
                    labelOperators.setTextFill(Color.WHITE);
                    labelDrivers.setTextFill(Color.RED);
                    labelOperatorsVE.setTextFill(Color.WHITE);
                    clientNum.setTextFill(Color.RED);
                    operClientNum.setTextFill(Color.WHITE);
                    driverNum.setTextFill(Color.RED);
                    operDriverNum.setTextFill(Color.WHITE);
                    clientNum.setText(newValue.getNkclients());
                    operClientNum.setText(newValue.getNkoperators());
                    driverNum.setText(newValue.getDriverclients());
                    operDriverNum.setText(newValue.getDriveroperators());
                } else {
                    labelStatus.setTextFill(Color.BLACK);
                    labelClients.setTextFill(Color.BLACK);
                    labelOperators.setTextFill(Color.BLACK);
                    labelDrivers.setTextFill(Color.BLACK);
                    labelOperatorsVE.setTextFill(Color.BLACK);
                    clientNum.setTextFill(Color.BLACK);
                    operClientNum.setTextFill(Color.BLACK);
                    driverNum.setTextFill(Color.BLACK);
                    operDriverNum.setTextFill(Color.BLACK);
                }
            });
            primaryStage.show();
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class StatusService extends Service<Status> {
        @Override
        protected Task<Status> createTask() {
            return new Task<Status>() {
                @Override
                protected Status call() throws Exception {
                    while (!isCancelled()) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException exc) {
                            Thread.currentThread().interrupt();
                            if (isCancelled()) {
                                break;
                            }
                        }
                        hour = (byte) Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                        if (hour > hourStart && hour < hourEnd) {
                            currentTime = true;
                            updateValue(http.Http.httpGetQueue(status));
                        } else {
                            currentTime = false;
                            updateValue(status = new Status("","","",""));
                        }
                    }
                    return null;
                }
            };
        }
    }

    public static class Status {
        private final String nkclients;
        private final String nkoperators;
        private final String driverclients;
        private final String driveroperators;

        public Status(String nkclients, String nkoperators, String driverclients, String driveroperators) {
            this.nkclients = nkclients;
            this.nkoperators = nkoperators;
            this.driverclients = driverclients;
            this.driveroperators = driveroperators;
        }

        public String getNkclients() {
            return nkclients;
        }

        public String getNkoperators() {
            return nkoperators;
        }

        public String getDriverclients() {
            return driverclients;
        }

        public String getDriveroperators() {
            return driveroperators;
        }

    }
}

