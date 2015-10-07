import org.omg.PortableInterceptor.SUCCESSFUL;
import sun.jvm.hotspot.debugger.cdbg.Sym;

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

    private final String IF_T = "IF_TRUE";
    private final String IF_F = "IF_FALSE";
    private final String IF_E = "IF_END";
    private final String W_EX = "WHILE_EXP";
    private final String W_EN = "WHILE_END";

    private int indentNumber;

    private Tokenizer tok;
    private PrintWriter writer;
    private VMWriter vmWriter;
    private symbolTable SymbolTable;

    private int numOfParameter;

    private String className;
    private int ifControl, WhileControl;

    CompilationEngine(String inFile, String outFile) throws IOException
    {
            tok = new Tokenizer(inFile);
            writer = new PrintWriter(outFile);
            indentNumber = 0;
            className="";
            numOfParameter = 0;
            String[] XMLFileName;
            XMLFileName = outFile.split("\\.");
            String vmName = XMLFileName[0]+".vm";
            vmWriter = new VMWriter(vmName);


            SymbolTable = new symbolTable();

            ifControl =0;
            WhileControl =0;

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
            className = current;
            writer.printf(createIndent(indentNumber)+"<identifier> " + current +" </identifier>\n");
            System.out.println(createIndent(indentNumber)+"<identifier> " + current +" </identifier>");
        }
        else{
            System.out.println("suppose to met a identifier, but actually met: "+current);
            exit(1);
        }


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
            SymbolTable.startSubroutine(current);

            //write this code later
            CompileSubroutine();
            tok.nextToken();
            current = tok.getCurrentToken();
        }


        if (tok.getCurrentToken().equals("}"))
        {
            writer.printf("<symbol> } </symbol>\n");
            System.out.println("<symbol> } </symbol>");
        }
        else{
            System.out.println("suppose to met a }, but met a: "+ tok.getCurrentToken());
            exit(1);
        }

        indentNumber--;

        writer.printf("</class>\n");
        System.out.println("</class>");
        vmWriter.close();


    }

    public void CompileClassVarDec() throws IOException {
        writer.printf(createIndent(indentNumber) + "<classVarDec>\n");
        System.out.println(createIndent(indentNumber) + "<classVarDec>");
        //output the label of the Var

        String name,type, kind;
        name = "";
        type = "";
        kind = "";

        indentNumber++;

        if (tok.getCurrentToken().equals("static") || tok.getCurrentToken().equals("field")) {

            if (tok.getCurrentToken().equals("static"))
            {
                kind  = "STATIC";
            }
            else
            {
                kind = "FIELD";
            }

            writer.printf(createIndent(indentNumber) + "<keyword> " + tok.getCurrentToken() + " </keyword>\n");
            System.out.println(createIndent(indentNumber) + "<keyword> " + tok.getCurrentToken() + " </keyword>");
        }
        else
        {
            System.out.println("suppose to met a 'field' or 'static' , but met a: " + tok.getCurrentToken());
            exit(1);
        }


        tok.nextToken();
        String current = tok.getCurrentToken();
        if (current.equals("int") || current.equals("char") || current.equals("boolean"))
        {
            type = current;
            writer.printf(createIndent(indentNumber) + "<keyword> " + current + " </keyword>\n");
            System.out.println(createIndent(indentNumber) + "<keyword> " + current + " </keyword>");
        }
        else if (tok.getType(current).equals("identifier"))
        {
            if (SymbolTable.IndexOf(current) != -1 || (!SymbolTable.KindOf(current).equals("NONE")))
            {
                System.out.println("This identifier is already used as a variable or a field");
            }
            type = current;

            writer.printf(createIndent(indentNumber) + "<identifier> " + current + " </identifier>\n");
            System.out.println(createIndent(indentNumber) + "<identifier> " + current + " </identifier>");
        }
        else {
            System.out.println("suppose to met a primitive or class type, but met a: " + current);
            exit(1);
        }


        tok.nextToken();
        current = tok.getCurrentToken();
        if (tok.getType(current).equals("identifier"))
        {
            name = current;

            if (SymbolTable.IndexOf(current) == -1)
            {
                SymbolTable.Define(name, type, kind);
            }
            else {
                System.out.println("This identifier is already used");
                exit(1);
            }


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
                name = current;
                if (SymbolTable.IndexOf(current) == -1)
                {
                    SymbolTable.Define(name,type,kind);
                }
                else {
                    System.out.println("This identifier is already used.");
                    exit(1);
                }
                writer.printf(createIndent(indentNumber) +"<identifier> "+current+" </identifier>\n");
                System.out.println(createIndent(indentNumber) +"<identifier> "+current+" </identifier>");

            } else {
                System.out.println("suppose to met a identifier, but met a: " + current);
                exit(1);
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

        String SubroutineName, SubroutineType;
        SubroutineName = "";

        SubroutineType ="";

        String current = tok.getCurrentToken();
        if (current.equals("constructor") || current.equals("function") || current.equals("method"))
        {
            SubroutineType = current;
            writer.printf(createIndent(indentNumber)+ "<keyword> "+current+" </keyword>\n");
            System.out.println(createIndent(indentNumber)+ "<keyword> "+current+" </keyword>");
        }
        else{
            System.out.println("suppose to me a 'constructor' or a 'function' or a 'method' but met a: "+current);
            exit(1);
        }


        tok.nextToken();
        current = tok.getCurrentToken();
        if (current.equals("void") || current.equals("int") || current.equals("char")|| current.equals("boolean"))
        {
            writer.printf(createIndent(indentNumber)+"<keyword> "+current+" </keyword>\n");
            System.out.println(createIndent(indentNumber)+"<keyword> "+current+" </keyword>");

        }
        else if (tok.getType(current).equals("identifier"))
        {
            if (SymbolTable.IndexOf(current) != -1 || (!SymbolTable.KindOf(current).equals("NONE")))
            {
                System.out.println("This identifier is already used as a variable or a field");
                exit(1);
            }

            writer.printf(createIndent(indentNumber) +"<identifier> "+current+" </identifier>\n");
            System.out.println(createIndent(indentNumber) +"<identifier> "+current+" </identifier>");

        }
        else{
            System.out.println("suppose to me a 'constructor' or a 'function' or a 'method' but met a: "+current);
            exit(1);
        }
        //the first declaration line is over,include the class

        tok.nextToken();
        current = tok.getCurrentToken();
        if (tok.getType(current).equals("identifier"))
        {
            if (SymbolTable.IndexOf(current) != -1 || (!SymbolTable.KindOf(current).equals("NONE")))
            {
                System.out.println("This identifier is already used as a variable or a field");
                exit(1);
            }

            SubroutineName = current;
            writer.printf(createIndent(indentNumber)+ "<identifier> "+current+" </identifier>\n");
            System.out.println(createIndent(indentNumber)+ "<identifier> "+current+" </identifer>");

        }
        else{
            System.out.println("suppose to me a 'constructor' or a 'function' or a 'method' but met a: "+current);
            exit(1);
        }
        //find the class name

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
        //compile the parameter


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
            tok.nextToken();
        }
        //compile all the variables

??
        vmWriter.WriteFunction(className+ "." +SubroutineName, SymbolTable.VarCount("VAR"));

        if (SubroutineType.equals("constructor"))
        {
            vmWriter.writePush("CONST", SymbolTable.VarCount("FIELD"));
            vmWriter.WriteCall("Memory.alloc",1);
            vmWriter.writePop("POINTER",0);
        }
        else if(SubroutineType.equals("method"))
        {
            vmWriter.writePush("ARG",0);
            vmWriter.writePop("POINTER",0);
        }


        CompileStatement();




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

        String name, type, kind;
        name ="";
        type ="";
        kind = "ARG";

        String current = tok.getCurrentToken();
        if (tok.getType(current).equals("keyword") || tok.getType(current).equals("identifier"))
        {
                if (tok.getType(current).equals("keyword") && (current.equals("int") || current.equals("char") || current.equals("boolean")) )
                {
                    type = current ;
                    writer.printf(createIndent(indentNumber)+ "<keyword> " +current + " </keyword>\n");
                    System.out.println(createIndent(indentNumber)+ "<keyword> " +current + " </keyword>");
                }
                else if (tok.getType(current).equals("identifier"))
                {
                    type = current;

                    if (SymbolTable.IndexOf(current) != -1 || (!SymbolTable.KindOf(current).equals("NONE")))
                    {
                        System.out.println("This identifier is already used as a variable or a field");
                        exit(1);
                    }


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
                name = current;

                if (SymbolTable.IndexOf(name) == -1)
                {
                    SymbolTable.Define(name, type,kind);
                }
                else{
                    System.out.println("This line is already used.");
                    exit(1);
                }


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
                    type = current;
                    writer.printf(createIndent(indentNumber) + "<keyword> "+ current +" </keyword>\n");
                    System.out.println(createIndent(indentNumber) + "<keyword> " + current + " </keyword>");
                }
                else if(tok.getType(current).equals("identifier"))
                {
                    type = current;
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
                    name = current;

                    if (SymbolTable.IndexOf(name) == -1)
                    {
                        SymbolTable.Define(name, type, kind);
                    }
                    else{
                        System.out.println("This line is already used.");
                        exit(1);
                    }

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

        String name, type ,kind;
        name = "";
        type = "";
        kind = "VAR";

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
            type = current;
            writer.printf(createIndent(indentNumber) + "<keyword> "+current+" </keyword>\n");
            System.out.println(createIndent(indentNumber) + "<keyword> "+current+" </keyword>");
        }
        else if (tok.getType(current).equals("identifier"))
        {
            type = current;

            if (SymbolTable.IndexOf(type) != -1 || (!SymbolTable.KindOf(type).equals("NONE")))
            {
                System.out.println("This identifier is already used");
                exit(1);
            }
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
            name = current;

            if (SymbolTable.IndexOf(name) == -1)
            {
                SymbolTable.Define(name, type, kind);
            }
            else {
                System.out.println("This line is already used");
                exit(1);
            }

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
                name = current;

                if (SymbolTable.IndexOf(name) == -1)
                {
                    SymbolTable.Define(name, type, kind);
                }
                else {
                    System.out.println("This line is already used");
                    exit(1);
                }

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

        String SubName = "";

        //subRoutine call, subRoutine name
        tok.nextToken();


        String current = tok.getCurrentToken();
        if (tok.getType(current).equals("identifier"))
        {
            SubName = current;

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
            String obj = SubName;
            SubName += ".";


            writer.printf(createIndent(indentNumber) + "<symbol> . </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> . </symbol>");


            if (SymbolTable.IndexOf(obj) != -1)
            {
                vmWriter.writePush(SymbolTable.KindOf(obj),SymbolTable.IndexOf(obj));
                numOfParameter++;
                SubName = SymbolTable.TypeOf(obj) + ".";

            }

            //subRoutine name
            tok.nextToken();
            current = tok.getCurrentToken();
            if (tok.getType(current).equals("identifier"))
            {
                if (SymbolTable.IndexOf(current) != -1 || (!SymbolTable.KindOf(current).equals("NONE")))
                {
                    System.out.println("This identifier is already used");
                    exit(1);
                }

                SubName += current;

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

        if (SubName.indexOf(".") == -1)
        {

            vmWriter.writePush("POINTER" , 0);
            SubName = className + "." + SubName;
            numOfParameter++;

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

        vmWriter.WriteCall(SubName, numOfParameter);
        vmWriter.writePop("TEMP",0);
        numOfParameter =0;


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

        String varName = "";

        //var's name
        tok.nextToken();
        String current = tok.getCurrentToken();
        if (tok.getType(current).equals("identifier"))
        {
            if (SymbolTable.IndexOf(current) == -1 || SymbolTable.KindOf(current).equals("NONE"))
            {
                System.out.println("Identifier is not declared");
                exit(1);
            }

            varName = current;

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
            vmWriter.writePush(SymbolTable.KindOf(varName), SymbolTable.IndexOf(varName));

            tok.nextToken();
            CompileExpression();

            vmWriter.WriteArithmetic("ADD");

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

                vmWriter.writePop("TEMP" ,0);
                vmWriter.writePop("POINTER" , 1);
                vmWriter.writePush("TEMP" ,0);
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



        if (!SymbolTable.KindOf(varName).equals("NONE"))
        {
            if (array){
             vmWriter.writePop("THAT",0);

            }
            else {
             vmWriter.writePop(SymbolTable.KindOf(varName),SymbolTable.IndexOf(varName));
            }
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


        vmWriter.WriteLabel(W_EX + WhileControl);


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

        vmWriter.WriteArithmetic("NOT");


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

        vmWriter.WriteIf(W_EN + WhileControl);
        int WhileControl2 = WhileControl++;

        tok.nextToken();
        CompileStatement();


        vmWriter.WriteGoto(W_EX + WhileControl2);

        if (tok.getCurrentToken().equals("}"))
        {
            writer.printf(createIndent(indentNumber) + "<symbol> } </symbol>\n");
            System.out.println(createIndent(indentNumber) + "<symbol> } </symbol>");
        }
        else{
            System.out.println("suppose to met a symbol '}', but met a: "+tok.getCurrentToken());
            exit(1);
        }

        vmWriter.WriteLabel(W_EN + WhileControl2);


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
            vmWriter.writeReturn();
        }
        else {
            vmWriter.writePush("CONST", 0);
            vmWriter.writeReturn();
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

        vmWriter.WriteIf(IF_T+ifControl);
        vmWriter.WriteGoto(IF_F + ifControl);
        int ifControl2 = ifControl++;


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

        vmWriter.WriteLabel(IF_T + ifControl2);
        tok.nextToken();
        CompileStatement();

        vmWriter.WriteGoto(IF_E + ifControl2);
        vmWriter.WriteLabel(IF_F + ifControl2);

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


        vmWriter.WriteLabel(IF_E + ifControl2);

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

            switch(current.charAt(0)) {
                case '+':		vmWriter.WriteArithmetic("ADD");
                    break;
                case '-':		vmWriter.WriteArithmetic("SUB");
                    break;
                case '&':		vmWriter.WriteArithmetic("AND");
                    break;
                case '|':		vmWriter.WriteArithmetic("OR");
                    break;
                case '<':		vmWriter.WriteArithmetic("LT");
                    break;
                case '>':		vmWriter.WriteArithmetic("GT");
                    break;
                case '=':		vmWriter.WriteArithmetic("EQ");
                    break;
                case '*':		vmWriter.WriteCall("Math.multiply", 2);
                    break;
                case '/':		vmWriter.WriteCall("Math.divide", 2);
                    break;
            }

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
            numOfParameter++;

            current = tok.getCurrentToken();
            while(current.equals(","))
            {
                writer.printf(createIndent(indentNumber) + "<symbol> , </symbol>\n");
                System.out.println(createIndent(indentNumber) + "<symbol> , </symbol>");

                tok.nextToken();
                CompileExpression();
                current = tok.getCurrentToken();
                numOfParameter++;
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
            vmWriter.writePush("CONST", Integer.parseInt(current));
            tok.nextToken();
        }
        //StringConstant
        else if (type.equals("StringConstant"))
        {
            String strValue = current.substring(1, current.length()-1);

            writer.printf(createIndent(indentNumber) + "<StringConstant> " + strValue + " </StringConstant>\n");
            System.out.println(createIndent(indentNumber) + "<StringConstant> " + strValue + " </StringConstant>");
            tok.nextToken();

            vmWriter.writePush("CONST", strValue.length());
            vmWriter.WriteCall("String.new", 1);
            for(int i=0;i<strValue.length();i++)
            {
                vmWriter.writePush("CONST", (int)strValue.charAt(i));
                vmWriter.WriteCall("String.appendChar", 2);
            }

        }
        //keyword
        else if (type.equals("keyword"))
        {
            writer.printf(createIndent(indentNumber) + "<keyword> " + current + " </keyword>\n");
            System.out.println(createIndent(indentNumber) + "<keyword> " + current + " </keyword>");

            if (current.equals("false"))
            {
                vmWriter.writePush("CONST",0);
            }
            else if (current.equals("true"))
            {
                vmWriter.writePush("CONST",0);
                vmWriter.WriteArithmetic("NOT");
            }
            else if (current.equals("null"))
            {
                vmWriter.writePush("CONST",0);
            }
            else if (current.equals("this"))
            {
                vmWriter.writePush("POINTER",0);
            }


            tok.nextToken();
        }
        //varName|subroutinename|className
        else if (type.equals("identifier"))
        {
            boolean array = false;

            String SubName = "";
            String obj = current;

            writer.printf(createIndent(indentNumber) + "<identifier> " + current + " </identifier>\n");
            System.out.println(createIndent(indentNumber) + "<identifier> " + current + " </identifier>");


            vmWriter.writePush(SymbolTable.KindOf(current), SymbolTable.IndexOf(current));
            SubName = current;

            tok.nextToken();

            current = tok.getCurrentToken();

            if (current.equals("["))
            {
                array = true;
                writer.printf(createIndent(indentNumber) + "<symbol> [ </symbol>\n");
                System.out.println(createIndent(indentNumber) + "<symbol> [ </symbol>");

                tok.nextToken();
                CompileExpression();

                vmWriter.WriteArithmetic("ADD");

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

                vmWriter.writePop("POINTER",1);
                vmWriter.writePush("THAT", 0);


            }
            else if (current.equals("(") || current.equals("."))
            {
                if (current.equals("("))
                {
                    writer.printf(createIndent(indentNumber) + "<symbol> ( </symbol>\n");
                    System.out.println(createIndent(indentNumber) + "<symbol> ( </symbol>");

                }
                else if (current.equals("."))
                {
                    if (SymbolTable.IndexOf(SubName)!=-1)
                    {
                        numOfParameter++;
                        SubName = SymbolTable.TypeOf(SubName);
                    }

                    SubName += ".";

                    writer.printf(createIndent(indentNumber) + "<symbol> . </symbol>\n");
                    System.out.println(createIndent(indentNumber) + "<symbol> . </symbol>");



                    tok.nextToken();
                    current = tok.getCurrentToken();
                    if (tok.getType(current).equals("identifier"))
                    {
                        if (SymbolTable.IndexOf(current) != -1 || (!SymbolTable.KindOf(current).equals("NONE")))
                        {
                            System.out.println("This line is already used");
                            exit(1);
                        }

                        SubName+= current;

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


                vmWriter.WriteCall(SubName,numOfParameter);
                numOfParameter=0;


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

            String command  = "";
            if (current.equals("-"))
            {
                command = "NEG";
            }else {
                command = "NOT";
            }


            tok.nextToken();
            CompileTerm();


            vmWriter.WriteArithmetic(command);
        }
        else{
            System.out.println("suppose to met a valid term, but met a: " + tok.getCurrentToken());
            exit(1);
        }

        indentNumber--;
        writer.printf(createIndent(indentNumber)+"</term>\n");
        System.out.println(createIndent(indentNumber)+"</term>");

    }
}
