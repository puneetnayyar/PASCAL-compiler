package parser;
import ast.*;
import ast.Number;
import emitter.Emitter;
import scanner.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import environment.Environment;

/**
 * The Parser takes in a stream of tokens outputted by a lexical analyzer, or Scanner,
 * and will parse through the tokens to check whether the input stream is well formed
 * according to the following grammar:
 *
 * program → PROCEDURE id ( maybeparms ) ; stmt program | stmt .
 * maybeparms → parms | ε
 * parms → parms , id | id
 * stmt → WRITELN ( expr ) ; | BEGIN stmts END ; | id := expr ;
 *        |IF cond THEN stmt | WHILE cond DO stmt
 * stmts → stmts stmt | ε
 * expr → expr + term | expr - term | term
 * term → term * factor | term / factor | factor
 * factor → ( expr ) | - factor | num | id ( maybeargs ) | id
 * maybeargs → args | ε
 * args → args , expr | expr
 * cond → expr relop expr
 * relop → = | <> | < | > | <= | >=
 *
 * @author Puneet Nayyar
 * @version March 8 2018
 */
public class Parser
{
    private Scanner lex;
    private String currentToken;

    /**
     * Creates a Parser object with a given Scanner object
     * and sets its instance Scanner and currentToken
     *
     * @param scan a Scanner object to be used with the Parser
     * @throws ScanErrorException if an unidentified character is encountered
     */
    public Parser(Scanner scan) throws ScanErrorException
    {
        lex = scan;
        currentToken = lex.nextToken();
    }

    /**
     * eat will take in a character, check it against the currentToken, and then set
     * the next character in the input stream
     *
     * @param expected the expected token to be eaten
     * @throws ScanErrorException if an unidentified character is encountered
     * @throws IllegalArgumentException if the expected and found tokens are not the same
     */
    private void eat(String expected) throws ScanErrorException, IllegalArgumentException
    {
        if (expected.equals(currentToken))
            currentToken = lex.nextToken();
        else
            throw new IllegalArgumentException(
                    "Expected: " + expected + "but found: " + currentToken);
    }

    /**
     * parseNumber will parse an integer
     *
     * @precondition currentToken is an integer
     * postcondition: number token has been eaten, currentToken is token after the integer
     * @return a Number object with a given integer value
     * @throws ScanErrorException if an unidentified character is encountered
     */
    private Number parseNumber() throws ScanErrorException
    {
        int num = Integer.parseInt(currentToken);
        eat(currentToken);
        return new Number(num);
    }

    /**
     * parseStatement will parse full blocks of statements
     *
     * precondition: currentToken is either WRITELN, BEGIN, or a variable identifier
     * postcondition: a full statement has been eaten, and currentToken
     *                is first token after the statement
     * @return a Statement object of type Writeln, Block, If, While, or Assignment
     * @throws ScanErrorException if an unidentified character is encountered
     */
    private Statement parseStatement() throws ScanErrorException
    {
        if (currentToken.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            Expression e = parseExpression();
            eat(")");
            eat(";");
            return new Writeln(e);
        }
        else if (currentToken.equals("READLN"))
        {
            eat("READLN");
            eat("(");
            String var = currentToken;
            eat(var);
            eat(")");
            eat(";");
            return new Readln(var);
        }
        else if (currentToken.equals("BEGIN"))
        {
            List<Statement> stmts = new ArrayList<>();
            eat("BEGIN");
            while(!currentToken.equals("END"))
            {
                stmts.add(parseStatement());
            }
            eat("END");
            eat(";");
            return new Block(stmts);
        }
        else if (currentToken.equals("IF"))
        {
            eat("IF");
            Expression exp1 = parseExpression();
            String op = currentToken;
            eat(currentToken);
            Expression exp2 = parseExpression();
            Condition cond = new Condition(exp1, exp2, op);
            eat("THEN");
            Statement stmt = parseStatement();
            return new If(cond, stmt);
        }
        else if (currentToken.equals("WHILE"))
        {
            eat("WHILE");
            Expression exp1 = parseExpression();
            String op = currentToken;
            eat(currentToken);
            Expression exp2 = parseExpression();
            Condition cond = new Condition(exp1, exp2, op);
            eat("DO");
            Statement stmt = parseStatement();
            return new While(cond, stmt);
        }
        else if (currentToken.equals("FOR"))
        {
            eat("FOR");
            String var = currentToken;
            eat(currentToken);
            eat(":=");
            Expression exp1 = parseExpression();
            eat("TO");
            Expression exp2 = parseExpression();
            eat("DO");
            Statement body = parseStatement();
            //return new For(var, exp1, exp2, body);
            return body; //delete after
        }
        else
        {
            String name = currentToken;
            eat(currentToken);
            eat(":=");
            Expression e = parseExpression();
            eat(";");
            return new Assignment(name, e);
        }
    }

