package model;

import com.google.gson.Gson;
import dto.ErrorInfo;
import dto.JsonConvert;
import dto.MoneyTO;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class RequestApi {
    private final String currencyExchangeRate;
    private final double amount;
    
    private static final String API_KEY = "19857e7a55481b1baeb9afff";
    
    public RequestApi(String currencyExchangeRate, double amount) {
        this.currencyExchangeRate = currencyExchangeRate;
        this.amount = amount;
    }
    
    public static void getRequest(RequestApi requestApi) {
        try {
            HttpResponse<String> response = createHttpClient(requestApi);
            
            assert response != null;
            String body = response.body();
            
            Gson gson = JsonConvert.getGson();
            
            MoneyTO coin = gson.fromJson(body, MoneyTO.class);
            Money money = new Money(
                coin.baseCode(),
                coin.targetCode(),
                coin.conversionRate(),
                coin.conversionResult(),
                requestApi.amount
            );
            
            money.showChange();
            
            History.saveToHistory(money);
        } catch (IllegalArgumentException ex) {
            ErrorInfo.showError("Error en la URL, verifique el formato correcto.");
        } catch (Exception ex) {
            ErrorInfo.globalError();
        }
    }
    
    private static HttpResponse<String> createHttpClient(RequestApi requestApi) {
        String apiUrl = createUrl(requestApi);
        
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();
        
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException ex) {
            ErrorInfo.showError("""
                                Error de entrada al realizar la petición HTTP:
                                %s
                                """.formatted(ex.getMessage()));
            return null;
        } catch (InterruptedException ex) {
            ErrorInfo.showError("""
                                La petición HTTP fue interrumpida:
                                %s
                                """.formatted(ex.getMessage()));
            return null;
        }
    }
    
    private static String createUrl(RequestApi requestApi) {
        return "https://v6.exchangerate-api.com/v6/%s/pair/%s%s".formatted(
            API_KEY,
            requestApi.currencyExchangeRate,
            urlEncoder(requestApi.amount)
        );
    }
    
    private static String urlEncoder(double value) {
        return URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8);
    }
}
