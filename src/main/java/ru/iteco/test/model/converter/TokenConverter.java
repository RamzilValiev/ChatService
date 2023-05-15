package ru.iteco.test.model.converter;

import jakarta.persistence.AttributeConverter;
import org.apache.commons.codec.digest.DigestUtils;

public class TokenConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return DigestUtils.md5Hex(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}
