package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * The Assignment class defines the execution and compilation of statements assigning variables
 * of the form variable := expression;
 *
 * @author Puneet Nayyar
 * @version March 17 2018
 */
public class Assignment extends Statement
{
    private String var;
    private Expression exp;

    /**
     * Creates an Assignment object with a given variable name and value
     *
     * @param s the variable being set to the given value
     * @param e the Expression which, when evaluated defines the value of the given variable
     */
    public Assignment(String s, Expression e)
    {
        var = s;
        exp = e;
    }

    /**
     * Executes the Assignment statement by setting the variable var to the value
     * given by Expression exp
     *
     * @param env the Environment in which the variables value will be stored
     */
    public void exec(Environment env)
    {
        env.setVariable(var, exp.eval(env));
    }

    /**
     * Compiles the instance expression and stores the value of the expression
     * in the variable var
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public void compile(Emitter e)
    {
        exp.compile(e);
        if (e.isLocalVariable(var))
        {
            e.emit("sw $v0, " + e.getOffset(var) + "($sp)\t\t#assign value in $v0 to " + var);
        }
        else
            e.emit("sw $v0, " + var + "\t\t#assign value in $v0 to " + var);
    }
}
