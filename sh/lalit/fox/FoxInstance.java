package sh.lalit.fox;

import java.util.HashMap;
import java.util.Map;

public class FoxInstance {
    private FoxClass klass;
    private final Map<String, Object> fields = new HashMap<>();

    FoxInstance(FoxClass klass) {
        this.klass = klass;
    }

    public String toString() {
        return klass.name + " instance";
    }

    Object get(Token name) {
        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }

        FoxFunction method = klass.findMethod(name.lexeme);
        // if a valid method is accessed via a getter,
        // `this` is first defined for the requested method and then is returned
        if (method != null)
            return method.bind(this);

        throw new RuntimeError(name, "Undefined property '" + name.lexeme + "'.");
    }

    void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }
}
