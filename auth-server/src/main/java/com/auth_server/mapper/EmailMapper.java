package com.auth_server.mapper;

import static com.auth_server.controller.AuthController.RESET_PASSWORD_URI;
import static com.auth_server.controller.AuthController.VERIFY_URI;
import static com.auth_server.service.AuthTokenService.RESET_PASSWORD_EMAIL_EXPIRATE;
import static com.auth_server.service.AuthTokenService.VERIFY_EMAIL_EXPIRATE;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;

import com.auth_server.dto.res.ResetPasswordEmailContext;
import com.auth_server.dto.res.VerifyEmailContext;
import com.auth_server.entity.AuthToken;
import com.auth_server.entity.SystemUser;
import com.auth_server.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class EmailMapper {

    @Value("${custom.mail.app.domain}")
    private String APP_DOMAIN;

    private static final String VERIFY_EMAIL_SUBJECT = "Lắc đầu shop - Chào mừng bạn đến với chúng tôi";
    private static final String RESET_PASSWORD_EMAIL_SUBJECT = "Lắc đầu shop - Reset mật khẩu";
    private static final String WELCOME_TEMPLATE = "email/welcome";
    private static final String RESET_PASSWORD_TEMPLATE = "email/reset-password";

    private static final String FROM = "buiducduong";

    @Mapping(target = "to", source = "user.email")
    @Mapping(target = "from", constant = FROM)
    @Mapping(target = "subject", constant = VERIFY_EMAIL_SUBJECT)
    @Mapping(target = "tokenUrl", source = "authToken", qualifiedByName = "VerificationEmailTokenToUrl")
    @Mapping(target = "template", constant = WELCOME_TEMPLATE)
    @Mapping(target = "expirateMin", constant = VERIFY_EMAIL_EXPIRATE)
    public abstract VerifyEmailContext toVerificationEmail(User user, AuthToken authToken);

    @Mapping(target = "tokenUrl", source = "authToken", qualifiedByName = "ResetPasswordTokenToUrl")
    @Mapping(target = "to", source = "user.email")
    @Mapping(target = "from", constant = FROM)
    @Mapping(target = "subject", constant = RESET_PASSWORD_EMAIL_SUBJECT)
    @Mapping(target = "template", constant = RESET_PASSWORD_TEMPLATE)
    @Mapping(target = "expirateMin", constant = RESET_PASSWORD_EMAIL_EXPIRATE)
    public abstract ResetPasswordEmailContext toResetPassword(SystemUser user, AuthToken authToken);

    @Named("VerificationEmailTokenToUrl")
    protected String toVerficationTokenUrl(AuthToken authToken) {
        return APP_DOMAIN + VERIFY_URI + "?token=" + authToken.getTokenValue();
    }

    @Named("ResetPasswordTokenToUrl")
    protected String toResetPasswordTokenUrl(AuthToken authToken) {
        return APP_DOMAIN + RESET_PASSWORD_URI + "?token=" + authToken.getTokenValue();
    }
}
