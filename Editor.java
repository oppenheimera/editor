package editor;

import javafx.scene.input.MouseEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.ScrollBar;
import javafx.geometry.Orientation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import editor.LinkedListDeque.*;
import java.util.List;

public class Editor extends Application {
    public static boolean debug = false;
    public static double screenWidth = 500;
    public static double screenHeight = 500;
    public static Group squad;
    public static Group textNodes = new Group();
    protected static Rectangle textBoundingBox = new Rectangle(1, KeyEventHandler.fontSize);
    protected static String title = "";
    protected static double layout = 0;
    protected static double min = 0;
    protected static double max;
    
    private static double textLength;

    protected static void grerender(LinkedListDeque list, Group root, double width) {
        double x = 10.0;
        double y = 10.0;
        Node pointer = list.findBeginning();

        for (int i = 0; i < DataHandler.corpus.size(); i += 1) {
            if (pointer.item.equals("\n") || x + 30.0 > width) { // newline || need to wrap
                x = 10.0;
                y += KeyEventHandler.fontSize * 1.3;
                textLength = y;
            }
            
            Text textObject = new Text(x, y, pointer.item);
            textObject.setTextOrigin(VPos.TOP);
            textObject.setX(x);
            textObject.setY(y);
            textObject.setFont(Font.font(KeyEventHandler.fontName, KeyEventHandler.fontSize));
            root.getChildren().add(textObject);

            if (pointer == list.cursor) {
                addCursor(textBoundingBox, x, y, textObject.getLayoutBounds().getWidth());
            } 

            x += textObject.getLayoutBounds().getWidth();
            pointer = pointer.next;
            textObject.setWrappingWidth(width - 20);
        }
    }
    

    protected static void rerender(LinkedListDeque list, Group root, double width) {
        double x = 10.0;
        double y = 10.0;
        double wordLength = 0.0;
        Node pointer = list.findBeginning();

        Group word = new Group();
        root.getChildren().add(word);

        for (int i = 0; i < DataHandler.corpus.size(); i += 1) {
            Text textObject = new Text(x, y, pointer.item);
            textObject.setTextOrigin(VPos.TOP);
            textObject.setFont(Font.font(KeyEventHandler.fontName, KeyEventHandler.fontSize));
            if (pointer == list.cursor || pointer == null) {
                addCursor(textBoundingBox, x, y, textObject.getLayoutBounds().getWidth());
            }
            word.getChildren().add(textObject);
            x += textObject.getLayoutBounds().getWidth();

            if (pointer.item.equals(" ")) {
                word = new Group();
                root.getChildren().add(word);
            }

            if (x > width - 30) {  // need to wrap
                x = 10;
                y += KeyEventHandler.fontSize * 1.3;
                for (javafx.scene.Node node : word.getChildren()) {
                    node.relocate(x, y);
                    x += textObject.getLayoutBounds().getWidth();
                }
                textLength = y;
            } else if (pointer.item.equals("\n")) { // newline
                x = 10;
                y += KeyEventHandler.fontSize * 1.3;
                word = new Group();
                root.getChildren().add(word);
            }
            
            if (debug) {
                System.out.println("x: " + x + ", y: " + y);
                System.out.println("wl: " + wordLength);
            }
            pointer = pointer.next;
            // scrollBar.setMin(min);
            // scrollBar.setMax(textLength - screenHeight);
        }
    }


    // an event handler for mousey events
    private class MouseClickEventHandler implements EventHandler<MouseEvent> {
        /** A Text object that will be used to print the current mouse position. */
        MouseClickEventHandler(Group root) {}

        @Override
        public void handle(MouseEvent mouseEvent) {
            // Because we registered this EventHandler using setOnMouseClicked, it will only called
            // with mouse events of type MouseEvent.MOUSE_CLICKED.  A mouse clicked event is
            // generated anytime the mouse is pressed and released on the same JavaFX node.
            double mousePressedX = mouseEvent.getX();
            double mousePressedY = mouseEvent.getY();
            if (debug) {
                System.out.println(mousePressedX + ", " + mousePressedY);
            }
        }
    }

    protected static void addCursor(Rectangle box, double x, double y, double size) {
        // textBoundingBox = new Rectangle(1, KeyEventHandler.fontSize);
        box.setFill(Color.BLACK);
        box.setX(x + size);
        // textBoundingBox.setHeight(KeyEventHandler.fontSize);
        box.setY(y);
    }
    
