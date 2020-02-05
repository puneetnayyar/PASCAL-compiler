package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * The BinOp class defines the evaluation and compilatoin of simple arithmetic operations
 * with two Expressions and one operator (* / + -)
 *
 * @author Puneet Nayyar
 * @version March 17 2018
 */
public class BinOp extends Expression
{
    private String op;
    private Expression exp1;
    private Expression exp2;

    /**
     * Creates a BinOp object with an operator and two Expressions
     *
     * @param s the given operator for the operation
     * @param e1 the first expression
     * @param e2 the second expression
     */
    public BinOp(String s, Expression e1, Expression e2)
    {
        op = s;
        exp1 = e1;
        exp2 = e2;
    }

    /**
     * Depending on the the operator op, evaluates a binary operation with the two Expressions
     * and returns the value
     *
     * @param env the Environment in which variables are stored
     * @return the value of the binary operation
     */
    public int eval(Environment env)
    {
        switch (op)
        {
            case "*":
                return (exp1.eval(env) * exp2.eval(env));
            case "/":
                return (exp1.eval(env) / exp2.eval(env));
            case "+":
                return (exp1.eval(env) + exp2.eval(env));
            default:
                return (exp1.eval(env) - exp2.eval(env));
        }
    }

    /**
     * Compiles 1 instance expression, pushes it into memory, compiles the second expression
     * and then performs the binary operation corresponding to op, storing the value in $v0
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public void compile(Emitter e)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");

        if (op.equals("*"))
        {
            e.emit("mult $t0, $v0\t\t#multiply $t0 and $v0");
            e.emit("mflo $v0\t\t\t#put product into $v0");
        }
        else if (op.equals("/"))
        {
            e.emit("div $t0, $v0\t\t#divide $t0 by $v0");
            e.emit("mflo $v0\t\t\t#put quotient into $v0");
        }
        else if (op.equals("+"))
            e.emit("addu $v0, $t0, $v0\t#add $t0 and $v0 and put sum into $v0");
        else
            e.emit("subu $v0, $t0, $v0\t#subtract $v0 from $t0 and put difference into $v0");
    }
}
