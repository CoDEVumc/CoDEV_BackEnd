package com.codevumc.codev_backend;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

class CoDevBackEndApplicationTests {

    public static void main(String[] args) throws ParseException {

        String data = "[\n" +
                "        {\"co_partId\" : 1,\"co_limit\" : 2},\n" +
                "        {\"co_partId\" : 2,\"co_limit\" : 3},\n" +
                "        {\"co_partId\" : 3,\"co_limit\" : 1},\n" +
                "        {\"co_partId\" : 4,\"co_limit\" : 0},\n" +
                "        {\"co_partId\" : 5,\"co_limit\" : 0}\n" +
                "    ]";

        JSONArray js = new JSONArray();
        js.add(data);
        System.out.println(js.get(0));
    }
}
