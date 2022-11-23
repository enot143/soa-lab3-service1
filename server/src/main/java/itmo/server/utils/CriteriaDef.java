package itmo.server.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaDef {
    private String criteria;
    private String nameOfColumn;
    private Table tableClass;
}



