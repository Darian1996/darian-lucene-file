package com.darian.darianlucenefile.rangeClass.domain;

import lombok.Data;

import java.util.Objects;

/**
 * 坐标转化
 */
@Data
public class XYCoordinate {
    private int x;
    private int y;

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        XYCoordinate xyCoordinate = (XYCoordinate) o;
        return x == xyCoordinate.x &&
                y == xyCoordinate.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}