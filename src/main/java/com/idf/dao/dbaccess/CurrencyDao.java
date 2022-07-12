package com.idf.dao.dbaccess;

import com.idf.dao.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface represents provides database access for Currency entities
 *
 * @author Stanislav Melnikov
 * @version 1.0
 */
public interface CurrencyDao extends JpaRepository<Currency, Integer> {
    /**
     * @param symbol of type String
     * @return Currency object
     */
    Currency findCurrencyBySymbol(String symbol);
}
