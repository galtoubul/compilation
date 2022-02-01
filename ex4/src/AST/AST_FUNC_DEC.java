package AST;

import java.util.Optional;

import SYMBOL_TABLE.*;
import TYPES.*;
import IR.*;

public class AST_FUNC_DEC extends AST_Node {
    public AST_TYPE returnTypeName;
    public String id;
    public AST_STMT_LIST body;
    public Optional<AST_PARM_LIST> params;
    public Optional<String> methodClass;
    public int localVarsNum;

    public AST_FUNC_DEC(AST_TYPE returnTypeName, String id, AST_STMT_LIST body, Optional<AST_PARM_LIST> params) {
        System.out.println("-- AST_FUNC_DEC ctor\n\n\t line num = " + lineNum);
        this.returnTypeName = returnTypeName;
        this.id = id;
        this.body = body;
        this.params = params;
    }

    public void PrintMe() {
        System.out.format("FUNC(%s):%s\n", id, returnTypeName);

        /***************************************/
        /* RECURSIVELY PRINT params + body ... */
        /***************************************/
        if (params.isPresent())
            params.get().PrintMe();
        if (body != null)
            body.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("FUNC(%s)\n:%s\n", id, returnTypeName));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (params.isPresent())
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, params.get().SerialNumber);
        if (body != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
    }

    public void checkOverloading(Optional<String> fatherClassId) {

        TYPE_CLASS fatherType = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(fatherClassId.get());
        System.out.println("-- AST_FUNC_DEC\n\t\t the method is part of a class that extends class " + fatherType.name);

        for (TYPE member : fatherType.data_members) {
            System.out.println("-- AST_FUNC_DEC SemantMe\n\t\twhile (fatherType.data_members nonempty)");
            if (member instanceof TYPE_FUNCTION ||
                    member instanceof TYPE_VOID ||
                    member instanceof TYPE_CLASS_VAR_DEC) {

                TYPE dm = member;
                System.out.format("-- AST_FUNC_DEC SemantMe\n\t\t%s is a method/field\n", dm.name);

                if (dm.name.equals(id)) {
                    System.out.format("-- AST_FUNC_DEC SemantMe\n\t\tdata member name = %s == %s = method name\n",
                            dm.name, id);
                    if (member instanceof TYPE_CLASS_VAR_DEC) {
                        System.out.println(">> ERROR [line] overloading field and method names isn't allowed");
                        throw new SemanticErrorException("" + lineNum);
                    } else if (member instanceof TYPE_VOID
                            && !this.returnTypeName.name().equals("void")) {
                        System.out.println(">> ERROR [line] overloading methods isn't allowed");
                        throw new SemanticErrorException("" + lineNum);
                    } else if (member instanceof TYPE_FUNCTION &&
                            !((TYPE_FUNCTION) dm).returnType.name.equals(this.returnTypeName.name())) {
                        System.out.format("-- AST_FUNC_DEC SemantMe\n\t\tthis.returnTypeName.name() = %s\n",
                                this.returnTypeName.name());
                        System.out.format("-- AST_FUNC_DEC SemantMe\n\t\tdm.returnType.name = %s\n",
                                ((TYPE_FUNCTION) dm).returnType.name);
                        System.out.println(">> ERROR [line] overloading methods isn't allowed");
                        throw new SemanticErrorException("" + lineNum);
                    }
                }
            }
        }
    }

    public TYPE SemantMe(Optional<String> fatherClassId) {
        System.out.format("-- AST_FUNC_DEC SemantMe%s\n", fatherClassId.isPresent() ? " extends" : "");

        TYPE returnType = this.returnTypeName.getTYPE(lineNum);

        // Check that id does NOT exist at the same scope
        if (SYMBOL_TABLE.getInstance().isInScope(id)) {
            System.out.format(">> ERROR [" + lineNum + "] '%s' is already defined\n", id);
            throw new SemanticErrorException("" + lineNum);
        }

        // if this is a method of a class that extends another class then check for
        // overloading violations
        if (fatherClassId.isPresent()) {
            checkOverloading(fatherClassId);
        }

        if (SYMBOL_TABLE.getInstance().currentScopeType().equals(ScopeType.Class)) {
            methodClass = SYMBOL_TABLE.getInstance().currentScope().scopeName;
        }

        // Begin Function Scope
        SYMBOL_TABLE.getInstance().beginScope(ScopeType.Function, id);
        System.out.println("\t\tStart of a new scope for function/method " + this.id);

        // Semant Input Params
        TYPE_LIST paramsTypesList = null;
        if (params.isPresent()) {
            paramsTypesList = params.get().SemantMe(0);
        }

        // Enter the function/method Type to the Symbol Table
        System.out.println("\t\tlocalVarsNum = " + localVarsNum);
        TYPE_FUNCTION funcType = new TYPE_FUNCTION(returnType, id, paramsTypesList);
        SYMBOL_TABLE.getInstance().enter(id, funcType, false);

        System.out.println("\t\tline number = " + lineNum);

        // Semant function/method body
        body.SemantMe(Optional.empty(), 0);

        // End Function Scope
        SYMBOL_TABLE.getInstance().endScope();
        System.out.println("\t\tEnding of a new scope for function/method " + this.id);

        // Enter the function/method Type to the Symbol Table again, as it was
        // popped-out with the rest of the scope
        // Also update the number of local variables
        this.localVarsNum = this.body.localVarsNum();
        funcType = new TYPE_FUNCTION(returnType, id, paramsTypesList, localVarsNum);
        SYMBOL_TABLE.getInstance().enter(id, funcType, false);

        return funcType;
    }

    // TODO
    public void IRme() {
        System.out.println("-- AST_FUNC_DEC IRme");
        id = this.methodClass.isPresent() ? id + "_" + this.methodClass.get() : id;
        IR.getInstance().Add_IRcommand(new IRcommand_Label("function_" + id)); // Avoid labels with reserved names
        IR.getInstance().Add_IRcommand(new IRcommand_Func_Prologue(id, localVarsNum));
        IR.getInstance().Add_IRcommand(new IRcommand_Label(id + "_after_prologue"));

        if (this.params.isPresent()) {
            this.params.get().IRme();
        }

        if (body != null) {
            body.IRme(); // including return
        }

        IR.getInstance().Add_IRcommand(new IRcommand_Func_Epilogue(id));
    }
}
