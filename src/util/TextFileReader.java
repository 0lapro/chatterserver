package chatterserver.src.util;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class can be used for reading a text file.
 *
 * @author Ola
 */
public class TextFileReader {

    String fileToRead;

    public TextFileReader(String fileLocation) {
        fileToRead = fileLocation;
    }

    /**
     * This method is used for opening the file to read. FileReader is meant for
     * reading streams of characters. BufferedReader reads text from a
     * character-input stream.
     *
     * @return array of texts
     */
    public String[] openFile() {
        FileReader txtFileReader = null;
        String[] text = null;
        try {
            txtFileReader = new FileReader(fileToRead);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (BufferedReader textReader = new BufferedReader(txtFileReader)) {
            int lines = readLines();
            text = new String[lines];
            for (int i = 0; i < lines; i++) {
                text[i] = textReader.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(TextFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return text;
    }//end method openFile

    /**
     * @return the number of text lines.
     */
    private int readLines() throws IOException {
        FileReader fileReader = new FileReader(fileToRead);
        int linesOfTxt = 0;
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while (bufferedReader.readLine() != null) {
                linesOfTxt++;
            }
        }
        return linesOfTxt;
    }
}
