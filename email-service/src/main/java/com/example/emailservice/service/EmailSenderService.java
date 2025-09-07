package com.example.emailservice.service;

import com.example.emailservice.model.EmailLog;
import com.example.emailservice.model.EmailTemplate;
import com.example.emailservice.model.EmailStatus;
import com.example.emailservice.repository.EmailLogRepository;
import com.example.emailservice.repository.EmailTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.Map;

@Service
public class EmailSenderService {

    private static final Logger log = LoggerFactory.getLogger(EmailSenderService.class);

    private final JavaMailSender mailSender;
    private final EmailTemplateRepository templateRepository;
    private final EmailLogRepository logRepository;
    private final TemplateRenderer renderer;

    @Value("${app.email.default-template:user_welcome}")
    private String defaultTemplateCode;

    public EmailSenderService(JavaMailSender mailSender,
                              EmailTemplateRepository templateRepository,
                              EmailLogRepository logRepository,
                              TemplateRenderer renderer) {
        this.mailSender = mailSender;
        this.templateRepository = templateRepository;
        this.logRepository = logRepository;
        this.renderer = renderer;
    }

    @Transactional
    public void sendTemplatedEmail(String recipient, String templateCode, Map<String, String> variables) {
        String code = (templateCode == null || templateCode.isBlank()) ? defaultTemplateCode : templateCode;
        EmailTemplate template = templateRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException("Email template not found: " + code));

        String subject = renderer.render(template.getSubject(), variables);
        String body = renderer.render(template.getBody(), variables);

        EmailLog emailLog = new EmailLog();
        emailLog.setRecipient(recipient);
        emailLog.setSubject(subject);
        emailLog.setBody(body);
        emailLog.setStatus(EmailStatus.PENDING);
        emailLog.setTemplateCode(code);
        logRepository.save(emailLog);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(body, true); 
            mailSender.send(message);

            emailLog.setStatus(EmailStatus.SENT);
            logRepository.save(emailLog);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", recipient, e.getMessage(), e);
            emailLog.setStatus(EmailStatus.FAILED);
            emailLog.setErrorMessage(e.getMessage());
            logRepository.save(emailLog);
        }
    }
}
