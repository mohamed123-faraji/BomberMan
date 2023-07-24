package fr.malibu.assistants.jws.utils.types;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResult<T> {
    private int first;
    private int last;
    private int limit;
    private int total;
    private List<T> data;
}