package com.ivoice.actors.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
public class AdditionalFunc {


    private static File file = new File("src/main/resources/logs.txt");
    private static ConcurrentLinkedDeque<String> lines = new ConcurrentLinkedDeque<>();   ///с обычным List-ом возникают ошибки сязанные с многопоточностью..

    private AdditionalFunc(){}

    public static void logToFile(String str, String from) throws IOException {
        if (!file.exists()){
            file.mkdir();
        }
        lines.add("LOGS FROM: " + from);
        lines.add(str + "\n");

        FileUtils.writeLines(file, "UTF-8", lines);
    }

}
