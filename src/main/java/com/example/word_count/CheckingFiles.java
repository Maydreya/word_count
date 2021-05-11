package com.example.word_count;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class CheckingFiles {
    public Map<String, Filesmap> countWords(String path) throws IOException{
        Map<String, Filesmap> maps = new TreeMap<>();
        File folder = new File(path);
        File[] listOffiles = folder.listFiles();
        final boolean[] check = new boolean[1];
        IntStream.range(0, listOffiles.length).parallel().forEach(i -> {
            File file = listOffiles[i];
            try {
                check[0] = isBinaryFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(check[0]){
                FileReader fr = null;
                try {
                    fr = new FileReader(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Scanner sc = new Scanner(fr);
                String text = "";
                while (sc.hasNextLine()) {
                    text += sc.nextLine();
                }
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                text = text.trim();
                text = text.toLowerCase();
                text = text.replaceAll("\\W", " ");
                for (String word : text.split(" "))
                {
                    Filesmap filesmap = maps.get(word);
                    if(filesmap == null) {
                        maps.put(word, new Filesmap().createOrUpdateWordCounter(file.toString()));
                    }
                    else {
                        filesmap.createOrUpdateWordCounter(file.toString());
                    }
                }
                maps.remove("");
            }
        });
        return maps;
    }
    public static boolean isBinaryFile(File f) throws FileNotFoundException, IOException {
        FileInputStream in = new FileInputStream(f);
        int size = in.available();
        if(size > 1024) size = 1024;
        byte[] data = new byte[size];
        in.read(data);
        in.close();

        int ascii = 0;
        int other = 0;

        for(int i = 0; i < data.length; i++) {
            byte b = data[i];
            if( b < 0x09 ) return true;

            if( b == 0x09 || b == 0x0A || b == 0x0C || b == 0x0D ) ascii++;
            else if( b >= 0x20  &&  b <= 0x7E ) ascii++;
            else other++;
        }

        if( other == 0 ) return false;

        return 100 * other / (ascii + other) > 95;
    }

}

