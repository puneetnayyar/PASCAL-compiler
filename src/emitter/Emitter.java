package emitter;
import ast.ProcedureDeclaration;
import java.util.*;
import java.io.*;

/**
 * The Emitter class's function is to create MIPS assembly code by
 * creating an output file into which all compiled code is placed
 *
 * @author Anu Datar
 * @author Puneet Nayyar
 *
 * @version 5/3/18
 */
public class Emitter
{
    private PrintWriter out;
    private int labelID;
    private ProcedureDeclaration procedure;
    private int excessStackHeight;

    /**
     * Creates an emitter for writing to a new file with given name
     *
     * @param outputFileName the name of the file in which the MIPS code will be written
     */
    public Emitter(String outputFileName)
    {
        procedure = null;
        labelID = 0;
        excessStackHeight = 0;
        try
        {
            out = new PrintWriter(new FileWriter(outputFileName), true);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    /**
     * Prints one line of code to file (with non-labels indented)
     *
     * @param code the code being written onto the output file
     */
    public void emit(String code)
    {
        if (!code.endsWith(":"))
            code = "\t" + code;
        out.println(code);
    }

    /**
     * Outputs MIPS code to push the value of the given register
     * onto the stack
     *
     * @param reg the register whose value will be pushed
     */
    public void emitPush(String reg)
    {
        emit("subu $sp, $sp, 4\t#subtract 4 from the stack pointer");
        emit("sw " + reg + ", ($sp)\t\t#push value of " + reg +  " onto the stack");
        excessStackHeight+=1;
    }

    /**
     * Outputs MIPS code to pop the value of the given register
     * off of the stack
     *
     * @param reg the register into which the the value
     *            of the stack will be popped
     */
    public void emitPop(String reg)
    {
        emit("lw " + reg + ", ($sp)\t\t#pop the stack and place value into " + reg);
        emit("addu $sp, $sp, 4\t#add 4 to the stack pointer");
        excessStackHeight-=1;
    }

    /**
     * Closes the file, should be called after all calls to emit
     */
    public void close()
    {
        out.close();
    }

    /**
     * Starting with 1, increments the value of the label counter
     *
     * @return the value of the label counter
     */
    public int nextLabelID()
    {
        labelID++;
        return labelID;
    }

    /**
     * Sets the current procedure and resets the excessStackHeight
     *
     * @param proc the new current procedure
     */
    public void setProcedureContext(ProcedureDeclaration proc)
    {
        excessStackHeight = 0;
        procedure = proc;
    }

    /**
     * Clears the current procedure
     */
    public void clearProcedureContext()
    {
        procedure = null;
    }

    /**
     * Determines whether the given variable name is a local variable of the current procedure,
     * including the return value stored in a variable with the same name as the procedure
     *
     * @param varName the variable being checked for as local
     * @return true if the given variable is a local variable; Otherwise,
     *         false
     */
    public boolean isLocalVariable(String varName)
    {
        if (procedure == null)
            return false;
        if (varName.equals(procedure.getName()))
            return true;
        List<String> procVars = procedure.getVariables();
        for (String var : procVars)
        {
            if (varName.equals(var))
                return true;
        }
        List<String> params = procedure.getParams();
        for (String param : params)
        {
            if (varName.equals(param))
                return true;
        }
        return false;
    }

    /**
     * Gets the offset to add to the stack pointer in order to properly retrieve
     * variables, params, return addresses, and return values from the stack
     *
     * @param localVarName the name of the whose stack offset is being returned
     * @return the numerical offset
     */
    public int getOffset(String localVarName)
    {
        List<String> params = procedure.getParams();
        List<String> vars = procedure.getVariables();
        if (procedure.getName().equals(localVarName))
        {
            return excessStackHeight*4;
        }
        int  paramIndex = params.indexOf(localVarName);
        int  varIndex = vars.indexOf(localVarName);
        if (varIndex == -1)
        {
            return (excessStackHeight+params.size()-paramIndex)*4;
        }
        else
        {
            return (excessStackHeight-1-varIndex)*4;
        }
    }
}