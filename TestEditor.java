package editor;

import static org.junit.Assert.*;
import org.junit.Test;
import editor.DataHandler.*;
import editor.LinkedListDeque.*;
import editor.TextBuffer.*;
import editor.UndoRedoBuffer.*;

public class TestEditor {
	@Test
    public void testAdd() {
        LinkedListDeque corpus = new LinkedListDeque();
        corpus.add("H");
        corpus.add("e");
        corpus.add("l");
        corpus.add("l");
        corpus.add("o");
        assertEquals("Hello", corpus.toString());
        corpus.add(" ");
        corpus.add("w");
        corpus.add("o");
        corpus.add("r");
        corpus.add("l");
        corpus.add("d");
        corpus.add("!");
        assertEquals("Hello world!", corpus.toString());
        assertEquals(12, corpus.size(), 0.0);
    }

	@Test
	public void testDeleteBasic() {
		LinkedListDeque corpus = new LinkedListDeque();
		corpus.add("H");
        corpus.add("e");
        corpus.add("l");
        corpus.add("l");
        corpus.add("o");
		assertEquals(5, corpus.size());
		assertEquals("Hello", corpus.toString());
		corpus.delete(corpus.cursor);
		assertEquals("Hell", corpus.toString());
       	assertEquals(4, corpus.size());
		corpus.delete(corpus.cursor);
		assertEquals("Hel", corpus.toString());
	}


	public void testUndoRedo() {
		UndoRedoBuffer ur = new UndoRedoBuffer();
		for (int i = 0; i < 200; i += 1) {
			ur.push(i);
		}
		assertEquals(100, ur.fillCount(), 0.0);
		ur.undo();
		ur.undo();
		ur.undo();
		ur.undo();
		assertEquals(100, ur.fillCount(), 0.0);
		assertEquals(195, (int) ur.get(ur.currentState), 0.0);
		ur.redo();
		ur.redo();
		ur.redo();
		ur.redo();
		assertEquals(100, ur.fillCount(), 0.0);
		assertEquals(199, (int) ur.get(ur.currentState), 0.0);
		ur.undo();
		ur.undo();
		ur.undo();
		ur.undo();
		ur.undo();
		ur.undo();
		ur.push(10000);
	}

    @Test
    public void testUndoRedoMore() {
        UndoRedoBuffer ur = new UndoRedoBuffer();
        ur.push("k");
        ur.push("e");
        ur.push("l");
        ur.push("p");
        ur.push("o");
        System.out.println("undo " + ur.undo());
        System.out.println("redo " + ur.redo());
        System.out.println("undo " + ur.undo());
        System.out.println("undo " + ur.undo());
    }

	public static void main(String[] args) {
		System.exit(jh61b.junit.textui.runClasses(TestEditor.class));
	}
}
