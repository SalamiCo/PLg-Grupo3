package plg.gr3;

import java.util.List;

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
}
