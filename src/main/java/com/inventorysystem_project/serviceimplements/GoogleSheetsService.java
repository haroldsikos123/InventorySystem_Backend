package com.inventorysystem_project.serviceimplements;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleSheetsService {

    private static final String APPLICATION_NAME = "Inventory System";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SPREADSHEET_ID = "1esMo2ILu_xNgcvEm6G49QBd8PNhpj_3gHWg7ZtVP04Q";

    private final Sheets sheetsService;

    // --- INICIO DE LA CORRECCIÓN ---
    // La inyección se hace en el constructor, no como un campo de la clase.
    @Autowired
    public GoogleSheetsService(@Value("${GOOGLE_CREDENTIALS_PATH:#{null}}") String credentialsPath) throws IOException, GeneralSecurityException {
        InputStream in;

        // La lógica usa el 'credentialsPath' que llega como parámetro del constructor.
        if (credentialsPath != null && !credentialsPath.isEmpty()) {
            System.out.println("Cargando credenciales desde la ruta externa: " + credentialsPath);
            in = new FileInputStream(credentialsPath);
        } else {
            // Este bloque 'else' es tu respaldo para que funcione en local.
            System.out.println("Cargando credenciales desde la ruta interna de resources: /credentials.json");
            in = GoogleSheetsService.class.getResourceAsStream("/credentials.json");
        }

        if (in == null) {
            // Este error ahora es mucho más claro sobre la causa raíz.
            throw new IOException("No se pudo encontrar el archivo de credenciales. Asegúrate de que la variable de entorno GOOGLE_CREDENTIALS_PATH esté bien configurada en Render o que credentials.json exista en resources para ejecución local.");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/spreadsheets"));

        this.sheetsService = new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    // --- FIN DE LA CORRECCIÓN ---


    public void appendValues(String sheetName, List<List<Object>> values) throws IOException {
        String range = sheetName + "!A:M";
        ValueRange body = new ValueRange().setValues(values);

        AppendValuesResponse result = sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, range, body)
                .setValueInputOption("USER_ENTERED")
                .execute();

        System.out.printf("%d celdas añadidas.%n", result.getUpdates().getUpdatedCells());
    }
}