package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {

    private static final Random random = new Random();

    private static final String[] VALID_CITIES = {
            "Москва", "Санкт-Петербург", "Казань", "Екатеринбург",
            "Новосибирск", "Нижний Новгород", "Самара", "Ростов-на-Дону",
            "Уфа", "Красноярск", "Воронеж", "Пермь", "Волгоград"
    };

    private DataGenerator() {
    }

    public static String generateDate(int shift, String pattern) {
        return LocalDate.now()
                .plusDays(shift)
                .format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String generateCity(Faker faker) {
        return VALID_CITIES[random.nextInt(VALID_CITIES.length)];
    }

    public static String generateName(Faker faker) {
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String generatePhone(Faker faker) {
        StringBuilder phone = new StringBuilder("+7");
        for (int i = 0; i < 10; i++) {
            phone.append(random.nextInt(10));
        }
        return phone.toString();
    }

    public static class Registration {
        private static Faker faker;

        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            faker = new Faker(new Locale(locale));
            return new UserInfo(
                    generateCity(faker),
                    generateName(faker),
                    generatePhone(faker)
            );
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}
