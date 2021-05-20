package com.nhatquang99.api.service.impl;

import com.nhatquang99.api.payload.response.GenericResponse;
import com.nhatquang99.api.repository.BillRepository;
import com.nhatquang99.api.service.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;

@Service
public class StatisticServiceImpl implements IStatisticService {

    @Autowired
    private BillRepository billRepository;

    @Override
    public GenericResponse getByYear(int year) {
        List<Object> resultList = billRepository.selectTotalsOfMonth(year);

        Map<Integer, BigDecimal> map = new HashMap<>();
        for (Iterator<Object> it = resultList.iterator(); it.hasNext(); ) {
            Object[] object = (Object[]) it.next();
            map.put((Integer) object[0], (BigDecimal) object[1]);
        }

        BigDecimal[] result = new BigDecimal[12];
        for (int i = 0; i < 12; i++) {
            result[i] = BigDecimal.valueOf(0);
        }

        for (int month : map.keySet()) {
            result[month - 1] = map.get(month);
        }
        return new GenericResponse(HttpStatus.OK, "Thành công", result);
    }

    @Override
    public GenericResponse getByMonth(int month, int year) {
        List<Object> resultList = billRepository.selectTotalsOfDay(month, year);

        Map<Integer, BigDecimal> map = new HashMap<>();
        for (Iterator<Object> it = resultList.iterator(); it.hasNext(); ) {
            Object[] object = (Object[]) it.next();
            map.put((Integer) object[0], (BigDecimal) object[1]);
        }

        // Lấy số lượng ngày trong tháng
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();

        BigDecimal[] result = new BigDecimal[daysInMonth];
        for (int i = 0; i < daysInMonth; i++) {
            result[i] = BigDecimal.valueOf(0);
        }

        for (int day : map.keySet()) {
            result[day - 1] = map.get(day);
        }

        return new GenericResponse(HttpStatus.OK, "Thành công", result);
    }
}
