package editor;

import editor.DataHandler.*;
import editor.LinkedListDeque.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextBuffer {

	/* takes in a linked list containing text objects, and parses
	 * those objects into a string to be read */
	protected static LinkedListDeque openFile(String filename) {
        LinkedListDeque corpus = new LinkedListDeque();

        try { 
            File inputFile = new File(filename);

            String s =  inputFile.toString();
            String[] sty = s.split("");
            int len = sty.length;
            String type = "";
            
            if (len > 4) {
                type = sty[len-4] + sty[len-3] + sty[len-2] + sty[len-1];    
            }
            

            if (!inputFile.exists() && type.equals(".txt")) {
                Editor.title = filename;
                FileWriter writer = new FileWriter(filename);
                writer.write("");
                writer.close();
            }

            FileReader reader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(reader);          

            int intRead = -1;

            while ((intRead = bufferedReader.read()) != -1) {
                char charRead = (char) intRead;
                String cr = Character.toString(charRead);
                corpus.add(cr);
            }

            bufferedReader.close();

        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File not found! Exception was: " + fileNotFoundException);
            return null;
        } catch (IOException ioException) {
            System.out.println("Error when copying; exception was: " + ioException);
        }

        return corpus;
	}
    protected static void saveFile(String filename, LinkedListDeque list) {
        try {
            if (filename == "") {
                filename = "Untitled, Unmastered";
            }

            FileWriter writer = new FileWriter(filename);
            writer.write(list.toString());
            writer.close();

        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File not found! Exception was: " + fileNotFoundException);
        } catch (IOException ioException) {
            System.out.println("Error when copying; exception was: " + ioException);
        }
    }
}

