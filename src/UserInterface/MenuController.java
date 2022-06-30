package UserInterface;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Scanner;


import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.text.html.ImageView;

import com.sun.glass.ui.Window;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;

public class MenuController  extends Application implements Initializable
{
    @FXML
    public AnchorPane volumePane;

    @FXML
    public Button exitButton;

    @FXML
    public Slider volumeSlider;

    MediaPlayer mediaPlayer;
    private boolean  onVolumePane = false;

    public void playButtonClicked(ActionEvent event) throws IOException
    {
        buttonClicked();
        Monopoly.mediaPlayer.stop();

        Parent menuStage = FXMLLoader.load(getClass().getResource("game.fxml"));
        Scene newGameScene = new Scene(menuStage);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(newGameScene);
        window.show();
    }

    public void buttonClicked()
    {

        String base = getHostServices().getDocumentBase();
        Media h = new Media(base + "/src/UserInterface/soundEffects/" + "buttonClick" + ".mp3");
        mediaPlayer = new MediaPlayer(h);
        mediaPlayer.play();
    }

    public void openVolumeSetting()
    {
        onVolumePane = !onVolumePane;
        volumePane.setVisible(onVolumePane);
        volumePane.setDisable(!onVolumePane);
    }


    public void exitButtonClicked()
    {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void initialize(URL arg0, ResourceBundle arg1)
    {
        volumeSlider.setValue(Monopoly.mediaPlayer.getVolume() * 100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Monopoly.mediaPlayer.setVolume(volumeSlider.getValue() / 100);
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //
    }
}