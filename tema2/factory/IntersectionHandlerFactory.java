package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.*;
import com.apd.tema2.intersections.*;
import com.apd.tema2.utils.Constants;

import java.sql.SQLOutput;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    try {
                        System.out.println("Car " + car.getId() +  " has " +
                                "reached the semaphore, now waiting...");
                        // sincronizarea celor n threaduri
                        Main.barrier.await();
                        // sleep pe timpul fiecarei masini
                        sleep(car.getWaitingTime());
                        System.out.println("Car " + car.getId() + " has waited "
                                + "enough, now driving...");
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                }
            };
            case "simple_n_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    try {
                        System.out.println("Car " + car.getId() +  " has reached"
                                + " the roundabout, now waiting...");
                        // cele maxim n masini pot trece la un moment dat mai
                        // departe
                        Main.semaphore.acquire();
                        System.out.println("Car " + car.getId() +  " has entered"
                                + " the roundabout");
                        // timpul alocat pentru fiecare masina in care trebuie
                        // sa stea
                        sleep(Constants.timeRoundabout);
                        System.out.println("Car " + car.getId() + " has exited "
                                + "the roundabout after " +
                                Constants.timeRoundabout / 1000 +  " seconds");
                        // incrementeaza semaforul pentru ca alta masina sa
                        // poata sa intre
                        Main.semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            case "simple_strict_1_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {

                    try {
                        System.out.println("Car " + car.getId() + " has reached" +
                                " the roundabout");
                        //cele maxim n masini pot trece la un moment dat mai
                        // departe pe linia corespunzatoare
                        Main.semTask3.get(car.getStartDirection()).acquire();
                        System.out.println("Car " + car.getId() + " has entered " +
                                "the roundabout from lane " +
                                car.getStartDirection());
                        // timpul alocat pentru fiecare masina in care trebuie
                        // sa stea
                        sleep(Constants.timeRoundabout);
                        System.out.println("Car " + car.getId() + " has exited " +
                                "the roundabout after " +
                                Constants.timeRoundabout / 1000 +  " seconds");
                        Main.semTask3.get(car.getStartDirection()).release();
                        // incrementeaza semaforul pentru ca alta masina sa
                        // poata sa intre
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            case "simple_strict_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    try {
                        System.out.println("Car " + car.getId() + " has reached " +
                                "the roundabout, now waiting...");
                        // pentru sincronizarea thread-urilor
                        Main.barrier.await();
                        //cele n masini pot trece la un moment dat mai
                        // departe pe linia corespunzatoare
                        Main.semTask4.get(car.getStartDirection()).acquire();
                        System.out.println("Car " + car.getId()  + " was selected " +
                                "to enter the roundabout from lane "
                                + car.getStartDirection());
                        // sincronizare intre mesaje pentru masinile aflate
                        // in send giratoriu
                        Main.barrierTask4.await();
                        System.out.println("Car "+ car.getId() +" has entered the " +
                                "roundabout from lane " +
                                car.getStartDirection());
                        sleep(Constants.timeRoundabout);
                        Main.barrierTask4.await();
                        System.out.println("Car "+ car.getId() +" has exited the" +
                                " roundabout after " +
                                Constants.timeRoundabout / 1000 +" seconds");
                        Main.barrierTask4.await();
                        // incrementeaza semaforul pentru ca alta masina sa
                        // poata sa intre
                        Main.semTask4.get(car.getStartDirection()).release();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            };
            case "simple_max_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici

                    try {
                        System.out.println("Car " + car.getId() + " has reached " +
                                "the roundabout from lane " +
                                car.getStartDirection());
                        //cele maxim n masini pot trece la un moment dat mai
                        // departe pe linia corespunzatoare
                        Main.semTask4.get(car.getStartDirection()).acquire();
                        System.out.println("Car " + car.getId() + " has entered " +
                                "the roundabout from lane " +
                                car.getStartDirection());
                        sleep(Constants.timeRoundabout);
                        System.out.println("Car " + car.getId() + " has exited" +
                                " the roundabout after " +
                                Constants.timeRoundabout / 1000 + " seconds");
                        Main.semTask4.get(car.getStartDirection()).release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            };
            case "priority_intersection" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici
                    if (car.getPriority() == 1) {
                        // id-ul masinii este pus in coada
                        try {
                            Main.queue.put(car.getId());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Car " + car.getId() + " with low " +
                                "priority is trying to enter the intersection...");
                        // busy waiting
                        // cat timp sunt masini cu prioritate mai mare in
                        // intersectie sau nu e randul masinii cu prioritate mica
                        // sa plece, asteapta
                        while (Main.nrCarsHighEntered.get() > 0 || (!Main.queue.isEmpty() &&
                                Main.queue.peek() != car.getId())) {
                            try {
                                synchronized (this) {
                                    wait(50);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("Car " + car.getId() +" with low " +
                                "priority has entered the intersection");
                        // masina e scoasa din coada
                        try {
                            Main.queue.take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else {
                        System.out.println("Car " + car.getId() +" with high " +
                                "priority has entered the intersection");
                        // incrementam atomic integerul pentru a anunta faptul
                        // ca o masina cu prioritate mare este deja in intersectie
                        Main.nrCarsHighEntered.incrementAndGet();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Car " + car.getId() + " with high " +
                                "priority has exited the intersection");
                        // decrementam atomic integerul pentru a anunta faptul
                        // ca o masina cu prioritate mare a iesti din intersectie
                        Main.nrCarsHighEntered.decrementAndGet();
                    }

                }
            };
            case "crosswalk" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // cat timp pietonii nu au trecut strada
                    while(!Main.pedestrians.isFinished()) {
                        // daca pietonii asteapta si masina nu a printat mesajul
                        // "hai now green light"
                        if (!Main.pedestrians.isPass() && !Main.isGreen.get(car.getId()).get()) {
                            Main.isGreen.set(car.getId(), new AtomicBoolean(true));
                            System.out.println("Car " + car.getId() + " has now green light");
                        // daca pietonii trec si masina nu a printat mesajul
                        // "hai now red light"
                        } else if (Main.pedestrians.isPass()  && Main.isGreen.get(car.getId()).get()) {
                            Main.isGreen.set(car.getId(), new AtomicBoolean(false));
                            System.out.println("Car " + car.getId() + " has now red light");
                        }
                    }
                    // daca pietonii au trecut dar masina nu apucat sa printeze
                    // mesajul cu "has now green light"
                    if (!Main.pedestrians.isPass() && !Main.isGreen.get(car.getId()).get()) {
                        System.out.println("Car " + car.getId() + " has now green light");
                    }
                }
            };
            case "simple_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {

                        try {
                            System.out.println("Car " + car.getId() + " from side number " +car.getStartDirection()
                                    + " has reached the bottleneck");
                            // sincronizarea thread-urilor
                            Main.barrier.await();
                            if (car.getStartDirection() == 0){
                                // incercarea de decrementa semaforul pentru
                                // linia 0 pentru fix n masini ce pot sa trece
                                // la un moment dat
                                Main.sem_task8_0.acquire();
                                System.out.println("Car " + car.getId() + " from side number " +
                                        car.getStartDirection() + " has passed the bottleneck");
                                // asigurarea ca printarea tuturor masinilor
                                // de pe linia 0 se termina inainte de a pornii
                                // cele de pe linia 1
                                Main.barrierTask8.await();
                                // incrementam semaforul pentru linia 1
                                Main.sem_task8_1.release();
                            } else {
                                // incercarea de decrementa semaforul pentru
                                // linia 1 pentru fix n masini ce pot sa trece
                                // la un moment dat
                                Main.sem_task8_1.acquire();
                                System.out.println("Car " + car.getId() + " from side number " +
                                        car.getStartDirection() + " has passed the bottleneck");
                                // asigurarea ca printarea tuturor masinilor
                                // de pe linia 0 se termina inainte de a pornii
                                // cele de pe linia 0
                                Main.barrierTask8.await();
                                // incrementam semaforul pentru linia 1
                                Main.sem_task8_0.release();
                            }
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }

                }
            };
            case "complex_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    
                }
            };
            case "railroad" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {

                    try {
                        synchronized (Car.class) {
                            System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() +
                                    " has stopped by the railroad");

                            if (car.getStartDirection() == 0) {
                                Main.queueTask10_0.put(car.getId());
                            } else {
                                Main.queueTask10_1.put(car.getId());
                            }
                        }
                        // sincronizarea thread-urilor
                        Main.barrier.await();
                        if (car.getId() == 0) {
                            // afisarea mesajului de catre un singur thread
                            System.out.println("The train has passed, cars can now proceed");
                        }
                        Main.barrier.await();
                        if (car.getStartDirection() == 0) {
                            // busy waiting
                            // cat timp sunt coada nu e goala si nu e randul masinii
                            // sa plece
                            while (!Main.queueTask10_0.isEmpty() &&
                                    (Main.queueTask10_0.peek() != car.getId())) {
                                try {
                                    synchronized (this) {
                                        wait(50);
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            // afisarea mesajului si scoaterea din coada
                            synchronized (Car.class) {
                                System.out.println("Car " + car.getId() + " " +
                                        "from side number " + car.getStartDirection()
                                        + " has started driving");
                                Main.queueTask10_0.take();
                            }

                        } else {
                            // busy waiting
                            // cat timp sunt coada nu e goala si nu e randul masinii
                            // sa plece
                            while (!Main.queueTask10_1.isEmpty() &&
                                    (Main.queueTask10_1.peek() != car.getId()) ) {
                                try {
                                    synchronized (this) {
                                        wait(50);
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            // afisarea mesajului si scoaterea din coada
                            synchronized (Car.class) {
                                System.out.println("Car " + car.getId() +
                                        " from side number " + car.getStartDirection()
                                        + " has started driving");
                                Main.queueTask10_1.take();
                            }
                        }
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            };
            default -> null;
        };
    }
}
