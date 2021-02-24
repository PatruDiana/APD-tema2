package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;
import com.apd.tema2.intersections.*;
import com.apd.tema2.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) {
                    // Exemplu de utilizare:
                    //Main.intersection = IntersectionFactory.getIntersection(handlerType);
                    Constants.isTask1 = true;
                }
            };
            case "simple_n_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    // To parse input line use:
                     String[] line = br.readLine().split(" ");
                    Constants.nRoundabout =  Integer.parseInt(line[0]);
                    Constants.timeRoundabout = Integer.parseInt(line[1]);
                    Constants.isTask2 = true;
                }
            };
            case "simple_strict_1_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Constants.nrLANES =  Integer.parseInt(line[0]);
                    Constants.timeRoundabout = Integer.parseInt(line[1]);
                    Constants.isTask3 = true;
                }
            };
            case "simple_strict_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Constants.nrLANES =  Integer.parseInt(line[0]);
                    Constants.timeRoundabout = Integer.parseInt(line[1]);
                    Constants.passingCar = Integer.parseInt(line[2]);
                    Constants.isTask4 = true;
                }
            };
            case "simple_max_x_car_roundabout" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Constants.nrLANES =  Integer.parseInt(line[0]);
                    Constants.timeRoundabout = Integer.parseInt(line[1]);
                    Constants.passingCar = Integer.parseInt(line[2]);
                    Constants.isTask5 = true;
                }
            };
            case "priority_intersection" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Constants.nrCarsHigh = Integer.parseInt(line[0]);
                    Constants.nrCarsLow = Integer.parseInt(line[1]);
                    Constants.isTask6 = true;
                }
            };
            case "crosswalk" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Constants.timePedestrian = Integer.parseInt(line[0]);
                    Constants.nrPedestrian = Integer.parseInt(line[1]);
                    Main.pedestrians = new Pedestrians(Constants.timePedestrian, Constants.nrPedestrian);
                    Constants.isTask7 = true;
                }
            };
            case "simple_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Constants.nrCarsAtOnce = Integer.parseInt(line[0]);
                    Constants.isTask8 = true;
                }
            };
            case "complex_maintenance" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    
                }
            };
            case "railroad" -> new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Constants.isTask10 = true;
                }
            };
            default -> null;
        };
    }

}
