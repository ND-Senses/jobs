package vn.ndm.importdataprefix.service.impl;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PhoneNumberGenerator {

    public static void main(String[] args) {
        genarateData();
    }

    public static void generatePhoneNumbers(long start, long end) {
        List<String> phoneNumbers = new ArrayList<>();
        int seq = 1;
        for (long i = start; i <= end; i++) {
            String formattedI = String.format("%06d", i); // Định dạng i thành chuỗi có 6 chữ số, bắt đầu bằng số 000000
            String sql = "data,1,1,admin,1,1,1";
            String phoneNumber = "84996" + formattedI;
            String data = "452070000" + formattedI;
            sql = sql.replace("seq", String.valueOf(seq++)).replace("data", phoneNumber + "," + data);
            phoneNumbers.add(sql);
        }
        writeToFile("data/msisdn.txt", phoneNumbers);
        log.info("Created file data success..");
    }

    public static void genarateData() {
        List<String> phoneNumbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("log/m.txt"))) {
            String line;
            int id = 499999;
            while ((line = reader.readLine()) != null) {
                id++;
                String sql = "data,1,1,admin,2,1,1";
                String data = "452070000" + id;
                sql = sql.replace("data", line + "," + data);
                phoneNumbers.add(sql);
            }
            writeToFile("data/msisdn.txt", phoneNumbers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void writeToFile(String filePath, List<String> data) {
        File packageFile = new File(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(packageFile))) {
            // Ghi danh sách vào tập tin
            for (String phoneNumber : data) {
                writer.write(phoneNumber + System.lineSeparator()); // Thêm dòng mới sau mỗi số điện thoại
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

