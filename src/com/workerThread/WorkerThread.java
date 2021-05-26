package com.workerThread;

import java.util.*;

public class WorkerThread extends Thread {

    private StringBuffer inputSentence;
    public Map<String, Integer> freqMap;

    public WorkerThread() {
        this.inputSentence = new StringBuffer("");
        this.freqMap = new HashMap<>();

    }

    public void appendToSentence(String s) {
        inputSentence.append(s);
    }


    private void performOperation() {
        String[] split = inputSentence.toString().split("\\s+");
        for (String s : split) {
            freqMap.put(s, 1 + freqMap.getOrDefault(s, 0));
        }
    }




    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " starts : ");
        this.performOperation();
    }
}
