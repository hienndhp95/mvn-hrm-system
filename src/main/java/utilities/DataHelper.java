package utilities;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import com.github.javafaker.Faker;

public class DataHelper {
    private Faker faker;

    public static DataHelper getData() {
        return new DataHelper();
    }

    public DataHelper() {
        faker = new Faker();
    }

    public String getFirstName() {
        return faker.name().firstName();
    }

    public String getLastName() {
        return faker.name().lastName();
    }

    public String getEditFirstName() {
        return faker.name().firstName();
    }

    public String getEditLastName() {
        return faker.name().lastName();
    }

    public String getAddress() {
        return faker.address().streetAddress();
    }

    public String getEmailAddress() {
        return faker.internet().emailAddress();
    }

    public String getPhoneNumber() {
        return faker.phoneNumber().cellPhone();
    }

    public String getParagraph() {
        return faker.lorem().paragraph();
    }

    /* Random email */
    public String generateFakeEmail() {
        Random rand = new Random();
        return rand.nextInt(99999) + "@gmail.com";
    }

    /* Random Alphanumeric */
    public String generateFakeAlphaNumeric(int lenght) {
        boolean hasLetters = true;
        boolean hasNumbers = true;
        return RandomStringUtils.random(lenght, hasLetters, hasNumbers);
    }

    /* Random Number */
    public String generateFakeNumber(int lenght) {
        boolean hasLetters = false;
        boolean hasNumbers = true;
        return RandomStringUtils.random(lenght, hasLetters, hasNumbers);
    }

    /* Random Price */
    public String randomPrice() {
        Random rand = new Random();
        return String.valueOf(rand.nextInt(2000));
    }

    /* Get date of system date */
    public String generateFakeDate(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date = LocalDate.now();
        String dateNow = formatter.format(date);
        return dateNow;
    }

    /* Get date added from system date */
    public static String generateFakeDate(String format, int addDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate dateNow = LocalDate.now();
        LocalDate plusDay = dateNow.plusDays(addDate);
        String date = formatter.format(plusDay);
        return date;
    }

    /* Get time added from system time */
    public String generateFakeSpecificTime(String format, int hours) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalTime localTime = LocalTime.now();
        LocalTime plusTime = localTime.plusHours(hours);
        String time = formatter.format(plusTime);
        return time;
    }

    /* Get time of system time */
    public String generateFakeTimeNow(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalTime localTime = LocalTime.now();
        String timeNow = formatter.format(localTime);
        return timeNow;
    }

    public String convertDateToPattern(String date, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dateConverted = LocalDate.parse(date, dtf);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateConverted.format(formatter);
    }

    public String getRandomWorkingDayInDateRange(String startDate, String endDate) {
        List<LocalDate> listWorkDays = workingDay(startDate, endDate);
        Random rand = new Random();
        int randIndex = rand.nextInt(listWorkDays.size());
        Date date = Date.valueOf(listWorkDays.get(randIndex));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String workingDay = formatter.format(date);
        return workingDay;
    }

    public String getRandomWeekendInDateRange(String startDate, String endDate) {
        List<LocalDate> listWeekends = weekend(startDate, endDate);
        Random rand = new Random();
        int randIndex = rand.nextInt(listWeekends.size());
        Date date = Date.valueOf(listWeekends.get(randIndex));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String weekend = formatter.format(date);
        return weekend;
    }

    private List<LocalDate> workingDay(String startDate, String endDate) {
        List<LocalDate> workingDays = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate firstDate = LocalDate.parse(startDate, formatter);
        LocalDate secondDate = LocalDate.parse(endDate, formatter);
        while (!firstDate.isAfter(secondDate)) {
            if (!(firstDate.getDayOfWeek() == DayOfWeek.SATURDAY || firstDate.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                workingDays.add(firstDate);
                firstDate = firstDate.plusDays(1);
            } else {
                firstDate = firstDate.plusDays(1);
            }
        }
        return workingDays;
    }

    private List<LocalDate> weekend(String startDate, String endDate) {
        List<LocalDate> weekend = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate firstDate = LocalDate.parse(startDate, formatter);
        LocalDate secondDate = LocalDate.parse(endDate, formatter);
        while (!firstDate.isAfter(secondDate)) {
            if ((firstDate.getDayOfWeek() == DayOfWeek.SATURDAY || firstDate.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                weekend.add(firstDate);
                firstDate = firstDate.plusDays(1);
            } else {
                firstDate = firstDate.plusDays(1);
            }
        }
        return weekend;
    }
}
