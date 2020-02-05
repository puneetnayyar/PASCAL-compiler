package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * The Variable class defines the evaluation and compilatoin
 * of variables and their values
 *
 * @author Puneet Nayyar
 * @version March 17 2018
 */
public class Variable extends Expression
{
    private String name;

    /**
     * Creates a Variable object with a given name
     *
     * @param s the name of the variable
     */
    public Variable(String s)
    {
        name = s;
    }

    /**
     * Evaluates the value of the Variable using the given Environment
     *
     * @param env the Environment in which variables are stored
     * @return the value of the Variable
     */
    public int eval(Environment env)
    {
        return env.getVariable(name);
    }


    /**
     * Compiles Variables by retrieving the variable from memory
     * and loading its value into $v0
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public void compile(Emitter e)
    {
        if (e.isLocalVariable(name))
        {
            int offset = e.getOffset(name);
            e.emit("la $t0, " + offset + "($sp)\t\t#load address of " + name + " into $t0");
            e.emit("lw $v0, ($t0)\t\t#load value of address in $t0 to $v0");
        }
        else
        {
            e.emit("la $t0, " + name + "\t\t#load address of " + name + " into $t0");
            e.emit("lw $v0, ($t0)\t\t#load value of address in $t0 to $v0");
        }
    }
}
