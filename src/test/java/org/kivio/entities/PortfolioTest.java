package org.kivio.entities;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PortfolioTest {
    @Test
    public void testIsoCode() {
        Portfolio p = new Portfolio();
        p.setPortfolioId(new PortfolioId(null, "US8740391003"));
        assertThat(p.getIsoCode(), is("US"));
    }
}
