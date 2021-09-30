package org.camunda.bpm.getstarted.loanapproval;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Table("mortgage_application")
public class MortgageApplication {
    @Id
    private UUID id;
    private UUID userId;
    private String property;
    private BigDecimal price;
    private boolean sent;
    private MortgageApplicationStatus status = MortgageApplicationStatus.NEW;
    @CreatedDate
    private Instant createdDateTime;

    public MortgageApplication() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Transient
    public MortgageAppDto toDto() {
        var ret = new MortgageAppDto();
        ret.setId(this.id);
        ret.setProperty(this.property);
        ret.setUserId(this.userId);
        ret.setCreatedDateTime(this.createdDateTime);
        ret.setPrice(this.price);
        ret.setStatus(this.status);
        ret.setSent(this.sent);
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

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
