package ast;
import emitter.Emitter;
import environment.Environment;

/**
 * The Number class defines the evaluation and compilation of single numbers
 *
 * @author Puneet Nayyar
 * @version March 17 2018
 */
public class Number extends Expression
{
    private int value;

    /**
     * Creates a Number object with a given value
     *
     * @param num the value for the Number
     */
    public Number(int num)
    {
        value = num;
    }

    /**
     * Evaluates the value of the number
     *
     * @param env the Environment in which variables are stored
     * @return the value of the number
     */
    public int eval(Environment env)
    {
        return value;
    }

    /**
     * Loads the value of the Number into $v0
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public void compile(Emitter e)
    {
        e.emit("li $v0, " + value + "\t\t\t#assign value of "+value+" to $v0");
    }
}
