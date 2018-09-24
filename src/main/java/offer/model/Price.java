package offer.model;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Price {

    @NotNull
    private BigDecimal amount;
    @NotNull
    private String currency;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
