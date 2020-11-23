package com.microservice.kalah.assignment;

import org.springframework.core.annotation.Order;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtils {
   public static Map<String, String> expectedStatus = Stream.of(
            new String[][]{
                    {"1", "0"},
                    {"2", "7"},
                    {"3", "7"},
                    {"4", "7"},
                    {"5", "7"},
                    {"6", "7"},
                    {"7", "1"},
                    {"8", "6"},
                    {"9", "6"},
                    {"10", "6"},
                    {"11", "6"},
                    {"12", "6"},
                    {"13", "6"},
                    {"14", "0"}})
            .collect(Collectors.toMap(data -> data[0], data -> data[1]));
}
