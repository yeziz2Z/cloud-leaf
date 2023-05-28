package com.leaf.oauth2.config;


import com.leaf.oauth2.CloudLeafOauth2Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

//@SpringBootTest(classes = CloudLeafOauth2Application.class)
@Slf4j
public class WebSecurityConfigTest {

    //    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void test() {
//        log.info("889527 {}", passwordEncoder.encode("889527"));
        String hello = "hello";
        System.out.println(hello.substring(2));
    }

    @Test
    public void test12() {
        int[] arr = {7,7,7,8,5,7,5,5,5,8};
        System.out.println(Arrays.toString(rearrangeBarcodes(arr)));
    }

    public int[] rearrangeBarcodes(int[] barcodes) {
        int[] res = new int[barcodes.length];

        Map<Integer, Integer> cnt = new HashMap<>();
        for (int key : barcodes) {
            cnt.put(key, cnt.getOrDefault(key, 0) + 1);
        }
        PriorityQueue<Pair> queue = new PriorityQueue<>((Comparator.comparingInt(o -> o.cnt)));
        for (Integer key : cnt.keySet()) {
            queue.offer(new Pair(key, cnt.get(key)));
        }
        List<Pair> list = new ArrayList<>();
        while (!queue.isEmpty()){
            list.add(queue.poll());
        }

        int idx = barcodes.length - 1;
        int s = 0;
        int e = list.size() - 1;
        Pair start, end;
        while (idx >= 0) {
            start = list.get(s);
            end = list.get(e);
            while (start.cnt > 0 && end.cnt > 0) {
                res[idx--] = end.val;
                res[idx--] = start.val;
                end.cnt--;
                start.cnt--;
            }
            if (start.cnt == 0) {
                s++;
            }
            if (end.cnt == 0) {
                e--;
            }
        }

        return res;
    }

    class Pair {
        public int val;
        public int cnt;

        public Pair(int val, int cnt) {
            this.val = val;
            this.cnt = cnt;
        }
    }


}
