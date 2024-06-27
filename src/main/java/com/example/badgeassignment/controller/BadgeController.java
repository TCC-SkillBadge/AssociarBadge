package com.example.badgeassignment.controller;

import com.example.badgeassignment.model.Badge;
import com.example.badgeassignment.service.BadgeService;
import com.example.badgeassignment.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/atribuir")
    public ResponseEntity<String> atribuirBadge(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, Object> payload) {

        String emailCom = (String) payload.get("emailCom");
        String emailEmpr = userDetails.getUsername();
        Integer idBadge = (Integer) payload.get("idBadge");

        try {
            badgeService.enviarEmailConfirmacao(emailCom, emailEmpr, idBadge);
            return ResponseEntity.ok("Email de confirmação enviado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao enviar email de confirmação: " + e.getMessage());
        }
    }

    @GetMapping("/confirmar-recebimento")
    public ResponseEntity<String> confirmarRecebimento(@RequestParam String token) {
        try {
            badgeService.confirmarRecebimento(token);
            return ResponseEntity.ok("Recebimento do badge confirmado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao confirmar recebimento do badge: " + e.getMessage());
        }
    }

    @GetMapping("/teste-modelo-badge")
    public ResponseEntity<?> testeModeloBadge(@RequestParam Integer idBadge) {
        String modeloBadgeUrl = "http://localhost:3001/consultar?id_badge=" + idBadge;
        try {
            Map<String, Object> response = restTemplate.getForObject(modeloBadgeUrl, Map.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao conectar com o serviço modelo_badge: " + e.getMessage());
        }
    }

    @PostMapping("/teste-email")
    public ResponseEntity<String> testeEmail(@RequestBody Map<String, Object> payload) {
        String to = (String) payload.get("to");
        String subject = (String) payload.get("subject");
        String text = (String) payload.get("text");

        try {
            emailService.sendSimpleMessage(to, subject, text);
            return ResponseEntity.ok("Email enviado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao enviar email: " + e.getMessage());
        }
    }

    @PostMapping("/resgatar")
    public ResponseEntity<String> resgatarBadge(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Integer idBadge) {

        String emailCom = userDetails.getUsername();
        try {
            // Lógica para resgatar o badge, se necessário
            return ResponseEntity.ok("Badge resgatado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao resgatar o badge: " + e.getMessage());
        }
    }
}
