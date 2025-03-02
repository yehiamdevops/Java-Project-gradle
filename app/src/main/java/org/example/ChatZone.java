package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChatZone {

    private static TextArea chatArea; // Displays messages
    private static TextField messageInput; // User input
        private HBox inputBox;
        private static Text alertMessage;
        private Button sendButton; // Send button
        public static Socket socket;
        public static BufferedReader reader;
        public static PrintWriter writer;
        private static String userId;
        private String matchId;
        private static  String finalMessage;
        private static  String finalstr;
        public static Thread listenerThread;
    
        public void showChatZone(Stage stage, String matchId, String _userId) {
            this.matchId = matchId;
            userId = _userId;
            
            // UI Components
            if (chatArea == null)
                chatArea = new TextArea();
            chatArea.setEditable(false);
            chatArea.setWrapText(true);
    
            messageInput = new TextField();
            sendButton = new Button("Send");
    
            // Handle sending messages
            sendButton.setOnAction(e -> {
                try {
                    sendMessage();
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            });

            if (alertMessage == null) {
                
                alertMessage = new Text();
            }
            alertMessage.setText(null);
            // Layout
            inputBox = new HBox(10, messageInput, sendButton, alertMessage);
            inputBox.setPadding(new Insets(10));
    
            Button backToMainScreen = new Button("back to main screen");
            backToMainScreen.setOnAction((actionEvent) -> {
             
                MainPage mp = new MainPage();
                mp.showMainPage(stage, _userId);
            });
            BorderPane layout = new BorderPane();
            layout.setCenter(chatArea);
            layout.setBottom(inputBox);
            layout.setTop(backToMainScreen);
            Scene scene = new Scene(layout, 400, 500);
            scene.setOnKeyPressed ((keyEvent) -> {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    try {
                        sendMessage();
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
            }
            } );
            stage.setScene(scene);
            stage.setTitle("Chat");
            stage.show();
    
            // Connect to server
            
        }
    
        public static void connectToServer() {
            try {
                if (socket == null ||socket.isClosed()) {
                    
                    socket = new Socket("127.0.0.1", 4000); // Replace with actual server details
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(socket.getOutputStream(), true);
                    
                }
                // Start a thread to listen for incoming messages
                 
                 if (listenerThread == null || !listenerThread.isAlive()) {
                    listenerThread = new Thread(ChatZone::listenForMessages);
                    listenerThread.setDaemon(true); // Optional: ensures it stops when the app closes
                    listenerThread.start();
                }
                // Send initial message to identify user
    
                System.out.println("thread test: " + listenerThread);
            } catch (IOException e) {
                e.printStackTrace();
                chatArea.appendText("Failed to connect to chat server.\n");
            }
        }
    
        private static void listenForMessages() {
            System.out.println("new thread");
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    finalMessage = message; // Effectively final
                    System.out.println(finalMessage);
                    // if (finalMessage.contains(":append")) {
                    //     finalstr = messageInput.getText().trim();
                    //     chatArea.appendText("Me| "+ finalstr +"\n");
                    //     messageInput.clear();
                    // }
                    if (finalMessage.equals("Unallowed")) {
                        LoginWindow.SocketLogin("Unallowed");
                        
                    }
                    else if(finalMessage.contains("Allowed|")){
                        LoginWindow.SocketLogin("allowed");

                    }
                    if (finalMessage.contains("Unmatched|")){
                        String userid = finalMessage.split("userid:")[1].trim();
                        MatchesPage.cleanCurrentUnmatch(userid);
                    }
                    if (finalMessage.contains("Error|")) {
                        System.out.println("test-current");
                        String errorMessage = finalMessage.split("\\|")[1]; // New variable
                        Platform.runLater(()-> messageInput.clear());
                    
                    Platform.runLater(()->{
                        alertMessage.setText(errorMessage);
                        alertMessage.setFill(Color.RED);
                    });
                       
                    

                }
                

                else{
                        if (finalMessage.contains("online")) {
                            Platform.runLater(() ->{ 
                                if (alertMessage != null) {
                                    alertMessage.setFill(Color.GREEN);
                                    alertMessage.setText(finalMessage);

                                
                                }   
                                
                                chatArea.appendText("Me|"+messageInput.getText().trim() + "\n");
                                messageInput.clear();
                            });
                            
                            
                        }
                    else if(finalMessage.contains("approved") ){
                    
                        finalstr = finalMessage.split(":")[0].trim();
                        Platform.runLater(() ->{ 
                            chatArea.appendText(finalstr + "\n");
                        
                    
                    });

                    }
                   
                
                }
            }
            System.out.println("end-while-test");
        }
        catch (IOException e) {
            closeConnection();
        }
    }

    private void sendMessage() throws InterruptedException {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            
            writer.println("MESSAGE| "  + userId + "input: "+ message + "| matchid:" + matchId );

            
            // if(finalMessage.contains("online")){
            // writer.println("MESSAGE| "  + userId + "input:" + message + "| matchid:" + matchId );
            // // chatArea.appendText("Me| " + message + "\n");
            // messageInput.clear();
            
            
            // }
            // else {
            //     writer.println("MESSAGE| "  + userId + "input:" + message + "| matchid:" + matchId );
              
            // }

        }
    }

    public static void closeConnection() {
        try {
            if (writer != null) writer.println("DISCONNECT:" + userId);
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
