import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.System.exit;
import static java.lang.System.in;
import static java.lang.System.setOut;

/**
 * Created by xiaoyifan on 11/16/14.
 */
public class CompilationEngine {


    private int indentNumber;

    private Tokenizer tok;
    private PrintWriter writer;

    CompilationEngine(String inFile, String outFile) throws IOException
    {
            tok = new Tokenizer(inFile);
            writer = new PrintWriter(outFile);
            indentNumber = 0;



            CompileClass();

        writer.close();
    }

    public String createIndent(int n)
    {
        String indent = "";
        for (int i=0;i<n;i++)
        {
            indent+=" ";
        }
        return indent;
    }

    public void CompileClass() throws IOException {
        tok.nextToken();
        writer.printf("<class>\n");
        System.out.println("<class>\n");

        indentNumber++;

        if (tok.getCurrentToken().equals("class"))
        {
            String type = tok.getType("class");
            writer.printf(createIndent(indentNumber)+"<"+type+"> " + "class" +" </"+type+">\n");
            System.out.println(createIndent(indentNumber)+"<"+type+">" + "class" +"</"+type+">");
        }
        else{
            System.out.println("suppose to have a class but input with a "+tok.getCurrentToken()+"\n");
            exit(1);
        }

        tok.nextToken();
        String current = tok.getCurrentToken();
        if (tok.getType(current).equals("identifier"))
        {
            writer.printf(createIndent(indentNumber)+"<identifier> " + current +" </identifier>\n");
            System.out.println(createIndent(indentNumber)+"<identifier> " + current +" </identifier>");
        }
        else{
            System.out.println("suppose to met a identifier, but actually met: "+current);
            exit(1);
        }
        current="";

        tok.nextToken();
        if (tok.getCurrentToken().equals("{"))
        {
            writer.printf(createIndent(indentNumber)+"<symbol> " + tok.getCurrentToken() +" </symbol>\n");
            System.out.println(createIndent(indentNumber)+"<symbol> " + tok.getCurrentToken() +" </symbol>");
        }
        else{
            System.out.println("suppose to met a {, but actually met: "+tok.getCurrentToken());
            exit(1);
        }

        tok.nextToken();
        current = tok.getCurrentToken();
        while (tok.getCurrentToken().equals("static") || tok.getCurrentToken().equals("field"))
        {
            //no nextToken here, the nextToken will goes on in the subRoutine
            CompileClassVarDec();
            //write this method later
        }


        current = tok.getCurrentToken();
        while (tok.getCurrentToken().equals("constructor") || tok.getCurrentToken().equals("function") || tok.getCurrentToken().equals("method"))
        {
            //write this code later
            CompileSubroutine();
            tok.nextToken();
        }


        tok.nextToken();
        if (tok.getCurrentToken().equals("}"))
        {
            writer.printf("<symbol> } </symbol>\n");
            System.out.println("<symbol> } </symbol>");
        }
        else{
            System.out.println("suppose to met a {, but met a: "+ tok.getCurrentToken());
            exit(1);
        }

        indentNumber--;

        writer.printf("</class>\n");
        System.out.println("</class>");


    }

    public void CompileClassVarDec() throws IOException {
        writer.printf(createIndent(indentNumber) + "<classVarDec>\n");
        System.out.println(createIndent(indentNumber) + "<classVarDec>");
        //output the label of the Var

        indentNumber++;

        if (tok.getCurrentToken().equals("static") || tok.getCurrentToken().equals("field")) {
            writer.printf(createIndent(indentNumber) + "<keyword> " + tok.getCurrentToken() + " </keyword>\n");
            System.out.println(createIndent(indentNumber) + "<keyword> " + tok.getCurrentToken() + " </keyword>");
        } else {
            System.out.println("suppose to met a 'field' or 'static' , but met a: " + tok.getCurrentToken());
            exit(1);
        }


        tok.nextToken();
        String current = tok.getCurrentToken();
        if (current.equals("int") || current.equals("char") || current.equals("boolean")) {
            writer.printf(createIndent(indentNumber) + "<keyword> " + current + " </keyword>\n");
            System.out.println(createIndent(indentNumber) + "<keyword> " + current + " </keyword>");
        } else if (tok.getType(current).equals("identifier")) {
            writer.printf(createIndent(indentNumber) + "<identifier> " + current + " </identifier>\n");
            System.out.println(createIndent(indentNumber) + "<identifier> " + current + " </identifier>");
        } else {
            System.out.println("suppose to met a primitive or class type, but met a: " + current);
            exit(1);
        }


        tok.nextToken();
        current = tok.getCurrentToken();
        if (tok.getType(current).equals("identifier")) {
            writer.printf(createIndent(indentNumber) + "<identifier> " + current + " </identifier>\n");
            System.out.println(createIndent(indentNumber) + "<identifier> " + current + " </identifier>");
        } else {
            System.out.println("suppose to met a identifier, but met a: " + current);
            exit(1);
        }


        tok.nextToken();
        while ((!tok.getCurrentToken().equals("#")) && (tok.getCurrentToken().equals(","))) {
            writer.printf(createIndent(indentNumber) +"<symbol> , </symbol>\n");
            System.out.println(createIndent(indentNumber) +"<symbol> , </symbol>");

            tok.nextToken();
            current = tok.getCurrentToken();
            if (tok.getType(current).equals("identifier"))
            {
                writer.printf(createIndent(indentNumber) +"<identifier> "+current+" </identifier>\n");
                System.out.println(createIndent(indentNumber) +"<identifier> "+current+" </identifier>");

            } else {
                System.out.println("suppose to met a identifier, but met a: " + current);
            }

            tok.nextToken();
         }



        if (tok.getCurrentToken().equals(";"))
        {
            writer.printf(createIndent(indentNumber) +"<symbol> ; </symbol>\n");
            System.out.println(createIndent(indentNumber) +"<symbol> ; </symbol>");
        }
        else{
            System.out.println("suppose to met a identifier, but met a ; ");

        }

        tok.nextToken();
        indentNumber--;
        writer.printf(createIndent(indentNumber)+"</classVarDec>\n");
        System.out.println(createIndent(indentNumber)+"</classVarDec>");

    }

