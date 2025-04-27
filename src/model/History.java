package model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import dto.ErrorInfo;
import dto.HistoryTO;
import dto.JsonConvert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class History {
    private Money money;
    private String timestamp;
    private static final List<History> historyList = new ArrayList<>();
    
    private static final String HISTORY_DIRECTORY = "resources/static/history/";
    private static final String HISTORY_FILE_NAME = "history.json";
    
    public History() {
    }
    
    public History(Money money) {
        this.money = money;
        this.timestamp = LocalDateTime.now().toString();
    }
    
    public Money getMoney() {
        return money;
    }
    
    public void setMoney(Money money) {
        this.money = money;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public static void saveToHistory(Money money) {
        historyList.add(new History(money));
        saveHistoryToFile();
    }
    
    private static void saveHistoryToFile() {
        Gson gson = JsonConvert.getGson();
        
        File directory = new File(HISTORY_DIRECTORY);
        File historyFile = new File(directory, HISTORY_FILE_NAME);
        
        if (!createDirectoryIfNotExists(directory)) {
            ErrorInfo.showError("Error al crear el directorio para el historial.");
            return;
        }
        
        try (FileWriter writer = new FileWriter(historyFile, false)) {
            writer.write(gson.toJson(historyList));
            writer.write(System.lineSeparator());
        } catch (IOException ex) {
            ErrorInfo.showError("""
                                Error al escribir en el archivo de historial.
                                %s
                                """.formatted(ex.getMessage()));
        } catch (Exception ex) {
            ErrorInfo.globalError();
        }
    }
    
    private static boolean createDirectoryIfNotExists(File directory) {
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return true;
    }
    
    private static List<HistoryTO> getHistory() {
        List<HistoryTO> histories = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("resources/static/history/history.json");
            
            Type historyListType = new TypeToken<ArrayList<HistoryTO>>() {}.getType();
            histories = new Gson().fromJson(fileReader, historyListType);
            fileReader.close();
        } catch (FileNotFoundException ex) {
            ErrorInfo.showError("El archivo history.json no fue encontrado.");
        } catch (JsonSyntaxException ex) {
            ErrorInfo.showError("El archivo history.json tiene un forma JSON inválido.");
        } catch (IOException ex) {
            ErrorInfo.showError("""
                                Error de lectura al acceder a history.json:
                                %s
                                """.formatted(ex.getMessage()));
        }
        
        return histories;
    }
    
    public static void showHistory() {
        String info;
        String message = "";
        
        List<HistoryTO> histories = getHistory();
        
        if (histories == null || histories.isEmpty()) {
            message = "Aún no hay historial de cambios de monedas.";
        } else {
            
            message += """
                       
                       --------------- HISTORIAL ---------------
                        📌 Conversión realizada:
                       -----------------------------------------
                       """;
            StringBuilder sb = new StringBuilder();
            
            for (HistoryTO history : histories) {
                String formattedDate = formatDate(history.timestamp());
                
                sb.append("💱").append(System.lineSeparator());
                sb.append("🔹 Monto:            %.2f".formatted(20f)).append(System.lineSeparator());
                sb.append("🔹 Moneda origen:    %s".formatted(history.money().baseCode())).append(
                    System.lineSeparator());
                sb.append("🔹 Moneda destino:   %s".formatted(history.money().targetCode())).append(
                    System.lineSeparator());
                sb.append("🔹 Valor de cambio:  %.2f".formatted(history.money().conversionRate()))
                  .append(System.lineSeparator());
                sb.append("🔹 Resultado:        %.2f".formatted(history.money().conversionResult()))
                  .append(System.lineSeparator());
                sb.append("🔹 Fecha:            %s".formatted(formattedDate))
                  .append(System.lineSeparator());
            }
            message += sb.toString();
        }
        
        info = """
               %s
               -----------------------------------------
               """.formatted(message);
        
        System.out.println(info);
    }
    
    private static String formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return LocalDateTime.parse(date).format(formatter);
    }
}
