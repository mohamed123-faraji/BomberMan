package fr.malibu.assistants.jws.converter;

import org.modelmapper.ModelMapper;

public class ConvertToModel {
    public static <T, U> U convertToModel(T entity, Class<U> modelClass) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(entity, modelClass);
    }
}