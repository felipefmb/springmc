package cursomc.com.felipebatista.cursomc.tenant;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

@Component
public class AuthenticationHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHolder.class);

    private ThreadLocal<AccessToken> accessTokenThreadLocal = new NamedThreadLocal<>("Access Token");
    private ThreadLocal<UserAccess> userAccessThreadLocal = new NamedThreadLocal<>("User Access");

    private final AuthenticationRequestHolder requestHolder;

    @Autowired
    public AuthenticationHolder(AuthenticationRequestHolder requestHolder) {
        this.requestHolder = requestHolder;
    }


    public AccessToken getAccessToken() {
        if (Objects.nonNull(RequestContextHolder.getRequestAttributes())) {
            return requestHolder.getAccessToken();
        }

        return accessTokenThreadLocal.get();
    }

    public void setAccessToken(AccessToken accessToken) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Setting access token: " + accessToken);
        }

        if (Objects.nonNull(RequestContextHolder.getRequestAttributes())) {
            requestHolder.setAccessToken(accessToken);
        }

        accessTokenThreadLocal.set(accessToken);
    }

    public UserAccess getUserAccess() {
        if (Objects.nonNull(RequestContextHolder.getRequestAttributes())) {
            return requestHolder.getUserAccess();
        }

        return userAccessThreadLocal.get();
    }

    public void setUserAccess(UserAccess userAccess) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Setting user access: " + userAccess);
        }

        if (Objects.nonNull(RequestContextHolder.getRequestAttributes())) {
            requestHolder.setUserAccess(userAccess);
        }

        userAccessThreadLocal.set(userAccess);
    }
}
