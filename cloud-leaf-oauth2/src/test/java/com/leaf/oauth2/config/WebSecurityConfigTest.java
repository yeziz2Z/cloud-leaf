package com.leaf.oauth2.config;


import cn.hutool.crypto.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

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
    public void generateRsaKey() throws NoSuchAlgorithmException {
        KeyPair keyPair;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        keyPair = keyPairGenerator.generateKeyPair();
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("privateKey : [" + KeyUtil.toBase64(keyPair.getPrivate()) + "]");
        System.out.println("publicKey : [" + KeyUtil.toBase64(keyPair.getPublic()) + "]");
        System.out.println("-------------------------------------------------------------------------------------------");

    }

    @Test
    public void test12() {
        int[] arr = {7, 7, 7, 8, 5, 7, 5, 5, 5, 8};
        System.out.println(Arrays.toString(rearrangeBarcodes(arr)));
        List<int[]> list = new ArrayList<>();
        list.add(new int[]{1, 1});
        list.add(new int[]{2, 1});
        list.add(new int[]{2, 3});

        list.sort((a, b) -> a[1] == b[1] ? Integer.compare(a[0], b[0]) : Integer.compare(b[1], a[1]));

        list.forEach(e -> System.out.println(Arrays.toString(e)));
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
        while (!queue.isEmpty()) {
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

    @Test
    public void testHe() {
        String[] source = {"void func(int k) {", "// this function does nothing /*", "   k = k*2/4;", "   k = k/2;*/", "}"};

        System.out.println(removeComments(source));
    }


    public List<String> removeComments(String[] source) {
        List<String> res = new ArrayList<>();
        StringBuilder tmp = new StringBuilder();
        boolean commentBlock = false;
        for (String sentence : source) {
            for (int i = 0; i < sentence.length(); i++) {
                char c = sentence.charAt(i);
                if (commentBlock) {
                    if (c == '*'
                            && i < sentence.length() - 1
                            && sentence.charAt(i + 1) == '/') {
                        i++;
                        commentBlock = false;
                    }
                } else {
                    if (c == '/') {
                        if (i < sentence.length() - 1) {
                            char t = sentence.charAt(++i);
                            if (t == '/') {
                                break;
                            } else if (t == '*') {
                                commentBlock = true;
                            } else {
                                tmp.append(c).append(t);
                            }
                        } else {
                            tmp.append(c);
                        }
                    } else if (c == '*') {

                    } else {
                        tmp.append(c);
                    }
                }

            }
            if (!commentBlock && tmp.length() > 0) {
                res.add(tmp.toString());
                tmp.setLength(0);
            }
        }
        if (tmp.length() > 0) {
            res.add(tmp.toString());
        }

        return res;
    }

    @Test
    public void testArray() {
        int[][] arr = {{2, 1}, {3, 4}, {3, 2}};

        System.out.println(Arrays.toString(restoreArray(arr)));
    }

    public int[] restoreArray(int[][] adjacentPairs) {
        int[] res = new int[adjacentPairs.length + 1];

        Map<Integer, Set<Integer>> cnt = new HashMap<>();
        int head = adjacentPairs[0][0];
        for (int[] arr : adjacentPairs) {
            Set<Integer> set = cnt.getOrDefault(arr[0], new HashSet<Integer>());
            set.add(arr[1]);
            cnt.put(arr[0], set);
            set = cnt.getOrDefault(arr[1], new HashSet<Integer>());
            set.add(arr[0]);
            cnt.put(arr[1], set);
        }

        for (Integer key : cnt.keySet()) {
            if (cnt.get(key).size() == 1) {
                head = key;
                break;
            }
        }
        for (int i = 0; i < res.length; i++) {
            res[i] = head;
            if (i == res.length - 1) {
                break;
            }
            Set<Integer> set = cnt.get(head);
            head = set.stream().findFirst().get();
            if (cnt.get(head) != null) {
                cnt.get(head).remove(res[i]);
            }
        }
        return res;
    }

    @Test
    public void testSTr() {
        System.out.println(replaceDigits("a"));
    }

    public String replaceDigits(String s) {
        int len = s.length();
        StringBuilder res = new StringBuilder();
        String str = "abcdefghijklmnopqrstuvwxyz";
        char[] chars = s.toCharArray();
        for (int i = 0; i < len; i++) {
            if (i % 2 == 1) {
                res.append(str.charAt(chars[i - 1] - 'a' + chars[i] - '0'));
            } else {
                res.append(chars[i]);
            }
        }
        return res.toString();
    }

}