    public void CompileSubroutine() throws IOException
    {
        writer.printf(createIndent(indentNumber) + "<subroutineDec>\n");
        System.out.println(createIndent(indentNumber) + "<subroutineDec>");

        indentNumber++;

        String current = tok.getCurrentToken();
        if (current.equals("constructor") || current.equals("function") || current.equals("method"))
        {
            writer.printf(createIndent(indentNumber)+ "<keyword> "+current+" </keyword>\n");
            System.out.println(createIndent(indentNumber)+ "<keyword> "+current+" </keyword>");
        }
        else{
            System.out.println("suppose to me a 'constructor' or a 'function' or a 'method' but met a: "+current);
            exit(1);
        }
        //Subroutine type

        tok.nextToken();
        current = tok.getCurrentToken();
        if (current.equals("void") || current.equals("int") || current.equals("char")|| current.equals("boolean"))
        {
            writer.printf(createIndent(indentNumber)+"<keyword> "+current+" </keyword>\n");
            System.out.println(createIndent(indentNumber)+"<keyword> "+current+" </keyword>");

        }
        else if (tok.getType(current).equals("identifier"))
        {
            writer.printf(createIndent(indentNumber) +"<identifier> "+current+" </identifier>\n");
            System.out.println(createIndent(indentNumber) +"<identifier> "+current+" </identifier>");

        }
        else{
            System.out.println("suppose to me a 'constructor' or a 'function' or a 'method' but met a: "+current);
            exit(1);
        }
        //find the subroutine return type

        tok.nextToken();
        current = tok.getCurrentToken();
        if (tok.getType(current).equals("identifier"))
        {
            writer.printf(createIndent(indentNumber)+ "<identifier> "+current+" </identifier>\n");
            System.out.println(createIndent(indentNumber)+ "<identifier> "+current+" </identifer>");

        }
        else{
            System.out.println("suppose to me a 'constructor' or a 'function' or a 'method' but met a: "+current);
            exit(1);
        }
        //find the subRountine name

        tok.nextToken();
        if (tok.getCurrentToken().equals("("))
        {
            writer.printf(createIndent(indentNumber)+ "<symbol> ( </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> ( </symbol>");

        }
        else {
            System.out.println("suppose to me a ( but met a: "+ tok.getCurrentToken());
            exit(1);
        }
        //compile the (

        tok.nextToken();
        CompileParameterList();
        //compile the parameterList


        current = tok.getCurrentToken();

        if (tok.getCurrentToken().equals(")"))
        {
            writer.printf(createIndent(indentNumber)+ "<symbol> ) </symbol>\n");
            System.out.println(createIndent(indentNumber)+ "<symbol> ) </symbol>");

        }
        else {
            System.out.println("suppose to me a ) but met a: "+ tok.getCurrentToken());
            exit(1);
        }
        //compile the )


        //Compile the subroutine body and in format like " { varDec statements } "
        writer.printf(createIndent(indentNumber) + "<subroutineBody>\n");
        System.out.println(createIndent(indentNumber) + "<subroutineBody>");
        indentNumber++;

        tok.nextToken();
        if (tok.getCurrentToken().equals("{"))
        {
            writer.printf(createIndent(indentNumber)+ "<symbol> { </symbol>\n");
            System.out.println(createIndent(indentNumber)+ "<symbol> { </symbol>");

        }
        else {
            System.out.println("suppose to me a { but met a: "+ tok.getCurrentToken());
            exit(1);
        }
        //compile the {

        tok.nextToken();
        while(tok.getCurrentToken().equals("var"))
        {
            CompileVarDec();
            //Compile the varirables in the subroutine, not the paramaters
            tok.nextToken();
        }
        //compile all the variables

        CompileStatement();
       //the main statements of the subroutine body

        if (tok.getCurrentToken().equals("}"))
        {
            writer.printf(createIndent(indentNumber)+ "<symbol> } </symbol>\n");
            System.out.println(createIndent(indentNumber)+ "<symbol> } </symbol>");

        }
        else {
            System.out.println("suppose to me a } but met a: "+ tok.getCurrentToken());
            exit(1);
        }
        //compile the }

        indentNumber--;
        writer.printf(createIndent(indentNumber)+"</subroutineBody>\n");
        System.out.println(createIndent(indentNumber)+"</subroutineBody>");

        indentNumber--;
        writer.printf(createIndent(indentNumber)+"</subroutineDec>\n");
        System.out.println(createIndent(indentNumber)+"</subroutineDec>");
    }

