package com.inventorysystem_project.controllers;

import com.inventorysystem_project.dtos.DetalleOrdenCompraDTO;
import com.inventorysystem_project.dtos.OrdenCompraDTO;
import com.inventorysystem_project.dtos.*;
import com.inventorysystem_project.entities.DetalleOrdenCompra;
import com.inventorysystem_project.entities.MateriaPrima;
import com.inventorysystem_project.entities.OrdenCompra;
import com.inventorysystem_project.serviceimplements.GoogleSheetsService;
import com.inventorysystem_project.serviceinterfaces.IMateriaPrimaService;
import com.inventorysystem_project.serviceinterfaces.IOrdenCompraService;
import com.inventorysystem_project.serviceinterfaces.IDetalleOrdenCompraService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.Map;
import java.io.IOException; // <-- AÑADE ESTA LÍNEA

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.Map;


@RestController
@RequestMapping("/ordenes-compra")
public class OrdenCompraController {
    @Value("${whatsapp.api.token}")
    private String WHATSAPP_API_TOKEN;
    @Value("${whatsapp.api.sender-id}")
    private String WHATSAPP_SENDER_ID;
    @Value("${whatsapp.api.version}")
    private String WHATSAPP_API_VERSION;
    @Autowired
    private GoogleSheetsService googleSheetsService; // <-- Inyecta el nuevo servicio

    @Autowired
    private IOrdenCompraService ordenCompraService;

