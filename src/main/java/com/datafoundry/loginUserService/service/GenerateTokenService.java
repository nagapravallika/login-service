package com.datafoundry.loginUserService.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datafoundry.loginUserService.model.GenerateToken;
import com.datafoundry.loginUserService.repository.GenerateTokenRepository;

@Service
public class GenerateTokenService {
    
    @Autowired
    private GenerateTokenRepository generateTokenRepository;

    public void save(GenerateToken generateToken) {
        generateTokenRepository.save(generateToken);
    }

    public void deleteGenerateToken(String tokenID, String jwt) {
        generateTokenRepository.deleteById(jwt);    
    }
    
    public void deleteGenerateToken(String jwt) {
        generateTokenRepository.deleteById(jwt);        
    }

    public boolean isExistsByToken(String jwt) {
        return generateTokenRepository.existsById(jwt);
    }

    public Optional<GenerateToken> findById(String jwt) {
        return generateTokenRepository.findById(jwt);
    }

}