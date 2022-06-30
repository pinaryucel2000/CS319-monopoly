package UserInterface;
import Controller.*;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class Monopoly extends Application
{
    public Scene scene;
    static MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        playMusic();

        // Shows the menu
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        primaryStage.setTitle("Monopoly");
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    // for playing the main menu theme music
    public void playMusic()
    {
        String base = getHostServices().getDocumentBase();
        Media h = new Media(base + "/src/UserInterface/soundEffects/" + "mainMenuTheme" + ".mp3");
        mediaPlayer = new MediaPlayer(h);
        mediaPlayer.play();
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
