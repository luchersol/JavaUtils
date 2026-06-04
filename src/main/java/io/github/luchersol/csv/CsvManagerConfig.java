package io.github.luchersol.csv;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CsvManagerConfig<T> {

    private String path;

    private Class<T> clazz;

    @Builder.Default
    private String separator = ",";

    @Builder.Default
    private boolean skipHeader = true;

}
