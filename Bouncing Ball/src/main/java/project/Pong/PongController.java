package project.Pong;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

public class PongController implements Initializable {

    @FXML
    private AnchorPane scene;
    @FXML
    private VBox vbox;
    @FXML
    private Circle circle;
    @FXML
    private Rectangle lPong;
    @FXML
    private Rectangle rPong;
    @FXML
    private Label rScoreLabel;
    @FXML
    private Label lScoreLabel;
    private int lScore = 0;

    private int rScore = 0;
    private double aiSpeed = 5;


    private Timeline timeline = new Timeline(new KeyFrame(Duration.millis(4.17), new EventHandler<>() {
        double deltaX = 3;
        double deltaY = 3;

        private void pongHit() {
            if (circle.getBoundsInParent().intersects(lPong.getBoundsInParent())) {
                Random random = new Random();
                deltaX = random.nextDouble(3, 7);
                if(deltaY < 0){
                    deltaY = -random.nextDouble(0, 5);
                } else{
                    deltaY = random.nextDouble(0, 5);
                }
            }
            if (circle.getBoundsInParent().intersects(rPong.getBoundsInParent())) {
                Random random = new Random();
                deltaX = -random.nextDouble(3, 7);
                if(deltaY < 0){
                    deltaY = -random.nextDouble(0, 5);
                } else{
                    deltaY = random.nextDouble(0, 5);
                }
            }
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            pongHit();
            circle.setLayoutX(circle.getLayoutX() + deltaX);
            circle.setLayoutY(circle.getLayoutY() + deltaY);

            Bounds bounds = vbox.getLayoutBounds();
            boolean rightBorder = circle.getLayoutX() >= (bounds.getMaxX() - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (bounds.getMinX() + circle.getRadius());
            boolean topBorder = circle.getLayoutY() <= (bounds.getMinY() + circle.getRadius());
            boolean bottomBorder = circle.getLayoutY() >= (bounds.getMaxY() - circle.getRadius());

            if (rightBorder) {
                ++lScore;
                String score = "" + lScore;
                lScoreLabel.setText(score);
                resetBall();
                deltaX = new Random().nextDouble(2,3);
                deltaY = new Random().nextDouble(0,3);
            }

            if (leftBorder) {
                ++rScore;
                String score = "" + rScore;
                rScoreLabel.setText(score);
                resetBall();
                deltaX = new Random().nextDouble(2,3);
                deltaY = new Random().nextDouble(0,3);
            }

            if (bottomBorder || topBorder) {
                deltaY *= -1;
            }

        }
    }));

    @FXML
    void move(MouseEvent event){
        double newY = event.getY() - lPong.getHeight()/2; // Adjusting for the center of the paddle

        // Ensure the new Y-coordinate is within the scene bounds
        if (newY >= 0 && lPong.getBoundsInParent().getMaxY()  < scene.getBoundsInParent().getMaxY()) {
            lPong.setTranslateY(newY);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        resetBall();
        Timeline aiTimeline = new Timeline(new KeyFrame(Duration.millis(16), new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                moveAI();
            }
        }));

        aiTimeline.setCycleCount(Animation.INDEFINITE);
        aiTimeline.play();
    }


    protected void resetBall(){
        Random random = new Random();
        Bounds bounds = scene.getBoundsInParent();
        circle.setLayoutX(bounds.getCenterX());
        circle.setLayoutY(random.nextInt((int) (0+ circle.getRadius()),(int)(bounds.getMaxY() - circle.getRadius())));

    }

    protected void moveAI() {
        Bounds sceneBounds = scene.getBoundsInParent();
        double minY = sceneBounds.getMinY();
        double maxY = sceneBounds.getMaxY() - rPong.getHeight();

        // Introduce a chance for the AI to make a mistake
        Random random = new Random();

        if(rPong.getBoundsInParent().getMaxY() <= maxY && rPong.getBoundsInParent().getMinY() >= minY){
            rPong.setTranslateY(circle.getBoundsInParent().getCenterY());
        } else{
            rPong.setTranslateY(rPong.getTranslateY() - 8);
        }
    }

}
