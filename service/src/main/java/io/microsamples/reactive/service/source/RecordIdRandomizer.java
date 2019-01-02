package io.microsamples.reactive.service.source;

import io.github.benas.randombeans.api.Randomizer;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RecordIdRandomizer implements Randomizer<String> {

    private List<String> trackUpdates = Arrays.asList(
            "94a0b5ef-462d-4f95-9512-60a0cc046aef"
            , "cba6a98b-e1ce-485c-9efd-6ee9dc253fc8"
            , "f641bc00-f1c7-44e7-8949-7fc9feb2e4f7"
            , "da69671a-ec5c-41b8-8dca-0d5e7ce18b2c"
            , "b3f1263e-c05d-4cd9-9bb5-f86109d33d52"
            , "29e6b3cd-49b8-4aab-a946-b16004771a5d"
            , "f04ef300-8547-49fa-b757-0a3f4ae632fa"
            , "d7fd53dc-8d03-4fdb-98bf-604e1c723bf9"
            , "3db281b6-e99d-4251-a57b-85b3e0cad838"
            , "e7048b07-9c4c-4cba-9fb2-04c25c9be2dd")
    ;

    @Override
    public String getRandomValue() {
        return trackUpdates.get(new Random().nextInt(trackUpdates.size()));
    }
}
