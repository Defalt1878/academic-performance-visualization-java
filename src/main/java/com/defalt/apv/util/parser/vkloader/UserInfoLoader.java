package com.defalt.apv.util.parser.vkloader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.base.Country;
import com.vk.api.sdk.objects.database.City;
import com.vk.api.sdk.objects.database.University;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.queries.users.UsersSearchQuery;

import java.util.Optional;
import java.util.stream.StreamSupport;

class UserInfoLoader {
    private final Gson gson = new Gson();

    private final VkApiClient vk;
    private final UserActor userActor;

    private final int countryId;
    private final int cityId;
    private final int universityId;

    public UserInfoLoader(String countryCode, String cityName, String universityName) {
        TransportClient transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);

        var userAuthData = VkAuthPropertiesLoader.loadData();
        userActor = new UserActor(userAuthData.userId(), userAuthData.accessToken());

        try {
            countryId = tryGetCountryId(countryCode);
            cityId = tryGetCityId(cityName);
            universityId = tryGetUniversityId(universityName);
        } catch (ClientException | ApiException e) {
            throw new RuntimeException("Api error!", e);
        }
    }

    private int tryGetCountryId(String countryCode) throws ClientException, ApiException {
        return vk.database()
            .getCountries(userActor)
            .code(countryCode)
            .count(1)
            .execute()
            .getItems()
            .stream()
            .findFirst()
            .map(Country::getId)
            .orElseThrow(() -> new IllegalArgumentException("Country not found!"));
    }

    private int tryGetCityId(String cityName) throws ClientException, ApiException {
        return vk.database()
            .getCities(userActor, countryId)
            .q(cityName)
            .count(1)
            .execute()
            .getItems()
            .stream()
            .findFirst()
            .map(City::getId)
            .orElseThrow(() -> new IllegalArgumentException("City not found!"));
    }

    private int tryGetUniversityId(String universityName) throws ClientException, ApiException {
        return vk.database()
            .getUniversities(userActor)
            .q(universityName)
            .countryId(countryId)
            .cityId(cityId)
            .count(1)
            .execute()
            .getItems()
            .stream()
            .findFirst()
            .map(University::getId)
            .orElseThrow(() -> new IllegalArgumentException("University not found!"));
    }

    public Optional<UserFull> findUser(String fullName) {
        try {
            Thread.sleep(250);
            return tryFindUser(fullName);
        } catch (ClientException | InterruptedException | ApiException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<UserFull> tryFindUser(String fullName) throws ClientException, ApiException {
        var searchResults = vk.execute().batch(
            userActor,
            getSimpleUserSearchQuery(fullName),
            getSimpleUserSearchQuery(fullName).university(universityId),
            getSimpleUserSearchQuery(fullName).city(cityId)
        ).execute().getAsJsonArray();

        return StreamSupport.stream(searchResults.spliterator(), false)
            .map(JsonElement::getAsJsonObject)
            .filter(searchResponse -> searchResponse.get("count").getAsInt() == 1)
            .map(searchResponse -> searchResponse.get("items").getAsJsonArray().get(0))
            .map(userFull -> this.gson.fromJson(userFull.toString(), UserFull.class))
            .findFirst();
    }

    private UsersSearchQuery getSimpleUserSearchQuery(String fullName) {
        return vk.users()
            .search(userActor)
            .fields(Fields.SEX, Fields.BDATE, Fields.HOME_TOWN, Fields.CITY)
            .lang(Lang.RU)
            .count(1)
            .q(fullName);
    }
}
