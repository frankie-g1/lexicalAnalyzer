
/***
MAIN DIFFERENCE IS THIS USES SCANNER INSTEAD OF READING SRC CODE TXT
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class lexicalAnalyzer_forParser {  // Finds the lexemes and corresponding tokens of source code

    // Char classes. Tokens are derived from char classes.
    private final static int LETTER = 0;
    private final static int DIGIT = 1;
    private final static int UNKNOWN = 99;

    // Token list (could be a table)
    private final static int INT_LIT = 10;
    private final static int IDENT = 11;
    private final static int ASSIGN_OP = 20;
    private final static int ADD_OP = 21;
    private final static int SUB_OP = 22;
    private final static int MULT_OP = 23;
    private final static int DIV_OP = 24;
    private final static int LEFT_PAREN = 25;
    private final static int RIGHT_PAREN = 26;


    private final static int UNEXPECTED = 307;
    private final static int EOF = 308;
    private final static int END_OF_INPUT = 309; //new, is a period


    static int token; // Any of the 10 tokens above

    static int charClass;  // Any of the 3 char classes: LETTER, DIGIT, UNKNOWN

    // Building the lexeme. ALWAYS: atleast build 1 char in lexeme buffer (i.e. + or ) ). Choose token after nextChar is blank or a period (unknown?)
    static char[] lexeme = new char[100]; // Accumulation. Substring of the source code. Will have token
    static char nextChar; //is built into the lexeme, unless it is of unknown.
    static int lexLen;
    //static File src_code;
    static String src_code;
    static Scanner inputLex;
    static boolean eof = false;

    //When something is read and didn't fit the charClass, it must be saved here (1) , must be added to lexeme (2), must determine charClass (3)
    // Even if it is just a space, we won't know unless we store and check it next round
    static char tempNext;
    static int tempLen;

    public lexicalAnalyzer_forParser(String parseScanner)
    {
        src_code = parseScanner;
        inputLex = new Scanner(src_code);
        inputLex.useDelimiter(""); // IMPORTANT: Scanner always read the whole substring delimited by " ". This sets up the scanner to read/consume 1 char and no further.
    }

    public int nextToken()
    {
        lex();
        //System.out.printf("Lexeme : %s Token : %d\n", new String(lexeme), token);
        return token;
    }

    public String getLexeme()

    {
        return String.valueOf(lexeme).trim();
    }

    public int getTokenCode() { return token; }

    private boolean isEmptyOrSpace(char ch)  //Hopefully -> getNonBlank returns a char -> getChar() uses this function.
    {                                           // When the char is '' : (true), getChar() will assign nextChar to EOF token.
        String x = String.valueOf(ch);          // This in turn ends the lexical analysis of the file.
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

    private int lex()
    {

        if(tempLen == 0 || isEmptyOrSpace(tempNext)) // didn't read an extra one , or did read and it was empty
        {
            lexLen = 0;
            lexeme = new char[100];
            getNonBlank(); // equivalent to getChar(), called at beginning of lexeme
            //start to build lexeme, at end match token once next char isn't of same char class
            addChar();
        }
        else
        {
            assignCharClass(tempNext);
            nextChar = tempNext;
            lexeme = new char[100];
            lexLen = 0;
            addChar();
        }

        switch (charClass)
        {
            case DIGIT:

                getChar();  // Gets the second character, places in nextChar and determines charClass (not token). ****token is derived from charClass****

                while(charClass==DIGIT)
                {
                    if(charClass==DIGIT)
                    {
                        addChar();
                    }
                    getChar();
                }
                //Eventually it won't line up
                tempNext = nextChar;
                tempLen = 1;
                token = INT_LIT;
                break;

            case LETTER:
                //build lexeme, at end match token
                getChar();
                while(charClass==LETTER || charClass == DIGIT)
                {
                    if(charClass==LETTER || charClass == DIGIT)
                    {
                        addChar();
                    }
                    getChar();
                }
                tempNext = nextChar;
                tempLen = 1;
                token = IDENT;
                break;

            case UNKNOWN:
                //Unknown class doesn't build a lexeme, it finds it's token and lexical moves on and starts anew lexeme and token. Redundant parenthesis can be deduced at end if needed
                lookup(nextChar); //Unknown charClass, match token here. Move onto next lexeme, don't build lexeme. Many tokens derive from unknown char class & usually are independent of each other i.e. ( (4+5) * 9 )
                //token is assigned in lookup
                //isn't a category to build/accumulate the lexeme
                tempLen = 0;
        }

        return token;
    }

    private void getChar()         //get Next & stores the next char in the static var nextChar
    {

        if(!inputLex.hasNext()) //infers a semicolon at the end
        {
            nextChar = ';';
            charClass = UNKNOWN;
            return;
        }
        // We create temp variables, I want to be able to read the next char's properties without building the lexeme and being able to go back (scanners can't go back)
        nextChar = inputLex.next().charAt(0);
        // find char class
        if(nextChar >= 48 && nextChar <= 57)
        {
            charClass = DIGIT;
        }
        else if((nextChar >= 65 && nextChar <= 90) || (nextChar >= 97 && nextChar <= 122))
        {
            charClass = LETTER;
        }
        else
        {
            charClass = UNKNOWN;
        }

    }

    private void assignCharClass(char ch)
    {
        // find char class
        if(ch >= 48 && ch <= 57)
        {
            charClass = DIGIT;
        }
        else if((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
        {
            charClass = LETTER;
        }
        else
        {
            charClass = UNKNOWN;
        }

    }

    private void addChar()
    {
        // build lexeme
        if(lexLen > 98)
        {
            System.out.println("Lexeme too long, exiting lexical analysis");
            System.exit(0);
        }
        lexeme[lexLen++] = nextChar;
    }


    private void lookup(char ch)
    {
        if (ch == '=')
        {
            token = ASSIGN_OP;
        }
        else if (ch == '+')
        {
            token = ADD_OP;
        }
        else if (ch == '-')
        {
            token = SUB_OP;
        }
        else if (ch == '*')
        {
            token = MULT_OP;
        }
        else if (ch == '/')
        {
            token = DIV_OP;
        }
        else if (ch == '(')
        {
            token = LEFT_PAREN;
        }
        else if (ch == ')')
        {
            token = RIGHT_PAREN;
        }
        else if (ch =='.')
        {
            token = END_OF_INPUT; // comes after unexpected, is a period after the semi colon
        }
        else
        {
            token = UNEXPECTED; // will correspond when nextChar is EOF
        }
    }

    private void getNonBlank()
    {

        do
        {
            if(!inputLex.hasNext())
            {
                charClass = UNEXPECTED;
                token = EOF;
                lexeme[0] = 'E';
                lexeme[1] = 'O';
                lexeme[2] = 'F';
                eof = true;
                nextChar = lexeme[0];
                break;
            }
            else
            {
                nextChar = inputLex.next().charAt(0);
                assignCharClass(nextChar);
            }


        }while(String.valueOf(nextChar).equals(" ") || String.valueOf(nextChar).equals("\u0000"));


    }
}
