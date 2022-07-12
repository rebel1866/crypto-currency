package com.idf.service.logic;

import com.idf.dao.entity.Currency;
import com.idf.service.dto.CurrencyDto;
import com.idf.service.dto.NotifyRequestDto;
import com.idf.service.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * This interface represents application logic
 *
 * @author Stanislav Melnikov
 * @version 1.0
 */
public interface CurrencyLogic {
    /**
     * Get all currencies
     * @param
     * @return list of CurrencyDto objects
     */
    List<CurrencyDto> findAllCurrencies();
    /**
     * Get currency by id
     * @param id
     * @return CurrencyDto object
     */
    CurrencyDto findCurrencyById(int id);

    @Transactional
    Currency updateCurrencyPriceById(int id, double price);

    /**
     * update currencies according to relevant prices
     * @param
     * @return CurrencyDto object
     */
    List<CurrencyDto> updateCurrencyPrices();
    /**
     * Method starts new thread which is supposed to check currency changes and
     * notify uses as soon as it changed
     * @param
     * @return NotifyRequestDto object
     */
    NotifyRequestDto handleNotifyRequest(NotifyRequestDto notifyRequestDto);

    double getRelevantPriceById(int id) throws ServiceException;
}
