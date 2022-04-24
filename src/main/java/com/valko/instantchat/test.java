package com.valko.instantchat;

import java.util.concurrent.Semaphore;

public class test
{
    static Semaphore sem = new Semaphore(7);

    public static void main(String[] args)
    {
        System.out.println(sem.getQueueLength());
    }
}