    // Cursor Stuff
    private class RectangleBlinkEventHandler implements EventHandler<ActionEvent> {
        private int currentColorIndex = 0;
        private Color[] boxColors =
                {Color.BLACK, Color.WHITE};
        RectangleBlinkEventHandler() {
            changeColor();
        }
        private void changeColor() {
            textBoundingBox.setFill(boxColors[currentColorIndex]);
            currentColorIndex = (currentColorIndex + 1) % boxColors.length;
        }
        @Override
        public void handle(ActionEvent event) {
            changeColor();
        }
    }

    public void makeRectangleColorChange() {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        RectangleBlinkEventHandler cursorChange = new RectangleBlinkEventHandler();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), cursorChange);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
    // Cursor Stuff

    protected void readerHelper(List parameters) {
        int listSize = parameters.size();

        if (listSize == 1) {
            String filename = parameters.get(0).toString();
            title += filename;
            DataHandler.corpus = TextBuffer.openFile(filename);
            rerender(DataHandler.corpus, textNodes, screenWidth);
        } else if (listSize == 2) {
            String filename = parameters.get(0).toString();
            title += filename;
            DataHandler.corpus = TextBuffer.openFile(filename);
            rerender(DataHandler.corpus, textNodes, screenWidth);
            if (parameters.get(1).equals("debug")) {
                debug = true;
                System.out.println("Welcome to debug mode hey hi how are you doing");
                System.out.println(DataHandler.corpus.toString());
                System.out.println("Done printing file.");
            }
        }    
    }   

    @Override
    public void start(Stage primaryStage) {
        // Create a Node that will be the parent of all things displayed on the screen.
        // String[] params = String[2];
        java.util.List<String> parameters = (List<String>) getParameters().getRaw();

        Group root = new Group();
        squad = root;
        root.getChildren().add(textNodes);

        // The Scene represents the window: its height and width will be the height and width
        // of the window displayed.
        int windowWidth = 500; screenWidth = 500;
        int windowHeight = 500;
        int MARGIN = 10;
        Scene scene = new Scene(root, windowWidth, windowHeight, Color.WHITE);

        ScrollBar scrollBar = new ScrollBar();
        double scrollBarWidth = scrollBar.getLayoutBounds().getWidth();

        readerHelper(parameters); // delegating reading work

        // To get information about what keys the user is pressing, create an EventHandler.
        // EventHandler subclasses must override the "handle" function, which will be called
        // by javafx.
        EventHandler<KeyEvent> keyEventHandler = 
            new KeyEventHandler(root, windowWidth, windowHeight);
        // Register the event handler to be called for all KEY_PRESSED and KEY_TYPED events.
        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(keyEventHandler);

        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.setPrefHeight(windowHeight);
        root.getChildren().add(scrollBar); // adds scrollbar to scene

        double usableScreenWidth = windowWidth - scrollBar.getLayoutBounds().getWidth();
        scrollBar.setLayoutX(usableScreenWidth);

        // Set the range of the scroll bar.
        scrollBar.setMin(0);
        scrollBar.setMax(textLength + 20 - screenHeight);
        
        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed (
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                int dif = newValue.intValue() - oldValue.intValue(); // -dif = up; +dif = down
                textNodes.setLayoutY(-newValue.doubleValue());
                if (debug) {
                    System.out.println("Min: " + min);
                    System.out.println("Max: " + max);
                    System.out.println("Dif: " + dif);
                }
                 
            }
        });

        // scrolly resize DOWN
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed (
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenWidth,
                    Number newScreenWidth) {
                double usableScreenWidth = newScreenWidth.intValue()
                    - scrollBar.getLayoutBounds().getWidth();
                screenWidth = usableScreenWidth;
                root.getChildren().clear();
                root.getChildren().add(scrollBar);
                scrollBar.setLayoutX(usableScreenWidth);
                rerender(DataHandler.corpus, root, usableScreenWidth);
                scrollBar.setMin(0);
                scrollBar.setMax(textLength + 20 - screenHeight);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed (
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenHeight,
                    Number newScreenHeight) {
                int windowHeight = newScreenHeight.intValue();
                scrollBar.setMin(0);
                scrollBar.setMax(textLength - newScreenHeight.intValue());
                scrollBar.setPrefHeight(windowHeight);
                screenHeight = newScreenHeight.intValue();
            }
        });
        // scrolly resize UP

        //broken because of opening and rendering
        
        root.getChildren().add(textBoundingBox);
        textBoundingBox.setX(10);
        textBoundingBox.setY(10);
        
        
        makeRectangleColorChange();
        // sets mouse clicker
        scene.setOnMouseClicked(new MouseClickEventHandler(root));

        primaryStage.setTitle("Editor");

        // This is boilerplate, necessary to setup the window where things are displayed.
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
