package com.config;

import com.service.impl.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Component
public class TranslateListener {
    @Value("${fileExtension}")
    String fileExtension;

    final TranslateService translateServiceImpl;

    @Autowired
    public TranslateListener(TranslateService translateServiceImpl) {
        this.translateServiceImpl = translateServiceImpl;
    }

    @EventListener(value = ApplicationReadyEvent.class)
    public void testLargeFile() throws Exception {
        long premem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long start = System.currentTimeMillis();
        int mb = 1024000;
        System.out.println("Used memory pre run (MB): " + (premem / mb));
        try {
             translateServiceImpl.asyncTranslate(fileExtension);//process file asynchronously and print details
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw e;
        }

        long postmem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        System.out.println("Used memory post run (MB): " + (postmem / mb));
        System.out.println("Memory consumed (MB): " + (postmem - premem) / mb);
        System.out.println("Time taken in MS: " + (System.currentTimeMillis() - start));
    }
}
