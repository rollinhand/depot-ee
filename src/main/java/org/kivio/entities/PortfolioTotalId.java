package org.kivio.entities;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class PortfolioTotalId implements Serializable, Comparable {
    private int year;
    private int month;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof PortfolioTotalId)) return false;

        PortfolioTotalId that = (PortfolioTotalId) o;

        return new EqualsBuilder()
                .append(year, that.year)
                .append(month, that.month)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(year)
                .append(month)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("year", year)
                .append("month", month)
                .toString();
    }

    @Override
    public int compareTo(Object o) {
        PortfolioTotalId other = (PortfolioTotalId) o;
        return new CompareToBuilder()
                .append(this.year, other.year)
                .append(this.month, other.month)
                .toComparison();
    }
}
