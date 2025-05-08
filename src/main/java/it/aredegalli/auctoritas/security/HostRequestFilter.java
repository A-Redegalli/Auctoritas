package it.aredegalli.auctoritas.security;

import it.aredegalli.auctoritas.enums.AuditEventTypeEnum;
import it.aredegalli.auctoritas.service.audit.AuditService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class HostRequestFilter extends OncePerRequestFilter {

    @Value("${security.whitelist.ips}")
    private String whitelistIps;

    private final AuditService auditService;

    private static boolean matches(String remoteAddr, String pattern) {
        if (pattern.contains("*")) {
            String regex = pattern.replace(".", "\\.").replace("*", ".*");
            return Pattern.matches(regex, remoteAddr);
        }
        if (pattern.contains("/")) {
            try {
                return isInSubnet(remoteAddr, pattern);
            } catch (Exception ignored) {
            }
        }
        return remoteAddr.equals(pattern);
    }

    private static boolean isInSubnet(String ip, String cidr) throws UnknownHostException {
        String[] parts = cidr.split("/");
        InetAddress inetAddress = InetAddress.getByName(parts[0]);
        InetAddress remote = InetAddress.getByName(ip);

        int prefix = Integer.parseInt(parts[1]);
        byte[] subnetBytes = inetAddress.getAddress();
        byte[] ipBytes = remote.getAddress();

        int byteCount = prefix / 8;
        int bitRemainder = prefix % 8;

        for (int i = 0; i < byteCount; i++) {
            if (subnetBytes[i] != ipBytes[i]) return false;
        }

        if (bitRemainder > 0) {
            int mask = (-1) << (8 - bitRemainder);
            return (subnetBytes[byteCount] & mask) == (ipBytes[byteCount] & mask);
        }

        return true;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();
        String remoteAddr = request.getRemoteAddr();
        List<String> allowedPatterns = Arrays.asList(whitelistIps.split(","));

        boolean isWhitelisted = allowedPatterns.stream().anyMatch(pattern -> matches(remoteAddr, pattern));

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("method", method);
        metadata.put("ip", remoteAddr);
        metadata.put("path", request.getRequestURI());

        if (!isWhitelisted) {
            log.warn("[SECURITY] Tentativo bloccato: IP={} metodo={}", remoteAddr, method);
            auditService.logEvent(null, AuditEventTypeEnum.API_ACCESS_DENIED, "Auctoritas", "Tentativo non autorizzato da IP: " + remoteAddr, metadata);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso riservato");
            return;
        }

        log.info("[SECURITY] Accesso consentito: IP={} metodo={}", remoteAddr, method);
        auditService.logEvent(null, AuditEventTypeEnum.API_ACCESS_GRANTED, "Auctoritas", "Accesso consentito da IP: " + remoteAddr, metadata);

        filterChain.doFilter(request, response);
    }
}
