package com.apd.tema2;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.io.Reader;
import com.apd.tema2.utils.Constants;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static Pedestrians pedestrians = null;
    public static Intersection intersection;
    public static int carsNo;


    public static CyclicBarrier barrier;
    public static CyclicBarrier barrierTask4;
    public static CyclicBarrier barrierTask8;
    public static Semaphore semaphore;
    public static Semaphore sem_task8_0;
    public static Semaphore sem_task8_1;

    public static ArrayList<Semaphore> semTask3 = new ArrayList<>();
    public static ArrayList<Semaphore> semTask4 = new ArrayList<>();
    public static ArrayBlockingQueue<Integer> queue;
    public static ArrayBlockingQueue<Integer> queueTask10_0;
    public static ArrayBlockingQueue<Integer> queueTask10_1;
    public static AtomicInteger nrCarsHighEntered;
    public static ArrayList<AtomicBoolean> isGreen;

    public static void main(String[] args) {
        Reader fileReader = Reader.getInstance(args[0]);
        Set<Thread> cars = fileReader.getCarsFromInput();
        if (Constants.isTask1 || Constants.isTask4 || Constants.isTask10 || Constants.isTask8) {
            barrier = new CyclicBarrier(carsNo);
        }
        if(Constants.isTask2) {
            semaphore = new Semaphore(Constants.nRoundabout);
        }
        if (Constants.isTask3) {
            for (int i = 0; i < Constants.nrLANES; i++) {
                semTask3.add(new Semaphore(1));
            }
        }
        if (Constants.isTask4 || Constants.isTask5) {
            for (int i = 0; i < Constants.nrLANES; i++) {
                semTask4.add(new Semaphore(Constants.passingCar));

            }
            if (Constants.isTask4) {
                barrierTask4 = new CyclicBarrier(Constants.nrLANES * Constants.passingCar);
            }
        }
        if (Constants.isTask6) {
            queue = new ArrayBlockingQueue<>(Constants.nrCarsLow);
            nrCarsHighEntered = new AtomicInteger(0);
        }
        if (Constants.isTask7) {
            isGreen = new ArrayList<>(carsNo);
            for (int i = 0 ; i < carsNo; i++) {
                isGreen.add(i ,new AtomicBoolean(false));
            }
        }
        if (Constants.isTask8) {
            sem_task8_0 = new Semaphore(Constants.nrCarsAtOnce);
            sem_task8_1 = new Semaphore(0);
            barrierTask8 = new CyclicBarrier(Constants.nrCarsAtOnce);
        }
        if (Constants.isTask10) {
            queueTask10_0 = new ArrayBlockingQueue<Integer>(carsNo);
            queueTask10_1 = new ArrayBlockingQueue<Integer>(carsNo);
        }
        for(Thread car : cars) {
            car.start();
        }

        if(pedestrians != null) {
            try {
                Thread p = new Thread(pedestrians);
                p.start();
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(Thread car : cars) {
            try {
                car.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
