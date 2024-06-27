package com.example.badgeassignment.controller;

import com.example.badgeassignment.model.Badge;
import com.example.badgeassignment.service.BadgeService;
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
    private RestTemplate restTemplate;

    @PostMapping("/atribuir")
    public ResponseEntity<Badge> atribuirBadge(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, Object> payload) {

        String emailCom = (String) payload.get("emailCom");
        String emailEmpr = userDetails.getUsername();
        Integer idBadge = (Integer) payload.get("idBadge");
        Date dtVencimento = new Date((Long) payload.get("dtVencimento"));

        try {
            Badge badge = badgeService.atribuirBadge(emailCom, emailEmpr, idBadge, dtVencimento);
            return ResponseEntity.ok(badge);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/teste-modelo-badge")
    public ResponseEntity<?> testeModeloBadge(@RequestParam Integer idBadge) {
        String modeloBadgeUrl = "http://localhost:7001/consultar?id_badge=" + idBadge;
        try {
            Map<String, Object> response = restTemplate.getForObject(modeloBadgeUrl, Map.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao conectar com o serviço modelo_badge: " + e.getMessage());
        }
    }
}
