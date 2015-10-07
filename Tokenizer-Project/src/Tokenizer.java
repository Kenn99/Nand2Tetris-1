import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Character.isDigit;

/**
 * Created by xiaoyifan on 11/16/14.
 */
public class Tokenizer {

    private String FileName;
    private String outName;


    private final String[] keyword = {"class", "method", "function", "constructor", "int", "boolean", "char", "void", "var",
            "static", "field", "let", "do", "if", "else", "while", "return", "true", "false", "null", "this"};
    private final char[] symbol = {'{', '}', '(', ')', '[', ']', '.', ',', ';', '+', '-', '*', '/', '&', '|', '<', '>', '=', '~'};

    public Tokenizer(String FileName) {

        this.FileName = FileName;

        String[] splitInputFileName;
        splitInputFileName = FileName.split("\\.");

        this.outName = splitInputFileName[0]+"T.xml";

    }
    //initialize the toakenizer with the file name and the generate the output filename

    public void generateXML() throws FileNotFoundException
    {
        int commentExist = 0;
        File inputFile = new File(FileName);
        Scanner in = new Scanner(inputFile);

        PrintWriter out = new PrintWriter(outName);

        out.printf("<token>\n");

        while(in.hasNextLine())
        {

            String Processing = in.nextLine().trim();

            if (Processing.length() != 0)
            {
                String noComment = stripDoubleSlashComment(Processing);

                if (noComment.length()!=0) //when the code is not blank after getting rid of the comment
                {

                    if (noComment.contains("/*") && noComment.contains("*/"))
                    {
                        int d = noComment.indexOf("/*");
                        int e = noComment.indexOf("*/");
                        String noComment1 = noComment.substring(0,d);
                        noComment1 += " "+ noComment.substring(e+2);
                        noComment = noComment1;

                    }
                    if (noComment.contains("/*"))
                      {
                          int d = noComment.indexOf("/*");
                          commentExist = 1;
                          noComment = noComment.substring(0,d);
                      }

                      if (noComment.contains("*/"))
                      {
                          int d = noComment.indexOf("*/");
                          commentExist = 0;
                          noComment = noComment.substring(d+2);
                      }
                      else if (commentExist == 1)
                      {
                          continue;
                      }

                    noComment = noComment.trim();

                    if (noComment.length()!=0)
                    {
                        //ALL THE COMMENT SHOULD BE FREE
                        String[] result = separate(noComment);
                        tokenize(result,out);
                    }

                }


            }
        }

        out.printf("</token>\n");

        in.close();
        out.close();

    }

    public static String stripDoubleSlashComment(String inputLine){

        int flag = 0;

        String outString="";

        for (int i=0;i<inputLine.length();i++)
        {

            char c = inputLine.charAt(i);
            if (i < inputLine.length() - 1)
            {
                char cNext = inputLine.charAt(i + 1);
                //read the next two char
                if (c == '/' && cNext == '/') {
                    flag = 1;

                }//if there is a that the remain of this string is comment, set the flag to 1
            }

            if (flag==0){

                outString+=c;
            }
        }

        outString = outString.trim();
        return outString;
    }


    public String[] separate(String command)
    {
        command = handleQuotes(command);
        command = command.replaceAll("\\s+", " ");
        String[] thisLine = command.split(" ");
        ArrayList<String> list = new ArrayList<String>();
        for (int i=0;i<thisLine.length;i++)
        {
            if (thisLine[i].isEmpty())
                continue;

            boolean containSymbol = false;

            String token = thisLine[i];

            for (int count = 0;count<symbol.length;count++)
            {
                if (token.equals(symbol[count]))
                {
                    break;
                    //will go to the add line, and add the symbol to the ArrayList
                }
                else if (token.contains(""+symbol[count]))
                {
                    containSymbol = true;
                    int index = 0;

                    for (int j = 0; j < token.length(); j++) {
                        if (token.charAt(j) == '\"') {
                            j++;
                            while (token.charAt(j) != '\"')
                                j++;
                            String sub = token.substring(index, j + 1);
                            j++;

                            sub = sub.replace("#s@", " ");
                            list.add(sub);

                            index = j;

                        } else {
                            for (int count2 = 0; count2 < symbol.length; count2++)
                            {
                                if (token.charAt(j) == symbol[count2])
                                {
                                    if (index != j)
                                        list.add(token.substring(index, j));

                                    list.add("" + symbol[count2]);
                                    index = j + 1;
                                }
                            }
                        }
                        if (index < token.length() && j == token.length() - 1)
                            list.add(token.substring(index, token.length()));
                    }
                    break;

                }

            }

            if (containSymbol == false)
            {
                list.add(token);
            }

        }

        String[] transfer = new String[list.size()];
        for (int i=0;i<list.size();i++)
        {
            transfer[i] = list.get(i);
        }


       return transfer;
    }

    public void tokenize(String[] tok, PrintWriter out)
    {
        for (int i=0;i<tok.length;i++)
        {
            String type = getType(tok[i]);
            if (type.equals("symbol"))
            {
                 if (tok[i].equals("<"))
                    tok[i] = "&lt;";
                else if (tok[i].equals(">"))
                     tok[i]  = "&gt;";
                 else if (tok[i].equals("&"))
                     tok[i] = "&amp;";

            }
            out.printf("<"+type+">");
            out.printf(tok[i]);
            out.printf("</"+type+">\n");
        }

    }

    public String getType(String token)
    {
         for (char ch: symbol)
         {

             if (token.equals(ch+""))
             {
                 return "symbol";
             }
         }

        for (String str: keyword)
        {
            if (token.equals(str))
            {
                return "keyword";
            }
        }

       int flag = 0;
       for (int i=0 ;i<token.length();i++)
       {
           if (!Character.isDigit(token.charAt(i)))
           {
               flag =1;
           }
       }

        if (flag == 0)
        {
            return "integerConstant";
        }

       if (token.charAt(0) == '"' && token.charAt(token.length()-1) == '"')
       {
           return "StringConstant";
       }

       boolean id = isFormalLabel(token);
        if (id)
        {
            return "identifier";

        }

      return null;
    }

    public static boolean isFormalLabel(String value)
    {
        for (int i=0;i<value.length();i++)
        {
            char ch = value.charAt(i);
            if (!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_'))
            {
                return false;
            }
        }

        if (isDigit(value.charAt(0)))
        {
            return false;
        }

        return true;
    }

    public String handleQuotes(String s) {
        String tmp = s;
        int c;
        if((c=s.indexOf("\"")) != -1) {
            tmp = "";
            c++;
            tmp += s.substring(0, c);
            char d;
            while((d=s.charAt(c)) != '\"') {
                if(d != ' ')
                    tmp += d;
                else
                    tmp += "#s@";
                c++;
            }
            if(d == '\"') {
                tmp += "\"";
                c++;
                String tmp2 = s.substring(c, s.length());
                tmp += handleQuotes(tmp2);
            }
            else
                tmp += s;
        }
        return tmp;
    }


}
