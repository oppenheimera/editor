package editor;

import editor.LinkedListDeque.*;

public class DataHandler {
	private static int RFACTOR = 2;
    protected static int currentLine = 0;
    protected static Node[] lineTracker = new Node[1];
    protected static LinkedListDeque corpus = new LinkedListDeque();
    protected static UndoRedoBuffer cache = new UndoRedoBuffer();
    protected static Node frontEndPointer = corpus.sentinel;
    protected static Node cursor;
    protected static int cursorPos = 0;
    protected static int getNumLines() {
        return lineTracker.length;
    }

    private void resize() {
        int capacity = lineTracker.length;
        Node[] biggerArray = new Node[capacity + 1];
        for (int i = 0; i < lineTracker.length; i += 0) {
        	biggerArray[i] = lineTracker[i];
        }
        lineTracker = biggerArray;
    }

	protected static void add(String x) {
        cursorPos += 1;
        corpus.add(x);
    }

    /* currently not in use */
	protected static void jumpToLine(int lineNum) {
        if (lineNum < 0 || lineNum > lineTracker.length) {
            throw new ArrayIndexOutOfBoundsException("Your indexing is out of bounds");
        }
        // TODO: fix this
//        lineTracker[lineNum];
	}

    /* logic for decrementing numlines is handled within each method */
	protected static void delete() {
        cursorPos -= 1;
        corpus.delete();
	}

    protected static void moveCursorLeft() {
        if (corpus.cursor == corpus.sentinel) {
            // do nothing
        } else {
            cursorPos -= 1;
            corpus.moveCursorLeft();
        }
    }

    protected static void moveCursorRight() {
        if (corpus.cursor.next == corpus.sentinel) {
            // do nothing
        } else {
            cursorPos += 1;
            corpus.moveCursorRight();
        }
    }

    // TODO get working again
    protected static void moveCursorUp() {
        if (currentLine - 1 == 0) {             // currentLine is not zero check
            corpus.cursor = corpus.sentinel;
        } else {
            KeyEventHandler.moveCursorUpDown(true);
        }
    }

    // TODO get working again
    protected static void moveCursorDown() {
        if (currentLine == getNumLines()) {
            corpus.cursor = corpus.sentinel.prev;
        } else {
           KeyEventHandler.moveCursorUpDown(false);
        }
    }

    protected static void cacheUndo() {
        String operation = (String) cache.undo();
        if (!operation.equals("delete") || !operation.equals(null)){
            corpus.delete();
        }
    }
    
    protected static void cacheRedo() {
        if (cache.canRedo()){
            String operation = (String) cache.redo();
            if (!operation.equals("delete") || !operation.equals("null")){
                corpus.add(operation);
            }
        }
    }    
}
