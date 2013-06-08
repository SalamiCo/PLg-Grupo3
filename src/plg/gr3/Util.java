package plg.gr3;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import plg.gr3.parser.sym;

public final class Util {

    public static final String join (String glue, List<?> objs) {
        StringBuilder sb = new StringBuilder();

        boolean started = false;
        for (Object obj : objs) {
            if (started) {
                sb.append(glue);
            }
            sb.append(String.valueOf(obj));
            started = true;
        }

        return sb.toString();
    }

    private Util () {
        throw new AssertionError();
    }

    public static String getSymbolName (int sym) {
        for (Field field : sym.class.getDeclaredFields()) {
            try {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && Modifier.isFinal(mod)
                    && field.getInt(null) == sym)
                {
                    return field.getName();
                }
            } catch (Exception exc) {
            }
        }

        return "?";
    }
}
