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

    public String addResult(Result result){
        Result addedResult = resultRepo.save(new Result(result.getResult(), result.getUser(), result.getSession(), result.getSimType()));
        return addedResult.getId();
    };

    private UUID extractSubject(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + bearerToken);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // Remove "Bearer " prefix
            String token = bearerToken.substring(7);

            try {
                // Load the RSA public key
                RSAPublicKey publicKey = RsaKeyUtil.getPublicKey(rsaPublicKeyString);

                // Parse the JWT and extract the claims
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(publicKey)  // Use RSA public key here
                        .build()
                        .parseClaimsJws(token)   // Use the stripped token
                        .getBody();

                // Extract and return the "sub" claim as UUID
                return UUID.fromString(claims.getSubject());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
