package com.service.impl;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface TranslateService {
    void asyncTranslate() throws IOException, InterruptedException, ExecutionException ;

    }
