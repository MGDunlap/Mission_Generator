module com.mission.missioncreator {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires ucanaccess;

    opens com.mission.missioncreator to javafx.fxml;
    exports com.mission.missioncreator;
}