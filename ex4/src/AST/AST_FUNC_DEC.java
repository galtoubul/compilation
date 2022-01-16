package AST;

import java.util.Optional;

import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;
import IR.*;

public class AST_FUNC_DEC extends AST_Node {
    public AST_TYPE returnTypeName;
    public String id;
    public AST_STMT_LIST body;
    public Optional<AST_PARM_LIST> params;

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

        TYPE_LIST dataMembers = fatherType.data_members;

        while (dataMembers != null && dataMembers.head != null) {
            System.out.println("-- AST_FUNC_DEC SemantMe\n\t\twhile (dataMembers.head != null)");
            if (dataMembers.head instanceof TYPE_FUNCTION ||
                    dataMembers.head instanceof TYPE_VOID ||
                    dataMembers.head instanceof TYPE_CLASS_VAR_DEC) {

                TYPE dm = dataMembers.head;
                System.out.format("-- AST_FUNC_DEC SemantMe\n\t\t%s is a method/field\n", dm.name);

                if (dm.name.equals(id)) {
                    System.out.format("-- AST_FUNC_DEC SemantMe\n\t\tdata member name = %s == %s = method name\n",
                            dm.name, id);
                    if (dataMembers.head instanceof TYPE_CLASS_VAR_DEC) {
                        System.out.println(">> ERROR [line] overloading field and method names isn't allowed");
                        throw new SemanticErrorException("" + lineNum);
                    } else if (dataMembers.head instanceof TYPE_VOID
                            && !this.returnTypeName.name().equals("void")) {
                        System.out.println(">> ERROR [line] overloading methods isn't allowed");
                        throw new SemanticErrorException("" + lineNum);
                    } else if (dataMembers.head instanceof TYPE_FUNCTION &&
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
            dataMembers = dataMembers.tail;
        }
    }

    private int getLocalVarsNum() {
        int localVarsNum = 0;
        AST_STMT_LIST ptr1 = this.body;
        while (ptr1 != null && ptr1.head != null) {
            if (ptr1.head instanceof AST.AST_STMT_VAR_DEC) {
                localVarsNum++;
            }
            ptr1 = ptr1.tail;
        }
        return localVarsNum;
    }

    public TYPE SemantMe(Optional<String> fatherClassId) {
        System.out.format("-- AST_FUNC_DEC SemantMe%s\n", fatherClassId.isPresent() ? " extends" : "");

        // Check that the return type is legal
        // TYPE returnType =
        // SYMBOL_TABLE.getInstance().find(this.returnTypeName.name());
        // if (returnType == null) {
        // System.out.format(">> ERROR [" + lineNum + "] non existing return type\n");
        // throw new SemanticErrorException("" + lineNum);
        // }

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

        // Begin Function Scope
        SYMBOL_TABLE.getInstance().beginScope(ScopeType.Function, id);
        System.out.println("\t\tStart of a new scope for function/method " + this.id);

        // Semant Input Params
        TYPE_LIST paramsTypesList = null;
        if (params.isPresent()) {
            paramsTypesList = params.get().SemantMe(0);
        }

        // Enter the function/method Type to the Symbol Table
        int localVarsNum = getLocalVarsNum();
        System.out.println("\t\tlocalVarsNum = " + localVarsNum);
        TYPE_FUNCTION funcType = new TYPE_FUNCTION(returnType, id, paramsTypesList, localVarsNum);
        SYMBOL_TABLE.getInstance().enter(id, funcType, false);

        System.out.println("\t\tline number = " + lineNum);

        // Semant function/method body
        body.SemantMe(Optional.empty());

        // End Function Scope
        SYMBOL_TABLE.getInstance().endScope();
        System.out.println("\t\tEnding of a new scope for function/method " + this.id);

        // Enter the function/method Type to the Symbol Table again, as it was
        // popped-out with the rest of the scope
        SYMBOL_TABLE.getInstance().enter(id, funcType, false);

        return funcType;
    }

    // TODO
    public TEMP IRme() {
        System.out.println("-- AST_FUNC_DEC IRme");

        IR.getInstance().Add_IRcommand(new IRcommand_Label(id));
        IR.getInstance().Add_IRcommand(new IRcommand_Func_Prologue(id));

        if (this.params.isPresent()) {
            this.params.get().IRme();
        }

        if (body != null) {
            body.IRme();
        }

//        IR.getInstance().Add_IRcommand(new IRcommand_Func_Epilogue(id));

        return null;
    }
}
