package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * The Writeln class defines the execution and compilation of Statements of the form
 * WRITELN(stmt);
 *
 * @author Puneet Nayyar
 * @version March 17 2018
 */
public class Writeln extends Statement
{
    private Expression exp;

    /**
     * Creates a Writeln object with a given Expression
     *
     * @param exp the Expression within the Writeln statement
     */
    public Writeln(Expression exp)
    {
        this.exp = exp;
    }

    /**
     * Executes the Writeln statement by printing the evaluation of its Expression
     *
     * @param env the Environment in which variables are stored
     */
    public void exec(Environment env)
    {
        System.out.println(exp.eval(env));

    }

    /**
     * Compiles Writeln statements by first compiling the instance expression
     * and then loading the value into $a0 and printing it out
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public void compile(Emitter e)
    {
        exp.compile(e);
        e.emit("move $a0, $v0\t\t#load value of $v0 into $a0");
        e.emit("li $v0, 1\t\t\t#print value in $a0");
        e.emit("syscall");
        e.emit("la $a0, newline\t\t#load new line character into $a0");
        e.emit("li $v0, 4\t\t\t#print new line");
        e.emit("syscall");
    }
}
