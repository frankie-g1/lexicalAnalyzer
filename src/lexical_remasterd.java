import java.util.Scanner;
import java.util.StringTokenizer;

public class lexical_remasterd {

    private static Scanner s;
    private static StringTokenizer st;

    private static String passedString;

    private static int token; //return
    private static String tokenString; //attribute

    private static int index;

    private final static int DIGIT = 1;
    private final static int ASSIGN_OP = 20;
    private final static int ADD_OP = 21;
    private final static int SUB_OP = 22;
    private final static int MULT_OP = 23;
    private final static int DIV_OP = 24;
    private final static int LEFT_PAREN = 25;
    private final static int RIGHT_PAREN = 26;
    private final static int SEMICOLON = 307;
    private final static int PERIOD = 308;

    public lexical_remasterd(String lineOfCode)
    {
        this.passedString = lineOfCode;
        this.s = new Scanner(passedString);
        s.useDelimiter("");
        this.st = new StringTokenizer(passedString, ")(+-*/;.");
        index = 0;
    }

    public String getTokenString()
    {
        return tokenString;
    }


    public int nextToken() {

        while(s.hasNext()){
            char n = passedString.charAt(index);
            if(Character.isDigit(n))
            {
                tokenString = st.nextToken().trim();
                index = passedString.indexOf(tokenString);
                index += tokenString.length();
                return 1;
            }
            else
            {
                if(isEmptyOrSpace(n))
                {
                    index++;
                    continue;
                }
                lookup(n);
                index++;
                return token;
            }
        }
        return token;
    }

    private boolean isEmptyOrSpace(char ch)
    {
        String x = String.valueOf(ch);
        if(x.equals("\u0000") ){
            return true;
        }
        if(String.valueOf(ch).equals(" "))
        {
            return true;
        }
        if(ch == ' ')
        {
            return  true;
        }
        return false;
    }

    private void lookup(char ch)
    {
        if (ch == '=')
        {
            token = ASSIGN_OP;
            tokenString = "=";
        }
        else if (ch == '+')
        {
            token = ADD_OP;
            tokenString = "+";
        }
        else if (ch == '-')
        {
            token = SUB_OP;
            tokenString = "-";
        }
        else if (ch == '*')
        {
            token = MULT_OP;
            tokenString = "*";
        }
        else if (ch == '/')
        {
            token = DIV_OP;
            tokenString = "/";
        }
        else if (ch == '(')
        {
            token = LEFT_PAREN;
            tokenString = "(";
        }
        else if (ch == ')')
        {
            token = RIGHT_PAREN;
            tokenString = ")";
        }
        else if (ch =='.')
        {
            token = PERIOD; // comes after unexpected, is a period after the semi colon
            tokenString = ".";
        }
        else
        {
            token = SEMICOLON; // will correspond when nextChar is EOF
            tokenString = ";";
        }
    }
}

