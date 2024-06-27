package com.example.badgeassignment.service;

// BadgeService.java

import com.example.badgeassignment.model.Badge;
import com.example.badgeassignment.model.UserCommon;
import com.example.badgeassignment.model.UserEnterprise;
import com.example.badgeassignment.repository.BadgeRepository;
import com.example.badgeassignment.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.util.Date;

@Service
public class BadgeService {
    @Autowired
    private BadgeRepository badgeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    public Badge atribuirBadge(String emailCom, String emailEmpr, Integer idBadge, String imagemB, Date dtVencimento) throws Exception {
        // Consultar serviços de usuário comum e empresarial
        UserCommon usuarioComum = userService.getUserCommon(emailCom);
        UserEnterprise usuarioEmpresarial = userService.getUserEnterprise(emailEmpr);

        if (usuarioComum == null || usuarioEmpresarial == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        Badge badge = new Badge();
        badge.setEmailCom(emailCom);
        badge.setEmailEmpr(emailEmpr);
        badge.setIdBadge(idBadge);
        badge.setDtEmissao(new Date());
        badge.setDtVencimento(dtVencimento);
        badge.setImagemB(imagemB);

        badge = badgeRepository.save(badge);

        enviarEmailConfirmacao(usuarioComum.getEmail());

        return badge;
    }

    private void enviarEmailConfirmacao(String emailCom) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String linkConfirmacao = "http://localhost:8080/api/badge/confirmar?email=" + emailCom;

            helper.setTo(emailCom);
            helper.setSubject("Confirmação de Recebimento de Badge");
            helper.setText("<p>Por favor, confirme o recebimento do seu badge clicando no link abaixo:</p>"
                    + "<a href=\"" + linkConfirmacao + "\">Confirmar Recebimento</a>", true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}