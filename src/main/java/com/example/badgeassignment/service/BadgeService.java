package com.example.badgeassignment.service;

import com.example.badgeassignment.model.Badge;
import com.example.badgeassignment.repository.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class BadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmailService emailService;

    public void enviarEmailConfirmacao(String emailCom, String emailEmpr, Integer idBadge) throws Exception {
        String confirmationToken = UUID.randomUUID().toString();
        String confirmationUrl = "http://localhost:8081/api/badges/confirmar-recebimento?token=" + confirmationToken;

        // Obtenha dados do ModeloBadge
        String modeloBadgeUrl = "http://localhost:7001/consultar?id_badge=" + idBadge;
        Map<String, Object> modeloBadgeResponse = restTemplate.getForObject(modeloBadgeUrl, Map.class);
        if (modeloBadgeResponse == null || !modeloBadgeResponse.containsKey("imagem_mb")) {
            throw new Exception("Modelo de Badge não encontrado");
        }
        String imagemMb = (String) modeloBadgeResponse.get("imagem_mb");

        // Crie um registro parcial de badge com o token de confirmação
        Badge badge = new Badge();
        badge.setEmailCom(emailCom);
        badge.setEmailEmpr(emailEmpr);
        badge.setIdBadge(idBadge);
        badge.setDtEmissao(new Date());
        badge.setImagemB(imagemMb);
        badge.setConfirmationToken(confirmationToken);
        badgeRepository.save(badge);

        // Enviar email de confirmação
        String subject = "Confirme o recebimento do seu Badge";
        String text = "Por favor, confirme o recebimento do seu badge clicando no link abaixo:\n" + confirmationUrl;
        emailService.sendSimpleMessage(emailCom, subject, text);
    }

    public Badge confirmarRecebimento(String confirmationToken) throws Exception {
        Optional<Badge> optionalBadge = badgeRepository.findByConfirmationToken(confirmationToken);
        if (!optionalBadge.isPresent()) {
            throw new Exception("Token de confirmação inválido");
        }

        Badge badge = optionalBadge.get();
        badge.setConfirmationToken(null);  // Token usado e removido
        badge.setDtVencimento(new Date()); // Adicione a lógica de data de vencimento se necessário

        return badgeRepository.save(badge);
    }
}
