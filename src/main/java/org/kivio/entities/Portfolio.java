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
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "PORTFOLIO")
@NamedQueries({
        @NamedQuery(name = "Portfolio.findAll",
                query = "SELECT p FROM Portfolio p ORDER BY p.portfolioId.navDate ASC"),
        @NamedQuery(name = "Portfolio.byNavDate",
                query = "SELECT p FROM Portfolio p WHERE p.portfolioId.navDate = :navDate"),
        @NamedQuery(name = "Portfolio.byIsin",
                query = "SELECT p FROM Portfolio p WHERE p.portfolioId.isin in (:isin)"),
        @NamedQuery(name = "Portfolio.byIsinAndDate",
                query = "SELECT p FROM Portfolio p WHERE p.portfolioId.navDate = :navDate AND p.portfoioId.isin in (:isin)"),

})
@NamedNativeQueries({
        @NamedNativeQuery(name = "Portfolio.navDates",
                          query = "SELECT DISTINCT p.NAV_DATE FROM PORTFOLIO p ORDER BY p.NAV_DATE DESC",
                          resultClass = Date.class),
        @NamedNativeQuery(name = "Portfolio.latestNavDate",
                          query = "SELECT MAX(p.NAV_DATE) FROM PORTFOLIO p",
                          resultClass = Date.class)
})
public class Portfolio implements Comparable {
    @EmbeddedId
    private PortfolioId portfolioId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "isin")
    private Instrument instrument;

    @Column(name = "NOMINAL")
    private BigDecimal nominal;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "BUYING_RATE")
    private BigDecimal buyingRate;

    @Column(name = "BUYING_MARKET_VALUE")
    private BigDecimal buyingMarketValue;

    @Column(name = "CURRENT_RATE")
    private BigDecimal currentRate;

    @Column(name = "CURRENT_MARKET_VALUE")
    private BigDecimal currentMarketValue;

    @Transient
    public BigDecimal getReturnNominal() {
        return currentMarketValue.subtract(buyingMarketValue);
    }

    @Transient
    public BigDecimal getReturnPct() {
        BigDecimal gain = getReturnNominal();
        return gain.divide(buyingMarketValue, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Portfolio portfolio = (Portfolio) o;

        return new EqualsBuilder()
                .append(portfolioId, portfolio.portfolioId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(portfolioId)
                .toHashCode();
    }

    @Override
    public int compareTo(Object obj) {
        Portfolio other = (Portfolio) obj;
        return new CompareToBuilder()
                .append(this.getPortfolioId(), other.getPortfolioId())
                .toComparison();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("portfolioId", portfolioId)
                .append("instrument", instrument)
                .append("nominal", nominal)
                .append("currency", currency)
                .append("buyingRate", buyingRate)
                .append("buyingMarketValue", buyingMarketValue)
                .append("currentRate", currentRate)
                .append("currentMarketValue", currentMarketValue)
                .toString();
    }
}
