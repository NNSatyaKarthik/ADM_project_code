package adm.birch;

import org.junit.*;

import java.util.HashMap;

/**
 * Created by nagasaty on 4/27/17.
 */
public class PerformanceTest {
    @org.junit.Test
    public void hashMapTest(){
        HashMap<Integer, Double> map = new HashMap<>();
        HashMap<Integer, Double> res = new HashMap<>();
        
        for(int i = 0 ; i < 1000000000; i++){
            map.put(i , i+1.0);
        }
        long start = System.currentTimeMillis();
        
        map.forEach((k, v)->res.put(k, v));
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        
        res.clear();
        start = System.currentTimeMillis();
        for(int key: map.keySet()){
            res.put(key, map.get(key));
        }
        end = System.currentTimeMillis();
        System.out.println(end-start);
    }
}
