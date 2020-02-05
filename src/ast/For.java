package ast;
import environment.Environment;

/**
 * The For class defines the execution of for loops of the form
 * WHILE Variable := Expression TO Expression DO Statement
 *
 * @author Puneet Nayyar
 * @version March 26 2018
 */
public class For// extends Statement
{
    private String var;
    private Expression exp1;
    private Expression exp2;
    private Statement stmt;

    /**
     * Creates a For object with a given start and end condition and a statement
     *
     * @param v the given Variable name
     * @param e1 the starting condition
     * @param e2 the end condition
     * @param s the body of the for loop
     */
    public For(String v, Expression e1, Expression e2, Statement s)
    {
        var = v;
        exp1 = e1;
        exp2 = e2;
        stmt = s;
    }

    /**
     * Executes the Statement within the for loop a set number of times
     *
     * @param env the Environment in which variables are stored
     */
    public void exec(Environment env)
    {
        int start = exp1.eval(env);
        env.setVariable(var, start);
        for (int i = start; i<=exp2.eval(env); i++)
        {
            stmt.exec(env);
            env.setVariable(var, env.getVariable(var)+1);
        }
    }

}
