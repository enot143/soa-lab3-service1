package itmo.server.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {
    private CriteriaDef def;
    private Operation operation;
    private String var1;
    private String var2;


    public enum Operation {
        BETWEEN ("between"),
        EQ ("eq"),
        LIKE ("like");
        private final String name;
        Operation(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
        public static Operation fromString(String s) {
            for (Operation b : Operation.values()) {
                if (b.getName().equalsIgnoreCase(s)) {
                    return b;
                }
            }
            return null;
        }
        public static boolean contains(String s) {
            for (Operation c : Operation.values()) {
                if (c.getName().equalsIgnoreCase(s)) {
                    return true;
                }
            }
            return false;
        }
    }

}

