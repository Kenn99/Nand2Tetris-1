import java.io.FileNotFoundException;

/**
 * Created by xiaoyifan on 11/16/14.
 */
public class Driver {

    public static void main(String[] args) throws FileNotFoundException {

        Tokenizer tok = new Tokenizer("Main.jack");
        try {
            tok.generateXML();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
