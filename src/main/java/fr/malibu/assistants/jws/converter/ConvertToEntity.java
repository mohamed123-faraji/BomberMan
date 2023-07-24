package fr.malibu.assistants.jws.converter;

import org.modelmapper.ModelMapper;

public class ConvertToEntity {
    public static <T, U> U convertToEntity(T dto, Class<U> entityClass) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, entityClass);
    }
}