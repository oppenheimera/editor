package editor;

import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import editor.LinkedListDeque.*;

public class KeyEventHandler implements EventHandler<KeyEvent> {
    protected static Rectangle textBoundingBox = new Rectangle(1, 13);

    int marginX = 10;
    int marginY = 10;

    static double x = 10.0;
    static double y = 10.0;

    /** The Text to display on the screen. */
    public Text displayText = new Text(250, 250, "");

    protected static int fontSize = 12;

    protected static String fontName = "Verdana";

    protected static double[] cursorFinder() {
        Node pointer = DataHandler.corpus.findBeginning();
        double x = 10.0;
        double y = 10.0;
        double[] coords = new double[2];
        for (int i = 0; i < DataHandler.corpus.size(); i += 1) {
            if (pointer.equals(DataHandler.corpus.cursor)) {
                coords[0] = x;
                coords[1] = y;
                return coords;
            } else if (pointer.item.equals("\n") || x + 20.0 > Editor.screenWidth) { // newline || need to wrap
                x = 10.0;
                y += fontSize;
            }
            Text textObject = new Text(x, y, pointer.item);
            x += textObject.getLayoutBounds().getWidth();
            pointer = pointer.next;
        }
        return null;   
    }

    protected static void moveCursorUpDown(boolean difference) {
        double[] coords = cursorFinder();
        double upperbound = coords[1];
        double lowerbound;
        
        if (difference) {
            lowerbound = coords[1] - fontSize;
        } else {
            lowerbound = coords[1] + fontSize;
        }
        
        Node pointer = DataHandler.corpus.findBeginning();
        double x = 10.0;
        double y = 10.0;
        for (int i = 0; i < DataHandler.corpus.size(); i += 1) {

            if (Editor.debug) {
                System.out.println("Target between: " + lowerbound + ", " + upperbound);
                System.out.println("Our pos: " + x + " , " + y);
            }

            if ((x >= coords[0] - 5) && y <= (upperbound + 1) && y >= (lowerbound - 1)) {
                System.out.println(" !!!!! !!!!! ! ! ! ! ! !  !!!!!!! ! ! ! !!! ");
                DataHandler.corpus.cursor = pointer;
                System.out.println(pointer.item);
            }

            if (pointer.item.equals("\n") || x + 20.0 >= Editor.screenWidth) { // newline || need to wrap
                x = 10.0;
                y += KeyEventHandler.fontSize;
            }
            Text textObject = new Text(x, y, pointer.item);
            x += textObject.getLayoutBounds().getWidth();
            pointer = pointer.next;

            System.out.println(DataHandler.corpus.cursor.item);
        }
        
    }

    public KeyEventHandler(final Group root, int windowWidth, int windowHeight) {

        // Initialize some empty text and add it to root so that it will be displayed.
        displayText = new Text(marginX, marginY, "");

        // should always be set to VPos.TOP
        displayText.setTextOrigin(VPos.TOP);
        displayText.setFont(Font.font (fontName, fontSize));

        // All new Nodes need to be added to the root in order to be displayed.
        root.getChildren().add(displayText);
    }

    public void handleKeyTyped(KeyEvent keyEvent) {
        String characterTyped = keyEvent.getCharacter();

        if (characterTyped.equals("")) {
            return;
        }
        DataHandler.cache.push(characterTyped);
        DataHandler.add(characterTyped);
        Editor.textNodes.getChildren().clear();
        Editor.rerender(DataHandler.corpus, Editor.textNodes, Editor.screenWidth);
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();

        if (code == KeyCode.LEFT) {
            DataHandler.moveCursorLeft();
        } else if (code == KeyCode.RIGHT) {
            DataHandler.moveCursorRight();
        } else if (code == KeyCode.UP) {
            DataHandler.moveCursorUp();
        } else if (code == KeyCode.DOWN){
            DataHandler.moveCursorDown();
        } else if (code == KeyCode.BACK_SPACE) {
            
            DataHandler.cache.undo();
            DataHandler.delete();
        } else if (code == KeyCode.ENTER) {
            DataHandler.add("\n");
        } else if (keyEvent.isShortcutDown()) {
            if (keyEvent.getCode() == KeyCode.EQUALS) {
                fontSize += 5;
                displayText.setFont(Font.font(fontName, fontSize));
            } else if (keyEvent.getCode() == KeyCode.MINUS){
                fontSize -= 5;
                displayText.setFont(Font.font(fontName, fontSize));
            } else if (keyEvent.getCode() == KeyCode.P) {
                double[] a = cursorFinder();
                System.out.println(a[0] + ", " + a[1]);
            } else if (keyEvent.getCode() == KeyCode.S) {
                TextBuffer.saveFile(Editor.title, DataHandler.corpus);
            } else if (code == KeyCode.Y) {
                DataHandler.cacheRedo();
                // DataHandler.cah = (LinkedListDeque) DataHandler.cache.redo();
                // DataHandler.frontEndPointer = DataHandler.corpus.sentinel;
            } else if (code == KeyCode.Z) {
                DataHandler.cacheUndo();

                // DataHandler.frontEndPointer = DataHandler.corpus.sentinel;
            }
        }
        Editor.textNodes.getChildren().clear();
        Editor.rerender(DataHandler.corpus, Editor.textNodes, Editor.screenWidth);
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.getEventType() == KeyEvent.KEY_TYPED && !keyEvent.isShortcutDown()) {
            handleKeyTyped(keyEvent);
        } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
            handleKeyPressed(keyEvent);
        }        
        keyEvent.consume();
    }

    private LinkedListDeque copy(LinkedListDeque list) {
        LinkedListDeque temp = new LinkedListDeque();
        Node pointer = list.sentinel.next;
        for (int i = 0; i < list.size(); i += 1) {
            temp.add(pointer.item);
            if (pointer == list.cursor) {
                temp.cursor = temp.sentinel.prev;
            }
            pointer = pointer.next;
        }
        return temp;
    }

}
