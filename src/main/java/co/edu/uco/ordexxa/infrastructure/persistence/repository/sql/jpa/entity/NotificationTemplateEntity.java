package co.edu.uco.ordexxa.infrastructure.persistence.repository.sql.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification_templates")
public class NotificationTemplateEntity {

    @Id
    @Column(name = "code", nullable = false, length = 80)
    private String code;

    @Column(name = "channel", nullable = false, length = 40)
    private String channel;

    @Column(name = "subject", nullable = false, length = 160)
    private String subject;

    @Column(name = "body", nullable = false, length = 1000)
    private String body;

    @Column(name = "active", nullable = false)
    private Boolean active;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(final String channel) {
        this.channel = channel;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }
}
