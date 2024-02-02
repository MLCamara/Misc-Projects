package project.bouncingball;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class BouncingBallController implements Initializable {

    @FXML
    private AnchorPane scene;
    @FXML
    private Circle circle;


    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(4.17), new EventHandler<>() {
        double deltaX = 2;
        double deltaY = 2;

        @Override
        public void handle(ActionEvent actionEvent) {
            circle.setLayoutX(circle.getLayoutX() + deltaX);
            circle.setLayoutY(circle.getLayoutY() + deltaY);

            Bounds bounds = scene.getLayoutBounds();
            boolean rightBorder = circle.getLayoutX() >= (bounds.getMaxX() - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (bounds.getMinX() + circle.getRadius());
            boolean topBorder = circle.getLayoutY() <= (bounds.getMinY() + circle.getRadius());
            boolean bottomBorder = circle.getLayoutY() >= (bounds.getMaxY() - circle.getRadius());

            if (rightBorder || leftBorder) {
                deltaX *= -1;
            }

            if (bottomBorder || topBorder) {
                deltaY *= -1;
            }

        }
    }));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Random  random = new Random();
        circle.setLayoutX(random.nextInt((int) (0+circle.getRadius()),(int) (600 - circle.getRadius())));
        circle.setLayoutY(random.nextInt((int) (0+ circle.getRadius()),(int)(400 - circle.getRadius())));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

}

