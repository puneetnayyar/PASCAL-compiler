package environment;
import ast.ProcedureDeclaration;

import java.util.HashMap;

/**
 * the Environment class defines a location in which variables can be stored
 * and from which they can be accessed
 *
 * @author Puneet Nayyar
 * @version March 17 2018
 */
public class Environment
{
    private HashMap<String, Integer> vars;
    private HashMap<String, ProcedureDeclaration> procedures;
    private Environment parent;

    /**
     * Creates a new Environment with an empty HashMap for variables and procedures
     * along with a parent Environment
     *
     * @param parent the parent Environment
     */
    public Environment(Environment parent)
    {
        vars = new HashMap<>();
        procedures = new HashMap<>();
        this.parent = parent;
    }

    /**
     * Returns the parent Environment
     *
     * @return the parent of this Environment
     */
    public Environment getParent()
    {
        return parent;
    }

    /**
     * If a given variable exists in the Environment, the value is set;
     * Otherwise, the varible is declared in the global Environment
     *
     * @param variable the given name of the variable
     * @param value the given value for the variable
     */
    public void setVariable(String variable, int value)
    {
        Environment env = this.parent;
        if (vars.containsKey(variable) || env==null )
            declareVariable(variable, value);
        else
        {
            while (env.getParent()!=null)
            {
                env = env.getParent();
            }
            env.declareVariable(variable, value);
        }

    }

    /**
     * Retrieves the value of a variable with a given name from
     * whichever Environment in which the variable exists
     *
     * @param variable the name of the variable being accessed
     * @return the value of the given variable
     */
    public int getVariable(String variable)
    {
        if (vars.containsKey(variable))
            return vars.get(variable);
        return parent.getVariable(variable);
    }

    /**
     * Sets the value of a ProcedureDeclaration in the global Environment
     *
     * @param name the name of the procedure
     * @param proc the actual ProcedureDeclaration
     */
    public void setProcedure(String name, ProcedureDeclaration proc)
    {
        if (parent==null)
            procedures.put(name, proc);
        else
            parent.setProcedure(name, proc);
    }

    /**
     * Gets the value of a procedure from the global Environment
     *
     * @param name the name of the Environment being retrieved
     * @return the ProcedureDeclaration associated with the given name
     */
    public ProcedureDeclaration getProcedure(String name)
    {
        if (parent==null)
            return(procedures.get(name));
        return parent.getProcedure(name);
    }

    /**
     * Sets the value of a variable in this Environment
     *
     * @param variable the name of the variable being set
     * @param value the value of the variable
     */
    public void declareVariable(String variable, int value)
    {
        vars.put(variable, value);
    }
}
