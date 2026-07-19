package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.Stock;
import com.cognizant.ormlearn.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional(readOnly = true)
    public List<Stock> getFacebookStocksSeptember2019() {
        return stockRepository.findByCodeAndDateBetween("FB", Date.valueOf("2019-09-01"), Date.valueOf("2019-09-30"));
    }

    @Transactional(readOnly = true)
    public List<Stock> getGoogleStocksAbovePrice(BigDecimal price) {
        return stockRepository.findByCodeAndCloseGreaterThan("GOOGL", price);
    }

    @Transactional(readOnly = true)
    public List<Stock> getTop3HighestVolumeStocks() {
        return stockRepository.findTop3ByOrderByVolumeDesc();
    }

    @Transactional(readOnly = true)
    public List<Stock> getTop3LowestStocks(String code) {
        return stockRepository.findTop3ByCodeOrderByCloseAsc(code);
    }
}
