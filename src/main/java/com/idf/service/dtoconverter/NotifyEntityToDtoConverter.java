package com.idf.service.dtoconverter;

import com.idf.dao.entity.NotifyRequest;
import com.idf.service.dto.NotifyRequestDto;

public class NotifyEntityToDtoConverter {
    public static NotifyRequestDto convert(NotifyRequest notifyRequest) {
        NotifyRequestDto notifyRequestDto = new NotifyRequestDto();
        notifyRequestDto.setPrice(notifyRequest.getPrice());
        notifyRequestDto.setSymbol(notifyRequest.getCurrency().getSymbol());
        notifyRequestDto.setUserName(notifyRequest.getUser().getUserName());
        return notifyRequestDto;
    }
}
