import com.google.gson.Gson;
import dto.ErrorInfo;
import dto.JsonConvert;
import dto.MenuTO;
import dto.MoneyTO;
import model.History;
import model.Money;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
        run();
    }
    
    private static void run() {
        int option;
        double amount;
        
        do {
            showMenu();
            option = getOption();
            
            switch (option) {
                case 1:
                    amount = getAmountToConvert();
                    getRequest("USD/PEN/", amount);
                    break;
                case 2:
                    amount = getAmountToConvert();
                    getRequest("PEN/USD/", amount);
                    break;
                case 3:
                    amount = getAmountToConvert();
                    getRequest("USD/ARS/", amount);
                    break;
                case 4:
                    amount = getAmountToConvert();
                    getRequest("ARS/USD/", amount);
                    break;
                case 5:
                    amount = getAmountToConvert();
                    getRequest("USD/BRL/", amount);
                    break;
                case 6:
                    amount = getAmountToConvert();
                    getRequest("BRL/USD/", amount);
                    break;
                case 7:
                    amount = getAmountToConvert();
                    getRequest("USD/COP/", amount);
                    break;
                case 8:
                    amount = getAmountToConvert();
                    getRequest("COP/USD/", amount);
                    break;
                case 20:
                    History.showHistory();
                    break;
            }
        }
        while (option != 0);
        
        farewellMessage();
    }
    
    private static void showMenu() {
        StringBuilder menuInfo = new StringBuilder();
        LinkedList<MenuTO> menuList = menuList();
        
        menuInfo.append("----------------------------------------\n");
        menuInfo.append("|  BIENVENIDO AL CONVERSOR DE MONEDAS  |\n");
        menuInfo.append("----------------------------------------\n");
        
        for (MenuTO menu : menuList) {
            menuInfo.append("| %s: %s|\n".formatted(menu.getKey(), menu.getValue()));
        }
        
        System.out.println(menuInfo);
    }
    
    private static int getOption() {
        try {
            System.out.print("Elija una opción válida: ");
            int option = Integer.parseInt(sc.nextLine());
            
            boolean exists = menuList().stream().anyMatch(menu -> menu.getKey() == option);
            
            if (!exists) {
                ErrorInfo.showError("Opción incorrecta. Vuelva a intentarlo.");
                return -1;
            }
            return option;
        } catch (NumberFormatException ex) {
            ErrorInfo.showError("El carácter digitado no es valido.");
        } catch (Exception ex) {
            ErrorInfo.globalError();
        }
        return -1;
    }
    
    private static double getAmountToConvert() {
        double value = -1;
        boolean validInput = false;
        
        while (!validInput) {
            System.out.print("Digite el monto a convertir: ");
            String input = sc.nextLine();
            try {
                value = Double.parseDouble(input);
                if (value > 0) {
                    validInput = true;
                } else {
                    ErrorInfo.showError("El monto debe ser un número positivo.");
                }
            } catch (NumberFormatException ex) {
                ErrorInfo.showError("El carácter digitado no es un número válido.");
            } catch (Exception ex) {
                ErrorInfo.globalError();
            }
        }
        return value;
    }
    
    private static String urlEncoder(double value) {
        return URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8);
    }
    
    private static void getRequest(String typeChangeUrl, double amount) {
        try {
            String apiUrl = createUrl(typeChangeUrl, amount);
            
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).build();
            
            HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );
            
            String body = response.body();
            
            Gson gson = JsonConvert.getGson();
            
            MoneyTO coin = gson.fromJson(body, MoneyTO.class);
            Money money = new Money(
                coin.baseCode(),
                                    coin.targetCode(),
                                    coin.conversionRate(),
                                    coin.conversionResult()
            );
            
            showChange(money, amount);
            History.saveToHistory(money);
        } catch (IllegalArgumentException ex) {
            ErrorInfo.showError("Error en la URL, verifique el formato correcto.");
        } catch (Exception ex) {
            ErrorInfo.globalError();
        }
    }
    
    private static void showChange(Money money, double amount) {
        String info = """
                      
                      ----------------------------------------
                      |          CONVERSIÓN EXITOSA          |
                      ----------------------------------------
                      | Valor en [%s]: %.2f
                      | Convertido a [%s]: %.2f
                      ----------------------------------------
                      
                      """.formatted(
            money.getBaseCode(),
            amount,
            money.getTargetCode(),
            money.getConversionResult()
        );
        System.out.println(info);
    }
    
    private static LinkedList<MenuTO> menuList() {
        LinkedList<MenuTO> options = new LinkedList<>();
        
        options.add(new MenuTO(1, "Dólar < a > Sol Peruano           "));
        options.add(new MenuTO(2, "Sol Peruano < a > Dólar           "));
        options.add(new MenuTO(3, "Dólar < a > Peso Argentino        "));
        options.add(new MenuTO(4, "Peso Argentino < a > Dólar        "));
        options.add(new MenuTO(5, "Dólar < a > Real Brasileño        "));
        options.add(new MenuTO(6, "Real Brasileño < a > Dólar        "));
        options.add(new MenuTO(7, "Dólar < a > Peso Colombiano       "));
        options.add(new MenuTO(8, "Peso Colombiano < a > Dólar       "));
        options.add(new MenuTO(20, "Historial                        "));
        options.add(new MenuTO(0, "Salir                             "));
        
        return options;
    }
    
    private static String createUrl(String typeChangeUrl, double amount) {
        final String API_KEY = "19857e7a55481b1baeb9afff";
        return "https://v6.exchangerate-api.com/v6/%s/pair/%s%s".formatted(
            API_KEY,
            typeChangeUrl,
            urlEncoder(amount)
        );
    }
    
    private static void farewellMessage() {
        System.out.print("""
                         \n-----------------------------------------
                         | GRACIAS POR SU VISITA. !HASTA PRONTO! |
                         -----------------------------------------
                         \n""");
    }
}

