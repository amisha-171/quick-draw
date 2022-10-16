package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;

public abstract class Controller {
    private @FXML ImageView speakerIcon;

    /**
     * Method to toggle the speaker icon in the controller based on the mute/unmute status.
     */
    public void toggleSpeakerIcon() {
        if (App.isBackgroundMusicPlaying()) {
            speakerIcon.setImage(new Image("/images/music.png"));
        } else {
            speakerIcon.setImage(new Image("/images/no-music.png"));
        }
    }
}
