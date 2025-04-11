package conversor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {
    private static final String API_KEY = "991a3d474c298d142a8e8e88";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String fromCurrency, toCurrency;
        double amount;

        while (true) {
            System.out.println("\n--- CONVERSOR DE MONEDAS ---");
            System.out.println("1. Convertir moneda");
            System.out.println("2. Salir");
            System.out.print("Selecciona una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            if (opcion == 2) {
                System.out.println("¡Gracias por usar el conversor!");
                break;
            }

            System.out.print("Moneda origen (ej. USD): ");
            fromCurrency = scanner.nextLine().toUpperCase();

            System.out.print("Moneda destino (ej. EUR): ");
            toCurrency = scanner.nextLine().toUpperCase();

            System.out.print("Cantidad a convertir: ");
            amount = scanner.nextDouble();
            scanner.nextLine();

            double resultado = convertirMoneda(fromCurrency, toCurrency, amount);
            System.out.printf("Resultado: %.2f %s = %.2f %s\n", amount, fromCurrency, resultado, toCurrency);
        }

        scanner.close();
    }

    public static double convertirMoneda(String from, String to, double amount) {
        try {
            String url = BASE_URL + API_KEY + "/pair/" + from + "/" + to + "/" + amount;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            if (!json.getString("result").equals("success")) {
                System.out.println("Error: No se pudo obtener la tasa de cambio.");
                return 0.0;
            }

            return json.getDouble("conversion_result");
        } catch (Exception e) {
            System.out.println("Error en la conversión: " + e.getMessage());
            return 0.0;
        }
    }
} 