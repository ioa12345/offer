package offer.model;


import offer.validator.StartDateValidator;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class Offer {

    private String id;

    @NotNull
    private String description;

    @StartDateValidator
    private String startDate;

    @NotNull
    private Integer validityTime;

    @NotNull
    @Valid
    private Price price;

    private Boolean isCancelled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(Integer validityTime) {
        this.validityTime = validityTime;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Boolean getCancelled() {
        return isCancelled;
    }

    public void setCancelled(Boolean cancelled) {
        isCancelled = cancelled;
    }
}
