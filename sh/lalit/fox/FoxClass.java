package sh.lalit.fox;

import java.util.List;
import java.util.Map;

public class FoxClass implements FoxCallable {
    final String name;
    private final Map<String, FoxFunction> methods;
    final FoxClass superclass;

    FoxClass(String name, FoxClass superclass, Map<String, FoxFunction> methods) {
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
    }

    FoxFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }
        if (superclass != null) {
            return superclass.findMethod(name);
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int arity() {
        FoxFunction initializer = findMethod("init");
        if (initializer == null) {
            return 0;
        }
        return initializer.arity();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        FoxInstance instance = new FoxInstance(this);
        FoxFunction initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }
}