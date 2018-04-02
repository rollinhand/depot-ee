package org.kivio.entities;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Embedded Id for a portfolio entry.
 */
@Getter
@Setter
@Embeddable
public class PortfolioId implements Comparable {
    @Temporal(TemporalType.DATE)
    @Column(name = "NAV_DATE", nullable = false)
    private Date navDate;

    @Column(name = "ISIN", nullable = false, length = 12)
    private String isin;

    @Transient
    private Date yearMonth;

    public PortfolioId() {

    }

    public PortfolioId(Date navDate, String isin) {
        this.navDate = navDate;
        this.isin = isin;
    }

    public Date getYearMonth() {
        return Date.from(
                LocalDateTime.ofInstant(navDate.toInstant(),
                        ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1)
                        .atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PortfolioId that = (PortfolioId) o;

        return new EqualsBuilder()
                .append(navDate, that.navDate)
                .append(isin, that.isin)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(navDate)
                .append(isin)
                .toHashCode();
    }

    @Override
    public int compareTo(Object o) {
        PortfolioId other = (PortfolioId) o;
        return new CompareToBuilder()
                .append(this.getNavDate(), other.getNavDate())
                .append(this.getIsin(), other.getIsin())
                .toComparison();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("navDate", navDate)
                .append("isin", isin)
                .append("yearMonth", yearMonth)
                .toString();
    }
}
