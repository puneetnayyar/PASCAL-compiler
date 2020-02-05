package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * The If class defines the execution and compilation of If statements
 * of the form IF condition THEN statement
 *
 * @author Puneet Nayyar
 * @version March 17 2018
 */
public class If extends Statement
{
    private Condition cond;
    private Statement stmt;

    /**
     * Creates an If object with a given Condition and Statement
     *
     * @param c the given Condition to be evaluated
     * @param s the given Statement to be executed
     */
    public If(Condition c, Statement s)
    {
        cond = c;
        stmt = s;
    }

    /**
     * Executes the the statement inside the if statement if the condition is evaluated to be true
     *
     * @param env the Environment in which variables are stored
     */
    public void exec(Environment env)
    {
        if (cond.eval(env)==1)
        {
            stmt.exec(env);
        }
    }

    /**
     * Gets a label from the given emitter and uses the instance condition
     * to jump over the statement if the If statement is false
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public void compile(Emitter e)
    {
        String labelID = "endif" + e.nextLabelID();
        cond.compile(e, labelID);
        stmt.compile(e);
        e.emit(labelID + ":");
    }
}
