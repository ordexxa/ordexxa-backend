package co.edu.uco.ordexxa.infrastructure.config;

import co.edu.uco.ordexxa.features.ops.AzureKeyVaultSecretReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Bean
    @Primary
    public JavaMailSender javaMailSender(
            final AzureKeyVaultSecretReader secretReader,
            @Value("${spring.mail.host:localhost}") String host,
            @Value("${spring.mail.port:1025}") int port,
            @Value("${spring.mail.username:}") String username,
            @Value("${spring.mail.password:}") String mailPassword,
            @Value("${ordexxa.vault.mail-password-secret-name:}") String mailPasswordSecretName,
            @Value("${spring.mail.properties.mail.smtp.auth:false}") boolean smtpAuth,
            @Value("${spring.mail.properties.mail.smtp.starttls.enable:false}") boolean startTls,
            @Value("${spring.mail.properties.mail.smtp.starttls.required:false}") boolean startTlsRequired,
            @Value("${spring.mail.properties.mail.smtp.ssl.trust:*}") String sslTrust
    ) {
        final JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(resolveMailPassword(secretReader, mailPasswordSecretName, mailPassword));

        final Properties properties = sender.getJavaMailProperties();
        properties.put("mail.smtp.auth", String.valueOf(smtpAuth));
        properties.put("mail.smtp.starttls.enable", String.valueOf(startTls));
        properties.put("mail.smtp.starttls.required", String.valueOf(startTlsRequired));

        if (sslTrust != null && !sslTrust.isBlank()) {
            properties.put("mail.smtp.ssl.trust", sslTrust);
        }

        return sender;
    }

    private String resolveMailPassword(
            final AzureKeyVaultSecretReader secretReader,
            final String mailPasswordSecretName,
            final String fallbackMailPassword
    ) {
        return secretReader.readSecret(mailPasswordSecretName)
                .orElse(fallbackMailPassword == null ? "" : fallbackMailPassword);
    }
}
