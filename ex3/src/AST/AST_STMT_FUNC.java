package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

import java.util.Optional;

public class AST_STMT_FUNC extends AST_STMT {
    public AST_EXP_FUNC expFunc;

    public AST_STMT_FUNC(String id, AST_EXP_LIST argsList) {
        this.expFunc = new AST_EXP_FUNC(id, argsList);
    }

    public TYPE SemantMe(Optional<String> fatherClassId) {
        this.expFunc.SemantMe(fatherClassId);
        return null;
    }

}


//                if(paramsList.head == null) {
//                    System.out.println("advancing paramsList is null");
//                }else {
//                    System.out.println("advancing paramsList.head.name = " + paramsList.head.name);
//
//                }
//                if(argsTypes.head == null) {
//                    System.out.println("advancing argsTypes is null");
//                }else {
//                    System.out.println("advancing argsTypes.head.name = " + argsTypes.head.name);
//                }