package com.example.musicplayer;
import javafx.application.Platform;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class UiThread implements Runnable{
    private long minutes;
    private long seconds;

    private long totalMinutes;
    private long totalSeconds;

    private Duration currentDuration;

    private MusicPlayerController MusicPlayerController;

    public UiThread(MusicPlayerController controller){
        MusicPlayerController = controller;
    }

    @Override
    public void run() {

        if(MusicPlayerController.mediaPlayer == null){
            return;
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(this::setTotalDuration);

        while (MusicPlayerController.mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
            Platform.runLater(() ->{
                updateSlider();
                setCurrentDuration();
            });

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void setTotalDuration(){
        totalMinutes = (long) MusicPlayerController.mediaPlayer.getTotalDuration().toMinutes();
        totalSeconds = (long) (MusicPlayerController.mediaPlayer.getTotalDuration().toSeconds() % 60);
        MusicPlayerController.totalDurationLabel.setText(String.format("%02d:%02d",totalMinutes,totalSeconds));
        MusicPlayerController.seekSlider.setMax(MusicPlayerController.mediaPlayer.getTotalDuration().toMillis());
    }

    private void setCurrentDuration(){
        currentDuration = MusicPlayerController.mediaPlayer.getCurrentTime();
        minutes = (long) currentDuration.toMinutes();
        seconds = (long) (currentDuration.toSeconds() % 60);
        MusicPlayerController.currentDurationLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateSlider(){
        if (!MusicPlayerController.seekSlider.isValueChanging()) {
            MusicPlayerController.seekSlider.adjustValue(MusicPlayerController.mediaPlayer.getCurrentTime().toMillis());
        }
    }
}
