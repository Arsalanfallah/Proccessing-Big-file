package com.service;

import com.service.impl.TranslateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@SpringBootTest()
public class TranslateServiceTest {
    @Value("${fileExtension}")
    String fileExtension;
    @Autowired
    TranslateService translateService;

    @Test
    public void asyncTranslate() {
        try {
            translateService.asyncTranslate();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
