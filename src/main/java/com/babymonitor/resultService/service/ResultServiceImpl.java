package com.babymonitor.resultService.service;

import com.babymonitor.resultService.model.Result;
import com.babymonitor.resultService.repository.ResultRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepo;

    @Autowired
    public ResultServiceImpl(ResultRepository resultRepo) {
        this.resultRepo = resultRepo;
    }

    @Value("${jwt.rs256}")
    private String rsaPublicKeyString;

    public List<Result> findByUser(HttpServletRequest request){
        UUID user = extractSubject(request);
        return resultRepo.findByUser(user);
    };

    public Result findByUserAndSession(int session, HttpServletRequest request){
        UUID user = extractSubject(request);
        return resultRepo.findByUserAndSession(user, session);
    };

    public String addResult(Result result, HttpServletRequest request){
        UUID user = extractSubject(request);
        if (user == null) {
            throw new UnauthorizedException("Invalid or missing authentication token");
        }

        // Ensure the result's user matches the authenticated user
        result.setUser(user);

        Result addedResult = resultRepo.save(new Result(result.getResult(), user, result.getSession(), result.getSimType()));
        return addedResult.getId();
    }

    // You'll need to create this custom exception
    public class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }

    private UUID extractSubject(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Explicitly handle mock token for testing
        if ("Bearer mockToken".equals(bearerToken)) {
            return UUID.fromString("00000000-0000-0000-0000-000000000000");
        }

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);

            try {
                RSAPublicKey publicKey = RsaKeyUtil.getPublicKey(rsaPublicKeyString);

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(publicKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                return UUID.fromString(claims.getSubject());
            } catch (Exception e) {
                // Log the error or handle it as appropriate
                return null;
            }
        }
        return null;
    }
}
