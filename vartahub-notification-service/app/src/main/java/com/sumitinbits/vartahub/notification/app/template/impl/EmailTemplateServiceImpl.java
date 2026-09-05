package com.sumitinbits.vartahub.notification.app.template.impl;

import com.sumitinbits.vartahub.notification.app.template.MeetingScheduledDto;
import com.sumitinbits.vartahub.notification.app.template.TemplateService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@Service
public class EmailTemplateServiceImpl implements TemplateService {
    private final Configuration freeMarkerConfig;

    public EmailTemplateServiceImpl() {
        this.freeMarkerConfig = new Configuration(Configuration.VERSION_2_3_34);
        this.freeMarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates/email");
        this.freeMarkerConfig.setDefaultEncoding("UTF-8");
        this.freeMarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        this.freeMarkerConfig.setLogTemplateExceptions(false);
        this.freeMarkerConfig.setWrapUncheckedExceptions(true);
    }

    @Override
    public String getTemplate(Map<String, Object> data) {
        try {
            Template template = freeMarkerConfig.getTemplate("meeting-scheduled.ftl");
            StringWriter writer = new StringWriter();
            template.process(getDto(data), writer);
            return writer.toString();
        } catch (IOException ioException) {
            throw new RuntimeException("Error in compiling email template for meeting-scheduled notification", ioException);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }


    private MeetingScheduledDto getDto(Map<String, Object> data) {
        return new MeetingScheduledDto(
                "topic",
                "scheduledAt",
                List.of(),
                ""
        );
    }
}
