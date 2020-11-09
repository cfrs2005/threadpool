package com.aj.tl;

/**
 * @author zhangqingyue
 * @date 2020/11/9
 */
public class TestEquals {

    public static void main(String[] args) {
        String x = "string";
        String y = "string";
        String z = new String("string");
        System.out.println(x == y);
        System.out.println(x == z);
        System.out.println(x.equals(y));
        System.out.println(x.equals(z));

        Integer a = 123;
        Integer b = 123;
        System.out.println(a.equals(b));
        char c = 'a';
        System.out.println('a' == 97);
    }
}
