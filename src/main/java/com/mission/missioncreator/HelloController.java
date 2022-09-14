package com.mission.missioncreator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Random;

public class HelloController {

    @FXML
    private Spinner<Integer> missionQuantitySpinner;
    @FXML
    private RadioButton specialMissionRadio;
    @FXML
    private VBox missionVBox;
    @FXML
    private Text connectedText, notConnectedText;

    final private int initialSpinVal = 3;
    private int totalMissions = 1;
    private int totalRewards = 1;
    private int totalSpecialMisions = 1;
    private String databaseURL;
    private Connection connection;
    private boolean databaseConnected = false;
    private boolean specialMission = false;

    public HelloController(){

        String URL = "";
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader("databaseURL.txt"));
            URL = bufferedReader.readLine();
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not found or Error reading file");
        }
        databaseURL = URL;
    }
    @FXML
    public void initialize(){
        initializeSpinner();
        try{
            connectToDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Class not found, FUCK!");
            databaseConnected = false;
        }
        specialMissionRadio.selectedProperty().addListener(e ->{
            if (specialMissionRadio.isSelected()){
                specialMission = true;
                System.out.println(specialMission);
            }else{
                specialMission = false;
                System.out.println(specialMission);
            }
        });
    }
    public void initializeSpinner(){
       SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, initialSpinVal);
       missionQuantitySpinner.setValueFactory(valueFactory);
    }
    private void toggleConnectionText(){
        if (databaseConnected){
            connectedText.setVisible(true);
            notConnectedText.setVisible(false);
        }else{
            connectedText.setVisible(false);
            notConnectedText.setVisible(true);
        }
    }
    private void connectToDatabase() throws ClassNotFoundException {
        try{
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");//Loading Driver
            connection= DriverManager.getConnection(databaseURL);//Establishing Connection
            System.out.println("Connected Successfully");
            databaseConnected = true;
            PreparedStatement preparedStatement = connection.prepareStatement("select * from Missions", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.last();
            totalMissions = resultSet.getRow();
            System.out.println(totalMissions);
            preparedStatement = connection.prepareStatement("select * from Rewards", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = preparedStatement.executeQuery();
            resultSet.last();
            totalRewards = resultSet.getRow();
            System.out.println(totalRewards);
            preparedStatement = connection.prepareStatement("select * from Special", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = preparedStatement.executeQuery();
            resultSet.last();
            totalSpecialMisions = resultSet.getRow();
            System.out.println(totalSpecialMisions);
        } catch (SQLException e) {
            e.printStackTrace();
            databaseConnected = false;
        }
        toggleConnectionText();
    }
    public void generateMissionButton() {
        generateMissions(missionQuantitySpinner.getValue());
    }
    //Generate new missions
    private void generateMissions(int num){
        if (specialMission){
            num = num - 1;
        }
        missionVBox.getChildren().clear();
        try{
            for (int i = 0; i < num; i++){
                //create separator between missions
                Separator separator = new Separator();
                missionVBox.getChildren().add(separator);
                //Random number for a random mission
                Random randyNewman = new Random();
                int randomMission = randyNewman.nextInt(totalMissions) + 1;
                //Mission information
                String mission = "Mission";
                String description = "Generic description";
                String type = "Type";
                String reward = generateRewards();
                //SQL stuff?
                PreparedStatement preparedStatement = connection.prepareStatement("select * from Missions");
                ResultSet resultSet = preparedStatement.executeQuery();
                //Find the random mission and get results
                while(resultSet.next()){
                    if (resultSet.getInt("ID") == randomMission){
                        mission = resultSet.getString("MissionName");
                        description = resultSet.getString("MissionDescription");
                        type = resultSet.getString("MissionType");
                    }
                }
                HBox hBox = new HBox();
                hBox.setPadding(new Insets(0, 10, 10, 10));
                hBox.setSpacing(95);
                Text missionName = new Text(mission);
                missionName.minWidth(100);
                Text missionDescr = new Text(description);
                missionDescr.setWrappingWidth(200);
                missionDescr.maxWidth(200);
                Text missionType = new Text(type);
                missionType.minWidth(120);
                Text rewardText = new Text(reward);
                rewardText.setWrappingWidth(150);
                hBox.getChildren().add(missionName);
                hBox.getChildren().add(missionType);
                hBox.getChildren().add(missionDescr);
                hBox.getChildren().add(rewardText);
                missionVBox.getChildren().add(hBox);
            }
            if (specialMission){
                //create separator between missions
                Separator separator = new Separator();
                missionVBox.getChildren().add(separator);
                //Random number for a random mission
                Random randyNewman = new Random();
                int randomMission = randyNewman.nextInt(totalSpecialMisions) + 1;
                //Mission information
                String mission = "Mission";
                String description = "Generic description";
                String type = "Type";
                String reward = generateRewards();
                //SQL stuff?
                PreparedStatement preparedStatement = connection.prepareStatement("select * from Special");
                ResultSet resultSet = preparedStatement.executeQuery();
                //Find the random mission and get results
                while(resultSet.next()){
                    if (resultSet.getInt("ID") == randomMission){
                        mission = resultSet.getString("MissionName");
                        description = resultSet.getString("MissionDescription");
                        type = resultSet.getString("MissionType");
                    }
                }
                HBox hBox = new HBox();
                hBox.setPadding(new Insets(0, 10, 10, 10));
                hBox.setSpacing(95);
                Text missionName = new Text(mission);
                missionName.minWidth(100);
                Text missionDescr = new Text(description);
                missionDescr.setWrappingWidth(200);
                missionDescr.maxWidth(200);
                Text missionType = new Text(type);
                missionType.minWidth(120);
                Text rewardText = new Text(reward);
                rewardText.setWrappingWidth(150);
                hBox.getChildren().add(missionName);
                hBox.getChildren().add(missionType);
                hBox.getChildren().add(missionDescr);
                hBox.getChildren().add(rewardText);
                missionVBox.getChildren().add(hBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error, something fucked with the database");
        }
    }
    //Connect to a new database
    public void connectToDatabaseBtn(ActionEvent actionEvent){
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        try{
            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            FileChooser chooser = new FileChooser();
            chooser.setInitialDirectory(new File(currentPath));
            chooser.setTitle("Connect database");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Access Database", "*.accdb"));
            File selectedFile = chooser.showOpenDialog(stage);
            if (selectedFile != null){
                FileWriter fileWriter = new FileWriter("databaseURL.txt", false);
                String URL = "jdbc:ucanaccess://" + selectedFile.getAbsolutePath();
                fileWriter.write(URL);
                fileWriter.close();
                System.out.println(URL);
                connectToDatabase();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private String generateRewards() throws SQLException {
        Random randyNewman = new Random();
        int randomReward = randyNewman.nextInt(totalRewards) + 1;
        String result = "";
        PreparedStatement preparedStatement = connection.prepareStatement("select * from Rewards");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            if (resultSet.getInt("ID") == randomReward){
                result = resultSet.getString("Reward");
            }
        }
        return result;
    }
}