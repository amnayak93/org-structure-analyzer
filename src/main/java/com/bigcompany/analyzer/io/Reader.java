package com.bigcompany.analyzer.io;

import java.io.IOException;

public interface Reader<T,S> {

    T read(S source) throws IOException;
}
