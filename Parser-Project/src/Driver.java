import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.System.exit;

/**
 * Created by xiaoyifan on 11/20/14.
 */
public class Driver {


    public static void main(String[] args) throws IOException {
        File FileIn = new File(args[0]);

        if(args.length<1)
        {
            System.out.println("input with no file");
            exit(0);
        }
        if (FileIn.isDirectory() && FileIn.exists())
        {

            File files[] = FileIn.listFiles();


            if (files != null)
            {
                for (int i=0;i<files.length;i++)
                {
                    String filename = files[i].getAbsolutePath();
                    if (filename.endsWith(".jack"))
                    {
                        String[] splitInputFileName;
                        splitInputFileName = filename.split("\\.");
                        String outName = splitInputFileName[0]+".xml";
                        CompilationEngine engine = new CompilationEngine(filename, outName);
                    }
                }

            }

        }
        else {
            String[] splitInputFileName;
            splitInputFileName = FileIn.getName().split("\\.");
            String outName = splitInputFileName[0]+".xml";

        }

    }



}
