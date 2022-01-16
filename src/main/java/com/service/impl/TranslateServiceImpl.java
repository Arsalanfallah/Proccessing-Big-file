package com.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TranslateServiceImpl implements TranslateService {

    private static final Logger logger = LoggerFactory.getLogger(TranslateServiceImpl.class);

    @Value("${source.file.folder.path}")
    String sourceFilePath;

    @Value("${process.file.folder.path}")
    String processFilePath;

    @Value("${header.config.file}")
    String headerConfigFile;

    @Value("${row.config.file}")
    String rowConfigFile;


    @Value("${max.file.size}")
    int maxFileSize;
    @Value("${column.separator}")
    String columnSeparator;

    @Value("${folder.separator}")
    String folderSeparator;


    @Override
    public void asyncTranslate(String fileExtension) throws IOException, InterruptedException, ExecutionException {

        Path sourcePath = Paths.get(sourceFilePath);
        logger.info("sourceFilePath is {}",sourceFilePath);
        if (!Files.isDirectory(sourcePath)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }
        try (
                Stream<Path> paths = Files.walk(sourcePath, 1)) {

            // translate all files from this path
            paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(fileExtension))
                    .forEach(path -> {

                        try {
                            List<CompletableFuture<String>> futures = Collections.synchronizedList(new ArrayList<>());
                            //translate Header sheet
                            Files.lines(path)
                                    .findFirst().map(a -> {
                                try {
                                    return futures.add(translateHeader(a));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    throw new RuntimeException(e);

                                }
                            });

                            //translate rows of file without header
                            List<String> items = Collections.synchronizedList(new ArrayList<>());
                            Files.lines(path)
                                    .skip(1)//skip header
                                    .forEach(line -> {
                                        items.add(line);
                                        if (items.size() % maxFileSize == 0) {
                                            //add completable task for each of 10000 rows
                                            try {
                                                futures.add(CompletableFuture.supplyAsync(translateIdentifier(line)));
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                throw new RuntimeException(e);

                                            }
                                            items.clear();
                                        }
                                    });
                            if (items.size() > 0) {
                                //add completable task for remaining rows
                                items.forEach(line ->
                                {
                                    try {
                                        futures.add(CompletableFuture.supplyAsync(translateIdentifier(line)));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        throw new RuntimeException(e);

                                    }
                                });
                            }

                            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                                    .thenApply($ -> {
                                        //join all task to collect result after all tasks completed
                                        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
                                    })
                                    .thenApply(maps -> {
                                        //write all rows which translated on new file
                                        try {
                                            writeIntoFile(maps, path);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            throw new RuntimeException(e);
                                        }
                                        return futures;
                                    })
                                    .get();
                        } catch (IOException | InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    private CompletableFuture<String> translateHeader(String str)throws Exception {
        AtomicReference<String> strings = new AtomicReference<>("");
        String[] strArray = str.split(columnSeparator);

        //read all items of  headerConfigFile
        String myTextFile = headerConfigFile;
        logger.info("headerConfigFile is {}",myTextFile);
        Path myPath = Paths.get(myTextFile);
        try {
            List<String> list = Files.lines(myPath).collect(Collectors.toList());

            for (int i = 0; i < strArray.length; i++) {
                for (String s : list) {
                    String[] strArray2 = ((String) s).split(columnSeparator);
                    //finding translation of header item
                    if (strArray[i].equals(strArray2[0])) {
                        strArray[i] = strArray2[1];
                        break;
                    }


                }

            }
            //append all columns of one row as String
            StringBuilder str2 = new StringBuilder();
            for (int i = 0; i < strArray.length; i++) {
                str2.append(strArray[i]);
                if (i != strArray.length - 1)
                    str2.append(columnSeparator);
            }
            strings.getAndSet(str2.toString());

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return CompletableFuture.supplyAsync(strings::get);
    }

    private Supplier<String> translateIdentifier(String str)throws IOException {
        AtomicReference<String> strings = new AtomicReference<String>("");
        String[] strArray = str.split(columnSeparator);

        String myTextFile = rowConfigFile;
        logger.info("rowConfigFile is {} ",myTextFile);
        Path myPath = Paths.get(myTextFile);
        try {
            List<String> list = Files.lines(myPath).collect(Collectors.toList());


            for (Object o : list) {
                String line = (String) o;
                String[] strArray2 = line.split(columnSeparator);
                //finding translation of header item
                if (strArray[0].equals(strArray2[0])) {
                    strArray[0] = strArray2[1];
                    break;
                }
            }
            //append all columns of one row as String
            StringBuffer str2 = new StringBuffer();
            for (int i = 0; i < strArray.length; i++) {
                str2.append(strArray[i]);
                if (i != strArray.length - 1)
                    str2.append(columnSeparator);
            }
            strings.getAndSet(str2.toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return strings::get;
    }

    private void writeIntoFile(List<String> maps, Path path)throws IOException {
        Path newPath = Paths.get(processFilePath + path.getFileName());
        try (BufferedWriter writer = Files.newBufferedWriter(newPath, StandardCharsets.UTF_8)) {

            maps.forEach(map -> {
                //write the result of all the tasks
                try {
                    writer.write(String.valueOf(map));
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);

                }
            });
            logger.info("Application create new file {} with rows {} ",path.getFileName(),maps.size());

        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }

    }
}
