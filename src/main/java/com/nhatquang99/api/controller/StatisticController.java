package com.nhatquang99.api.controller;

import com.nhatquang99.api.service.IStatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistic")
@RequiredArgsConstructor
@Tag(name = "Statistic Service")
public class StatisticController {

    @Autowired
    private IStatisticService statisticService;

    @CrossOrigin
    @GetMapping("/year")
    @Operation(description = "Lấy thống kê theo năm")
    public ResponseEntity<Object> getStatisticByYear(@RequestParam(name = "year") int year) {
        return ResponseEntity.ok(statisticService.getByYear(year));
    }

    @CrossOrigin
    @GetMapping("/month")
    @Operation(description = "Lấy thống kê theo tháng")
    public ResponseEntity<Object> getStatisticByMonth(@RequestParam(name = "month") int month, @RequestParam(name = "year") int year) {
        return ResponseEntity.ok(statisticService.getByMonth(month, year));
    }

}
