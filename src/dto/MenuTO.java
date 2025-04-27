package dto;

import java.util.LinkedList;
import java.util.Scanner;

public record MenuTO(int key, String value) {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final LinkedList<MenuTO> menuList = new LinkedList<>();
    
    static {
        createMenu();
    }
    
    private static void createMenu() {
        menuList.add(new MenuTO(1, "Dólar < a > Sol Peruano"));
        menuList.add(new MenuTO(2, "Sol Peruano < a > Dólar"));
        menuList.add(new MenuTO(3, "Dólar < a > Peso Argentino"));
        menuList.add(new MenuTO(4, "Peso Argentino < a > Dólar"));
        menuList.add(new MenuTO(5, "Dólar < a > Real Brasileño"));
        menuList.add(new MenuTO(6, "Real Brasileño < a > Dólar"));
        menuList.add(new MenuTO(7, "Dólar < a > Peso Colombiano"));
        menuList.add(new MenuTO(8, "Peso Colombiano < a > Dólar"));
        menuList.add(new MenuTO(20, "Historial"));
        menuList.add(new MenuTO(0, "Salir"));
    }
    
    public static void showMenu() {
        StringBuilder menuInfo = new StringBuilder();
        
        menuInfo.append("-------------------------------------\n");
        menuInfo.append("|            BIENVENIDO             |\n");
        menuInfo.append("|      AL CONVERSOR DE MONEDAS      |\n");
        menuInfo.append("-------------------------------------\n");
        
        for (MenuTO menu : menuList) {
            menuInfo.append("| %-2d: %-30s|\n".formatted(menu.key(), menu.value()));
        }
        
        System.out.println(menuInfo);
    }
    
    public static int getMenuOption() {
        int option;
        
        try {
            System.out.print("Elija una opción válida: ");
            if (SCANNER.hasNextInt()) {
                option = SCANNER.nextInt();
                SCANNER.nextLine();
                
                int finalOption = option;
                boolean exists = menuList.stream().anyMatch(menu -> menu.key() == finalOption);
                
                if (!exists) {
                    ErrorInfo.showError("Opción incorrecta. Vuelva a intentarlo.");
                    return -1;
                }
                return option;
            } else {
                ErrorInfo.showError("El carácter digitado no es valido.");
                SCANNER.nextLine();
                return -1;
            }
        } catch (NumberFormatException ex) {
            ErrorInfo.showError("El carácter digitado no es valido.");
        } catch (Exception ex) {
            ErrorInfo.globalError();
        }
        return -1;
    }
    
    public static double getAmountToConvert() {
        double value = -1;
        boolean validInput = false;
        
        while (!validInput) {
            System.out.print("Digite el monto a convertir: ");
            String input = SCANNER.nextLine();
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
    
    public static void farewellMessage() {
        System.out.print("""
                         \n-------------------------------------------
                         | GRACIAS POR SU VISITA.👋 !HASTA PRONTO! |
                         -------------------------------------------
                         \n""");
    }
}
