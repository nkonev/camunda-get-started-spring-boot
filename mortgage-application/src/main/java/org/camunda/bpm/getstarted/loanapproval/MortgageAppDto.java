package org.camunda.bpm.getstarted.loanapproval;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

class MortgageAppDto {
    private UUID id;
    private UUID userId;
    private String property;
    private BigDecimal price;
    private MortgageApplicationStatus status;
    private Instant createdDateTime;

    public MortgageAppDto() {
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @JsonIgnore
    public MortgageApplication toEntityForCreate(UUID userId) {
        var ret = new MortgageApplication();
        ret.setProperty(this.property);
        ret.setUserId(userId);
        ret.setCreatedDateTime(this.createdDateTime);
        ret.setPrice(this.price);
        if (this.status != null) {
            ret.setStatus(this.status);
        }
        return ret;
    }

    @JsonIgnore
    public MortgageApplication toEntityForUpdate(UUID userId) {
        var ret = new MortgageApplication();
        ret.setProperty(this.property);
        ret.setUserId(userId);
        ret.setCreatedDateTime(this.createdDateTime);
        ret.setPrice(this.price);
        ret.setId(this.id);
        if (this.status != null) {
            ret.setStatus(this.status);
        }
        return ret;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Instant getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Instant createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public MortgageApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(MortgageApplicationStatus status) {
        this.status = status;
    }
}
