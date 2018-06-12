package browser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SaveToFile {

    private File plik;
    private PrintWriter saver;

    public SaveToFile(String fileName){
        this.plik = new File(fileName);
        try {
            this.saver = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save(String text){
        saver.println(text);
    }

    public void close(){
        saver.close();
    }


}
