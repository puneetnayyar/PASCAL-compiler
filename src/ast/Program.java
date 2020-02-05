package ast;
import emitter.Emitter;
import environment.Environment;
import java.util.List;

/**
 * The Program class defines a program which consists of procedure declarations
 * and a single statement
 *
 * @author Puneet Nayyar
 * @version March 30 2018
 */
public class Program
{
    private List<ProcedureDeclaration> procedures;
    private List<String> vars;
    private Statement stmt;

    /**
     * Creates a Program object with a given List of procedures and a statement
     *
     * @param procedures the List of procedures in the program
     * @param stmt the main statement of the program
     * @param vars the list of variables to be used in the program
     */
    public Program(List<String> vars, List<ProcedureDeclaration> procedures, Statement stmt)
    {
        this.vars = vars;
        this.procedures = procedures;
        this.stmt = stmt;
    }

    /**
     * First executes each procedure declaration in the program and finally
     * executes the main statement
     *
     * @param env the Environment in which variables are stored
     */
    public void exec(Environment env)
    {
        for (ProcedureDeclaration dec : procedures)
        {
            dec.exec(env);
        }
        stmt.exec(env);
    }

    /**
     * Creates the basic template for a MIPS assembly program, including
     * the header, the .data section with all variables, the .text section, the main
     * label, the standard termination, and also all subroutines
     *
     * @param e the Emitter object used to create the output assembly file
     */
    public void compile(Emitter e)
    {
        e.emit("#Program description");
        e.emit("#@author Puneet Nayyar");
        e.emit("#@version 5/3/18");
        e.emit("\n");
        e.emit(".data");
        for (String v : vars)
        {
            e.emit(v + ":");
            e.emit(".word 0");
        }
        e.emit("newline:");
        e.emit(".asciiz \"\\n\"");
        e.emit(".text");
        e.emit(".globl main");
        e.emit("main:");
        stmt.compile(e);
        e.emit("li $v0, 10\t\t\t#standard termination");
        e.emit("syscall");
        for (ProcedureDeclaration proc : procedures)
        {
            proc.compile(e);
        }
        e.close();
    }
}