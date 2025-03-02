package org.example;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MatchedProfilePage {
    private int count = 0;

    public void showMatchedProfilePage(Stage stage, String _id, String matchID) {
        File file;
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("matchID", matchID);
        User matchedUserDetails = UsersRouteRequests.getMatchedProfile(_id, jsonMap);
        List<String> pics = matchedUserDetails.getPictures();
        VBox layout = new VBox();
        layout.setPadding(new Insets(15));
        ImageView bigProfileImageView = new ImageView();
        file = new File(pics.get(0));
        Image image;
        try {
            image = ImageUtils.loadCorrectedImage(file);
            bigProfileImageView.setImage(image);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bigProfileImageView.setFitHeight(600);
        bigProfileImageView.setFitWidth(500);
        HBox imageControl = new HBox(15);
        Button prev = new Button("<");
        int pictureListSize = pics.size();
        prev.setOnAction((actionEvent) -> {
            File lastFile = new File(pics.get(pictureListSize - 1));
            if(count != 0){
                File modFile = new File(pics.get(--count%pictureListSize));
                try {
                    bigProfileImageView.setImage(ImageUtils.loadCorrectedImage(modFile));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                
            } else
                try {
                    bigProfileImageView.setImage(ImageUtils.loadCorrectedImage(lastFile));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        });
        Button next = new Button(">");
        next.setOnAction((actionEvent) -> {
            File modFile = new File(pics.get(++count% pictureListSize));
            try {
                bigProfileImageView.setImage(ImageUtils.loadCorrectedImage(modFile));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        prev.setAlignment(Pos.BOTTOM_CENTER);
        next.setAlignment(Pos.BOTTOM_CENTER);
        imageControl.getChildren().addAll(prev ,bigProfileImageView ,next);
        imageControl.setAlignment(Pos.TOP_CENTER);
        VBox contentDetails = new VBox();
        Label name = new Label(String.format("Name:   %s", matchedUserDetails.getFirstName()));
        Label age = new Label(String.format("Age:   %d", matchedUserDetails.getAge()));

        contentDetails.getChildren().addAll(name, age);
        contentDetails.setAlignment(Pos.CENTER);
        
        layout.getChildren().addAll(imageControl,contentDetails);
        Scene scene = new Scene(layout, 700, 700);
        stage.setScene(scene);
        
    }
    


}
