package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * The abstract class Statement provides a template for
 * the execution and compilation of different types of statements
 *
 * @author Puneet Nayyar
 * @version March 17 2018
 */
public abstract class Statement
{
    /**
     * Defines the execution of Statements
     *
     * @param env the Environment in which variables are stored
     */
    public abstract void exec(Environment env);

    /**
     * Defines the compilation of Statements
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public abstract void compile(Emitter e);
}