    @Autowired
    private IDetalleOrdenCompraService detalleOrdenCompraService;
    @Autowired
    private IMateriaPrimaService materiaPrimaService; // <-- AÑADE ESTA LÍNEA
    // En OrdenCompraController.java
    @PostMapping("/registrar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    @Transactional
// CAMBIO 1: La función ya no es 'void'. Ahora devuelve una respuesta con la orden creada.
    public ResponseEntity<OrdenCompraDTO> registrar(@RequestBody OrdenCompraDTO dto) {
        ModelMapper m = new ModelMapper();

        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La orden de compra debe tener al menos un detalle.");
        }

        OrdenCompra ordenCompra = m.map(dto, OrdenCompra.class);
        ordenCompra.setDetalles(null);

        // Guardamos la cabecera y la re-asignamos para obtenerla con su ID generado
        ordenCompra = ordenCompraService.insert(ordenCompra);

        // Procesamos y guardamos los detalles
        for (DetalleOrdenCompraDTO detalleDTO : dto.getDetalles()) {
            if (detalleDTO.getMateriaPrimaId() == null) {
                throw new IllegalArgumentException("El producto en la orden de compra no tiene un ID válido (es nulo).");
            }
            DetalleOrdenCompra detalle = new DetalleOrdenCompra();
            detalle.setOrdenCompra(ordenCompra);

            MateriaPrima materiaPrimaPersistente = materiaPrimaService.listId(detalleDTO.getMateriaPrimaId());
            if (materiaPrimaPersistente == null) {
                throw new RuntimeException("Materia Prima con ID " + detalleDTO.getMateriaPrimaId() + " no encontrada.");
            }
            detalle.setMateriaPrima(materiaPrimaPersistente);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalleOrdenCompraService.insert(detalle);
        }

        // CAMBIO 2: Preparamos y devolvemos la orden completa al frontend.
        // Buscamos la orden recién creada para que incluya todos sus detalles.
        OrdenCompra ordenCompleta = ordenCompraService.listId(ordenCompra.getId());
        OrdenCompraDTO ordenCompletaDTO = m.map(ordenCompleta, OrdenCompraDTO.class);

        // Devolvemos el objeto DTO con un estado HTTP 201 (Created), que es la práctica correcta.
        return new ResponseEntity<>(ordenCompletaDTO, HttpStatus.CREATED);
    }
    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public List<OrdenCompraDTO> listar() {
        return ordenCompraService.list().stream().map(ordenCompra -> {
            ModelMapper m = new ModelMapper();
            return m.map(ordenCompra, OrdenCompraDTO.class);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public OrdenCompraDTO listarPorId(@PathVariable("id") Long id) {
        OrdenCompra ordenCompra = ordenCompraService.listId(id);
        ModelMapper m = new ModelMapper();
        return m.map(ordenCompra, OrdenCompraDTO.class);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void eliminar(@PathVariable("id") Long id) {
        ordenCompraService.delete(id);
    }


    // AÑADE ESTE NUEVO ENDPOINT
    @PostMapping("/registrar-en-sheet")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<String> registrarEnSheet(@RequestBody List<List<Object>> rows) {
        try {
            googleSheetsService.appendValues("InputRPA", rows);
            return ResponseEntity.ok("Registro añadido a Google Sheet exitosamente.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de IO al escribir en Google Sheets: " + e.getMessage());
        }
    }


    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public void modificar(@RequestBody OrdenCompraDTO dto) {
        ModelMapper m = new ModelMapper();
        OrdenCompra ordenCompra = m.map(dto, OrdenCompra.class);
        ordenCompraService.insert(ordenCompra);
    }
    // --- AÑADE ESTE NUEVO ENDPOINT COMPLETO PARA ENVIAR PDF ---
    // --- AÑADE ESTE NUEVO ENDPOINT COMPLETO ---
    @PostMapping("/enviar-pdf-whatsapp")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('GUEST')")
    public ResponseEntity<String> enviarPdfPorWhatsApp(@RequestBody WhatsAppDocumentRequestDTO requestDTO) {
        RestTemplate restTemplate = new RestTemplate();
        String mediaId;

        // --- PASO 1: SUBIR EL PDF A WHATSAPP ---
        try {
            String uploadUrl = String.format("https://graph.facebook.com/%s/%s/media", WHATSAPP_API_VERSION, WHATSAPP_SENDER_ID);

            HttpHeaders mediaHeaders = new HttpHeaders();
            mediaHeaders.setBearerAuth(WHATSAPP_API_TOKEN);
            mediaHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> mediaBody = new LinkedMultiValueMap<>();
            mediaBody.add("messaging_product", "whatsapp");

            byte[] pdfBytes = Base64.getDecoder().decode(requestDTO.getPdfBase64());

            // Para adjuntar el archivo como un recurso en memoria
            mediaBody.add("file", new org.springframework.core.io.ByteArrayResource(pdfBytes) {
                @Override
                public String getFilename() {
                    return requestDTO.getFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> mediaRequestEntity = new HttpEntity<>(mediaBody, mediaHeaders);

            ResponseEntity<Map> mediaResponse = restTemplate.postForEntity(uploadUrl, mediaRequestEntity, Map.class);
            mediaId = (String) mediaResponse.getBody().get("id");

            if (mediaId == null) {
                throw new RuntimeException("Fallo al subir el PDF: WhatsApp no devolvió un ID de medio.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el PDF a WhatsApp: " + e.getMessage());
        }

        // --- PASO 2: ENVIAR EL MENSAJE CON EL PDF ---
        try {
            String messagesUrl = String.format("https://graph.facebook.com/%s/%s/messages", WHATSAPP_API_VERSION, WHATSAPP_SENDER_ID);

            HttpHeaders messageHeaders = new HttpHeaders();
            messageHeaders.setContentType(MediaType.APPLICATION_JSON);
            messageHeaders.setBearerAuth(WHATSAPP_API_TOKEN);
            // --- INICIO DE LA MODIFICACIÓN ---
            // 1. Construir el mensaje de saludo personalizado
            String caption = String.format(
                    "Hola, %s. Somos de Frederick Cueros EIRL y quisiéramos solicitar una cotización para la siguiente orden de compra. Por favor, mandanos un mensaje a +51 912 921 025 para coordinar la cotizacion. Gracias. ",
                    requestDTO.getProviderName()
            );

            // 2. Modificar el cuerpo del mensaje para incluir el "caption"
            Map<String, Object> messageBody = Map.of(
                    "messaging_product", "whatsapp",
                    "to", requestDTO.getTo(),
                    "type", "document",
                    "document", Map.of(
                            "id", mediaId,
                            "filename", requestDTO.getFilename(),
                            "caption", caption // <-- AÑADIMOS EL SALUDO AQUÍ
                    )
            );
            HttpEntity<Map<String, Object>> messageEntity = new HttpEntity<>(messageBody, messageHeaders);
            restTemplate.postForEntity(messagesUrl, messageEntity, String.class);

            return ResponseEntity.ok("Mensaje con PDF enviado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el mensaje de WhatsApp: " + e.getMessage());
        }
    }
}
