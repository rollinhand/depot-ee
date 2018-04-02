package org.kivio.entities;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Class taking totals about portfolio for a month and year.
 *
 * @author Björn Berg
 * @since 2018-03-10
 * @version 1.0
 */
@Getter
@Setter
@Entity
@NamedNativeQueries({
        @NamedNativeQuery(name = "Portfolio.totalByNavDate",
                query = "select YEAR(NAV_DATE) AS YEAR, MONTH(NAV_DATE) AS MONTH,\n" +
                        "  AVG(CURRENT_MARKET_VALUE) AS MARKET_VALUE_AVG,\n" +
                        "  MAX(CURRENT_MARKET_VALUE) AS MARKET_VALUE_MAX,\n" +
                        "  MIN(CURRENT_MARKET_VALUE) AS MARKET_VALUE_MIN,\n" +
                        "  AVG(BUYING_MARKET_VALUE) AS BUYING_VALUE_AVG,\n" +
                        "  MAX(BUYING_MARKET_VALUE) AS BUYING_VALUE_MAX,\n" +
                        "  MIN(BUYING_MARKET_VALUE) AS BUYING_VALUE_MIN\n" +
                        "from PORTFOLIO_TOTAL \n" +
                        "WHERE NAV_DATE = ?1 \n" +
                        "GROUP BY YEAR(NAV_DATE), MONTH(NAV_DATE) \n" +
                        "ORDER BY YEAR DESC, MONTH DESC",
                resultClass = PortfolioTotal.class),
        @NamedNativeQuery(name = "Portfolio.total",
                query = "select YEAR(NAV_DATE) AS YEAR, MONTH(NAV_DATE) AS MONTH,\n" +
                        "  AVG(CURRENT_MARKET_VALUE) AS MARKET_VALUE_AVG,\n" +
                        "  MAX(CURRENT_MARKET_VALUE) AS MARKET_VALUE_MAX,\n" +
                        "  MIN(CURRENT_MARKET_VALUE) AS MARKET_VALUE_MIN,\n" +
                        "  AVG(BUYING_MARKET_VALUE) AS BUYING_VALUE_AVG,\n" +
                        "  MAX(BUYING_MARKET_VALUE) AS BUYING_VALUE_MAX,\n" +
                        "  MIN(BUYING_MARKET_VALUE) AS BUYING_VALUE_MIN\n" +
                        "from PORTFOLIO_TOTAL\n" +
                        "GROUP BY YEAR(NAV_DATE), MONTH(NAV_DATE)\n" +
                        "ORDER BY YEAR DESC, MONTH DESC",
               resultClass = PortfolioTotal.class)
})
public class PortfolioTotal implements Comparable {
    @Transient
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    @EmbeddedId
    private PortfolioTotalId totalId;
    @Column(name = "MARKET_VALUE_AVG")
    private BigDecimal marketValueAvg;

    @Column(name = "MARKET_VALUE_MAX")
    private BigDecimal marketValueMax;

    @Column(name = "MARKET_VALUE_MIN")
    private BigDecimal marketValueMin;

    @Column(name = "BUYING_VALUE_AVG")
    private BigDecimal buyingValueAvg;

    @Column(name = "BUYING_VALUE_MAX")
    private BigDecimal buyingValueMax;

    @Column(name = "BUYING_VALUE_MIN")
    private BigDecimal buyingValueMin;

    public PortfolioTotal() {}

    @Transient
    public YearMonth getYearMonth() {
        return YearMonth.of(this.totalId.getYear(), this.totalId.getMonth());
    }

    @Transient
    public Date getNavDate() {
        return Date.from(getYearMonth()
                .atDay(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
    }

    @Transient
    public BigDecimal getReturnNominalAvg() {
        if (marketValueAvg != null && buyingValueAvg != null) {
            return marketValueAvg.subtract(buyingValueAvg).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Transient
    public BigDecimal getReturnPct() {
        BigDecimal gain = getReturnNominalAvg();
        return gain.divide(buyingValueAvg, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getMarketValueAvg() {
        return this.marketValueAvg.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof PortfolioTotal)) return false;

        PortfolioTotal that = (PortfolioTotal) o;

        return new EqualsBuilder()
                .append(totalId, that.totalId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(totalId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("totalId", totalId)
                .append("marketValueAvg", marketValueAvg)
                .append("marketValueMax", marketValueMax)
                .append("marketValueMin", marketValueMin)
                .append("buyingValueAvg", buyingValueAvg)
                .append("buyingValueMax", buyingValueMax)
                .append("buyingValueMin", buyingValueMin)
                .toString();
    }

    @Override
    public int compareTo(Object o) {
        PortfolioTotal other = (PortfolioTotal) o;

        return new CompareToBuilder()
                .append(this.totalId, other.totalId)
                .toComparison();
    }
}
