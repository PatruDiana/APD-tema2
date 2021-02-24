package com.apd.tema2.factory;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.intersections.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Prototype Factory: va puteti crea cate o instanta din fiecare tip de implementare de Intersection.
 */
public class IntersectionFactory {
    private static Map<String, Intersection> cache = new HashMap<>();

    static {
         //cache.put(" simple_semaphore", new Intersection() {
         //});
         //cache.put("simple_n_roundabout", new Intersection() {
         //});
    }

    public static Intersection getIntersection(String handlerType) {
        return cache.get(handlerType);
    }

}
