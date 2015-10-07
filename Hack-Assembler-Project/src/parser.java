import static java.lang.Character.isDigit;
import static java.lang.System.exit;
import static java.lang.System.setOut;

/**
 * Created by xiaoyifan on 10/30/14.
 */
public class parser {

    public static String commandParse(String command, symbol table) {
        String type = whichType(command);
        String valueTrue="";

        if (type.equals("A_COMMAND")) {

            String value = command.substring(1);
            //get the xxx
            if (!isNumber(value)) {

               int ramPosition = table.getValue(value);
               //check the symbol table for the ram-value, if not, the function returns -1

               if (ramPosition == -1)//not in the table
               {
                 valueTrue = ""+table.getRamAvailable();
                   //valueTrue is the new added symbol's ram position

                 if(isFormalLabel(value)) {
                     table.addAPair(value);//add the string into the table, and the ram-available position will +1
                 }
                   else {
                     throw new IllegalArgumentException("the label is not right");
                 }
               }
               else
               {
                   valueTrue = ""+ramPosition;//the symbol is already in the table, take the position out and use it
               }

            }//if the xxx is not a number, find table or create one.

            else{
                valueTrue = value;//if value is the string formatted number ,just use the original one
            }


           //generic processing, psarse the string to number ,add make it a 16-bits string
            int valueNum = Integer.parseInt(valueTrue);

            String binary = Integer.toBinaryString(valueNum);
            if (binary.length() <= 15) {
                String machineCode = "0";
                int extra = 15 - binary.length();
                for (int i = 0; i < extra; i++) {
                    machineCode += '0';
                }
                machineCode += binary;
                return machineCode;
            } else {
                System.out.println("A-Command is overflow");
                exit(1);
            }


        }//A-Command is moved to machine code directly in this method, without calling other functions

        else if (type.equals("C_COMMAND"))
        {
            String compCommand = cCompParse(command);
            String comp = code.compCode(compCommand);

            String destCommand = cDestParse(command);
            String dest = code.destCode(destCommand);

            String jumpCommand = cJumpParse(command);
            String jump = code.jumpCode(jumpCommand);

            return "111" + comp + dest + jump;
        }


     return null;
    }

    public static String whichType(String command) {
        if (command.contains("@")) {
            return "A_COMMAND";
        } else if (command.contains("(") && command.contains(")")) {
            return "L_COMMAND";
        } else if (command.contains("=") || command.contains(";")) {
            return "C_COMMAND";
        } else return null;
    }


    public static String cDestParse(String command) {

            for (int i = 0; i < command.length(); i++)
            {
                if (command.charAt(i) == '=') {

                    return command.substring(0, i);
                }

            }
        return "null";
    }

    public static String cCompParse(String command) {

        System.out.println(command);
        String compCommand="";
        int startPoint=0,endPoint=0;

        if (command.contains("=") && command.contains(";"))
        {
            System.out.println(1);
            for (int i=0;i<command.length();i++)
            {
                if (command.charAt(i) == '=')
                {
                    startPoint =i;
                }
                if (command.charAt(i) == ';')
                {
                    endPoint = i;
                }
            }
            compCommand = command.substring(startPoint+1,endPoint);
        }
        else if (command.contains("=") && !command.contains(";"))
        {System.out.println(2);
            for (int i=0;i<command.length();i++)
            {
                if (command.charAt(i) == '=') {
                    startPoint = i;
                }
            }
           compCommand = command.substring(startPoint+1);
        }
        else if (!command.contains("=") && command.contains(";"))
        {System.out.println(3);
            for (int i=0;i<command.length();i++)
            {
                if (command.charAt(i) == ';') {
                    endPoint = i;
                }
            }
         compCommand = command.substring(0,endPoint);
        }
        else {
            throw new IllegalArgumentException("Comp Input is invalid");
        }

     return compCommand;
    }

    public static String cJumpParse(String command) {

        for (int i = 0; i < command.length(); i++)
        {
            if (command.charAt(i) == ';') {

                return command.substring(i+1);
            }

        }
        return "null";

    }


    public static boolean isNumber(String str){
        for (int i = 0; i < str.length(); i++){

            if (!isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }


    public static boolean isFormalLabel(String value)
    {
        for (int i=0;i<value.length();i++)
        {
            char ch = value.charAt(i);
             if (!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '.' || ch == '_' || ch == '$' || ch == ':'))
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
}


