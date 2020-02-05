package scanner;
import java.io.*;
/**
 * Scanner is a simple scanner for Compilers and Interpreters (2014-2015) lab exercise 1
 * @author Puneet Nayyar
 * @author Ms. Datar
 *
 * @version March 6 2018
 *
 * Usage:
 * FileInputStream inStream = new FileInputStream(new File(<file name>);
 * Scanner lex = new Scanner(inStream);
 *
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;

    /**
     * Scanner constructor for construction of a scanner that
     * uses an InputStream object for input.
     * Usage:
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }
    /**
     * Scanner constructor for constructing a scanner that
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * Method: getNextChar reads the input steam and updates the currentChar
     * to the next character in the input stream
     */
    private void getNextChar()
    {
        try
        {
            int temp = in.read();
            if (temp == -1)
                eof = true;
            else
                currentChar = (char)temp;
        }
        catch (IOException err)
        {
            System.exit(0);
        }

    }
    /**
     * Method: eat will take in a character, check it against the currentChar, and then get
     * the next character in the input stream
     *
     * @param expected the char which is being checked
     * @throws ScanErrorException if the given char is not the same as currentChar
     */
    private void eat(char expected) throws ScanErrorException
    {
        if(expected==currentChar)
            getNextChar();
        else
            throw new ScanErrorException("Illegal character - expected" + currentChar +
                                         "and found" + expected);
    }

    /**
     * Method: hasNext will check whether the scanner has reached the end of the input stream
     *
     * @return true if there is a next character in the input stream; Otherwise,
     *         false
     */
    public boolean hasNext()
    {
        return (!eof && !(currentChar=='.'));
    }


    /**
     * Method: skipLineComment will skip a full line comment and return the next token
     *
     * @return the next token in the input
     * @throws ScanErrorException if an unrecognized character is scanned
     */
    private String skipLineComment() throws ScanErrorException
    {
        while (hasNext() && currentChar!='\n')
        {
            eat(currentChar);
        }
        if (hasNext())
            return nextToken();
        return ".";
    }

    /**
     * Method: skipBlockComment will skip a full block comment and return the next token
     *
     * @return the next token in the input
     * @throws ScanErrorException if an unrecognized character is scanned
     */
    private String skipBlockComment() throws ScanErrorException
    {
        while (hasNext() && currentChar!='/')
        {
            eat(currentChar);
        }
        if (hasNext())
        {
            eat(currentChar);
            return nextToken();
        }
        return ".";
    }

    /**
     * Method: nextToken will scan the input stream, skipping any leading whitespace,
     * and find the next token
     *
     * @return the next token in the input stream
     * @throws ScanErrorException if an unknown character is found
     */
    public String nextToken() throws ScanErrorException
    {
        String token = new String();
        while (hasNext() && isWhiteSpace(currentChar))
        {
            eat(currentChar);
        }
        if (hasNext())
        {
            if (currentChar == '/')
            {
                token += currentChar;
                eat(currentChar);
                if (hasNext() && currentChar == '/')
                    return skipLineComment();
                else if (hasNext() && currentChar == '*')
                    return skipBlockComment();
                return token;
            }
            if (currentChar == ':')
            {
                token += currentChar;
                eat(currentChar);
                if (hasNext() && currentChar == '=')
                {
                    token += currentChar;
                    eat(currentChar);
                }
            }
            else if (currentChar == '<')
            {
                token += currentChar;
                eat(currentChar);
                if (hasNext() && currentChar == '=')
                {
                    token += currentChar;
                    eat(currentChar);
                }
                else if (hasNext() && currentChar == '>')
                {
                    token += currentChar;
                    eat(currentChar);
                }
            }
            else if (currentChar == '>')
            {
                token += currentChar;
                eat(currentChar);
                if (hasNext() && currentChar == '=')
                {
                    token += currentChar;
                    eat(currentChar);
                }
            }
            else if (isDigit(currentChar))
                token = scanNumber();
            else if (isLetter(currentChar))
                token = scanIdentifier();
            else
                token = scanOperand();
            return token;
        }
        return ".";
    }

    /**
     * Method: isDigit checks whether a given char is a number
     *
     * @param c the character being checked
     * @return true if char c is a digit; Otherwise,
     *         false
     */
    public static boolean isDigit(char c)
    {
        return (c>='0' && c<='9');
    }

    /**
     * Method: isLetter checks whether a given char is a letter
     *
     * @param c the character being checked
     * @return true if char c is a letter; Otherwise,
     *         false
     */
    public static boolean isLetter(char c)
    {
        return ((c>='a' && c<='z') || (c>='A') && (c<='Z'));
    }

    /**
     * Method: isWhiteSpace checks whether a given char is whitespace
     *
     * @param c the character being checked
     * @return true if char c is whitespace; Otherwise,
     *         false
     */
    public static boolean isWhiteSpace(char c)
    {
        return (c==' ' || c=='\t' || c=='\r' || c=='\n' );
    }

    /**
     * Method: scanNumber will scan the input stream and return a number token
     *
     * @return the number token
     * @throws ScanErrorException if an unknown character is found
     */
    private String scanNumber() throws ScanErrorException
    {
        String num = new String();
        while (hasNext() && isDigit(currentChar))
        {
            if (isDigit(currentChar))
            {
                num += currentChar;
                eat(currentChar);
            }
            else if (!isWhiteSpace(currentChar))
                throw new ScanErrorException("Character not recognized - " + currentChar);
        }
        return num;
    }

    /**
     * Method: scanIdentifier will scan the input stream and return an identifier token
     *
     * @return the identifier token
     * @throws ScanErrorException if an unknown character is found
     */
    private String scanIdentifier() throws ScanErrorException
    {
        String id = new String();
        while(hasNext() && (isDigit(currentChar) || isLetter(currentChar)))
        {
            if ((isDigit(currentChar) || isLetter(currentChar)))
            {
                id += currentChar;
                eat(currentChar);
            }
            else if (!isWhiteSpace(currentChar))
                throw new ScanErrorException("Character not recognized - " + currentChar);
        }
        return id;
    }

    /**
     * Method: scanOperand will scan the input stream and return an operater token
     *
     * @return the operator token
     * @throws ScanErrorException if an unknown character is found
     */
    private String scanOperand() throws ScanErrorException
    {
        String operand = new String();
        if (hasNext())
        {
            if ((currentChar=='=') || (currentChar=='+') || (currentChar=='-') ||
                    (currentChar=='*') || (currentChar=='/') || (currentChar=='%') ||
                    (currentChar=='(') || (currentChar==')') || (currentChar==';') ||
                    (currentChar=='.') || (currentChar==','))
            {
                operand += currentChar;
                eat(currentChar);
            }
            else if(!isWhiteSpace(currentChar))
                throw new ScanErrorException("Character not recognized - " + currentChar);
        }
        return operand;
    }

    /**
     * Method: main tests the scanner class by loading a test file and scanning it for tokens
     *
     * @param args arguments for the command line
     * @throws ScanErrorException if an unknown character is found
     * @throws FileNotFoundException if the given file cannot be found
     */
    public static void main(String[] args) throws ScanErrorException, FileNotFoundException
    {
        FileInputStream inStream = new FileInputStream(
                new File("C:\\Users\\Puneet\\Downloads\\parserTest.txt"));
        Scanner lex = new Scanner(inStream);
        while (lex.hasNext())
            System.out.println(lex.nextToken());
    }
}