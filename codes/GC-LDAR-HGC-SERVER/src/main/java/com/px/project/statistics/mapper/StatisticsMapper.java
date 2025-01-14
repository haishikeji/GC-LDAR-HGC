package com.px.project.statistics.mapper;

import com.px.common.annotation.DataScopePlant;
import com.px.project.statistics.domain.Statistics;

import java.util.List;

public interface StatisticsMapper {

    @DataScopePlant(deptAlias = "d")
    public List<Statistics> countPlant(Statistics statistics);

    public Statistics countDevice(Statistics statistics);

    public List<Statistics> countPoint(Statistics statistics);

    public Statistics countYxf(Statistics statistics);

    public Statistics countWxf(Statistics statistics);

    public List<Statistics> countXlcd(Statistics statistics);

    @DataScopePlant(deptAlias = "d")
    public List<Statistics> countPfl(Statistics statistics);

    @DataScopePlant(deptAlias = "d")
    public List<Statistics> countXlcdByPoint(Statistics statistics);
}
