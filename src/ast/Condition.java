package ast;
import environment.Environment;
import emitter.Emitter;

/**
 * The Condition class defines evaluation and compilatoin of the family of
 * expressions of the form:
 * cond → expr relop expr
 * relop → = | <> | < | > | <= | >=
 *
 * @author Puneet Nayyar
 * @version March 27 2018
 */
public class Condition extends Expression
{
    private Expression exp1;
    private Expression exp2;
    private String op;

    /**
     * Creates a Condition object with two expression and a relation operator
     *
     * @param e1 the first expression
     * @param e2 the second expression
     * @param s the relation operator
     */
    public Condition(Expression e1, Expression e2, String s)
    {
        exp1 = e1;
        exp2 = e2;
        op = s;
    }

    /**
     * Default compile procedure
     * @param e the Emitter object used to create the output assembly file
     */
    public void compile(Emitter e){};

    /**
     * Depending on the instance operator, returns -1 if the condition is false
     * and 1 if true
     *
     * @param env the Environment in which variables are stored
     * @return 1 if true, -1 if false
     */
    public int eval(Environment env)
    {
        if (op.equals("=") && exp1.eval(env)==exp2.eval(env))
            return 1;
        if (op.equals("<>") && exp1.eval(env)!=exp2.eval(env))
            return 1;
        if (op.equals("<") && exp1.eval(env)<exp2.eval(env))
            return 1;
        if (op.equals(">") && exp1.eval(env)>exp2.eval(env))
            return 1;
        if (op.equals("<=") && exp1.eval(env)<=exp2.eval(env))
            return 1;
        if (op.equals(">=") && exp1.eval(env)>=exp2.eval(env))
            return 1;
        return -1;
    }

    /**
     * Compiles 1 expression, pushes it to memory, compiles the second, and then
     * compares the two expressions, based upon relop, jumping to the label
     * if the condition is not satisfied
     *
     * @param e the Emitter object used to create the output assembly file
     * @param label the label jumped to if the condition is not satisfied
     */
    public void compile(Emitter e, String label)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");
        if (op.equals("="))
            e.emit("bne $t0, $v0, " + label + "#if $t0 is not equal to $v0, jump to " + label);
        else if (op.equals("<>"))
            e.emit("beq $t0, $v0, " + label + "#if $t0 is equal to $v0, jump to " + label);
        else if (op.equals("<"))
            e.emit("bge $t0, $v0, " + label + "#if $t0 is greater than $v0, jump to " + label);
        else if (op.equals(">"))
            e.emit("ble $t0, $v0, " + label + "#if $t0 is less than $v0, jump to " + label);
        else if (op.equals("<="))
            e.emit("bgt $t0, $v0, " + label + "#if $t0 is greater than $v0, jump to " + label);
        else if (op.equals(">="))
            e.emit("blt $t0, $v0, " + label + "#if $t0 is less than $v0, jump to " + label);
    }
}
