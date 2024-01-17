package com.bnguimgo.springboot.celad.mapper;

import com.bnguimgo.springboot.celad.dto.UserDTO;
import com.bnguimgo.springboot.celad.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoToDomainMapper {

    User toDomainModel(UserDTO userDto);
}
