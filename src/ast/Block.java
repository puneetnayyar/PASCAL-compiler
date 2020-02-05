package ast;
import environment.Environment;
import java.util.*;
import emitter.Emitter;

/**
 * The Block class defines the execution and compilation of blocks of statements beginning
 * with BEGIN and ending with END
 *
 * @author Puneet Nayyar
 * @version March 17 2018
 */
public class Block extends Statement
{
    private List<Statement> stmts;

    /**
     * Creates a Block object with a given list of statements contained within the Block
     *
     * @param statements the List of statements in the Block
     */
    public Block(List<Statement> statements)
    {
        stmts = statements;
    }

    /**
     * Executes all statements within the List stmts
     *
     * @param env the Environment in which variables are stored
     */
    public void exec(Environment env)
    {
        for (Statement s : stmts)
        {
            s.exec(env);
        }
    }

    /**
     * Compiles all of the statements contained within the block
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public void compile(Emitter e)
    {
        for (Statement s : stmts)
        {
            s.compile(e);
        }
    }
}
