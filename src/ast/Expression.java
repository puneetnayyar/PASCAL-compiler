package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * The abstract class Expression provides a template for
 * numerical expressions which can be evaluated and compiled
 *
 * @author Puneet Nayyar
 * @version March 17 2018
 */
public abstract class Expression
{
    /**
     * Defines the evaluation of numerical Expressions
     *
     * @param env the Environment in which variables are stored
     * @return the value of the Expression
     */
    public abstract int eval(Environment env);

    /**
     * Defines the compilation of numerical Expressions
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public abstract void compile(Emitter e);

}
