package com.example.musicplayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class PlaylistController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Playlist<String, Song> playlist;
    private ObservableList<String> observableList;
    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ListView<String> listViewer;

    @FXML
    private Button openButton;

    @FXML
    private Button saveButton;


    @FXML
    public void initialize(){
        playlist = new Playlist<>();
    }


    @FXML
    void SwitchSceneHandler(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MusicPlayer.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void deleteSongHandler(ActionEvent event) {
        if(listViewer.getSelectionModel().getSelectedItem() != null){
            String songName = listViewer.getSelectionModel().getSelectedItem();
            playlist.remove(songName);
            observableList = FXCollections.observableArrayList(playlist.getNames());
            listViewer.setItems(observableList);

        }
    }

    @FXML
    void getSongHandler(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select MP3 File");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Audio Files", "*.mp3");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setInitialDirectory(new File("C:\\Downloads"));
        Song song = new Song(fileChooser.showOpenDialog(null));
        if (!song.isNull()) {
            playlist.add(song.getTitleAndName(), song);
            observableList = FXCollections.observableArrayList(playlist.getNames());
            listViewer.setItems(observableList);
        }
    }

    @FXML
    void savePlaylistHandler(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MusicPlayer.fxml"));
        root = loader.load();
        MusicPlayerController MusicPlayerController = loader.getController();
        if(!playlist.isEmpty()){
            MusicPlayerController.setPlaylist(playlist);
        }
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
