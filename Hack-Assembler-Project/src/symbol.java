import java.util.ArrayList;

/**
 * Created by xiaoyifan on 10/30/14.
 */
public class symbol {

    private ArrayList<String> symbol;
    private ArrayList<Integer> value;

    private int ramAvailable;


    public symbol()
    {
        symbol = new ArrayList<String>();
        value = new ArrayList<Integer>();

        symbol.add("SP");
        value.add(0);

        symbol.add("LCL");
        value.add(1);

        symbol.add("ARG");
        value.add(2);

        symbol.add("THIS");
        value.add(3);

        symbol.add("THAT");
        value.add(4);

        for (int i=0;i<16;i++)
        {
            symbol.add("R"+i);
            value.add(i);
        }

        symbol.add("SCREEN");
        value.add(16384);

        symbol.add("KBD");
        value.add(24576);

        this.ramAvailable=16;

    }

    public void addAPair(String symbol)
    {
        this.symbol.add(symbol);
        this.value.add(this.ramAvailable);
        this.ramAvailable++;
    }

    public int getRamAvailable()
    {
        return this.ramAvailable;
    }

    public void addLPair(String symbol,int romAvailable)//this value is the number of command, the result of counter
    {
        this.symbol.add(symbol);
        this.value.add(romAvailable);
    }

    int getValue(String symbol)
    {
        for (int i=0;i<this.symbol.size();i++)
        {
            if(this.symbol.get(i).equals(symbol))
            {
                return this.value.get(i);
            }
        }

        return -1;
    }
}
