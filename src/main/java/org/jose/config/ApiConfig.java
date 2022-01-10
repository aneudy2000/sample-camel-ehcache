package org.jose.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    private String treasuryUrl;

    public String getTreasuryUrl() {
        return treasuryUrl;
    }

    @Value("${http.treasury.url}")
    public void setTreasuryUrl(String treasuryUrl) {
        this.treasuryUrl = treasuryUrl;
    }
}
