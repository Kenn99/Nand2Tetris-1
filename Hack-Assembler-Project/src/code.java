/**
 * Created by xiaoyifan on 10/29/14.
 */
public class code {

    public static String destCode(String destMnemonic)
    {
        String destBits;
        if (destMnemonic.equals("null")) {
            destBits = "000";

        } else if (destMnemonic.equals("M")) {
            destBits = "001";

        } else if (destMnemonic.equals("D")) {
            destBits = "010";

        } else if (destMnemonic.equals("MD")) {
            destBits = "011";

        } else if (destMnemonic.equals("A")) {
            destBits = "100";

        } else if (destMnemonic.equals("AM")) {
            destBits = "101";

        } else if (destMnemonic.equals("AD")) {
            destBits = "110";

        } else if (destMnemonic.equals("AMD")) {
            destBits = "111";

        } else {
            throw new IllegalArgumentException("dest Input is invalid");
        }

        return destBits;
    }

    public static String compCode(String compMnemonic)
    {

         String compBits;

        if (compMnemonic.equals("0"))
        {
            compBits = "0101010";
        }
        else if (compMnemonic.equals("1"))
        {
            compBits = "0111111";
        }
        else if (compMnemonic.equals("-1"))
        {
            compBits = "0111010";
        }
        else if (compMnemonic.equals("D"))
        {
            compBits = "0001100";
        }
        else if (compMnemonic.equals("A"))
        {
            compBits = "0110000";
        }
        else if (compMnemonic.equals("M"))
        {

            compBits = "1110000";
        }
        else if (compMnemonic.equals("!D"))
        {
            compBits = "0001101";
        }
        else if (compMnemonic.equals("!A"))
        {
            compBits = "0110001";
        }
        else if (compMnemonic.equals("!M"))
        {
            compBits = "1110001";
        }
        else if (compMnemonic.equals("-D"))
        {
            compBits = "0001111";
        }
        else if (compMnemonic.equals("-A"))
        {
            compBits = "0110011";
        }
        else if (compMnemonic.equals("-M"))
        {
            compBits = "1110011";
        }
        else if (compMnemonic.equals("D+1"))
        {
            compBits = "0011111";
        }
        else if (compMnemonic.equals("A+1"))
        {
            compBits = "0110111";
        }
        else if (compMnemonic.equals("M+1"))
        {
            compBits = "1110111";
        }
        else if (compMnemonic.equals("D-1"))
        {
            compBits = "0001110";
        }
        else if (compMnemonic.equals("A-1"))
        {
            compBits = "0110010";
        }
        else if (compMnemonic.equals("M-1"))
        {
            compBits = "1110010";
        }
        else if (compMnemonic.equals("D+A")||compMnemonic.equals("A+D"))
        {
            compBits = "0000010";
        }
        else if (compMnemonic.equals("D+M")||compMnemonic.equals("M+D"))
        {
            compBits = "1000010";
        }
        else if (compMnemonic.equals("D-A"))
        {
            compBits = "0010011";
        }
        else if (compMnemonic.equals("D-M"))
        {
            compBits = "1010011";
        }
        else if (compMnemonic.equals("A-D"))
        {
            compBits = "0000111";
        }
        else if (compMnemonic.equals("M-D"))
        {
            compBits = "1000111";
        }
        else if (compMnemonic.equals("D&A")||compMnemonic.equals("A&D"))
        {
            compBits = "0000000";
        }
        else if (compMnemonic.equals("D&M")||compMnemonic.equals("M&D"))
        {
            compBits = "1000000";
        }
        else if (compMnemonic.equals("D|A")||compMnemonic.equals("A|D"))
        {
            compBits = "0010101";
        }
        else if (compMnemonic.equals("D|M")||compMnemonic.equals("M|D"))
        {
            compBits = "1010101";
        }
        else {
            throw new IllegalArgumentException("comp code is invalid");
        }


        return compBits;
    }

    public static String jumpCode(String jumpMnemonic)
    {
         String jumpBits;
        if (jumpMnemonic.equals("null"))
        {
            jumpBits = "000";
        }
        else if(jumpMnemonic.equals("JGT"))
        {
            jumpBits ="001";
        }
        else if(jumpMnemonic.equals("JEQ"))
        {
            jumpBits ="010";
        }
        else if(jumpMnemonic.equals("JGE"))
        {
            jumpBits ="011";
        }
        else if(jumpMnemonic.equals("JLT"))
        {
            jumpBits ="100";
        }
        else if(jumpMnemonic.equals("JNE"))
        {
            jumpBits ="101";
        }
        else if(jumpMnemonic.equals("JLE"))
        {
            jumpBits ="110";
        }
        else if(jumpMnemonic.equals("JMP"))
        {
            jumpBits ="111";
        }
        else {
            throw new IllegalArgumentException("jump Input is invalid");
        }

        return jumpBits;
    }
}
