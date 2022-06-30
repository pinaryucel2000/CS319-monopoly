package UserInterface;

import Controller.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameController implements Initializable {
    @FXML
    ImageView diceImage1;

    @FXML
    ImageView diceImage2;

    @FXML
    Button rollDiceButton;

    @FXML
    Button buildButton;

    @FXML
    Pane buildHousePane;

    @FXML
    ImageView orangePawnImage;

    @FXML
    Label orangePlayersBalance;

    @FXML
    Label orangePlayersJailCards;

    @FXML
    TextArea orangePlayersProperties;

    @FXML
    AnchorPane orangePlayerPropertiesPane;

    @FXML
    ImageView yellowPawnImage;

    @FXML
    Label yellowPlayersJailCards;

    @FXML
    Label yellowPlayersBalance;

    @FXML
    TextArea yellowPlayersProperties;

    @FXML
    AnchorPane yellowPlayerPropertiesPane;

    @FXML
    ImageView bluePawnImage;

    @FXML
    Label bluePlayersJailCards;

    @FXML
    Label bluePlayersBalance;

    @FXML
    TextArea bluePlayersProperties;

    @FXML
    AnchorPane bluePlayerPropertiesPane;

    @FXML
    ImageView greenPawnImage;

    @FXML
    Label greenPlayersJailCards;

    @FXML
    Label greenPlayersBalance;

    @FXML
    TextArea greenPlayersProperties;

    @FXML
    AnchorPane greenPlayerPropertiesPane;




    @FXML
    ImageView medView, balticView, orientalView, vermontView, connView, charlesView, statesView, virginiaView,
            jamesView, tennView, nyView, kenView, indianaView,
            illView, atlanticView, ventView, marvinView, pacificView, carolinaView, pennView, parkView, boardView;

    @FXML
    Pane medPane, balticPane, orientalPane, vermontPane, connPane, charlesPane, statesPane, virginiaPane,
            jamesPane, tennPane, nyPane, kenPane, indianaPane,
            illinoisPane, atlanticPane, ventPane, marvinPane, pacificPane, carolinaPane, pennPane,
            parkPane, boardPane, boRailPane, pennRailPane, readRailPane, shortRailPane, electricPane, waterPane;

    @FXML
    Label buildbtnPrompt;

    @FXML
    Label hotelCountLabel;

    @FXML
    Label houseCountLabel;

    private Roller roller;
    private OrangePawnMover orangePawnMover;
    private GreenPawnMover greenPawnMover;
    private YellowPawnMover yellowPawnMover;
    private BluePawnMover bluePawnMover;
    private GameManager gameManager;
    private MediaPlayer mediaPlayer;
    private double[] orangePawnLocation;
    private int orangePawnLocationIndex;
    private double[][] orangePawnLocations;
    private double[] orangePawnJailLocation;
    private boolean onOrangePlayerPropertiesPane;

    private double[] greenPawnLocation;
    private int greenPawnLocationIndex;
    private double[][] greenPawnLocations;
    private double[] greenPawnJailLocation;
    private boolean onGreenPlayerPropertiesPane;

    private double[] yellowPawnLocation;
    private int yellowPawnLocationIndex;
    private double[][] yellowPawnLocations;
    private double[] yellowPawnJailLocation;
    private boolean onYellowPlayerPropertiesPane;

    private double[] bluePawnLocation;
    private int bluePawnLocationIndex;
    private double[][] bluePawnLocations;
    private double[] bluePawnJailLocation;
    private boolean onBluePlayerPropertiesPane;

    boolean build, sell, mortgage, unmortgage, trade;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setAllPanesDisabled();
        // for utils and rails they are not included in the above function
        boRailPane.setDisable(true);
        pennRailPane.setDisable(true);
        readRailPane.setDisable(true);
        shortRailPane.setDisable(true);
        electricPane.setDisable(true);
        waterPane.setDisable(true);
        buildHousePane.setVisible(false);
        buildbtnPrompt.setVisible(false);

        diceImage1.setImage(new Image("UserInterface/images/dice/dice1.png"));
        diceImage2.setImage(new Image("UserInterface/images/dice/dice2.png"));

        roller = new Roller();
        orangePawnMover = new OrangePawnMover();
        greenPawnMover = new GreenPawnMover();
        yellowPawnMover = new YellowPawnMover();
        bluePawnMover = new BluePawnMover();



        initializePawnLocations();

        gameManager = new GameManager();
        build = false;
        sell = false;
        mortgage = false;
        unmortgage = false;
        trade = false;
        onOrangePlayerPropertiesPane = false;
        onYellowPlayerPropertiesPane = false;
        onBluePlayerPropertiesPane = false;
        onGreenPlayerPropertiesPane = false;
    }
    public void buttonClicked()
    {
        String path = "src\\UserInterface\\soundEffects\\buttonClick.mp3";
        Media h = new Media(Paths.get(path).toUri().toString());
        mediaPlayer = new MediaPlayer(h);
        mediaPlayer.play();
    }

    public void showBluePawnProperties()
    {
        String properties = gameManager.getPlayersProperties("blue");

        if(properties != null)
        {
            bluePlayersProperties.setText(properties);
        }

        onBluePlayerPropertiesPane = !onBluePlayerPropertiesPane;
        bluePlayerPropertiesPane.setVisible(onBluePlayerPropertiesPane);
        bluePlayerPropertiesPane.setDisable(!onBluePlayerPropertiesPane);
    }

    public void showGreenPawnProperties()
    {
        String properties = gameManager.getPlayersProperties("green");

        if(properties != null)
        {
            greenPlayersProperties.setText(properties);
        }

        onGreenPlayerPropertiesPane = !onGreenPlayerPropertiesPane;
        greenPlayerPropertiesPane.setVisible(onGreenPlayerPropertiesPane);
        greenPlayerPropertiesPane.setDisable(!onGreenPlayerPropertiesPane);
    }

    public void showYellowPawnProperties()
    {
        String properties = gameManager.getPlayersProperties("yellow");

        if(properties != null)
        {
            yellowPlayersProperties.setText(properties);
        }

        onYellowPlayerPropertiesPane = !onYellowPlayerPropertiesPane;
        yellowPlayerPropertiesPane.setVisible(onYellowPlayerPropertiesPane);
        yellowPlayerPropertiesPane.setDisable(!onYellowPlayerPropertiesPane);
    }

    public void showOrangePawnProperties()
    {
        String properties = gameManager.getPlayersProperties("orange");

        if(properties != null)
        {
            orangePlayersProperties.setText(properties);
        }

        onOrangePlayerPropertiesPane = !onOrangePlayerPropertiesPane;
        orangePlayerPropertiesPane.setVisible(onOrangePlayerPropertiesPane);
        orangePlayerPropertiesPane.setDisable(!onOrangePlayerPropertiesPane);
    }

    private void initializePawnLocations()
    {
        // Initialize the initial x and y coordinates of the orange pawn.
        orangePawnLocation = new double[2];
        orangePawnLocations = new double[40][2];

        orangePawnLocation[0] = 801;
        orangePawnLocation[1] = 801;
        orangePawnLocationIndex = 0;
        orangePawnJailLocation = new double[2];
        orangePawnJailLocation[0] = 24;
        orangePawnJailLocation[1] = 821;

        for(int i = 0; i < 40; i++)
        {
            orangePawnLocations[i] = new double[2];

            if(i == 0)
            {
                orangePawnLocations[i][0] = 801;
                orangePawnLocations[i][1] = 801;
            }
            else if(i == 1)
            {
                orangePawnLocations[i][0] = 722;
                orangePawnLocations[i][1] = 801;
            }
            else if(i < 10)
            {
                orangePawnLocations[i][0] = 722 - (i - 1) * 79;
                orangePawnLocations[i][1] = 801;
            }
            else if (i == 10)
            {
                orangePawnLocations[i][0] = 1;
                orangePawnLocations[i][1] = 801;
            }
            else if(i == 11)
            {
                orangePawnLocations[i][0] = 1;
                orangePawnLocations[i][1] = 721;
            }
            else if(i < 20)
            {
                orangePawnLocations[i][0] = 1;
                orangePawnLocations[i][1] = 721 - (i - 11) * 78.875;
            }
            else if(i == 20)
            {
                orangePawnLocations[i][0] = 1;
                orangePawnLocations[i][1] = 1;
            }
            else if(i == 21)
            {
                orangePawnLocations[i][0] = 90;
                orangePawnLocations[i][1] = 1;
            }
            else if(i < 30)
            {
                orangePawnLocations[i][0] = 90 + (i - 21) * 79;
                orangePawnLocations[i][1] = 1;
            }
            else if(i == 30)
            {
                orangePawnLocations[i][0] = 801;
                orangePawnLocations[i][1] = 1;
            }
            else if(i == 31)
            {
                orangePawnLocations[i][0] = 801;
                orangePawnLocations[i][1] = 90;
            }
            else
            {
                orangePawnLocations[i][0] = 801;
                orangePawnLocations[i][1] = 90 + (i - 31) * 78.875;
            }

        }

        // Initialize the initial x and y coordinates of the green pawn.
        greenPawnLocation = new double[2];
        greenPawnLocations = new double[40][2];

        greenPawnLocation[0] = 801;
        greenPawnLocation[1] = 873;
        greenPawnLocationIndex = 0;
        greenPawnJailLocation = new double[2];
        greenPawnJailLocation[0] = 24;
        greenPawnJailLocation[1] = 852;

        for(int i = 0; i < 40; i++)
        {
            greenPawnLocations[i] = new double[2];

            if(i == 0)
            {
                greenPawnLocations[i][0] = 801;
                greenPawnLocations[i][1] = 873;
            }
            else if(i == 1)
            {
                greenPawnLocations[i][0] = 722;
                greenPawnLocations[i][1] = 873;
            }
            else if(i < 10)
            {
                greenPawnLocations[i][0] = 722 - (i - 1) * 79;
                greenPawnLocations[i][1] = 873;
            }
            else if (i == 10)
            {
                greenPawnLocations[i][0] = 1;
                greenPawnLocations[i][1] = 873;
            }
            else if(i == 11)
            {
                greenPawnLocations[i][0] = 1;
                greenPawnLocations[i][1] = 775;
            }
            else if(i < 20)
            {
                greenPawnLocations[i][0] = 1;
                greenPawnLocations[i][1] = 775 - (i - 11) * 78.875;
            }
            else if(i == 20)
            {
                greenPawnLocations[i][0] = 1;
                greenPawnLocations[i][1] = 63;
            }
            else if(i == 21)
            {
                greenPawnLocations[i][0] = 90;
                greenPawnLocations[i][1] = 63;
            }
            else if(i < 30)
            {
                greenPawnLocations[i][0] = 90 + (i - 21) * 79;
                greenPawnLocations[i][1] = 63;
            }
            else if(i == 30)
            {
                greenPawnLocations[i][0] = 801;
                greenPawnLocations[i][1] = 63;
            }
            else if(i == 31)
            {
                greenPawnLocations[i][0] = 801;
                greenPawnLocations[i][1] = 144;
            }
            else
            {
                greenPawnLocations[i][0] = 801;
                greenPawnLocations[i][1] = 144 + (i - 31) * 78.875;
            }

        }

        System.out.println(greenPawnLocations[0][0]+ " "+ greenPawnLocations[0][1]);
        greenPawnImage.setLayoutX(greenPawnLocations[0][0]);
        greenPawnImage.setLayoutY(greenPawnLocations[0][1]);

        // Initialize the initial x and y coordinates of the yellow pawn.
        yellowPawnLocation = new double[2];
        yellowPawnLocations = new double[40][2];

        yellowPawnLocation[0] = 801;
        yellowPawnLocation[1] = 801;
        yellowPawnLocationIndex = 0;
        yellowPawnJailLocation = new double[2];
        yellowPawnJailLocation[0] = 53;
        yellowPawnJailLocation[1] = 821;

        for(int i = 0; i < 40; i++)
        {
            yellowPawnLocations[i] = new double[2];

            if(i == 0)
            {
                yellowPawnLocations[i][0] = 880;
                yellowPawnLocations[i][1] = 801;
            }
            else if(i == 1)
            {
                yellowPawnLocations[i][0] = 780;
                yellowPawnLocations[i][1] = 801;
            }
            else if(i < 10)
            {
                yellowPawnLocations[i][0] = 780 - (i - 1) * 79;
                yellowPawnLocations[i][1] = 801;
            }
            else if (i == 10)
            {
                yellowPawnLocations[i][0] = 68;
                yellowPawnLocations[i][1] = 801;
            }
            else if(i == 11)
            {
                yellowPawnLocations[i][0] = 68;
                yellowPawnLocations[i][1] = 721;
            }
            else if(i < 20)
            {
                yellowPawnLocations[i][0] = 68;
                yellowPawnLocations[i][1] = 721 - (i - 11) * 78.875;
            }
            else if(i == 20)
            {
                yellowPawnLocations[i][0] = 68;
                yellowPawnLocations[i][1] = 1;
            }
            else if(i == 21)
            {
                yellowPawnLocations[i][0] = 148;
                yellowPawnLocations[i][1] = 1;
            }
            else if(i < 30)
            {
                yellowPawnLocations[i][0] = 148 + (i - 21) * 79;
                yellowPawnLocations[i][1] = 1;
            }
            else if(i == 30)
            {
                yellowPawnLocations[i][0] = 880;
                yellowPawnLocations[i][1] = 1;
            }
            else if(i == 31)
            {
                yellowPawnLocations[i][0] = 880;
                yellowPawnLocations[i][1] = 90;
            }
            else
            {
                yellowPawnLocations[i][0] = 801;
                yellowPawnLocations[i][1] = 90 + (i - 31) * 78.875;
            }
        }

        yellowPawnImage.setLayoutX(yellowPawnLocations[0][0]);
        yellowPawnImage.setLayoutY(yellowPawnLocations[0][1]);

        // Initialize the initial x and y coordinates of the blue pawn.
        bluePawnLocation = new double[2];
        bluePawnLocations = new double[40][2];

        bluePawnLocation[0] = 877;
        bluePawnLocation[1] = 873;
        bluePawnLocationIndex = 0;
        bluePawnJailLocation = new double[2];
        bluePawnJailLocation[0] = 53;
        bluePawnJailLocation[1] = 850;

        for(int i = 0; i < 40; i++)
        {
            bluePawnLocations[i] = new double[2];

            if(i == 0)
            {
                bluePawnLocations[i][0] = 877;
                bluePawnLocations[i][1] = 873;
            }
            else if(i == 1)
            {
                bluePawnLocations[i][0] = 780;
                bluePawnLocations[i][1] = 873;
            }
            else if(i < 10)
            {
                bluePawnLocations[i][0] = 780 - (i - 1) * 79;
                bluePawnLocations[i][1] = 873;
            }
            else if (i == 10)
            {
                bluePawnLocations[i][0] = 68;
                bluePawnLocations[i][1] = 873;
            }
            else if(i == 11)
            {
                bluePawnLocations[i][0] = 68;
                bluePawnLocations[i][1] = 775;
            }
            else if(i < 20)
            {
                bluePawnLocations[i][0] = 68;
                bluePawnLocations[i][1] = 775 - (i - 11) * 78.875;
            }
            else if(i == 20)
            {
                bluePawnLocations[i][0] = 68;
                bluePawnLocations[i][1] = 63;
            }
            else if(i == 21)
            {
                bluePawnLocations[i][0] = 148;
                bluePawnLocations[i][1] = 63;
            }
            else if(i < 30)
            {
                bluePawnLocations[i][0] = 148 + (i - 21) * 79;
                bluePawnLocations[i][1] = 63;
            }
            else if(i == 30)
            {
                bluePawnLocations[i][0] = 877;
                bluePawnLocations[i][1] = 63;
            }
            else if(i == 31)
            {
                bluePawnLocations[i][0] = 877;
                bluePawnLocations[i][1] = 144;
            }
            else
            {
                bluePawnLocations[i][0] = 877;
                bluePawnLocations[i][1] = 144 + (i - 31) * 78.875;
            }
        }

        bluePawnImage.setLayoutX(bluePawnLocations[0][0]);
        bluePawnImage.setLayoutY(bluePawnLocations[0][1]);
    }

    public void movePawn(String color, int amount)
    {
        if(color == "orange")
        {
            if(orangePawnLocationIndex == -1)
            {
                teleportPawn("orange", 11);

            }

            orangePawnMover.start();
            orangePawnLocationIndex = (orangePawnLocationIndex + amount) % 40;
            orangePawnLocation = orangePawnLocations[orangePawnLocationIndex];
        }
        else if(color == "yellow")
        {
            if(yellowPawnLocationIndex == -1)
            {
                teleportPawn("yellow", 11);

            }

            yellowPawnMover.start();
            yellowPawnLocationIndex = (yellowPawnLocationIndex + amount) % 40;
            yellowPawnLocation = yellowPawnLocations[yellowPawnLocationIndex];
        }
        else if(color == "green")
        {
            if(greenPawnLocationIndex == -1)
            {
                teleportPawn("green", 11);

            }

            greenPawnMover.start();
            greenPawnLocationIndex = (greenPawnLocationIndex + amount) % 40;
            greenPawnLocation = greenPawnLocations[greenPawnLocationIndex];
        }
        else if(color == "blue")
        {
            if(bluePawnLocationIndex == -1)
            {
                teleportPawn("blue", 11);

            }

            bluePawnMover.start();
            bluePawnLocationIndex = (bluePawnLocationIndex + amount) % 40;
            bluePawnLocation = bluePawnLocations[bluePawnLocationIndex];
        }
    }



    public void teleportPawn(String color, int teleportAmount)
    {
        if(color == "orange")
        {
            orangePawnLocationIndex = (orangePawnLocationIndex + teleportAmount) % 40;
            orangePawnLocation = orangePawnLocations[orangePawnLocationIndex];
            orangePawnImage.setLayoutX(orangePawnLocation[0]);
            orangePawnImage.setLayoutY(orangePawnLocation[1]);

        }
        else if(color == "yellow")
        {
            yellowPawnLocationIndex = (yellowPawnLocationIndex + teleportAmount) % 40;
            yellowPawnLocation = yellowPawnLocations[yellowPawnLocationIndex];
            yellowPawnImage.setLayoutX(yellowPawnLocation[0]);
            yellowPawnImage.setLayoutY(yellowPawnLocation[1]);
        }
    }


    public void sendPawnToJail(String color) {
        if (color == "orange") {
            orangePawnLocationIndex = -1;
            orangePawnImage.setLayoutX(orangePawnJailLocation[0]);
            orangePawnImage.setLayoutY(orangePawnJailLocation[1]);

        } else if (color == "yellow") {
            yellowPawnLocationIndex = -1;
            yellowPawnImage.setLayoutX(yellowPawnJailLocation[0]);
            yellowPawnImage.setLayoutY(yellowPawnJailLocation[1]);
        }
    }

    public void rollDice()
    {
        int[] dice = new int[2];
        dice = gameManager.rollDice();

        if(dice == null)
        {
            return;
        }

        diceImage1.setImage(new Image("UserInterface/images/dice/dice" + dice[0] + ".png"));
        diceImage2.setImage(new Image("UserInterface/images/dice/dice" + dice[1] + ".png"));

        // Animation constants
        final int DURATION = 600;
        final int ROTATE_ANGLE = 1080;

        // Animate dice rotation
        RotateTransition rt1 = new RotateTransition(Duration.millis(DURATION), diceImage1);
        RotateTransition rt2 = new RotateTransition(Duration.millis(DURATION), diceImage2);
        rt1.setByAngle(ROTATE_ANGLE);
        rt2.setByAngle(-ROTATE_ANGLE);

        // Animate dice movement
        TranslateTransition tt1 = new TranslateTransition(Duration.millis(DURATION), diceImage1);
        TranslateTransition tt2 = new TranslateTransition(Duration.millis(DURATION), diceImage2);
        tt1.setFromY(diceImage1.getY() + 100);
        tt1.setFromX(diceImage1.getX() + 200);
        tt1.setToY(diceImage1.getY());
        tt1.setToX(diceImage1.getX());

        tt2.setFromY(diceImage2.getY() + 100);
        tt2.setFromX(diceImage1.getX() - 200);
        tt2.setToY(diceImage2.getY());
        tt2.setToX(diceImage1.getX());

        // Run rotate- and translate (movement) animation in parallel
        ParallelTransition pt = new ParallelTransition(rt1, rt2, tt1, tt2);
        pt.play();


        String color = gameManager.getTurnsColor();

        if(!gameManager.isTurnInJail())
        {
            movePawn(color, dice[0] + dice[1]);
        }
    }

    public void rollAnimation() {

        buttonClicked();
        if (!gameManager.getIsDiceRolled()) {
            roller.start();
        }
    }
    public void goBanktupt()
    {
        buttonClicked();
        String color = gameManager.getTurnsColor();
        gameManager.turnWentBankrupt();

        if(color == "orange")
        {
            orangePawnImage.setVisible(false);
            orangePlayersBalance.setVisible(false);
            orangePlayersJailCards.setVisible(false);
            orangePlayersProperties.setVisible(false);
        }
        else if(color == "yellow")
        {
            yellowPawnImage.setVisible(false);
            yellowPlayersBalance.setVisible(false);
            yellowPlayersJailCards.setVisible(false);
            yellowPlayersProperties.setVisible(false);
        }
        else if(color == "green")
        {
            greenPawnImage.setVisible(false);
            greenPlayersBalance.setVisible(false);
            greenPlayersJailCards.setVisible(false);
            greenPlayersProperties.setVisible(false);
        }
        else if(color == "blue")
        {
            bluePawnImage.setVisible(false);
            bluePlayersBalance.setVisible(false);
            bluePlayersJailCards.setVisible(false);
            bluePlayersProperties.setVisible(false);
        }

    }

    public void drawChanceCard()
    {
        buttonClicked();

        String color = gameManager.getTurnsColor();
        int movement = gameManager.drawChanceCard();

        if(movement == -1)
        {
            sendPawnToJail(color);
            return;
        }
        else if(movement == -2)
        {
            updateJailCards();
            return;
        }

        teleportPawn(color, movement);
        updateBalances();

    }

    public void updateJailCards()
    {
        int jailCardCount = gameManager.getJailCardCount("orange");

        if(jailCardCount != -1)
        {
            orangePlayersJailCards.setText(jailCardCount + "");
        }

        jailCardCount = gameManager.getJailCardCount("yellow");

        if(jailCardCount != -1)
        {
            yellowPlayersJailCards.setText(jailCardCount + "");
        }

        jailCardCount = gameManager.getJailCardCount("green");

        if(jailCardCount != -1)
        {
            greenPlayersJailCards.setText(jailCardCount + "");
        }

        jailCardCount = gameManager.getJailCardCount("blue");

        if(jailCardCount != -1)
        {
            bluePlayersJailCards.setText(jailCardCount + "");
        }
    }

    public void drawCommunityChestCard()
    {
        buttonClicked();

        String color = gameManager.getTurnsColor();
        teleportPawn(color, gameManager.drawCommunityChestCard());
        updateBalances();
    }

    public void payRent()
    {
        buttonClicked();

        if(gameManager.payRent())
        {
            updateBalances();
        }
    }

    public void payTax()
    {
        buttonClicked();

        if(gameManager.payTax())
        {
            updateBalances();
        }
    }

    public void payJailBail()
    {
        buttonClicked();

        if(gameManager.payJailBail())
        {
            teleportPawn(gameManager.getTurnsColor(), 11);
            updateBalances();
        }
    }


    public void endTurn()
    {
        buttonClicked();
        gameManager.turnEnded();
    }

    public void setDiceImage(int top1, int top2) {
        diceImage1.setImage(new Image("UserInterface/images/dice/dice" + top1 + ".png"));
        diceImage2.setImage(new Image("UserInterface/images/dice/dice" + top2 + ".png"));
    }

    public void updateBalances()
    {
        int balance = gameManager.getBalance("orange");

        if(balance != -1)
        {
            orangePlayersBalance.setText(balance + "$");
        }

        balance = gameManager.getBalance("yellow");

        if(balance != -1)
        {
            yellowPlayersBalance.setText(balance + "$");
        }

    }


    public void buyProperty()
    {
        buttonClicked();

        if(gameManager.buyProperty())
        {
            updateBalances();
        }
    }

