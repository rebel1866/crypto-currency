package com.idf.dao.dbaccess;

import com.idf.dao.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyDao extends JpaRepository<Currency, Integer> {
    Currency findCurrencyBySymbol(String symbol);
}
