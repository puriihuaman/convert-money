import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;

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
            }
        }
        while (option != 0);
        
        farewellMessage();
    }
    
    private static void showMenu() {
        StringBuilder menuInfo = new StringBuilder();
        LinkedList<Menu> menuList = menuList();
        
        menuInfo.append("----------------------------------------\n");
        menuInfo.append("|  BIENVENIDO AL CONVERSOR DE MONEDAS  |\n");
        menuInfo.append("----------------------------------------\n");
        
        for (Menu menu : menuList) {
            menuInfo.append("| %s: %s|\n".formatted(menu.getKey(), menu.getValue()));
        }
        
        System.out.println(menuInfo);
    }
    
    private static Integer getOption() {
        try {
            System.out.print("Elija una opción válida: ");
            int option = Integer.parseInt(sc.nextLine());
            
            if (option <= 0) option = 0;
            else {
                if (menuList().get(option - 1).getKey() != option) {
                    System.out.println("\n******************************************");
                    System.out.println("❌ Opción incorrecta. Vuelva a intentarlo.");
                    System.out.println("******************************************\n");
                }
            }
            return option;
        } catch (NumberFormatException ex) {
            System.out.println("\n*************************************");
            System.out.println("❌ El carácter digitado no es valido.");
            System.out.println("*************************************\n");
            
        } catch (Exception ex) {
            System.out.println("Ocurrió un error inesperado.");
        }
        return 9;
    }
    
    private static Double getAmountToConvert() {
        try {
            double value;
            do {
                System.out.print("Digite el monto a convertir: ");
                value = Double.parseDouble(sc.nextLine());
            }
            while (value <= 0);
            
            return value;
        } catch (NumberFormatException ex) {
            System.out.println("\n*********************************************");
            System.out.println("❌ El carácter digitado no es un número válido.");
            System.out.println("*********************************************\n");
        } catch (Exception ex) {
            System.out.println("Ocurrió un error inesperado.");
        }
        return 0.0;
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
            
            Gson
                gson =
                new Gson()
                    .newBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
                    .setPrettyPrinting()
                    .create();
            
            
            MoneyTO coin = gson.fromJson(body, MoneyTO.class);
            Money money = new Money(
                coin.baseCode(),
                                    coin.targetCode(),
                                    coin.conversionRate(),
                                    coin.conversionResult()
            );
            
            System.out.print(showChange(money, amount));
        } catch (IllegalArgumentException ex) {
            System.out.println("\n************************************************");
            System.out.println("❌ Error en la URL, verifique el formato correcto.");
            System.out.println("************************************************\n");
        } catch (Exception ex) {
            System.out.println("\n*****************************");
            System.out.println("❌ Ocurrió un error inesperado.");
            System.out.println("*****************************\n");
        }
    }
    
    private static String showChange(Money money, double amount) {
        return """
               
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
        
    }
    
    private static LinkedList<Menu> menuList() {
        LinkedList<Menu> options = new LinkedList<>();
        
        options.add(new Menu(1, "Dólar < a > Sol Peruano           "));
        options.add(new Menu(2, "Sol Peruano < a > Dólar           "));
        options.add(new Menu(3, "Dólar < a > Peso Argentino        "));
        options.add(new Menu(4, "Peso Argentino < a > Dólar        "));
        options.add(new Menu(5, "Dólar < a > Real Brasileño        "));
        options.add(new Menu(6, "Real Brasileño < a > Dólar        "));
        options.add(new Menu(7, "Dólar < a > Peso Colombiano       "));
        options.add(new Menu(8, "Peso Colombiano < a > Dólar       "));
        options.add(new Menu(0, "Salir                             "));
        
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

