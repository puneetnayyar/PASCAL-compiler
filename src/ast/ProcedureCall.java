package ast;
import environment.Environment;
import java.util.List;
import emitter.Emitter;

/**
 * The ProcedureCall class defines the evaluation of procedures with concrete
 * parameters and return values
 *
 * @author Puneet Nayyar
 * @version March 30 2018
 */
public class ProcedureCall extends Expression
{
    private String name;
    private List<Expression> params;

    /**
     * Creates a ProcedureCall object with a given name and list of
     * parameters
     *
     * @param name the name of the procedure
     * @param exps the list of parameter expressions
     */
    public ProcedureCall(String name, List<Expression> exps)
    {
        this.name = name;
        params = exps;
    }

    /**
     * Evaluates the ProcedureCall by creating a child Environment to store
     * parameter variables and executing the body of the ProcedureDeclaration
     * and returning its value
     *
     * @param env the Environment in which variables are stored
     * @return the value of the procedure, if any
     */
    public int eval(Environment env)
    {
        Environment subEnv = new Environment(env);
        ProcedureDeclaration proc = env.getProcedure(name);
        List<String> paramNames = proc.getParams();
        subEnv.declareVariable(name, 0);
        for(int i=0; i<params.size(); i++)
        {
            subEnv.declareVariable(paramNames.get(i), params.get(i).eval(env));
        }
        proc.getStatement().exec(subEnv);
        return subEnv.getVariable(name);
    }

    /**
     * Compiles a procedure call by compiling all its parameters and emitting a jal statement
     * to the procedure
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public void compile(Emitter e)
    {
        for (Expression exp : params)
        {
            exp.compile(e);
            e.emitPush("$v0");
        }
        e.emit("jal proc" + name + "\t\t#jump and link to proc" + name);
    }
}