    /**
     * parseFactor can parse combinations of negative signs, integers, variable ids, or procedures
     *
     * precondition: currentToken is -, (, a variable/procedure id, or an integer
     * postcondition: the factor has been eaten and currentToken is the first token after the factor
     * @return an Expression object of type BinOp, Variable, Number, or ProcedureCall
     * @throws ScanErrorException if an unidentified character is encountered
     */
    private Expression parseFactor() throws ScanErrorException
    {
        if (currentToken.equals("-"))
        {
            eat("-");
            return new BinOp("*", new Number(-1), parseFactor());
        }
        else if (currentToken.equals("("))
        {
            eat("(");
            Expression e = parseExpression();
            eat(")");
            return e;
        }
        else if (lex.isDigit(currentToken.charAt(0)))
        {
            return parseNumber();
        }
        else
        {
            String var = currentToken;
            eat(currentToken);
            if(currentToken.equals("("))
            {
                List<Expression> params = new ArrayList<Expression>();
                eat("(");
                while (!currentToken.equals(")"))
                {
                    params.add(parseExpression());
                    if(currentToken.equals(","))
                        eat(",");
                }
                eat(")");
                return new ProcedureCall(var, params);
            }
            return new Variable(var);
        }
    }

    /**
     * parseTerm can parse full terms of a factor followed by combinations of
     * factors and multiplication or division signs
     *
     * precondition: the currentToken is the start of a factor
     * postcondition: the full term has been eaten and the currentToken
     *                is the first token after the term
     * @return an Expression object of type BinOp, Variable, or Number
     * @throws ScanErrorException if an unidentified character is encountered
     */
    private Expression parseTerm() throws ScanErrorException
    {
        Expression e = parseFactor();
        while (currentToken.equals("/") || currentToken.equals("*"))
        {
            if (currentToken.equals("/"))
            {
                eat("/");
                e = new BinOp("/", e, parseFactor());

            }
            if (currentToken.equals("*"))
            {
                eat("*");
                e = new BinOp("*", e, parseFactor());
            }
        }
        return e;
    }

    /**
     * parseExpression can parse full expressions of a term followed by combinations of
     * terms and subtraction or subtraction signs
     *
     * precondition: the currentToken is the start of a term
     * postconditionL: the full expression has been eaten and the currentToken
     *                 is the first token after the term
     * @return an Expression object of type BinOp, Variable, or Number
     * @throws ScanErrorException if an unidentified character is encountered
     */
    private Expression parseExpression() throws ScanErrorException
    {
        Expression e = parseTerm();
        while (currentToken.equals("-") || currentToken.equals("+"))
        {
            if (currentToken.equals("-"))
            {
                eat("-");
                e = new BinOp("-", e, parseTerm());
            }
            if (currentToken.equals("+"))
            {
                eat("+");
                e = new BinOp("+", e, parseTerm());
            }
        }
        return e;
    }

    /**
     * parseProgram will parse through 0 or more procedure declarations and
     * return a program object with these procedures and a statement
     *
     * @return a Program object which contains all procedures and statements
     * @throws ScanErrorException if an unidentified character is encountered
     */
    private Program parseProgram() throws ScanErrorException
    {
        List<ProcedureDeclaration> procedures = new ArrayList<ProcedureDeclaration>();
        List<String> vars = new ArrayList<String>();
        while (currentToken.equals("VAR"))
        {
            eat("VAR");
            while (!currentToken.equals(";"))
            {
                vars.add(currentToken);
                eat(currentToken);
                if (currentToken.equals(","))
                {
                    eat(",");
                }
            }
            eat(";");
        }
        while (currentToken.equals("PROCEDURE"))
        {
            procedures.add(parseProcedure());
        }
        return new Program(vars, procedures, parseStatement());
    }

    /**
     * Parses a procedure by parsing all of its parameters, local variables,
     * and main body statement
     *
     * @return a new ProcedureDeclaration with all params and variables
     * @throws ScanErrorException if an unidentified character is encounterd
     */
    public ProcedureDeclaration parseProcedure() throws ScanErrorException
    {
        List<String> params = new ArrayList<String>();
        List<String> procVars = new ArrayList<String>();
        eat("PROCEDURE");
        String name = currentToken;
        eat(name);
        eat("(");
        while(!currentToken.equals(")"))
        {
            params.add(currentToken);
            eat(currentToken);
            if(currentToken.equals(","))
                eat(",");
        }
        eat(")");
        eat(";");
        if (currentToken.equals("VAR"))
        {
            eat("VAR");
            while (!currentToken.equals(";"))
            {
                procVars.add(currentToken);
                eat(currentToken);
                if (currentToken.equals(","))
                    eat(",");
            }
            eat(";");
        }
        return new ProcedureDeclaration(name, parseStatement(), params, procVars);
    }

    /**
     * Compiles an input text file written in Pascal into a MIPS assembly program
     *
     * @param args arguments for the command line
     * @throws FileNotFoundException if the file for the scanner does not exist
     * @throws ScanErrorException if an unidentified character  is encountered
     */
    public static void main (String[] args) throws FileNotFoundException, ScanErrorException
    {
        FileInputStream inStream = new FileInputStream(
                new File("C:\\Users\\Puneet\\IdeaProjects\\Compiler\\src\\program.txt"));
        Environment env = new Environment(null);
        Scanner lex = new Scanner(inStream);
        Parser parse = new Parser(lex);
        Program program = parse.parseProgram();
        Emitter e = new Emitter("output.asm");
        program.compile(e);
        program.exec(env);
    }
}
