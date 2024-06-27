package com.example.badgeassignment.service;

import com.example.badgeassignment.model.Badge;
import com.example.badgeassignment.repository.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class BadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Badge atribuirBadge(String emailCom, String emailEmpr, Integer idBadge, Date dtVencimento) throws Exception {
        String modeloBadgeUrl = "http://localhost:PORTA_MODELO_BADGE/consultar?id_badge=" + idBadge;
        String usuarioComumUrl = "http://localhost:PORTA_USUARIO_COMUM/acessa-info?email=" + emailCom;

        // Obtenha dados do ModeloBadge
        Map<String, Object> modeloBadgeResponse = restTemplate.getForObject(modeloBadgeUrl, Map.class);
        if (modeloBadgeResponse == null || !modeloBadgeResponse.containsKey("imagem_mb")) {
            throw new Exception("Modelo de Badge não encontrado");
        }
        String imagemMb = (String) modeloBadgeResponse.get("imagem_mb");

        // Verifique se o usuário comum existe
        Map<String, Object> usuarioComumResponse = restTemplate.getForObject(usuarioComumUrl, Map.class);
        if (usuarioComumResponse == null) {
            throw new Exception("Usuário Comum não encontrado");
        }

        Badge badge = new Badge();
        badge.setEmailCom(emailCom);
        badge.setEmailEmpr(emailEmpr);
        badge.setIdBadge(idBadge);
        badge.setDtEmissao(new Date());
        badge.setDtVencimento(dtVencimento);
        badge.setImagemB(imagemMb);

        return badgeRepository.save(badge);
    }
}
