package com.example.musicplayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MusicPlayerController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    protected MediaPlayer mediaPlayer;
    private Media media;
    private ArrayList<Song> savedPlaylist;

    private int playlistIndex;
    private Song song;

    private final Image SPEAKER_ICON = new Image(getClass().getResourceAsStream("SpeakerIcon.png"));
    private final Image MUTE_ICON =  new Image(getClass().getResourceAsStream("MuteIcon.png"));
    private final Image PLAY_IMAGE = new Image(getClass().getResourceAsStream("play.png"));
    private final Image PAUSE_IMAGE = new Image(getClass().getResourceAsStream("pause.png"));
    @FXML
    private Button fastForwardButton;
    @FXML
    private ImageView fastForwardImage;
    @FXML
    private ImageView musicImage;
    @FXML
    private ImageView playPauseImage;
    @FXML
    private ImageView volumeIcon;
    @FXML
    private Button playPauseButton;
    @FXML
    private Button rewindButton;
    @FXML
    private Button playlistButton;
    @FXML
    private Button loadSongButton;
    @FXML
    private Button speakerButton;
    @FXML
    private ImageView rewindImage;
    @FXML
    protected Label currentDurationLabel;
    @FXML
    protected Label totalDurationLabel;
    @FXML
    protected Slider seekSlider;
    @FXML
    private Slider volumeSlider;

    @FXML
    private Label titleLabel;
    @FXML
    private Label artistLabel;
    @FXML
    private MenuItem loadSong;
    private boolean seekDragged;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        volumeIcon.setImage(SPEAKER_ICON);
        playPauseImage.setImage(PLAY_IMAGE);
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(mediaPlayer != null){
                    mediaPlayer.setVolume(volumeSlider.getValue()/100.00);
                    if(volumeSlider.getValue() == 0.0 && volumeIcon.getImage().equals(SPEAKER_ICON)){
                        volumeIcon.setImage(MUTE_ICON);
                        mediaPlayer.setMute(true);
                    } else if(volumeSlider.getValue() > 0.0 && mediaPlayer.isMute()){
                        volumeIcon.setImage(SPEAKER_ICON);
                        mediaPlayer.setMute(false);
                    }
                }
            }
        });

        seekSlider.setOnDragDetected(new EventHandler() {
            @Override
            public void handle(Event event) {
                seekDragged =true;
            }
        });

        seekSlider.setOnMouseReleased(new EventHandler() {
            @Override
            public void handle(Event event) {
                if(seekDragged && mediaPlayer != null){
                    mediaPlayer.seek(new Duration (seekSlider.getValue()));
                    seekDragged =false;
                }
            }
        });

    }

    protected void setPlaylist(Playlist playlist){
        if(playlist != null || !playlist.isEmpty()){
            savedPlaylist = new ArrayList<Song>(playlist.getSongs());
            song = savedPlaylist.getFirst();
            media = new Media(song.toURItoString());
            mediaPlayer = new MediaPlayer(media);
            updateMusicLabels();
        }
    }
    private void updateMusicLabels(){
        titleLabel.setText(song.getTitle());
        artistLabel.setText(song.getArtist());
        if(playPauseImage.getImage().equals(PLAY_IMAGE)){
            playPauseImage.setImage(PAUSE_IMAGE);
        }
        if(volumeIcon.getImage().equals(MUTE_ICON)){
            mediaPlayer.setMute(true);
        }
        mediaPlayer.setVolume(volumeSlider.getValue()/100.00);
        mediaPlayer.play();
        updateUiThread();
    }

    protected void play(){
        if(mediaPlayer != null){
            if(playPauseImage.getImage().equals(PLAY_IMAGE)) {
                playPauseImage.setImage(PAUSE_IMAGE);
                mediaPlayer.play();
                updateUiThread();
            } else{
                mediaPlayer.pause();
                playPauseImage.setImage(PLAY_IMAGE);
            }
        }
    }
    @FXML
    void fastForwardHandler(ActionEvent event) {
        if( mediaPlayer != null){

            if(savedPlaylist == null){
                mediaPlayer.seek(Duration.ZERO);
                return;
            }

            mediaPlayer.stop();
            playlistIndex++;

            if(playlistIndex >= savedPlaylist.size()){
                playlistIndex = 0;
                updateSongInPlaylist();
                updateMusicLabels();
            } else if(playlistIndex < savedPlaylist.size()){
                updateSongInPlaylist();
                updateMusicLabels();
            }
        }

    }
    private void updateSongInPlaylist(){
        mediaPlayer.stop();
        song = savedPlaylist.get(playlistIndex);
        media = new Media(song.toURItoString());
        mediaPlayer = new MediaPlayer(media);
    }
    @FXML
    void playPauseHandler(ActionEvent event) {
        this.play();
    }

    @FXML
    void rewindHandler(ActionEvent event) {

        if( mediaPlayer == null){
            return;
        }

        if(savedPlaylist == null){
            mediaPlayer.seek(Duration.ZERO);
            return;
        }

        mediaPlayer.stop();
        playlistIndex--;

        if(playlistIndex < 0){
            playlistIndex = savedPlaylist.size()-1;
            updateSongInPlaylist();
            updateMusicLabels();
        } else if(playlistIndex < savedPlaylist.size()){
            updateSongInPlaylist();
            updateMusicLabels();
        }
    }

    @FXML
    void switchSceneHandler(ActionEvent event) throws IOException {
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
        root = FXMLLoader.load(getClass().getResource("playlist.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void LoadSongHandler(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Song");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Audio Files", "*.mp3");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setInitialDirectory(new File("C:\\Downloads"));
        song = new Song(fileChooser.showOpenDialog(null));

        if (song.isNull()) {
            return;
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                playPauseImage.setImage(PLAY_IMAGE);
            }

            seekSlider.adjustValue(0.0);
            media = new Media(song.toURItoString());
            mediaPlayer = new MediaPlayer(media);
            updateMusicLabels();

        }
    }

    @FXML
    void changeSpeakerIcon(ActionEvent event) {
        if(mediaPlayer != null) {
            if (volumeIcon.getImage().equals(SPEAKER_ICON)) {
                volumeIcon.setImage(MUTE_ICON);
                mediaPlayer.setMute(true);
            } else if (volumeIcon.getImage().equals(MUTE_ICON)) {
                volumeIcon.setImage(SPEAKER_ICON);
                mediaPlayer.setMute(false);
                if(volumeSlider.getValue() == 0.0){
                    volumeSlider.setValue(100.00);
                }
            }
        }
    }

    private void updateUiThread(){
        new Thread(new UiThread(this)).start();
    }

}
