package ast;
import environment.Environment;
import java.util.List;
import emitter.Emitter;

/**
 * The ProcedureDeclaration class defines the declaration of a procedure
 * with a given name, executable statement, and parameters
 *
 * @author Puneet Nayyar
 * @version March 30 2018
 */
public class ProcedureDeclaration extends Statement
{
    private String name;
    private List<String> params;
    private List<String> vars;
    private Statement stmt;

    /**
     * Creates a ProcedureDeclaration object with a given name, Statement, and
     * List of parameter names
     *
     * @param name the name of the procedure
     * @param stmt the executable statement body of the procedure
     * @param params the list of param names
     * @param vars the list of local variables
     */
    public ProcedureDeclaration(String name, Statement stmt, List<String> params, List<String> vars)
    {
        this.name = name;
        this.stmt = stmt;
        this.params = params;
        this.vars = vars;
    }

    /**
     * Returns the executable Statement body of this prpcedure
     *
     * @return the Statement body of this procedure
     */
    public Statement getStatement()
    {
        return stmt;
    }

    /**
     * Gets the list of parameter names for this procedure
     *
     * @return the parameter List
     */
    public List<String> getParams()
    {
        return params;
    }

    /**
     * Executes the ProcedureDeclaration by setting its value in the
     * given Environment
     *
     * @param env the Environment in which variables are stored
     */
    public void exec(Environment env)
    {
        env.setProcedure(name, this);
    }

    /**
     * Compiles a ProcedureDeclaration by emitting a procedure label, pushing a return value,
     * setting the procedure context, pushing all local vars onto the stack, pushing the
     * return address, compiling the procedure statement, popping off all previously pushed values,
     * emitting a jr $ra statement, and finally clearing the procedure context
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public void compile(Emitter e)
    {
        e.emit("proc"+ name + ":");
        e.emit("li $v0, 0\t\t#load 0 into $v0");
        e.emitPush("$v0");
        e.setProcedureContext(this);
        for (String v : vars)
        {
            e.emit("li $t0, 0\t\t#load 0 into $t0");
            e.emitPush("$t0");
        }
        e.emitPush("$ra");
        stmt.compile(e);
        e.emitPop("$ra");
        for (String v : vars)
        {
            e.emitPop("$t0");
        }
        e.emitPop("$v0");
        e.emit("jr $ra\t\t\t\t#return to address in $ra");
        e.clearProcedureContext();
    }

    /**
     * Gets the name of this procedure
     *
     * @return the procedure name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns a List of all the procedures local variables
     *
     * @return the list of local vars
     */
    public List<String> getVariables()
    {
        return vars;
    }
}
