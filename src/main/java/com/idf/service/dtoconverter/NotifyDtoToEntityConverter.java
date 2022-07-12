package com.idf.service.dtoconverter;

import com.idf.dao.entity.Currency;
import com.idf.dao.entity.NotifyRequest;
import com.idf.dao.entity.User;
import com.idf.service.dto.NotifyRequestDto;

public class NotifyDtoToEntityConverter {
    public static NotifyRequest convert(NotifyRequestDto notifyRequestDto) {
        NotifyRequest notifyRequest = new NotifyRequest();
        User user = new User();
        user.setUserName(notifyRequestDto.getUserName());
        notifyRequest.setUser(user);
        notifyRequest.setPrice(notifyRequestDto.getPrice());
        return notifyRequest;
    }
}
