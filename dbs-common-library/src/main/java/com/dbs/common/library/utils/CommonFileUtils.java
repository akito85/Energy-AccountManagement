package com.dbs.common.library.utils;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.library.utils.dto.CheckMT940Dto;
import com.prowidesoftware.swift.io.parser.SwiftParser;
import com.prowidesoftware.swift.model.SwiftMessage;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonFileUtils {

    private CommonFileUtils() {}

    private static final Logger logger = LoggerFactory.getLogger(CommonFileUtils.class);
    
    private static final String BANK = "BANK";
    private static final String TRANSACTION_CALENDAR = "TRANSACTION CALENDAR";
    private static final String PAYMENT_METHOD = "PAYMENT METHOD";
    private static final String TRANSACTION_PERIOD = "TRANSACTION PERIOD";
    private static final String BANK_ACCOUNT = "BANK ACCOUNT";
    private static final String RECEIPT = "RECEIPT";

    public static String mt940ToJsonString(String filePath) throws IOException {
        SwiftParser parser = new SwiftParser(readFileToString(filePath));
        SwiftMessage mt = parser.message();
        return mt.toJson();
    }

    private static String readFileToString(String filePath) throws IOException {
        File file = new File(filePath);
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    public static String mt940BcaToString(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        }
    }

    public static String getPathFileByCategory(String category) {
        String pathFile = "";

        if (category.equalsIgnoreCase(BANK))
            pathFile = "PATH_16";
        else if (category.equalsIgnoreCase(TRANSACTION_CALENDAR))
            pathFile = "PATH_17";
        else if (category.equalsIgnoreCase(PAYMENT_METHOD))
            pathFile = "PATH_18";
        else if (category.equalsIgnoreCase(TRANSACTION_PERIOD))
            pathFile = "PATH_19";
        else if (category.equalsIgnoreCase(BANK_ACCOUNT))
            pathFile = "PATH_21";
        else if (category.equalsIgnoreCase(RECEIPT))
            pathFile = "PATH_33";

        return pathFile;
    }

    public static CheckMT940Dto isMT940File(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read the first few lines to check for MT940 headers
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(Constant.RETENSI)) {
                    return CheckMT940Dto.builder()
                            .isMT940(true)
                            .bankName(BankName.BCA.name())
                            .build();
                }
                if (line.contains("{1:")) {
                    return CheckMT940Dto.builder()
                            .isMT940(true)
                            .bankName(BankName.NON_BCA.name())
                            .build();
                }
            }
        } catch (IOException e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
        }

        return CheckMT940Dto.builder()
                .isMT940(false)
                .bankName(null)
                .build();
    }
}
