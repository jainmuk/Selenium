package cucumber.runtime.groovy;

import cucumber.runtime.snippets.Snippet;
import io.cucumber.datatable.DataTable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GroovySnippet implements Snippet {
    @Override
    public final String arguments(Map<String, Type> arguments) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Type> argType : arguments.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(getArgType(argType.getValue())).append(" ").append(argType.getKey());
        }
        return sb.toString();
    }

    private String getArgType(Type argType) {
        if (argType instanceof Class) {
            Class cType = (Class) argType;
            if (cType.equals(DataTable.class)) {
                return cType.getName();
            }
            return cType.getSimpleName();
        }

        // Got a better idea? Send a PR.
        return argType.toString();
    }

    @Override
    public String template() {
        return "{0}(/{1}/) '{' {3} ->\n" +
                "    // {4}\n" +
                "    throw new PendingException()\n" +
                "'}'\n";
    }

    @Override
    public final String tableHint() {
        return "" +
                "    // For automatic transformation, change DataTable to one of\n" +
                "    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or\n" +
                "    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,\n" +
                "    // Double, Byte, Short, Long, BigInteger or BigDecimal.\n" +
                "    //\n" +
                "    // For other transformations you can register a DataTableType.\n"; //TODO: Add doc URL
    }

    @Override
    public final String escapePattern(String pattern) {
        return pattern.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
