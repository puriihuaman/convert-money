package dto;

public abstract class ErrorInfo {
    public static void showError(String message) {
        System.out.printf(
            """
            
            ***********************************************
            ¡¡❌ OCURRIÓ UN ERROR!!
            
             %s
            ***********************************************
            
            """, message
        );
    }
    
    public static void globalError() {
        showError("Ocurrió un error inesperado.");
    }
}
