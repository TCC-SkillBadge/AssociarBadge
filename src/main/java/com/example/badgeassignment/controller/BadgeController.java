package com.example.badgeassignment.controller;

import com.example.badgeassignment.model.Badge;
import com.example.badgeassignment.service.BadgeService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/badge")
public class BadgeController {
    @Autowired
    private BadgeService badgeService;

    @PostMapping("/atribuir")
    public ResponseEntity<?> atribuirBadge(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, Object> payload) {

        String emailEmpr = userDetails.getUsername();
        String emailCom = (String) payload.get("emailCom");
        Integer idBadge = (Integer) payload.get("idBadge");
        String imagemB = (String) payload.get("imagemB");
        Date dtVencimento = (Date) payload.get("dtVencimento");

        try {
            Badge badge = badgeService.atribuirBadge(emailCom, emailEmpr, idBadge, imagemB, dtVencimento);
            return ResponseEntity.ok(badge);
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Erro ao enviar email de confirmação");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/confirmar")
    public ResponseEntity<?> confirmarRecebimento(@RequestParam String email) {
        // Lógica para confirmar o recebimento do badge
        return ResponseEntity.ok("Recebimento confirmado para o email: " + email);
    }
}