    public void CompileParameterList() throws IOException
    {
      writer.printf(createIndent(indentNumber) + "<parameterList>\n");
        System.out.println(createIndent(indentNumber) + "<parameterList>");

        indentNumber++;

        String current = tok.getCurrentToken();
        if (tok.getType(current).equals("keyword") || tok.getType(current).equals("identifier"))
        {
                if (tok.getType(current).equals("keyword") && (current.equals("int") || current.equals("char") || current.equals("boolean")) )
                {
                    writer.printf(createIndent(indentNumber)+ "<keyword> " +current + " </keyword>\n");
                    System.out.println(createIndent(indentNumber)+ "<keyword> " +current + " </keyword>");
                }
                else if (tok.getType(current).equals("identifier"))
                {
                    writer.printf(createIndent(indentNumber)+"<keyword> "+current+" </keyword>\n");
                    System.out.println(createIndent(indentNumber)+"<keyword> "+current+" </keyword>");
                }
                else
                {
                    System.out.println("suppose to met a variable, but met a: "+current);
                    exit(1);
                }


            tok.nextToken();
            current = tok.getCurrentToken();

            if (tok.getType(current).equals("identifier"))
            {
                writer.printf(createIndent(indentNumber) + "<identifier> "+ current +" </identifier>\n");
                System.out.println(createIndent(indentNumber) + "<identifier> " + current + " </identifier>");
            }
            else {
                System.out.println("suppose to met a variable, but met a: "+current);
                exit(1);
            }


            tok.nextToken();
            while (tok.getCurrentToken().equals(","))
            {
                writer.printf(createIndent(indentNumber) + "<symbol> , </symbol>\n");
                System.out.println(createIndent(indentNumber)+ "<symbol> , </symbol>");


                tok.nextToken();
                current = tok.getCurrentToken();
                if (current.equals("int") || current.equals("char") || current.equals("boolean"))
                {
                    writer.printf(createIndent(indentNumber) + "<keyword> "+ current +" </keyword>\n");
                    System.out.println(createIndent(indentNumber) + "<keyword> " + current + " </keyword>");
                }
                else if(tok.getType(current).equals("identifier"))
                {
                    writer.printf(createIndent(indentNumber) + "<identifier> "+ current +" </identifier>\n");
                    System.out.println(createIndent(indentNumber) + "<identifier> " + current + " </identifier>");
                }
                else{
                    System.out.println("suppose to met a variable, but met a: "+current);
                    exit(1);
                }

                tok.nextToken();
                current = tok.getCurrentToken();
                if (tok.getType(current).equals("identifier"))
                {
                    writer.printf(createIndent(indentNumber) + "<identifier> "+ current +" </identifier>\n");
                    System.out.println(createIndent(indentNumber) + "<identifier> " + current + " </identifier>");
                }
                else{
                    System.out.println("suppose to met a variable, but met a: " + current);
                    exit(1);
                }

                tok.nextToken();

            }




        }

        indentNumber--;
        writer.printf(createIndent(indentNumber)+ "</parameterList>\n");
        System.out.println(createIndent(indentNumber)+ "</parameterList>");
    }

