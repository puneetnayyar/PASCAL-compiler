package ast;
import emitter.Emitter;
import environment.Environment;
import java.util.Scanner;

/**
 * The Readln class defines the execution and compilation of statements of the form
 * READLN(Variable); where the user inputs a value for the given variable
 *
 * @author Puneet Nayyar
 * @version March 26 2018
 */
public class Readln extends Statement
{
    private String var;

    /**
     * Creates a Readln object with a given variable name
     *
     * @param var the name of the variable to be set
     */
    public Readln(String var)
    {
        this.var = var;
    }

    /**
     * Executes the Readln statement by asking the user for a
     * value for the variable and setting its value in the Environment
     *
     * @param env the Environment in which variables are stored
     */
    public void exec(Environment env)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Set the value of " + var + " to: ");
        Integer i = scanner.nextInt();
        env.setVariable(var, i);
    }

    /**
     * Compiles Readln statements by taking user input and assigning
     * the given value to variable var
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public void compile(Emitter e)
    {
        e.emit("li $v0, 5\t\t\t#get user input");
        e.emit("syscall");
        e.emit("sw $v0, " + var + "\t\t#assign value in $v0 to " + var);
    }
}
