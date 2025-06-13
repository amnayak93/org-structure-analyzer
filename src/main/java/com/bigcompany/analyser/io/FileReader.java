package com.bigcompany.analyser.io;

import java.io.IOException;
import java.util.List;

public interface FileReader<T> extends Reader<List<T>, String> {

    @Override
    List<T> read(String source) throws IOException;
}