    public void CompileVarDec() throws IOException
    {
        writer.printf(createIndent(indentNumber)+"<varDec>\n");
        System.out.println(createIndent(indentNumber)+"<varDec>");

        indentNumber++;

        //keyword Var
        if (tok.getCurrentToken().equals("var"))
        {
            writer.printf(createIndent(indentNumber)+"<keyword> var </keyword>\n");
            System.out.println(createIndent(indentNumber)+"<keyword> var </keyword>");
        }
        else
        {
            System.out.println("suppose to met a keyword, but met a: " + tok.getCurrentToken());
            exit(1);
        }


        //type
        tok.nextToken();
        String current = tok.getCurrentToken();
        if (tok.getCurrentToken().equals("int") || tok.getCurrentToken().equals("char") || tok.getCurrentToken().equals("boolean"))
        {
            writer.printf(createIndent(indentNumber) + "<keyword> "+current+" </keyword>\n");
            System.out.println(createIndent(indentNumber) + "<keyword> "+current+" </keyword>");
        }
        else if (tok.getType(current).equals("identifier"))
        {
            writer.printf(createIndent(indentNumber) + "<identifier> "+ current +" </identifier>\n");
            System.out.println(createIndent(indentNumber) + "<identifier> " + current + " </identifier>");
        }
        else
        {
                System.out.println("suppose to met a variable type, but met a: "+tok.getCurrentToken());
                exit(1);

        }


        //varName
        tok.nextToken();
        current = tok.getCurrentToken();
        if (tok.getType(current).equals("identifier"))
        {

            writer.printf(createIndent(indentNumber) + "<identifier> "+ tok.getCurrentToken() + " </identifier>\n");
            System.out.println(createIndent(indentNumber) + "<identifier> " + tok.getCurrentToken() + " </identifier>");
        }
        else{
            System.out.println("suppose to met identifier, but met a: "+tok.getCurrentToken());
            exit(1);
        }

        //', '
        tok.nextToken();
        current = tok.getCurrentToken();
        while (current.equals(","))
        {
            writer.printf(createIndent(indentNumber)+"<symbol> , </symbol>\n");
            System.out.println(createIndent(indentNumber)+"<symbol> , </symbol>");


            tok.nextToken();
            current = tok.getCurrentToken();
            if (tok.getType(current).equals("identifier"))
            {
                writer.printf(createIndent(indentNumber) + "<identifier> " + tok.getCurrentToken() + " </identifier>\n");
                System.out.println(createIndent(indentNumber) + "<identifier> " + tok.getCurrentToken() + " </identifier>");
            }
            else{
                System.out.println("suppose to met a identifier, but met a: "+ current);
                exit(1);
            }


            tok.nextToken();
            current = tok.getCurrentToken();

        }
        //(',', VarName)*

        if (tok.getCurrentToken().equals(";"))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> ; </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> ; </symbol>");

        }
        else{
            System.out.println("suppose to met a symbol ';', but met a: "+ current);
            exit(1);
        }

