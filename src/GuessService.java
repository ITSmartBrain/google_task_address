import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GuessService {
    List<Address> addresses;
    List<Address> secondList;
    Random rnd = new Random();

    /**
     * Запускаем попытку отгадать адрес
     * @return
     */
    boolean startProgram() {
        generateAddresses();
        secondList = new ArrayList<>(addresses);
        //Петя загадывает адрес
        Address think = think();
        System.out.println("Я загадал адрес: " + think);
        int tryNumber = -1;
        //запускаем 10 попыток
        for (int i = 1; i <= 10; i++) {
            //программа берет адрес из списка в попытке отгадать
            Address guess = guess();
            //считаем кол-во сопадений
            int count = count(guess, think);
            //если равно 5, то угадали
            if (count == 5) {
                System.out.println("Это адрес: " + guess);
                //если угадали, запоминаем с какой попытки угадали слово
                tryNumber = i;
                break;
            } else {
                //иначе отфильтровываем остальные адреса
                if (count > 0) filterSecondList(guess, count);
            }
        }
        //если после 10-и попыток угадали слово, то выводим с какой попытки смогли угадать
        if (tryNumber != -1) {
            System.out.println("Угадал с " + tryNumber + " попытки");
            return true;
        } else {
            System.out.println("Не угадал");
            return false;
        }
    }

    /**
     * В secondaryList остаются только адреса, у которых количество совпадение больше либо равно с адресов guess
     *
     * @param guess адрес, который выбрала наша программа в попытке угадать загаднный Петей
     * @param matches количество совпадений адреса guess с загаданным Петей
     */
    void filterSecondList(Address guess, int matches) {
        secondList = secondList.stream().filter(t -> count(guess, t) >= matches).collect(Collectors.toList());
    }

    /**
     * Петя загадыает адрес из списка 100 адресов, который нужно будет отгадать
     * @return загаданный адрес
     */
    Address think() {
        return addresses.get(rnd.nextInt(addresses.size()));
    }

    /**
     * Выбирает адрес из списка доступных
     * @return адрес
     */
    Address guess() {
        return secondList.get(rnd.nextInt(secondList.size()));
    }

    /**
     * Считаем количество совпавших атрибутов у двух адресов
     * @param guess адрес, который выбрала наша программа в попытке отгадать загаданный Петей
     * @param think загаданный адрес Петей
     * @return количество совпадений
     */
    int count(Address guess, Address think) {
        int count = 0;
        if (think.country.equalsIgnoreCase(guess.country)) count++;
        if (think.city.equalsIgnoreCase(guess.city)) count++;
        if (think.street.equalsIgnoreCase(guess.street)) count++;
        if (think.house == guess.house) count++;
        if (think.flat == guess.flat) count++;
        if (count == 0) return -1;
        return count;
    }


    /**
     * Генерируем 100 адресов
     */
    void generateAddresses() {
        addresses = new ArrayList<>();
        int size = 10;
        //генерируем 10 стран
        //City1, City2, City3
        List<String> countries = IntStream.range(1, size).mapToObj(t -> "Country" + t).collect(Collectors.toList());
        //генерируем 10 городов
        List<String> cities = IntStream.range(1, size).mapToObj(t -> "City" + t).collect(Collectors.toList());
        //генерируем 10 улиц
        List<String> streets = IntStream.range(1, size).mapToObj(t -> "Street" + t).collect(Collectors.toList());
        //генерируем 10 домов
        List<Integer> houses = IntStream.range(1, size).boxed().collect(Collectors.toList());
        //генерируем 10 квартир
        List<Integer> flats = IntStream.range(1, size).boxed().collect(Collectors.toList());
        Set<Address> set = new HashSet<>();
        //комбинируем
        while (set.size() < 100) {
            String country = countries.get(rnd.nextInt(countries.size())); //[0; 9]
            String city = cities.get(rnd.nextInt(cities.size()));
            String street = streets.get(rnd.nextInt(streets.size()));
            int house = houses.get(rnd.nextInt(houses.size()));
            int flat = flats.get(rnd.nextInt(flats.size()));
            set.add(new Address(country, city, street, house, flat));
        }
        addresses.addAll(set);
    }

}
