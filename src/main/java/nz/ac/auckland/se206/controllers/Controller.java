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

    /**
     * This method mutes or unmutes the main background music and will also toggle the mute icon
     * symbol accordingly based on the state of the music.
     */
    @FXML
    private void onToggleMute() {
        App.toggleMusicPlaying();
    }
}
