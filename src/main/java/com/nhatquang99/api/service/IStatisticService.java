package com.nhatquang99.api.service;

import com.nhatquang99.api.payload.response.GenericResponse;

public interface IStatisticService {
    GenericResponse getByYear(int year);
    GenericResponse getByMonth(int month, int year);
}
