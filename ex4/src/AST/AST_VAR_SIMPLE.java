package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TEMP.*;
import IR.*;
import ast_annotation.AstAnnotation;
import SYMBOL_TABLE.*;
import global_variables.GlobalVariables;
import ast_notation_type.AstNotationType;

public class AST_VAR_SIMPLE extends AST_VAR {

	public String name;
	public AstAnnotation astAnnotation;

	public AST_VAR_SIMPLE(String name) {

		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.format("====================== var -> ID( %s )\n", name);

		this.name = name;
	}

	public void PrintMe() {
		System.out.format("AST NODE SIMPLE VAR( %s )\n", name);

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("SIMPLE\nVAR\n(%s)", name));
	}

	@Override
	public TYPE SemantMe(Optional<String> classId) {
		System.out.format("-- AST_VAR_SIMPLE SemantMe\n\t\tvar name = %s\n", name);

		Optional<TYPE> type = SYMBOL_TABLE.getInstance().findPotentialMember(name);
		if (type.isPresent()) {
			if (type.get().isFunction()) {
				System.out.format(">> ERROR [%d] '%s' is a not a variable\n", lineNum, name);
			} else {
				setNotation();
				return type.get();
			}
		} else {
			System.out.format(">> ERROR [%d] variable '%s' does not exist at the current scope/outer scopes\n", lineNum,
					name);
		}

		throw new SemanticErrorException("" + lineNum);
	}

	private void setNotation() {
		System.out.println("-- AST_VAR_SIMPLE setNotation");

		ScopeType scopeType = SYMBOL_TABLE.getInstance().getScopeTypeByEntryName(name);
		System.out.println("\t\tvariable scope type = " + scopeType);
		int varInd = SYMBOL_TABLE.getInstance().findEntry(name).get().position;
		AstNotationType astNotationType = SYMBOL_TABLE.getInstance().findEntry(name).get().astNotationType;

		if (scopeType == ScopeType.Global) {
			astAnnotation = new AstAnnotation(AstAnnotation.TYPE.GLOBAL_VAR, Optional.empty());
			System.out.format("\t\t%s is a global variable\n", name);
		} else if (astNotationType == AstNotationType.parameter) {
			astAnnotation = new AstAnnotation(AstAnnotation.TYPE.PARAMETER, Optional.of(varInd));
			System.out.format("\t\t%s is a parameter | its index = %s\n", name, varInd);
		} else { // local
			astAnnotation = new AstAnnotation(AstAnnotation.TYPE.LOCAL_VAR, Optional.of(varInd));
			System.out.format("\t\t%s is a local variable | its index = %s\n", name, varInd);
		}
	}

	public TEMP IRme() {
		System.out.println("-- AST_VAR_SIMPLE IRme");

		TEMP tmp = TEMP_FACTORY.getInstance().getFreshTEMP();
		System.out.println("SIMPLE is " + tmp.getSerialNumber());

		if (astAnnotation.type == AstAnnotation.TYPE.GLOBAL_VAR) {
			System.out.format("\t\t%s is a global variable\n", name);

			String globalVarLabel = GlobalVariables.getGlobalVarLabel(name);

			IR.getInstance().Add_IRcommand(new IRcommand_Initialize_Tmp_With_Global_Var(tmp, globalVarLabel));
		} else if (astAnnotation.type == AstAnnotation.TYPE.LOCAL_VAR) {
			System.out.format("\t\t%s is a local variable\n", name);
			IR.getInstance().Add_IRcommand(new IRcommand_Initialize_Tmp_With_Local_Var(tmp, astAnnotation.ind.get()));
		} else { // parameter
			System.out.format("\t\t%s is a parameter\n", name);
			IR.getInstance().Add_IRcommand(new IRcommand_Initialize_Tmp_With_Parameter(tmp, astAnnotation.ind.get()));
		}

		return tmp;
	}
}