//    there are imageviews on each property so they will need to be altered depending on the state of the property
//    the user will tell us a string property
//    we need to get that imageview according to the string
//    public ImageView convertStringToPane(String propertyName){
//
//        if(propertyName == "MediteraneanAvenue" ){ return medView ;}
//
//        else
//        {
//            throw new IllegalArgumentException("String isnt correct");
//        }
//
//    }

    private class GreenPawnMover extends AnimationTimer
    {
        private long FRAMES_PER_SECOND = 50l;
        private long INTERVAL = 1000000000L / FRAMES_PER_SECOND;

        private long last = 0;

        @Override
        public void handle(long now)
        {
            if(now - last > INTERVAL)
            {
                if( Math.abs(greenPawnImage.getLayoutY() - 873) < 5  || Math.abs(greenPawnImage.getLayoutY() - 63) < 5)
                {
                    if(Math.abs(greenPawnImage.getLayoutX() - greenPawnLocation[0]) > 5)
                    {
                        if(greenPawnImage.getLayoutX() > greenPawnLocation[0])
                        {
                            greenPawnImage.setLayoutX(greenPawnImage.getLayoutX() - 7.9);
                        }
                        else if(greenPawnImage.getLayoutX() < greenPawnLocation[0])
                        {
                            greenPawnImage.setLayoutX(greenPawnImage.getLayoutX() + 7.9);

                        }
                    }
                    else
                    {
                        if(Math.abs(greenPawnImage.getLayoutY() - greenPawnLocation[1]) > 5)
                        {
                            if(greenPawnImage.getLayoutY() > greenPawnLocation[1])
                            {
                                greenPawnImage.setLayoutY(greenPawnImage.getLayoutY() - 7.8875);
                            }
                            else if(greenPawnImage.getLayoutY() < greenPawnLocation[1])
                            {
                                greenPawnImage.setLayoutY(greenPawnImage.getLayoutY() + 7.8875);
                            }
                        }
                    }
                }
                else
                {
                    if(Math.abs(greenPawnImage.getLayoutY() - greenPawnLocation[1]) > 5)
                    {
                        if(Math.abs(greenPawnImage.getLayoutY() - greenPawnLocation[1]) > 5)
                        {
                            if(greenPawnImage.getLayoutY() > greenPawnLocation[1])
                            {
                                greenPawnImage.setLayoutY(greenPawnImage.getLayoutY() - 7.8875);
                            }
                            else if(greenPawnImage.getLayoutY() < greenPawnLocation[1])
                            {
                                greenPawnImage.setLayoutY(greenPawnImage.getLayoutY() + 7.8875);
                            }
                        }
                    }
                    else
                    {
                        if(greenPawnImage.getLayoutX() > greenPawnLocation[0])
                        {
                            greenPawnImage.setLayoutX(greenPawnImage.getLayoutX() - 7.9);
                        }
                        else if(greenPawnImage.getLayoutX() < greenPawnLocation[0])
                        {
                            greenPawnImage.setLayoutX(greenPawnImage.getLayoutX() + 7.9);
                        }
                    }
                }

                last = now;

                if(Math.abs(greenPawnLocation[0] - greenPawnImage.getLayoutX()) <= 7 && Math.abs(greenPawnLocation[1] - greenPawnImage.getLayoutY()) <= 7)
                {
                    greenPawnImage.setLayoutX(greenPawnLocation[0]);
                    greenPawnImage.setLayoutY(greenPawnLocation[1]);
                    greenPawnMover.stop();
                }
            }
        }
    }

    private class OrangePawnMover extends AnimationTimer
    {
        private long FRAMES_PER_SECOND = 50l;
        private long INTERVAL = 1000000000L / FRAMES_PER_SECOND;

        private long last = 0;

        @Override
        public void handle(long now)
        {
            if(now - last > INTERVAL)
            {
                if( Math.abs(orangePawnImage.getLayoutY() - 801) < 5  || Math.abs(orangePawnImage.getLayoutY() - 1) < 5)
                {
                    if(Math.abs(orangePawnImage.getLayoutX() - orangePawnLocation[0]) > 5)
                    {
                        if(orangePawnImage.getLayoutX() > orangePawnLocation[0])
                        {
                            orangePawnImage.setLayoutX(orangePawnImage.getLayoutX() - 7.9);
                        }
                        else if(orangePawnImage.getLayoutX() < orangePawnLocation[0])
                        {
                            orangePawnImage.setLayoutX(orangePawnImage.getLayoutX() + 7.9);

                        }
                    }
                    else
                    {
                        if(Math.abs(orangePawnImage.getLayoutY() - orangePawnLocation[1]) > 5)
                        {
                            if(orangePawnImage.getLayoutY() > orangePawnLocation[1])
                            {
                                orangePawnImage.setLayoutY(orangePawnImage.getLayoutY() - 7.8875);
                            }
                            else if(orangePawnImage.getLayoutY() < orangePawnLocation[1])
                            {
                                orangePawnImage.setLayoutY(orangePawnImage.getLayoutY() + 7.8875);
                            }
                        }
                    }
                }
                else
                {
                    if(Math.abs(orangePawnImage.getLayoutY() - orangePawnLocation[1]) > 5)
                    {
                        if(Math.abs(orangePawnImage.getLayoutY() - orangePawnLocation[1]) > 5)
                        {
                            if(orangePawnImage.getLayoutY() > orangePawnLocation[1])
                            {
                                orangePawnImage.setLayoutY(orangePawnImage.getLayoutY() - 7.8875);
                            }
                            else if(orangePawnImage.getLayoutY() < orangePawnLocation[1])
                            {
                                orangePawnImage.setLayoutY(orangePawnImage.getLayoutY() + 7.8875);
                            }
                        }
                    }
                    else
                    {
                        if(orangePawnImage.getLayoutX() > orangePawnLocation[0])
                        {
                            orangePawnImage.setLayoutX(orangePawnImage.getLayoutX() - 7.9);
                        }
                        else if(orangePawnImage.getLayoutX() < orangePawnLocation[0])
                        {
                            orangePawnImage.setLayoutX(orangePawnImage.getLayoutX() + 7.9);
                        }
                    }
                }

                last = now;


                if(Math.abs(orangePawnLocation[0] - orangePawnImage.getLayoutX()) <= 7 && Math.abs(orangePawnLocation[1] - orangePawnImage.getLayoutY()) <= 7)
                {
                    orangePawnImage.setLayoutX(orangePawnLocation[0]);
                    orangePawnImage.setLayoutY(orangePawnLocation[1]);
                    orangePawnMover.stop();
                }
            }
        }
    }

    private class BluePawnMover extends AnimationTimer
    {
        private long FRAMES_PER_SECOND = 50l;
        private long INTERVAL = 1000000000L / FRAMES_PER_SECOND;

        private long last = 0;

        @Override
        public void handle(long now)
        {
            if(now - last > INTERVAL)
            {
                if( Math.abs(bluePawnImage.getLayoutY() - 873) < 5  || Math.abs(bluePawnImage.getLayoutY() - 63) < 5)
                {
                    if(Math.abs(bluePawnImage.getLayoutX() - bluePawnLocation[0]) > 5)
                    {
                        if(bluePawnImage.getLayoutX() > bluePawnLocation[0])
                        {
                            bluePawnImage.setLayoutX(bluePawnImage.getLayoutX() - 7.9);
                        }
                        else if(bluePawnImage.getLayoutX() < bluePawnLocation[0])
                        {
                            bluePawnImage.setLayoutX(bluePawnImage.getLayoutX() + 7.9);

                        }
                    }
                    else
                    {
                        if(Math.abs(bluePawnImage.getLayoutY() - bluePawnLocation[1]) > 5)
                        {
                            if(bluePawnImage.getLayoutY() > bluePawnLocation[1])
                            {
                                bluePawnImage.setLayoutY(bluePawnImage.getLayoutY() - 7.8875);
                            }
                            else if(bluePawnImage.getLayoutY() < bluePawnLocation[1])
                            {
                                bluePawnImage.setLayoutY(bluePawnImage.getLayoutY() + 7.8875);
                            }
                        }
                    }
                }
                else
                {
                    if(Math.abs(bluePawnImage.getLayoutY() - bluePawnLocation[1]) > 5)
                    {
                        if(Math.abs(bluePawnImage.getLayoutY() - bluePawnLocation[1]) > 5)
                        {
                            if(bluePawnImage.getLayoutY() > bluePawnLocation[1])
                            {
                                bluePawnImage.setLayoutY(bluePawnImage.getLayoutY() - 7.8875);
                            }
                            else if(bluePawnImage.getLayoutY() < bluePawnLocation[1])
                            {
                                bluePawnImage.setLayoutY(bluePawnImage.getLayoutY() + 7.8875);
                            }
                        }
                    }
                    else
                    {
                        if(bluePawnImage.getLayoutX() > bluePawnLocation[0])
                        {
                            bluePawnImage.setLayoutX(bluePawnImage.getLayoutX() - 7.9);
                        }
                        else if(bluePawnImage.getLayoutX() < bluePawnLocation[0])
                        {
                            bluePawnImage.setLayoutX(bluePawnImage.getLayoutX() + 7.9);
                        }
                    }
                }

                last = now;


                if(Math.abs(bluePawnLocation[0] - bluePawnImage.getLayoutX()) <= 7 && Math.abs(bluePawnLocation[1] - bluePawnImage.getLayoutY()) <= 7)
                {
                    bluePawnImage.setLayoutX(bluePawnLocation[0]);
                    bluePawnImage.setLayoutY(bluePawnLocation[1]);
                    bluePawnMover.stop();
                }
            }
        }
    }

    private class YellowPawnMover extends AnimationTimer
    {
        private long FRAMES_PER_SECOND = 50l;
        private long INTERVAL = 1000000000L / FRAMES_PER_SECOND;

        private long last = 0;

        @Override
        public void handle(long now)
        {
            if(now - last > INTERVAL)
            {
                if( Math.abs(yellowPawnImage.getLayoutY() - 801) < 5  || Math.abs(yellowPawnImage.getLayoutY() - 1) < 5)
                {
                    if(Math.abs(yellowPawnImage.getLayoutX() - yellowPawnLocation[0]) > 5)
                    {
                        if(yellowPawnImage.getLayoutX() > yellowPawnLocation[0])
                        {
                            yellowPawnImage.setLayoutX(yellowPawnImage.getLayoutX() - 7.9);
                        }
                        else if(yellowPawnImage.getLayoutX() < yellowPawnLocation[0])
                        {
                            yellowPawnImage.setLayoutX(yellowPawnImage.getLayoutX() + 7.9);

                        }
                    }
                    else
                    {
                        if(Math.abs(yellowPawnImage.getLayoutY() - yellowPawnLocation[1]) > 5)
                        {
                            if(yellowPawnImage.getLayoutY() > yellowPawnLocation[1])
                            {
                                yellowPawnImage.setLayoutY(yellowPawnImage.getLayoutY() - 7.8875);
                            }
                            else if(yellowPawnImage.getLayoutY() < yellowPawnLocation[1])
                            {
                                yellowPawnImage.setLayoutY(yellowPawnImage.getLayoutY() + 7.8875);
                            }
                        }
                    }
                }
                else
                {
                    if(Math.abs(yellowPawnImage.getLayoutY() - yellowPawnLocation[1]) >5)
                    {
                        if(Math.abs(yellowPawnImage.getLayoutY() - yellowPawnLocation[1]) > 5)
                        {
                            if(yellowPawnImage.getLayoutY() > yellowPawnLocation[1])
                            {
                                yellowPawnImage.setLayoutY(yellowPawnImage.getLayoutY() - 7.8875);
                            }
                            else if(yellowPawnImage.getLayoutY() < yellowPawnLocation[1])
                            {
                                yellowPawnImage.setLayoutY(yellowPawnImage.getLayoutY() + 7.8875);
                            }
                        }
                    }
                    else
                    {
                        if(yellowPawnImage.getLayoutX() > yellowPawnLocation[0])
                        {
                            yellowPawnImage.setLayoutX(yellowPawnImage.getLayoutX() - 7.9);
                        }
                        else if(yellowPawnImage.getLayoutX() < yellowPawnLocation[0])
                        {
                            yellowPawnImage.setLayoutX(yellowPawnImage.getLayoutX() + 7.9);
                        }
                    }
                }


                last = now;

                if(Math.abs(yellowPawnLocation[0] - yellowPawnImage.getLayoutX()) <= 3 && Math.abs(yellowPawnLocation[1] - yellowPawnImage.getLayoutY()) <= 3)
                {
                    yellowPawnImage.setLayoutX(yellowPawnLocation[0]);
                    yellowPawnImage.setLayoutY(yellowPawnLocation[1]);
                    yellowPawnMover.stop();
                }
            }
        }
    }

    //method that runs when build button is pressed
    public void buildHouse() {
        build = true;
        sell = false;
        mortgage = false;
        unmortgage = false;
        unmortgage = false;
        setAllPanesEnabled();
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3));
        buildbtnPrompt.setText("Press on a property that you own on the board to build on it");
        buildbtnPrompt.setVisible(true);
        visiblePause.setOnFinished(
                event -> buildbtnPrompt.setVisible(false)
        );
        visiblePause.play();
        updateBalances();
    }

    //method that runs when unmortgage button is pressed
    public void unMortgageBtnClicked()
    {
        sell = false;
        mortgage = false;
        unmortgage = false;
        build = false;
        unmortgage = true;

        setAllPanesEnabled();
        boRailPane.setDisable(false);
        pennRailPane.setDisable(false);
        readRailPane.setDisable(false);
        shortRailPane.setDisable(false);
        electricPane.setDisable(false);
        waterPane.setDisable(false);

        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3));
        buildbtnPrompt.setText("Click on a property you own to unmortgage it");
        buildbtnPrompt.setVisible(true);
        visiblePause.setOnFinished(
                event -> {
                    buildbtnPrompt.setVisible(false);
                }

        );
        visiblePause.play();
        updateBalances();
    }

    //buildbtnPrompt will be modified here
    public void sellButtonClicked() {
        sell = true;
        mortgage = false;
        unmortgage = false;
        build = false;
        setAllPanesEnabled();
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3));
        buildbtnPrompt.setText("Click on a property you own to sell a house on it");
        buildbtnPrompt.setVisible(true);
        visiblePause.setOnFinished(
                event -> {
                    buildbtnPrompt.setVisible(false);
                }

        );
        visiblePause.play();
        updateBalances();
    }

    //sell all the houses on that property
    //give the player the mortgage money
    //make that properties attibute to mortgaged
    //also make the

    public void mortgageBtnClicked() {
        mortgage = true;
        unmortgage = false;
        sell = false;
        build = false;
        setAllPanesEnabled();
        boRailPane.setDisable(false);
        pennRailPane.setDisable(false);
        readRailPane.setDisable(false);
        shortRailPane.setDisable(false);
        electricPane.setDisable(false);
        waterPane.setDisable(false);

        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3));
        buildbtnPrompt.setText("Click on a property you own to mortgage it");
        buildbtnPrompt.setVisible(true);
        visiblePause.setOnFinished(
                event -> {
                    buildbtnPrompt.setVisible(false);
                }

        );
        visiblePause.play();
        updateBalances();
    }

    public void setAllPanesDisabled() {
        medPane.setDisable(true);
        balticPane.setDisable(true);
        orientalPane.setDisable(true);
        vermontPane.setDisable(true);
        connPane.setDisable(true);
        charlesPane.setDisable(true);
        statesPane.setDisable(true);
        virginiaPane.setDisable(true);
        jamesPane.setDisable(true);
        tennPane.setDisable(true);
        nyPane.setDisable(true);
        kenPane.setDisable(true);
        indianaPane.setDisable(true);
        illinoisPane.setDisable(true);
        atlanticPane.setDisable(true);
        ventPane.setDisable(true);
        marvinPane.setDisable(true);
        pacificPane.setDisable(true);
        carolinaPane.setDisable(true);
        pennPane.setDisable(true);
        parkPane.setDisable(true);
        boardPane.setDisable(true);
    }


    public void setAllPanesEnabled() {
        medPane.setDisable(false);
        balticPane.setDisable(false);
        orientalPane.setDisable(false);
        vermontPane.setDisable(false);
        connPane.setDisable(false);
        charlesPane.setDisable(false);
        statesPane.setDisable(false);
        virginiaPane.setDisable(false);
        jamesPane.setDisable(false);
        tennPane.setDisable(false);
        nyPane.setDisable(false);
        kenPane.setDisable(false);
        indianaPane.setDisable(false);
        illinoisPane.setDisable(true);
        atlanticPane.setDisable(false);
        ventPane.setDisable(false);
        marvinPane.setDisable(false);
        pacificPane.setDisable(false);
        carolinaPane.setDisable(false);
        pennPane.setDisable(false);
        parkPane.setDisable(false);
        boardPane.setDisable(false);
    }

    private class Roller extends AnimationTimer {
        private long FRAMES_PER_SECOND = 50l;
        private long INTERVAL = 1000000000L / FRAMES_PER_SECOND;

        // Roll it 20 times.
        private int MAX_ROLLS = 20;

        private long last = 0;
        private int count = 0;

        @Override
        public void handle(long now) {
            if (now - last > INTERVAL) {
                int top1 = 1 + (int) (Math.random() * 6);
                int top2 = 1 + (int) (Math.random() * 6);
                setDiceImage(top1, top2);
                last = now;
                count++;

                if (count > MAX_ROLLS) {
                    roller.stop();

                    // Roll 20 times for the animation then roll for the last time.
                    rollDice();
                    count = 0;
                }
            }
        }
    }

    //change Sell state
    public void changeSellState(ImageView imgView, int index) {
        int state = gameManager.sellHouse(index);
        System.out.println("i am inside the sell state func and the value of state is "+ state);
        if(state == 0)
        {
            imgView.setImage(null);
        }
        else if (state == 1) {
            updateBalances();
            imgView.setImage(new Image("UserInterface/images/housestates/state1.png"));
            sell = false;
        } else if (state == 2) {
            updateBalances();
            imgView.setImage(new Image("UserInterface/images/housestates/state2.png"));
            sell = false;
        } else if (state == 3) {
            updateBalances();
            imgView.setImage(new Image("UserInterface/images/housestates/state3.png"));
            sell = false;
        } else if (state == 4) {
            updateBalances();
            imgView.setImage(new Image("UserInterface/images/housestates/state4.png"));
            sell = false;
        } else if (state == 5) {
            updateBalances();
            imgView.setImage(new Image("UserInterface/images/housestates/state5.png"));
            sell = false;
        }
        else if (state == 6) {
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3));
            buildbtnPrompt.setText("Sell your houses evenly! ");
            buildbtnPrompt.setVisible(true);
            visiblePause.setOnFinished(
                    event -> {
                        buildbtnPrompt.setVisible(false);
                        buildbtnPrompt.setText("Click on a property you own to sell a house on it");
                        setAllPanesDisabled();
                        sell = false;
                    });
            visiblePause.play();
        }
        else {
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3));
            buildbtnPrompt.setText("You own no houses on that property to sell. Click on the sell button again to try again! ");
            buildbtnPrompt.setVisible(true);
            visiblePause.setOnFinished(
                    event -> {
                        buildbtnPrompt.setVisible(false);
                        buildbtnPrompt.setText("Click on a property you own to sell a house on it");
                        setAllPanesDisabled();
                        sell = false;
                    });
            visiblePause.play();
        }
    }

    public void changeStateUtilsAndRail(int index) {

        int state = gameManager.mortgageBtnClicked(index);



        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3));
        if (state == 1) {
            updateBalances();
            buildbtnPrompt.setText("You have mortgaged this property");
            mortgage = false;
        } else {
            buildbtnPrompt.setText("You do not own this property. Try again after pressing the Mortgage button!");
            mortgage = false;
        }
        buildbtnPrompt.setVisible(true);
        visiblePause.setOnFinished(
                event -> {
                    buildbtnPrompt.setVisible(false);
                    buildbtnPrompt.setText("Click on a property you own to mortgage it");
                    boRailPane.setDisable(true);
                    pennRailPane.setDisable(true);
                    readRailPane.setDisable(true);
                    shortRailPane.setDisable(true);
                    electricPane.setDisable(true);
                    waterPane.setDisable(true);
                    mortgage = false;
                });
        visiblePause.play();

    }



    public void changeUnMortgageState(int index) {

        System.out.println("i am inside unmortgage state");

        int state = gameManager.unmortgageBtnClicked(index);

        System.out.println("value of state is: "  + state);
        if (state == 1) {
            System.out.println("i am inside state 1");
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3));
            updateBalances();
            buildbtnPrompt.setText("You have unmortgaged the property");
            buildbtnPrompt.setVisible(true);
            visiblePause.setOnFinished(
                    event -> {
                        buildbtnPrompt.setVisible(false);
                        buildbtnPrompt.setText("Click on a property you own to mortgage it");
                        setAllPanesDisabled();
                        boRailPane.setDisable(true);
                        pennRailPane.setDisable(true);
                        readRailPane.setDisable(true);
                        shortRailPane.setDisable(true);
                        electricPane.setDisable(true);
                        waterPane.setDisable(true);
                        unmortgage = false;
                    });
            visiblePause.play();
        }

        else if(state == 2)
        {
            System.out.println("i am inside state 2");
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3));
            buildbtnPrompt.setText("You dont have money to unmortgage this property");
            buildbtnPrompt.setVisible(true);
            visiblePause.setOnFinished(
                    event -> {
                        buildbtnPrompt.setVisible(false);
                        buildbtnPrompt.setText("Click on a property you own to mortgage it");
                        setAllPanesDisabled();
                        boRailPane.setDisable(true);
                        pennRailPane.setDisable(true);
                        readRailPane.setDisable(true);
                        shortRailPane.setDisable(true);
                        electricPane.setDisable(true);
                        waterPane.setDisable(true);
                        unmortgage = false;
                    });
            visiblePause.play();

        }

        else if (state == 0) {
            System.out.println("i am inside state 0");
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3));
            buildbtnPrompt.setText("Either property is already unmortgaged or you dont own this property");
            buildbtnPrompt.setVisible(true);
            visiblePause.setOnFinished(
                    event -> {
                        buildbtnPrompt.setVisible(false);
                        buildbtnPrompt.setText("Click on a property you own to mortgage it");
                        setAllPanesDisabled();
                        boRailPane.setDisable(true);
                        pennRailPane.setDisable(true);
                        readRailPane.setDisable(true);
                        shortRailPane.setDisable(true);
                        electricPane.setDisable(true);
                        waterPane.setDisable(true);
                        unmortgage = false;
                    });
            visiblePause.play();
        }

    }

    public void changeMortgageState(ImageView imgView, int index) {
        int state = gameManager.mortgageBtnClicked(index);
        if (state == 1) {
            imgView.setImage(null);
            updateBalances();
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3));
            buildbtnPrompt.setText("You have mortgaged the property");
            buildbtnPrompt.setVisible(true);
            visiblePause.setOnFinished(
                    event -> {
                        buildbtnPrompt.setVisible(false);
                        buildbtnPrompt.setText("Click on a property you own to mortgage it");
                        setAllPanesDisabled();
                        mortgage = false;
                    });
            visiblePause.play();
        } else {
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3));
            buildbtnPrompt.setText("You do not own this property. Try again after pressing the Mortgage button!");
            buildbtnPrompt.setVisible(true);
            visiblePause.setOnFinished(
                    event -> {
                        buildbtnPrompt.setVisible(false);
                        buildbtnPrompt.setText("Click on a property you own to mortgage it");
                        setAllPanesDisabled();
                        mortgage = false;
                    });
            visiblePause.play();
        }

    }

    public void changeBuildState(ImageView imgView, int index) {
        int state = gameManager.buildHouse(index);

        if (state == 1) {
            imgView.setImage(new Image("UserInterface/images/housestates/state1.png"));
            build = false;
            updateBalances();
        } else if (state == 2) {
            imgView.setImage(new Image("UserInterface/images/housestates/state2.png"));
            build = false;
            updateBalances();
        } else if (state == 3) {
            imgView.setImage(new Image("UserInterface/images/housestates/state3.png"));
            build = false;
            updateBalances();
        } else if (state == 4) {
            imgView.setImage(new Image("UserInterface/images/housestates/state4.png"));
            build = false;
            updateBalances();
        } else if (state == 5) {
            imgView.setImage(new Image("UserInterface/images/housestates/state5.png"));
            build = false;
            updateBalances();
        } else {
            build = false;
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3));
            buildbtnPrompt.setText("Own or evenly distribute your houses on this property.Press build button to try again ");
            buildbtnPrompt.setVisible(true);
            visiblePause.setOnFinished(
                    event -> {
                        buildbtnPrompt.setVisible(false);
                        buildbtnPrompt.setText("Press on a property that you own on the board to build on it ");
                        setAllPanesDisabled();
                        build = false;
                    });
            visiblePause.play();
        }
    }

    //these are methods that are called when the panes are clicked on the specific property tiles
    //changed
    public void medPaneClicked() {
        if (build == true) {
            changeBuildState(medView, 0);
        } else if (sell == true) {
            changeSellState(medView, 0);
        } else if (mortgage == true) {
            changeMortgageState(medView, 0);
        } else if (unmortgage) {
            changeUnMortgageState(0);
        }
    }

    public void balticPaneClicked() {
        if (build) {
            changeBuildState(balticView, 1);
        } else if (sell) {
            changeSellState(balticView, 1);
        } else if (mortgage) {
            changeMortgageState(balticView, 1);
        } else if (unmortgage) {
            changeUnMortgageState(1);
        }
    }

    public void readingRalePaneClicked() {
        if (mortgage) {
            changeStateUtilsAndRail(2);
        }
        else if (unmortgage) {
            changeUnMortgageState(2);
        }
    }

    public void orientalPaneClicked() {
        if (build) {
            changeBuildState(orientalView, 2);
        } else if (sell) {
            changeSellState(orientalView, 2);
        } else if (mortgage) {
            changeMortgageState(orientalView, 3);
        } else if (unmortgage) {
            changeUnMortgageState(3);
        }
    }

    public void vermontPaneClicked() {
        if (build) {
            changeBuildState(vermontView, 3);
        } else if (sell) {
            changeSellState(vermontView, 3);
        } else if (mortgage) {
            changeMortgageState(vermontView, 4);
        } else if (unmortgage) {
            changeUnMortgageState(4);
        }
    }

    public void connPaneClicked() {
        if (build) {
            changeBuildState(connView, 4);
        } else if (sell) {
            changeSellState(connView, 4);
        } else if (mortgage) {
            changeMortgageState(connView, 5);
        } else if (unmortgage) {
            changeUnMortgageState(5);
        }
    }

    public void charlesPaneClicked() {
        if (build == true) {
            changeBuildState(charlesView, 5);
        } else if (sell == true) {
            changeSellState(charlesView, 5);
        } else if (mortgage) {
            changeMortgageState(charlesView, 6);
        } else if (unmortgage) {
            changeUnMortgageState(6);
        }
    }

    public void electricPaneClicked() {
        if (mortgage) {
            changeStateUtilsAndRail(7);
        } else if (unmortgage) {
            changeUnMortgageState(7);
        }
    }

    public void statesPaneClicked() {
        if (build == true) {
            changeBuildState(statesView, 6);
        } else if (sell == true) {
            changeSellState(statesView, 6);
        } else if (mortgage) {
            changeMortgageState(statesView, 8);
        } else if (unmortgage) {
            changeUnMortgageState(8);
        }
    }

    public void virginiaPaneClicked() {
        if (build == true) {
            changeBuildState(virginiaView, 7);
        } else if (sell == true) {
            changeSellState(virginiaView, 7);
        } else if (mortgage) {
            changeMortgageState(virginiaView, 9);
        } else if (unmortgage) {
            changeUnMortgageState(9);
        }
    }

    public void pennRailPaneClicked() {
        if (mortgage) {
            changeStateUtilsAndRail(10);
        }
        else if (unmortgage) {
            changeUnMortgageState(10);
        }
    }

    public void jamesPaneClicked() {
        if (build == true) {
            changeBuildState(jamesView, 8);
        } else if (sell == true) {
            changeSellState(jamesView, 8);
        } else if (mortgage) {
            changeMortgageState(jamesView, 11);
        } else if (unmortgage) {
            changeUnMortgageState(11);
        }
    }

    public void tennPaneClicked() {
        if (build == true) {
            changeBuildState(tennView, 9);
        } else if (sell == true) {
            changeSellState(tennView, 9);
        } else if (mortgage) {
            changeMortgageState(tennView, 12);
            ;
        } else if (unmortgage) {
            changeUnMortgageState(12);
        }
    }

    public void nyPaneClicked() {
        if (build == true) {
            changeBuildState(nyView, 10);
        } else if (sell == true) {
            changeSellState(nyView, 10);
        } else if (mortgage) {
            changeMortgageState(nyView, 13);
        } else if (unmortgage) {
            changeUnMortgageState(13);
        }
    }

    public void kenPaneClicked() {
        if (build == true) {
            changeBuildState(kenView, 11);
        } else if (sell == true) {
            changeSellState(kenView, 11);
        } else if (mortgage) {
            changeMortgageState(kenView, 14);
        } else if (unmortgage) {
            changeUnMortgageState(14);
        }
    }

    public void indianaPaneClicked() {
        if (build == true) {
            changeBuildState(indianaView, 12);
        } else if (sell == true) {
            changeSellState(indianaView, 12);
        } else if (mortgage) {
            changeMortgageState(indianaView, 15);
        } else if (unmortgage) {
            changeUnMortgageState(15);
        }
    }

    public void illPaneClicked() {
        if (build == true) {
            changeBuildState(illView, 13);
        } else if (sell == true) {
            changeSellState(illView, 13);
        } else if (mortgage) {
            changeMortgageState(illView, 16);
        } else if (unmortgage) {
            changeUnMortgageState(16);
        }
    }

    //other panes only for the mortgage unmortgage buttons
    public void boRailPaneClicked() {
        if (mortgage) {
            changeStateUtilsAndRail(17);
        } else if (unmortgage) {
            changeUnMortgageState(17);
        }

    }

    public void atlanticPaneClicked() {
        if (build == true) {
            changeBuildState(atlanticView, 14);
        } else if (sell == true) {
            changeSellState(atlanticView, 14);
        } else if (mortgage) {
            changeMortgageState(atlanticView, 18);
            ;
        } else if (unmortgage) {
            changeUnMortgageState(18);
        }
    }

    public void ventnorPaneClicked() {
        if (build == true) {
            changeBuildState(ventView, 15);
        } else if (sell == true) {
            changeSellState(ventView, 15);
        } else if (mortgage) {
            changeMortgageState(ventView, 19);
        } else if (unmortgage) {
            changeUnMortgageState(19);
        }
    }

    public void waterPaneClicked() {
        if (mortgage) {
            changeStateUtilsAndRail(20);
        } else if (unmortgage) {
            changeUnMortgageState(20);
        }
    }

    public void marvinPaneClicked() {
        if (build == true) {
            changeBuildState(marvinView, 16);
        } else if (sell == true) {
            changeSellState(marvinView, 16);
            ;
        } else if (mortgage) {
            changeMortgageState(marvinView, 21);
            ;
        } else if (unmortgage) {
            changeUnMortgageState(21);
        }
    }

    public void pacificPaneClicked() {
        if (build == true) {
            changeBuildState(pacificView, 17);
        } else if (sell == true) {
            changeSellState(pacificView, 17);
        } else if (mortgage) {
            changeMortgageState(pacificView, 22);
        } else if (unmortgage) {
            changeUnMortgageState(22);
        }
    }

    public void carolinaPaneClicked() {
        if (build == true) {
            changeBuildState(carolinaView, 18);
        } else if (sell == true) {
            changeSellState(carolinaView, 18);
        } else if (mortgage) {
            changeMortgageState(carolinaView, 23);
        } else if (unmortgage) {
            changeUnMortgageState(23);
        }
    }

    public void pennPaneClicked() {
        if (build == true) {
            changeBuildState(pennView, 19);
        } else if (sell == true) {
            changeSellState(pennView, 19);
        } else if (mortgage) {
            changeMortgageState(pennView, 24);
        } else if (unmortgage) {
            changeUnMortgageState(24);
        }
    }

    public void shortRailPaneClicked() {
        if (mortgage) {
            changeStateUtilsAndRail(25);
        }
        else if(unmortgage) {
            changeUnMortgageState(25);
        }
    }

    public void parkPaneClicked() {
        if (build == true) {
            changeBuildState(parkView, 20);
        } else if (sell == true) {
            changeSellState(parkView, 20);
        } else if (mortgage) {
            changeMortgageState(parkView, 26);
        } else if (unmortgage) {
            changeUnMortgageState(26);
        }
    }

    public void boardPaneClicked() {
        if (build == true) {
            changeBuildState(boardView, 21);
        } else if (sell == true) {
            changeSellState(boardView, 21);
        } else if (mortgage) {
            changeMortgageState(boardView, 27);
        } else if (unmortgage) {
            changeUnMortgageState(27);
        }
    }
}
