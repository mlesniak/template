package com.mlesniak.template.statistic;

import com.mlesniak.template.model.StatisticDO;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Provied different statistical computation methods for timings.
 */
public class Computation implements Serializable {
    List<Long> values;
    long start = Long.MAX_VALUE;
    long end = Long.MIN_VALUE;
    private double average;
    private double min;
    private double max;

    public Computation(List<StatisticDO> source) {
        values = new LinkedList<>();
        if (source.isEmpty()) {
            return;
        }

        for (StatisticDO statisticDO : source) {
            values.add(statisticDO.getTime());

            if (start > statisticDO.getTimestamp()) {
                start = statisticDO.getTimestamp();
            }
            if (end < statisticDO.getTimestamp()) {
                end = statisticDO.getTimestamp();
            }
        }

        computeAverage();
        computeMinAndMax();
    }

    public Computation() {
        average = min = max = 0;
        values = Collections.emptyList();
    }

    private void computeMinAndMax() {
        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;

        for (Long value : values) {
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }
    }

    private void computeAverage() {
        average = 0.0;
        double sum = 0.0;

        for (Long value : values) {
            sum += value;
        }

        if (!values.isEmpty()) {
            average = sum / values.size();
        }
    }

    public double getAverage() {
        return average;
    }

    public List<Long> getValues() {
        return Collections.unmodifiableList(values);
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "Computation{" +
                "values=" + values +
                ", start=" + start +
                ", end=" + end +
                ", average=" + average +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
