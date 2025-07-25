package com.dbs.common.base.utils;

import org.apache.commons.lang3.StringUtils;

public class ConvertData {

    private ConvertData() {}

    public static String convertToTitleCase(String input) {
        if (StringUtils.isBlank(input))
            return input;

        // Split the input string into words using whitespace as the delimiter
        String[] words = input.split("\\s+");

        // Initialize a StringBuilder to build the result
        StringBuilder resultBuilder = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                // Convert the first character of the word to uppercase and the rest to lowercase
                String titleCaseWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();

                // Append the title-cased word to the result
                resultBuilder.append(titleCaseWord);

                // If there are more words, add a space
                if (!word.equals(words[words.length - 1])) {
                    resultBuilder.append(" ");
                }
            }
        }

        return resultBuilder.toString();
    }

    public static String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex >= 0) {
            int maskedStart = email.indexOf(email.charAt(0)) + 2; // Start masking from the third character
            if (maskedStart < 0) {
                maskedStart = 2; // Default to the second character
            }
            StringBuilder maskedEmail = new StringBuilder(email);
            for (int i = maskedStart; i < atIndex; i++) {
                maskedEmail.setCharAt(i, '*');
            }
            return maskedEmail.toString();
        } else {
            return email;
        }
    }
}
