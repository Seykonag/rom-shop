package kz.services.romshop.utilits;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.math.BigDecimal;
import java.util.Map;

public class CurrencyConverter {
    private static final String API_KEY = "a951883aeb6a4541a3ff43a9b9caa3df";
    private static final String BASE_URL = "https://openexchangerates.org/api/latest.json";

    public BigDecimal convertKZTtoRUB(BigDecimal amountInKZT) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("app_id", API_KEY)
                .queryParam("symbols", "KZT,RUB");

        OpenExchangeRatesResponse response = restTemplate.getForObject(builder.toUriString(), OpenExchangeRatesResponse.class);

        if (response != null && response.getRates() != null) {
            BigDecimal kztToUsd = response.getRates().get("KZT");
            BigDecimal rubToUsd = response.getRates().get("RUB");

            if (kztToUsd != null && rubToUsd != null) {
                BigDecimal rubToKzt = rubToUsd.divide(kztToUsd, 4, BigDecimal.ROUND_HALF_UP);
                return amountInKZT.multiply(rubToKzt);
            }
        }
        throw new RuntimeException("Unable to get currency conversion rate");
    }

    private static class OpenExchangeRatesResponse {
        private Map<String, BigDecimal> rates;

        public Map<String, BigDecimal> getRates() {
            return rates;
        }

        public void setRates(Map<String, BigDecimal> rates) {
            this.rates = rates;
        }
    }
}
