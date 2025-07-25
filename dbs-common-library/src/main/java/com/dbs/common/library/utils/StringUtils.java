/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.common.library.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author RachmatY
 */
public class StringUtils {

    private StringUtils() {}

    public static boolean hasValue(Object o) {
        return o != null && !o.toString().trim().isEmpty() && !o.toString().isEmpty();
    }

    public static boolean isValidString(String input) {
        return input != null && input.equals(input.trim());
    }

    public static String capitalizeFully(String input) {
        if (isBlank(input))
            return "";

        return Arrays.stream(input.toLowerCase().split("\\s+"))
                .map(org.apache.commons.lang3.StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }

    public static String capitalizeFullyApproval(String input) {
        if (isBlank(input))
            return "";

        String normalizedInput = input.replace("_", " ").toLowerCase();

        return Arrays.stream(normalizedInput.split("\\s+"))
                .map(org.apache.commons.lang3.StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }

    public static String getBranchName(Boolean isBranch) {
        return isBranch.equals(Boolean.TRUE) ? "BRANCH" : "";
    }
    
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) 
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static String getMessageNotValid(String fieldName) {
        return fieldName + " is not valid";
    }
}
