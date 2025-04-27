package controller;

import dto.MenuTO;
import model.History;
import model.RequestApi;

public class RunApp {
    public static void run() {
        int option;
        double amount;
        
        do {
            MenuTO.showMenu();
            option = MenuTO.getMenuOption();
            
            switch (option) {
                case 1:
                    amount = MenuTO.getAmountToConvert();
                    RequestApi.getRequest(new RequestApi("USD/PEN/", amount));
                    break;
                case 2:
                    amount = MenuTO.getAmountToConvert();
                    RequestApi.getRequest(new RequestApi("EUR/PEN/", amount));
                    break;
                case 3:
                    amount = MenuTO.getAmountToConvert();
                    RequestApi.getRequest(new RequestApi("USD/ARS/", amount));
                    break;
                case 4:
                    amount = MenuTO.getAmountToConvert();
                    RequestApi.getRequest(new RequestApi("ARS/USD/", amount));
                    break;
                case 5:
                    amount = MenuTO.getAmountToConvert();
                    RequestApi.getRequest(new RequestApi("USD/BRL/", amount));
                    break;
                case 6:
                    amount = MenuTO.getAmountToConvert();
                    RequestApi.getRequest(new RequestApi("BRL/USD/", amount));
                    break;
                case 7:
                    amount = MenuTO.getAmountToConvert();
                    RequestApi.getRequest(new RequestApi("USD/COP/", amount));
                    break;
                case 8:
                    amount = MenuTO.getAmountToConvert();
                    RequestApi.getRequest(new RequestApi("COP/USD/", amount));
                    break;
                case 20:
                    History.showHistory();
                    break;
            }
        } while (option != 0);
        
        MenuTO.farewellMessage();
    }
}
