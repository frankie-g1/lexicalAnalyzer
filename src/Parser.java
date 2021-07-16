import java.util.Scanner;
import java.util.StringTokenizer;



class Parser {
    private Scanner scanner; //reads user input once
    private String statement; // stored as string
    private int nextToken; // nextToken - evaluated
    private String nextTokenLexeme;
    private int saveToken; // evaluated with previous token, next Token.
    private String saveTokenLexeme;
    private lexicalAnalyzer_forParser tokenScanner; // reads the user input delimited.

    public Parser(Scanner scanner) {
        this.scanner = scanner;
    } // Parser

    public void run () {
        //get string
        System.out.println("Enter code");
        statement = scanner.nextLine();
        //call statement
        statement(statement);

    }

    private void statement (String statement) {
        /*   statement = {expression  ";" } "."    */

        if(statement.charAt(statement.length()-2) != ';')
        {
            System.out.println("Missing semicolon");
            System.exit(0);
        }
        if(statement.charAt(statement.length()-1) != '.')
        {
            System.out.println("No period to indicate end of input");
            System.exit(0);
        }

        tokenScanner = new lexicalAnalyzer_forParser(statement);
        nextToken = tokenScanner.nextToken();
        nextTokenLexeme = tokenScanner.getLexeme();
            while(nextToken != 309) // period
            {
                int value = expression();
                System.out.println(value);
                if(nextToken == 308) // semicolon
                {
                    continue;
                }
                nextToken = tokenScanner.nextToken();
                nextTokenLexeme = tokenScanner.getLexeme();
            }

    }

    private int expression () {
        /* expression = term { ( "+" | "-" ) term } */
        // Parse first term:  	int left = term();
        int left = term();
        int right = 0;
        int result = left;

        while (nextToken == 21 || nextToken == 22) {
            saveToken = nextToken; // saves + or -
            saveTokenLexeme = nextTokenLexeme;
            nextToken = tokenScanner.nextToken();
            nextTokenLexeme = tokenScanner.getLexeme();
            if (saveToken == 21) {
                right = term();
                result = left + right;
            }
            else if (saveToken == 22) {
                right = term();
                result = left - right;
            }

        }
        return result;
    }



    private int term () {

        int value = factor();
        int result = value;
        while(nextToken == 23 || nextToken == 24)
        {
            saveToken = nextToken;
            saveTokenLexeme = nextTokenLexeme;
            nextToken = tokenScanner.nextToken();
            nextTokenLexeme = tokenScanner.getLexeme();
            if(saveToken == 23)
            {
                int factor = factor();
                result = value * factor;
            }
            if(saveToken == 24)
            {
                int factor = factor();
                result = value / factor;
            }
        }

        return result;
    }



    private int factor () {
        int value = 0;
        int saveIfInt;
        if(nextToken==10)
        {
            saveIfInt = Integer.parseInt(nextTokenLexeme);
            nextToken = tokenScanner.nextToken();
            nextTokenLexeme = tokenScanner.getLexeme();
            return saveIfInt;
        }
        else if(nextToken== 25)
        {
            nextToken = tokenScanner.nextToken();
            nextTokenLexeme = tokenScanner.getLexeme();
            value = expression();
            if(nextToken != 26)
            {
                System.out.println("Expecting a right parenthesis");
                System.out.println(nextToken);
            }
            else
            {
                nextToken = tokenScanner.nextToken();
                nextTokenLexeme = tokenScanner.getLexeme();
                return value;
            }
        }
        else
        {
            System.out.println("Expecting number or (");
            System.exit(0);
        }
        return  value;
    }

                }


                /*

        //First do groupings
        StringTokenizer statementTokens = new StringTokenizer(statement, " )(");
        //First group
        String leftExpression = statementTokens.nextToken();
        //In first group, grab the leftmost term
        StringTokenizer left = new StringTokenizer(leftExpression);


        int left = term(leftExpression);
        while(statementTokens.hasMoreTokens())
        {
            String operator = statementTokens.nextToken();
            if(operator.equals("+"))
            {
                String rightExpression = statementTokens.nextToken();
                int right = term(rightExpression);
                result = left + right;
            }
            if(operator.equals("-"))
            {
                String rightExpression = statementTokens.nextToken();
                int right = term(rightExpression);
                result = left - right;
            }
        }

        return result;


        /* expression = term { ( "+" | "-" ) term } */
// Parse first term:  	int left = term();
//lexicalAnalyzer_forParser lexical = new lexicalAnalyzer_forParser(expr);

//Divide the expressions / expressions and terms / terms

//Inside parenthesis may be a term or expression, but we must divide them.
// (term) + expression is still an expression, but we must solve expressions first recursively
//Parenthesis always has top priority in arithmetic
        /*
        StringTokenizer parenthesis = new StringTokenizer(expr, "()");

        String[] orderOfOperationArray = new String[parenthesis.countTokens()];

        for(int i = 0 ; i < parenthesis.countTokens(); i++)
        {
            orderOfOperationArray[i++] = parenthesis.nextToken();
        }

        //Now that we've organized our expression into multiple terms, lets find out recursive +'s and -'s

        //sumarry: find left of op, solve. find right of op, treat as new left if there are more +'s!

        for(int i = 0; i < orderOfOperationArray.length; i++)
        {
            String part = orderOfOperationArray[i];
            boolean innerExpression = false;
            for(int j = 0; j < orderOfOperationArray[i].length(); j++){
                if(part.charAt(j)=='+' || part.charAt(j)=='-')
                {
                    innerExpression = true;
                    String[] leftExp = part.split(String.valueOf(part.charAt(j)));
                    int leftValue = expression(leftExp[0]);
                }
            }
        }


        */        /*	 factor    = number | "(" expression ")"   */
//   int value = 0;
// Depending on the token read from the scanner, process it
// If it is a number, call the number method and save the value
// get the next token.
// return
// If the token is a lparen, read the next token
// Call expression procedure. Store the result
// If token read is not a rparen, display error message
// Get the next token and return
// Otherwise:
// Display error saying expecting “number” or “(“
// return value.
//    } // end of factor
//    } // end of class Parser
/*    term = factor {("*" | "/") factor}  	*/
// compute term by calling factor first
// While (token is a multOp or divOp {
// save the token
// get the next token
// if saved token is multOp
// call factor again.
// multiply its value by the value of the left token and
//save the result
// If the saved token is divOp
// call factor again.
// Divide the saved value of the first factor
// by the value of the second call to factor.
// Save the result.
// } // while
// return result
// } // end of term