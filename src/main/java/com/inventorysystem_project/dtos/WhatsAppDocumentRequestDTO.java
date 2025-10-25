package com.inventorysystem_project.dtos;

public class WhatsAppDocumentRequestDTO {
    private String to;
    private String filename;
    private String pdfBase64;
    private String providerName; // <-- AÑADE ESTA LÍNEA

    // --- Getters y Setters ---
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public String getPdfBase64() { return pdfBase64; }
    public void setPdfBase64(String pdfBase64) { this.pdfBase64 = pdfBase64; }
    public String getProviderName() { return providerName; } // <-- AÑADE ESTE GETTER
    public void setProviderName(String providerName) { this.providerName = providerName; } // <-- AÑADE ESTE SETTER
}