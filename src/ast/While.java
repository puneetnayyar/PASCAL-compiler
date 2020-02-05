package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * The While class defines the execution and compilation
 * of while loops of the form
 * WHILE condition DO statement
 *
 * @author Puneet Nayyar
 * @version March 17 2018
 */
public class While extends Statement
{
    private Condition cond;
    private Statement stmt;

    /**
     * Creates a While object with a given Condition and Statement
     *
     * @param c the Condition of the while loop
     * @param s the statement within the while loop
     */
    public While(Condition c, Statement s)
    {
        cond = c;
        stmt = s;
    }

    /**
     * Executes the Statement within the while loop as long as
     * its Condition is true
     *
     * @param env the Environment in which variables are stored
     */
    public void exec(Environment env)
    {
        while (cond.eval(env) == 1)
        {
            stmt.exec(env);
        }
    }

    /**
     * Uses the instance condition to compile a while statement by
     * assigning a label to the while loop and a label to jump to
     * after the loop is complete
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public void compile(Emitter e)
    {
        int count = e.nextLabelID();
        String labelID = "endif" + count;
        String whileID = "while" + count;
        e.emit(whileID + ":");
        cond.compile(e, labelID);
        stmt.compile(e);
        e.emit("j " + whileID + "\t\t\t#jump to " + whileID);
        e.emit(labelID + ":");
    }
}
