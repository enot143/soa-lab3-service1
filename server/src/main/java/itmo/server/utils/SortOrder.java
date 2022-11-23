package itmo.server.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortOrder {
   private boolean asc;
   private CriteriaDef def;

    public SortOrder(CriteriaDef def) {
        this.def = def;
    }
}
