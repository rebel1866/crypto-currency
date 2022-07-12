package com.idf.service.dtoconverter;

import com.idf.dao.entity.NotifyRequest;
import com.idf.service.dto.NotifyRequestDto;

public class NotifyDtoToEntityConverter {
    public static NotifyRequest convert(NotifyRequestDto notifyRequestDto) {
        NotifyRequest notifyRequest = new NotifyRequest();
        notifyRequest.setPrice(notifyRequestDto.getPrice());
        notifyRequest.setSymbol(notifyRequestDto.getSymbol());
        notifyRequest.setUserName(notifyRequestDto.getUserName());
        return notifyRequest;
    }
}
