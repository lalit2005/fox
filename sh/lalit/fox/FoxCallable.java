package sh.lalit.fox;

import java.util.List;

public interface FoxCallable {
    int arity();

    Object call(Interpreter interpreter, List<Object> arguments);
}