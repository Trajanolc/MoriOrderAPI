package com.apprefrig.services;

import com.apprefrig.model.Equipamento;
import com.apprefrig.repository.DynamoDBRepositories;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import java.text.Normalizer;
import java.text.Normalizer.Form;

@Service
public class EquipServices {

    private DynamoDbTable<Equipamento> equipTable = DynamoDBRepositories.equipTable();

    public void addHistoric(String TAG, String instalacao, String orderID){
        if(instalacao.equals("equatorial")) instalacao = "SEDE";

        if (instalacao.equalsIgnoreCase("SEDE") || instalacao.equalsIgnoreCase("AGROPALMA")) {

            instalacao = instalacao.toUpperCase();

            Key keyP = Key.builder()
                    .partitionValue(AttributeValue.builder().s(instalacao).build())
                    .sortValue(AttributeValue.builder().s(removeAccents(TAG)).build())
                    .build();
            Equipamento equip = equipTable.getItem(r -> r.key(keyP));

            equip.getHistorico().add(orderID);

            equipTable.putItem(equip);
        }

    }

    private static String removeAccents(String text) {
        return text == null ? null :
                Normalizer.normalize(text, Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

}
