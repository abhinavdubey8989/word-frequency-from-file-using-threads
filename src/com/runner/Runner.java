package com.runner;

import com.workerThread.WorkerThread;
import java.util.*;
import java.lang.*;
import java.io.*;

/**
 *
 * read from input file line by line
 *
 * append to each threads' private sentence
 *
 * start all threads
 *
 * form a final map
 *
 *
 *
 *
 */

public class Runner {

    List<WorkerThread> threads;
    final static int THREAD_COUNT = 3;
    final static String FILE_ABSOLUTE_LOCATION = "/path/to/.txt/file";


    public static void main(String[] args) {

        Runner r = new Runner();
        r.setupThreads();
        r.startThreads();
        r.showStats();


    }

    void setupThreads() {
        this.threads = new LinkedList<>();

        //initialize the list of threads
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads.add(new WorkerThread());
        }

        int iterator = 0;

        //these 2 are used to read from file
        FileInputStream inputStream = null;
        Scanner sc = null;


        try {
            inputStream = new FileInputStream(FILE_ABSOLUTE_LOCATION);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                //System.out.println(line);

                //append to each thread's private string to work on
                threads.get(iterator).appendToSentence(line);
                iterator = (iterator + 1) % THREAD_COUNT;

            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }

    }


    //each threads will form frequency map
    void startThreads() {
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads.get(i).start();
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void showStats() {
        //take the first thread's map
        //and put all other frequencies in this map only
        Map<String, Integer> finalMap = threads.get(0).freqMap;
        for (int i = 1; i < THREAD_COUNT; i++) {
            Map<String, Integer> threadSpecificMap = threads.get(i).freqMap;

            for (Map.Entry<String, Integer> entry : threadSpecificMap.entrySet()) {
                int previousFreq = finalMap.getOrDefault(entry.getKey(), 0);
                finalMap.put(entry.getKey(), previousFreq + entry.getValue());
            }
        }

        for (Map.Entry<String, Integer> entry : finalMap.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }


}
