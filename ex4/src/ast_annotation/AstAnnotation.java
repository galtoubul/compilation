package ast_annotation;

import java.util.Optional;

public class AstAnnotation {
    public enum TYPE {
        GLOBAL_VAR,
        LOCAL_VAR,
        PARAMETER,
        FIELD;
    }

    public TYPE type;
    public Optional<Integer> ind;

    public AstAnnotation(TYPE type, Optional<Integer> ind) {
        this.type = type;
        this.ind = ind;
    }
}