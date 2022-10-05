package com.defalt.apv.util.parser;

import com.defalt.apv.report.person.Gender;
import com.defalt.apv.report.person.Student;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.base.Sex;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserFull;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class VkStudentLoader implements StudentLoader {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
    private static final Integer USER_ID;
    private static final String ACCESS_TOKEN;
    private static final VkApiClient vk;
    private static final UserActor userActor;

    static {
        var properties = loadConfigurationProperties();
        USER_ID = Integer.parseInt(
            Objects.requireNonNull(properties.getProperty("user_id"), "User id not found in config!"));
        ACCESS_TOKEN =
            Objects.requireNonNull(properties.getProperty("access_token"), "Access token not found in config!");
        TransportClient transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);
        userActor = new UserActor(USER_ID, ACCESS_TOKEN);
    }

    private static Properties loadConfigurationProperties() {
        var properties = new Properties();
        var configFile = "src/main/resources/vk_user_actor.properties";
        try (var fis = new FileInputStream(configFile)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration!", e);
        }
        return properties;
    }

    private final int countryRussiaId = 1;
    private final int cityId;
    private final int universityId;

    public VkStudentLoader(String cityName, String universityName) {
        try {
            cityId = tryGetCityCode(cityName);
            universityId = tryGetUniversityCode(universityName, cityName);
        } catch (ClientException | ApiException e) {
            throw new RuntimeException("Api error!", e);
        }
    }

    private int tryGetCityCode(String cityName) throws ClientException, ApiException {
        var cities = vk.database().getCities(userActor, countryRussiaId)
            .q(cityName)
            .count(1)
            .execute();
        if (cities.getCount() == 0)
            throw new IllegalArgumentException(String.format("City \"%s\" not found in Russia", cityName));
        return cities.getItems().get(0).getId();
    }

    private int tryGetUniversityCode(String universityName, String cityName) throws ClientException, ApiException {
        var universities = vk.database().getUniversities(userActor)
            .countryId(countryRussiaId)
            .cityId(cityId)
            .q(universityName)
            .count(1)
            .execute();
        if (universities.getCount() == 0)
            throw new IllegalArgumentException(
                String.format("University \"%s\" not found in city %s", universityName, cityName));

        return universities.getItems().get(0).getId();
    }

    @Override
    public Student load(String fullName, String groupName) {
        try {
            Thread.sleep(250);
            return tryLoad(fullName, groupName);
        } catch (ClientException | ApiException | InterruptedException e) {
            return new Student(fullName, groupName);
        }
    }

    private Student tryLoad(String fullName, String groupName) throws ClientException, ApiException {
        return tryFindUser(fullName)
            .map(userFull -> loadStudentFromResponse(fullName, groupName, userFull))
            .orElseGet(() -> new Student(fullName, groupName));
    }

    private Optional<UserFull> tryFindUser(String fullName) throws ClientException, ApiException {
        var searchResponse = vk.users()
            .search(userActor)
            .q(fullName)
            .fields(Fields.SEX, Fields.BDATE, Fields.HOME_TOWN, Fields.CITY, Fields.UNIVERSITIES)
            .lang(Lang.RU)
            .count(1000)
            .execute();

        if (searchResponse.getCount() == 0 || searchResponse.getCount() > 1000)
            return Optional.empty();

        var results = searchResponse.getItems();
        if (results.size() == 1)
            return Optional.of(results.get(0));

        var universityFiltered = results.parallelStream()
            .filter(userFull -> userFull.getUniversities() != null)
            .filter(userFull -> userFull.getUniversities().stream()
                .anyMatch(university -> university.getId() == universityId))
            .collect(Collectors.toSet());
        if (universityFiltered.size() == 1)
            return universityFiltered.stream().findAny();

        var cityFiltered = results.parallelStream()
            .filter(userFull -> userFull.getCity() != null)
            .filter(userFull -> userFull.getCity().getId() == cityId)
            .collect(Collectors.toSet());
        if (cityFiltered.size() == 1)
            return cityFiltered.stream().findAny();

        var universityCityFiltered = cityFiltered.parallelStream()
            .filter(universityFiltered::contains)
            .collect(Collectors.toSet());
        if (universityCityFiltered.size() == 1)
            return universityCityFiltered.stream().findAny();

        return Optional.empty();
    }

    private static Student loadStudentFromResponse(String fullName, String groupName, UserFull user) {
        var sex = user.getSex();
        var gender = sex == Sex.MALE ? Gender.MALE : sex == Sex.FEMALE ? Gender.FEMALE : null;

        var bDate = user.getBdate();
        LocalDate birthdate = null;
        if (bDate != null) {
            if (bDate.split("\\.").length == 2)
                bDate += ".0001";
            birthdate = LocalDate.parse(bDate, dateFormatter);
        }

        var homeTown = user.getHomeTown();
        if (homeTown == null || homeTown.isBlank()) {
            var city = user.getCity();
            var cityTitle = city == null ? null : city.getTitle();
            homeTown = cityTitle == null || cityTitle.isBlank() ? null : cityTitle;
        }

        return new Student(fullName, groupName, gender, birthdate, homeTown);
    }
}
