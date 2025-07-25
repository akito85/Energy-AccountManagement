/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.common.library.services;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.library.security.CryptoSecurity;
import com.dbs.common.library.utils.AesUtil;
import com.dbs.common.library.utils.UrlDecodeEncodeUtil;
import com.dbs.database.crm.entities.usermanagement.LOG_EMAIL;
import com.dbs.database.crm.entities.usermanagement.M_GLOBAL_PROPERTIES;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_PROPERTIES_DTL;
import com.dbs.database.crm.repositories.usermanagement.LogEmailRepo;
import com.dbs.database.crm.repositories.usermanagement.MGlobalPropertiesRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author RachmatY
 */
@Service
@SuppressWarnings("java:S6813")
public class EmailServices {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailServices.class);

    @Autowired
    private MGlobalPropertiesRepo mGlobalPropertiesRepo;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Autowired
    private LogEmailRepo logEmailRepo;

    public boolean email(M_USER dataUser, String subject, String ctxVar, 
            String template, String link, String encryptData, String type) {
        M_GLOBAL_PROPERTIES mGlobalProperties = mGlobalPropertiesRepo.findAllByName("EMAIL_CONFIG");
        String key = Constant.KEY;
        String initVector = Constant.INIT_VECTOR;
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Optional<R_GLOBAL_PROPERTIES_DTL> rgpHostOptional = mGlobalProperties.getRGlobalPropertiesDtls().stream()
                .filter(e -> e.getGpdKey().equalsIgnoreCase("E_HOST"))
                .findFirst();
        mailSender.setHost(rgpHostOptional.isPresent() ? rgpHostOptional.get().getGpdVal() : "");
        
        Optional<R_GLOBAL_PROPERTIES_DTL> rgpPortOptional = mGlobalProperties.getRGlobalPropertiesDtls().stream()
                .filter(e -> e.getGpdKey().equalsIgnoreCase("E_PORT"))
                .findFirst();
        mailSender.setPort(rgpPortOptional.map(rGlobalPropertiesDtl -> Integer.parseInt(rGlobalPropertiesDtl.getGpdVal())).orElse(0));

        Optional<R_GLOBAL_PROPERTIES_DTL> rgpUsernameOptional = mGlobalProperties.getRGlobalPropertiesDtls().stream()
                .filter(e -> e.getGpdKey().equalsIgnoreCase("E_USERNAME"))
                .findFirst();
        mailSender.setUsername(rgpUsernameOptional.isPresent() ? rgpUsernameOptional.get().getGpdVal() : "");

        Optional<R_GLOBAL_PROPERTIES_DTL> rgpPassOptional = mGlobalProperties.getRGlobalPropertiesDtls().stream()
                .filter(e -> e.getGpdKey().equalsIgnoreCase("E_PASS"))
                .findFirst();
        mailSender.setPassword(rgpPassOptional.isPresent() ? AesUtil.decrypt(rgpPassOptional.get().getGpdVal(), key, initVector) : "");

        Properties props = mailSender.getJavaMailProperties();

//        Optional<R_GLOBAL_PROPERTIES_DTL> rgpProtocolOptional = mGlobalProperties.getRGlobalPropertiesDtls().stream()
//                .filter(e -> e.getGpdKey().equalsIgnoreCase("E_PROTOCOL"))
//                .findFirst();
//        props.put("mail.transport.protocol", rgpProtocolOptional.isPresent() ? rgpProtocolOptional.get().getGpdVal() : "");

        Optional<R_GLOBAL_PROPERTIES_DTL> rgpAuthOptional = mGlobalProperties.getRGlobalPropertiesDtls().stream()
                .filter(e -> e.getGpdKey().equalsIgnoreCase("E_AUTH"))
                .findFirst();
        String auth = rgpAuthOptional.isPresent() ? convertValBoolean(rgpAuthOptional.get().getGpdVal()) : "";
        props.put("mail.smtp.auth", auth);

        Optional<R_GLOBAL_PROPERTIES_DTL> rgpTlsOptional = mGlobalProperties.getRGlobalPropertiesDtls().stream()
                .filter(e -> e.getGpdKey().equalsIgnoreCase("E_STARTTLS_ENABLE"))
                .findFirst();
        String tls = rgpTlsOptional.isPresent() ? convertValBoolean(rgpTlsOptional.get().getGpdVal()) : "";
        props.put("mail.smtp.starttls.enable", tls);

        Optional<R_GLOBAL_PROPERTIES_DTL> rgpDebugOptional = mGlobalProperties.getRGlobalPropertiesDtls().stream()
                .filter(e -> e.getGpdKey().equalsIgnoreCase("E_DEBUG"))
                .findFirst();
        String debug = rgpDebugOptional.isPresent() ? convertValBoolean(rgpDebugOptional.get().getGpdVal()) : "";
        props.put("mail.debug", debug);

        props.put("mail.smtp.socketFactory.port", rgpPortOptional.map(rGlobalPropertiesDtl -> Integer.parseInt(rGlobalPropertiesDtl.getGpdVal())).orElse(0));
        Optional<R_GLOBAL_PROPERTIES_DTL> rgpClassOptional = mGlobalProperties.getRGlobalPropertiesDtls().stream()
                .filter(e -> e.getGpdKey().equalsIgnoreCase("E_CLASS"))
                .findFirst();
        props.put("mail.smtp.socketFactory.class", rgpClassOptional.isPresent() ? rgpClassOptional.get().getGpdVal() : "");
//        props.put("mail.smtp.ssl.trust", rgpHostOptional.isPresent() ? rgpHostOptional.get().getGpdVal() : "");
//        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.connectiontimeout", 5000);
        props.put("mail.smtp.timeout", 5000);
        props.put("mail.smtp.writetimeout", 5000);
//        props.put("mail.debug", true);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        try {
            helper.setTo(dataUser.getEmail());
            helper.setFrom(new InternetAddress(rgpUsernameOptional.isPresent() ? rgpUsernameOptional.get().getGpdVal() : ""));
            helper.setSubject(subject);

            final Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("username", dataUser.getUsername());
            if ("EMAIL".equals(type)) {
                ctx.setVariable("email", ctxVar);
            } else if("PHONE".equals(type)){
                ctx.setVariable("phone", ctxVar);
            } else if("EMAIL_PHONE".equals(type)) {
                String[] strCtxs = ctxVar.split("<>");
                ctx.setVariable("email", strCtxs[1]);
                ctx.setVariable("phone", strCtxs[0]);
            } else if("EMAIL_PASWORD".equals(type)) {
                String[] strCtxs = ctxVar.split("<>");
                ctx.setVariable("username", strCtxs[0]);
                ctx.setVariable("password", strCtxs[1]);
            }
            
            ctx.setVariable("id", dataUser.getUserId());
            String encryptUsernameAndEmail = CryptoSecurity.encrypt(encryptData);
            String encrypt = UrlDecodeEncodeUtil.encodePath(encryptUsernameAndEmail);
            M_GLOBAL_PROPERTIES mGlobalPropUrl = mGlobalPropertiesRepo.findAllByName("PATH_URL");
            Optional<R_GLOBAL_PROPERTIES_DTL> rgpUrl = mGlobalPropUrl.getRGlobalPropertiesDtls().stream()
                    .filter(e -> e.getGpdKey().equalsIgnoreCase(link))
                    .findFirst();
            String url = (rgpUrl.isPresent() ? rgpUrl.get().getGpdVal() : "") + encrypt;
            ctx.setVariable("url", url);
            final String htmlContent1 = templateEngine.process(template, ctx);
            helper.setText(htmlContent1, true);

            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return false;
        }
    }
    
    public String convertValBoolean(String val){
        if("Y".equalsIgnoreCase(val)){
            return "true";
        }
        return "false";
    }
    
    public void saveEmailLog(M_USER dataUser, String subject, String ctxVar, 
            String template, String link, String encryptData, String type, String status){
        ObjectMapper mapper = new ObjectMapper();
        String dtUser = null;
        try {
            M_USER usr = new M_USER();
            usr.setUserId(dataUser.getUserId());
            usr.setUsername(dataUser.getUsername());
            usr.setEmail(dataUser.getEmail());
            dtUser = mapper.writeValueAsString(usr);
        } catch (JsonProcessingException e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
        }
        
        LOG_EMAIL logEmail = new LOG_EMAIL();
        logEmail.setDataUser(dtUser);
        logEmail.setSubject(subject);
        logEmail.setCtxVar(ctxVar);
        logEmail.setTemplateEmail(template);
        logEmail.setLink(link);
        logEmail.setEncrypData(encryptData);
        logEmail.setTypeEmail(type);
        logEmail.setCreatedBy(dataUser.getUsername());
        logEmail.setCreatedDate(new Date());
        logEmail.setStatus(status);
        logEmailRepo.saveAndFlush(logEmail);
    }

}