        indentNumber--;
        writer.printf(createIndent(indentNumber) + "</varDec>\n");
        System.out.println(createIndent(indentNumber) + "</varDec>");


    }

    public void CompileStatement() throws IOException
    {
        writer.printf(createIndent(indentNumber)+ "<statement>\n");
        System.out.println(createIndent(indentNumber)+ "<statement>");

        indentNumber++;

        String current  = tok.getCurrentToken();
        while(current.equals("let") || current.equals("if") ||current.equals("while") ||current.equals("do") ||current.equals("return"))
        {
         if (current.equals("let"))
         {
             CompileLet();
             tok.nextToken();
         }
         else if(current.equals("while"))
         {
             CompileWhile();
             tok.nextToken();
         }
         else if(current.equals("if"))
         {
             CompileIf();

         }
         else if(current.equals("do"))
         {
             CompileDo();
             tok.nextToken();
         }
         else if (current.equals("return"))
         {
             CompileReturn();
             tok.nextToken();
         }
          else {
             System.out.println("suppose to met 'let','if','do','return','while', but met a "+current);
             exit(1);
         }

            current = tok.getCurrentToken();
        }

        indentNumber--;
        writer.printf(createIndent(indentNumber)+ "</statement>\n");
        System.out.println(createIndent(indentNumber)+ "</statement>");
    }

    public void CompileDo() throws IOException
    {
         writer.printf(createIndent(indentNumber) + "<doStatement>\n");
         System.out.println(createIndent(indentNumber) + "<doStatement>");

        indentNumber++;

        if (tok.getCurrentToken().equals("do"))
        {
            writer.printf(createIndent(indentNumber) + "<keyword> do </keyword>\n");
            System.out.println(createIndent(indentNumber) + "<keyword> do </keyword>");
        }
        else{
            System.out.println("suppose to met a keyword 'do', but met a: " +tok.getCurrentToken());
            exit(1);
        }


        //subRoutine call, subRoutine name
        tok.nextToken();
        String current = tok.getCurrentToken();
        if (tok.getType(current).equals("identifier"))
        {
            writer.printf(createIndent(indentNumber) + "<identifier> "+ current + " </identifier>\n");
            System.out.println(createIndent(indentNumber) + "<identifier> " + current + " </identifier>");
        }
        else{
            System.out.println("suppose to met a identifier, but met a: " +tok.getCurrentToken());
            exit(1);
        }


        //(
        tok.nextToken();
        if (tok.getCurrentToken().equals("("))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> ( </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> ( </symbol>");
        }
        //.
        else if(tok.getCurrentToken().equals("."))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> . </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> . </symbol>");


            //subRoutine name
            tok.nextToken();
            current = tok.getCurrentToken();
            if (tok.getType(current).equals("identifier"))
            {
               writer.printf(createIndent(indentNumber) + "<identifier> " + current + " </identifier>\n");
                System.out.println(createIndent(indentNumber) + "<identifier> " + current + " </identifier>");

            }
            else{
                System.out.println("suppose to met s identifier, but met a: "+current);
                exit(1);
            }

            //(
            tok.nextToken();
            if (tok.getCurrentToken().equals("("))
            {
                writer.printf(createIndent(indentNumber) + "<symbol> ( </symbol>\n");
                System.out.println(createIndent(indentNumber) + "<symbol> ( </symbol>");
            }
            else
            {
                System.out.println("suppose to met a symbol'(', but met a: "+tok.getCurrentToken());
                exit(1);
            }

         }
         else
         {
              System.out.println("suppose to met a '(' or a '.', but met a: "+current);
              exit(1);
         }

      tok.nextToken();

      CompileExpressionList();


      //)
      if (tok.getCurrentToken().equals(")"))
      {
          writer.printf(createIndent(indentNumber) + "<symbol> ) </symbol>\n");
          System.out.println(createIndent(indentNumber) + "<symbol> ) </symbol>");
      }
      else{

          System.out.println("suppose to met a symbol ')', but met a: "+tok.getCurrentToken());
          exit(1);
      }


        tok.nextToken();
        if (tok.getCurrentToken().equals(";"))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> ; </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> ; </symbol>");
        }
        else{
            System.out.println("suppose to met a symbol ';', but met a: "+tok.getCurrentToken());
            exit(1);

        }

        indentNumber--;
        writer.printf(createIndent(indentNumber) + "</doStatement>\n");
        System.out.println(createIndent(indentNumber) + "</doStatement>");

    }

    public void CompileLet() throws IOException
    {
        boolean array = false;

        writer.printf(createIndent(indentNumber) + "<letStatement>\n");
        System.out.println(createIndent(indentNumber) + "<letStatement>");

        indentNumber++;

        if (tok.getCurrentToken().equals("let"))
        {
            writer.printf(createIndent(indentNumber) + "<keyword> let </keyword>\n");
            System.out.println(createIndent(indentNumber) + "<keyword> let </keyword>");
        }
        else{
            System.out.println("suppose to met a keyword 'let', but met a: "+tok.getCurrentToken());
            exit(1);
        }

        //var's name
        tok.nextToken();
        String current = tok.getCurrentToken();
        if (tok.getType(current).equals("identifier"))
        {
            writer.printf(createIndent(indentNumber) + "<identifier> " + current + " </identifier>\n");
            System.out.println(createIndent(indentNumber) + "<identifier> " + current + " </identifier>");
        }
        else{
            System.out.println("suppose to met a identifier, but met a: "+current);
            exit(1);
        }


        //[
        tok.nextToken();
        if (tok.getCurrentToken().equals("["))
        {
            array = true;
            writer.printf(createIndent(indentNumber) + "<symbol> [ </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> [ </symbol>");

            tok.nextToken();
            CompileExpression();


            if (tok.getCurrentToken().equals("]"))
            {
                writer.printf(createIndent(indentNumber) + "<symbol> ] </symbol>\n");
                System.out.println(createIndent(indentNumber) + "<symbol> ] </symbol>");
            }
            else{
                System.out.println("suppose to met a symbol']', but met a: "+tok.getCurrentToken());
                exit(1);
            }

            tok.nextToken();
        }



        //=
        if (tok.getCurrentToken().equals("="))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> = </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> = </symbol>");

            if (array)
            {
                tok.nextToken();
                CompileExpression();
            }
            else{
                tok.nextToken();
                CompileExpression();
            }


        }
        else{
            System.out.println("suppose to met a symbol '=', but met a: "+tok.getCurrentToken());
            exit(1);
        }


        //;
        if (tok.getCurrentToken().equals(";"))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> ; </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> ; </symbol>");
        }
        else{
            System.out.println("suppose to met a symbol';', but met a: "+tok.getCurrentToken());
            exit(1);
        }

        indentNumber--;
        writer.printf(createIndent(indentNumber) + "</letStatement>\n");
        System.out.println(createIndent(indentNumber) + "</letStatement>");

    }

    public void CompileWhile() throws IOException
    {
        writer.printf(createIndent(indentNumber) + "<whileStatement>\n");
        System.out.println(createIndent(indentNumber) + "<whileStatement>\n");

        indentNumber++;

        //while
        if (tok.getCurrentToken().equals("while"))
        {
            writer.printf(createIndent(indentNumber)+"<keyword> while </keyword>\n");
            System.out.println(createIndent(indentNumber)+"<keyword> while </keyword>");
        }
        else
        {
            System.out.println("suppose to met keyword 'while', but met a: "+ tok.getCurrentToken());
            exit(1);
        }

        //(
        tok.nextToken();
        if (tok.getCurrentToken().equals("("))
        {
            writer.printf(createIndent(indentNumber)+ "<symbol> ( </symbol>\n");
            System.out.println(createIndent(indentNumber)+ "<symbol> ( </symbol>");
        }
        else{
            System.out.println("suppose to met symbol '(', but met a: "+ tok.getCurrentToken());
            exit(1);
        }

        //CompileExpression
        tok.nextToken();
        CompileExpression();


        //)
        if (tok.getCurrentToken().equals(")"))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> ) </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> ) </symbol>");
        }
        else{
            System.out.println("suppose to met a symbol ')', but met a: "+tok.getCurrentToken());
            exit(1);
        }

        //{
        tok.nextToken();
        if (tok.getCurrentToken().equals("{"))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> { </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> { </symbol>");
        }
        else{
            System.out.println("suppose to met a symbol '{', but met a: "+tok.getCurrentToken());
            exit(1);
        }

        tok.nextToken();
        CompileStatement();


        if (tok.getCurrentToken().equals("}"))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> } </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> } </symbol>");
        }
        else{
            System.out.println("suppose to met a symbol '}', but met a: "+tok.getCurrentToken());
            exit(1);
        }


        indentNumber++;
        writer.printf(createIndent(indentNumber)+"</whileStatement>\n");
        System.out.println(createIndent(indentNumber)+"</whileStatement>\n");


    }

    public void CompileReturn() throws IOException
    {
        writer.printf(createIndent(indentNumber)+ "<returnStatement>\n");
        System.out.println(createIndent(indentNumber)+ "<returnStatement>");

        indentNumber++;

        //return
        if (tok.getCurrentToken().equals("return"))
        {
            writer.printf(createIndent(indentNumber) + "<keyword> return </keyword>\n");
            System.out.println(createIndent(indentNumber) + "<keyword> return </keyword>");
        }
        else{
            System.out.println("suppose to met a keyword 'return', but met a: "+tok.getCurrentToken());
            exit(1);
        }


        tok.nextToken();
        String current = tok.getCurrentToken();
        String type = tok.getType(current);
        if (type.equals("integerConstant") || type.equals("StringConstant") || type.equals("keyword") ||
            type.equals("identifier") || (type.equals("symbol") && (type.equals("(") || type.equals("-") ||type.equals("~"))))
        {
            CompileExpression();
        }


        if (tok.getCurrentToken().equals(";"))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> ; </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> ; </symbol>");
        }
        else{
            System.out.println("suppose to met a symbol ';', but met a: "+tok.getCurrentToken());
            exit(1);
        }


        indentNumber--;
        writer.printf(createIndent(indentNumber) + "</returnStatement>\n");
        System.out.println(createIndent(indentNumber) + "</returnStatement>");

    }

    public void CompileIf() throws IOException
    {
        writer.printf(createIndent(indentNumber)+"<ifStatement>\n");
        System.out.println(createIndent(indentNumber)+"<ifStatement>");

        indentNumber++;

        if (tok.getCurrentToken().equals("if"))
        {
            writer.printf(createIndent(indentNumber) + "<keyword> if </keyword>\n");
            System.out.println(createIndent(indentNumber) + "<keyword> if </keyword>");
        }
        else{
            System.out.println("suppose to met a keyword 'if', but met a: "+tok.getCurrentToken());
            exit(1);
        }

        //(
        tok.nextToken();
        if (tok.getCurrentToken().equals("("))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> ( </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> ( </symbol>");
        }
        else{
            System.out.println("suppose to met a symbol '(', but met a: "+tok.getCurrentToken());
            exit(1);
        }

        tok.nextToken();
        CompileExpression();


        //)
        if (tok.getCurrentToken().equals(")"))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> ) </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> ) </symbol>");
        }
        else{
            System.out.println("suppose to met a symbol ')', but met a: "+tok.getCurrentToken());
            exit(1);
        }

        tok.nextToken();
        if (tok.getCurrentToken().equals("{"))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> { </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> { </symbol>");
        }
        else{
            System.out.println("suppose to met a symbol '{', but met a: "+tok.getCurrentToken());
            exit(1);
        }

        tok.nextToken();
        CompileStatement();


        if (tok.getCurrentToken().equals("}"))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> } </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> } </symbol>");
        }
        else{
            System.out.println("suppose to met a symbol '}', but met a: "+tok.getCurrentToken());
            exit(1);
        }


        tok.nextToken();
        if (tok.getCurrentToken().equals("else"))
        {
            writer.printf(createIndent(indentNumber) + "<keyword> else </keyword>\n");
            System.out.println(createIndent(indentNumber) + "<keyword> else </keyword>");

            tok.nextToken();
            if (tok.getCurrentToken().equals("{"))
            {
                writer.printf(createIndent(indentNumber) + "<symbol> { </symbol>\n");
                System.out.println(createIndent(indentNumber) + "<symbol> { </symbol>");
            }
            else{
                System.out.println("suppose to met a symbol '{', but met a: "+tok.getCurrentToken());
                exit(1);
            }


            tok.nextToken();
            CompileStatement();

            if (tok.getCurrentToken().equals("}"))
            {
                writer.printf(createIndent(indentNumber) + "<symbol> } </symbol>\n");
                System.out.println(createIndent(indentNumber) + "<symbol> } </symbol>");
            }
            else{
                System.out.println("suppose to met a symbol '}', but met a: "+tok.getCurrentToken());
                exit(1);
            }

            tok.nextToken();

        }

           indentNumber--;
        writer.printf(createIndent(indentNumber)+ "</ifStatement>\n");
        System.out.println(createIndent(indentNumber)+ "</ifStatement>");

    }

    public void CompileExpression() throws IOException
    {
       writer.printf(createIndent(indentNumber) + "<expression>\n");
        System.out.println(createIndent(indentNumber) + "<expression>");

        indentNumber++;

        CompileTerm();

        String current = tok.getCurrentToken();
        while(current.equals("+") ||current.equals("-") ||current.equals("*") ||current.equals("/") ||current.equals("&") ||
                current.equals("|") ||current.equals("<") ||current.equals(">") ||current.equals("="))
        {
            String outputCode="";
            if (current.equals("<"))
             outputCode = "&lt;";
            else if (current.equals(">"))
                outputCode = "&gt;";
            else if (current.equals("&"))
                outputCode = "&amp;";
            else
                outputCode = current;

            writer.printf(createIndent(indentNumber) + "<symbol> "+ outputCode+" </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> "+ outputCode+" </symbol>");

            tok.nextToken();
            CompileTerm();

            current = tok.getCurrentToken();
        }

        indentNumber--;
        writer.printf(createIndent(indentNumber) + "</expression>\n");
        System.out.println(createIndent(indentNumber) + "</expression>");

    }

    public void CompileExpressionList() throws IOException
    {
       writer.printf(createIndent(indentNumber) + "<expressionList>\n");
        System.out.println(createIndent(indentNumber) + "<expressionList>");

        indentNumber++;

        String current = tok.getCurrentToken();

        if(tok.getCurrentToken() != null && (tok.getType(current).equals("integerConstant")|| tok.getType(current).equals("StringConstant") || tok.getType(current).equals("keyword") ||
                tok.getType(current).equals("identifier")) || (tok.getType(current).equals("symbol")) && (current.equals("(") ||current.equals("-") || current.equals("~") ))
        {
            CompileExpression();

            current = tok.getCurrentToken();
            while(current.equals(","))
            {
                writer.printf(createIndent(indentNumber) + "<symbol> , </symbol>\n");
                System.out.println(createIndent(indentNumber) + "<symbol> , </symbol>");

                tok.nextToken();
                CompileExpression();
                current = tok.getCurrentToken();
            }
        }

        indentNumber--;
        writer.printf(createIndent(indentNumber) + "</expressionList>\n");
        System.out.println(createIndent(indentNumber) + "</expressionList>");

    }

    public void CompileTerm() throws IOException
    {
        writer.printf(createIndent(indentNumber) +"<term>\n");
        System.out.println(createIndent(indentNumber) +"<term>");

        indentNumber++;

        String current = tok.getCurrentToken();
        String type = tok.getType(current);

        //integerConstant
        if (type.equals("integerConstant"))
        {
            writer.printf(createIndent(indentNumber) + "<integerConstant> " + current + " </integerConstant>\n");
            System.out.println(createIndent(indentNumber) + "<integerConstant> " + current + " </integerConstant>");
            tok.nextToken();
        }
        //StringConstant
        else if (type.equals("StringConstant"))
        {
            writer.printf(createIndent(indentNumber) + "<StringConstant> " + current + " </StringConstant>\n");
            System.out.println(createIndent(indentNumber) + "<StringConstant> " + current + " </StringConstant>");
            tok.nextToken();
        }
        //keyword
        else if (type.equals("keyword"))
        {
            writer.printf(createIndent(indentNumber) + "<keyword> " + current + " </keyword>\n");
            System.out.println(createIndent(indentNumber) + "<keyword> " + current + " </keyword>");
            tok.nextToken();
        }
        //varName|subroutinename|className
        else if (type.equals("identifier"))
        {
            writer.printf(createIndent(indentNumber) + "<identifier> " + current + " </identifier>\n");
            System.out.println(createIndent(indentNumber) + "<identifier> " + current + " </identifier>");
            tok.nextToken();

            current = tok.getCurrentToken();

            if (current.equals("["))
            {
                writer.printf(createIndent(indentNumber) + "<symbol> [ </symbol>\n");
                System.out.println(createIndent(indentNumber) + "<symbol> [ </symbol>");

                tok.nextToken();
                CompileExpression();

                if (tok.getCurrentToken().equals("]"))
                {
                    writer.printf(createIndent(indentNumber) + "<symbol> ] </symbol>\n");
                    System.out.println(createIndent(indentNumber) + "<symbol> ] </symbol>");
                }
                else{
                    System.out.println("suppose to met a symbol ']', but met a: "+current);
                    exit(1);

                }
                tok.nextToken();
            }
            //Varname | VarName [ Expression ]



            else if (current.equals("(") || current.equals("."))
            {
                if (current.equals("("))
                {
                    writer.printf(createIndent(indentNumber) + "<symbol> ( </symbol>\n");
                    System.out.println(createIndent(indentNumber) + "<symbol> ( </symbol>");

                }
                else if (current.equals("."))
                {
                    writer.printf(createIndent(indentNumber) + "<symbol> . </symbol>\n");
                    System.out.println(createIndent(indentNumber) + "<symbol> . </symbol>");

                    tok.nextToken();
                    current = tok.getCurrentToken();
                    if (tok.getType(current).equals("identifier"))
                    {
                        writer.printf(createIndent(indentNumber) + "<identifier> " + current + " </identifier>\n");
                        System.out.println(createIndent(indentNumber) + "<identifier> " + current + " </identifier>");

                    }
                    else{
                        System.out.println("suppose to met a identifier, but met a: "+current);
                        exit(1);
                    }

                    //(
                    tok.nextToken();
                    current = tok.getCurrentToken();
                    if (current.equals("("))
                    {
                        writer.printf(createIndent(indentNumber) + "<symbol> ( </symbol>\n");
                        System.out.println(createIndent(indentNumber) + "<symbol> ( </symbol>");
                    }
                    else{
                        System.out.println("suppose to met a symbol '(', but met a: "+current);
                        exit(1);
                    }

                }

                tok.nextToken();
                CompileExpressionList();

                if (tok.getCurrentToken().equals(")"))
                {
                    writer.printf(createIndent(indentNumber) + "<symbol> ) </symbol>\n");
                    System.out.println(createIndent(indentNumber) + "<symbol> ) </symbol>");
                }
                else
                {
                    System.out.println("suppose to met a symbol ')', but met a: "+current);
                    exit(1);
                }


                tok.nextToken();
            }

        }
        else if (type.equals("symbol") && current.equals("("))
        {
            writer.printf(createIndent(indentNumber) +"<symbol> ( </symbol>\n");
            System.out.println(createIndent(indentNumber) +"<symbol> ( </symbol>");

            tok.nextToken();
            CompileExpression();

            if (tok.getCurrentToken().equals(")"))
            {
                writer.printf(createIndent(indentNumber) + "<symbol> ) </symbol>\n");
                System.out.println(createIndent(indentNumber) + "<symbol> ) </symbol>");
            }

            tok.nextToken();

        }
        else if (type.equals("symbol") && (current.equals("-")||current.equals("~")))
        {
            writer.printf(createIndent(indentNumber)+"<symbol> "+ current+ " </symbol>\n");
            System.out.println(createIndent(indentNumber)+"<symbol> "+ current+ " </symbol>");

            tok.nextToken();
            CompileTerm();
        }
        else{
            System.out.println("suppose to met a valid term, but met a: "+tok.getCurrentToken());
            exit(1);
        }

        indentNumber--;
        writer.printf(createIndent(indentNumber)+"</term>\n");
        System.out.println(createIndent(indentNumber)+"</term>");

    }
}
