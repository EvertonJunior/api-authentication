package com.ej.authentication.service;

import com.ej.authentication.entities.EmailEvent;
import com.ej.authentication.entities.ResetPasswordToken;
import com.ej.authentication.entities.Usuario;
import com.ej.authentication.exceptions.NewPasswordDivergentException;
import com.ej.authentication.exceptions.ResourceNotFoundException;
import com.ej.authentication.exceptions.TokenValidUniqueViolationException;
import com.ej.authentication.exceptions.UsernameUniqueViolationException;
import com.ej.authentication.repository.ResetPasswordTokenRepository;
import com.ej.authentication.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final ResetPasswordTokenRepository tokenRepository;
    private final NotificacaoEmailService notificacaoEmailService;

    @Transactional
    public Usuario save(Usuario usuario){
        try{
            usuario.setPassword(encoder.encode(usuario.getPassword()));
            Usuario usuario1 = repository.save(usuario);
            EmailEvent emailEvent = new EmailEvent(usuario.getUsername(), "Bem vindo", "Seja bem vindo ao nosso sistema");
            notificacaoEmailService.notificar(emailEvent);
            return usuario1;
        } catch (DataIntegrityViolationException e){
            throw new UsernameUniqueViolationException("Usuario ja existe no sistema");
        }
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario findById(long id){
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id nao encontrado"));
    }

    @Transactional(readOnly = true)
    public Usuario findByUsername(String username){
        return repository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));
    }

    @Transactional
    public void deleteById(long id){
        findById(id);
        repository.deleteById(id);
    }

    @Transactional
    public void forgotPassword(String email) {
        findByUsername(email);
        var token = createToken(email);
        String text = "Link para recuperacao de senha:\n http://localhost:8080/api/v1/usuarios/reset-password?token=" + token.getToken();
        EmailEvent emailEvent = new EmailEvent(email, "Recuperacao de senha", text);
        notificacaoEmailService.notificar(emailEvent);
    }

    @Transactional
    public ResetPasswordToken createToken(String email){
        List<ResetPasswordToken> tokens = tokenRepository.findByUsername(email);
        for (ResetPasswordToken token : tokens){
            if (token.getStatus().equals(ResetPasswordToken.Status.VALIDO)){
                throw new TokenValidUniqueViolationException("Ja existe um token valido para esse usuario");
            }
        }
        String token = UUID.randomUUID().toString();
        Usuario usuario = findByUsername(email);
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(token, usuario, usuario.getUsername());
        return tokenRepository.save(resetPasswordToken);
    }

    @Transactional
    public void resetPassword(String token, String newPassword, String confirmNewPassword){
        ResetPasswordToken resetPasswordToken = tokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Token nao e valido ou nao foi encontrado"));
        Usuario usuario = resetPasswordToken.getUsuario();
        if(!newPassword.equals(confirmNewPassword)){
            throw new NewPasswordDivergentException("Senhas digitadas estao divergentes");
        }
        usuario.setPassword(encoder.encode(newPassword));
        resetPasswordToken.setStatus(ResetPasswordToken.Status.EXPIRADO);
        tokenRepository.save(resetPasswordToken);
        EmailEvent emailEvent = new EmailEvent(usuario.getUsername(), "alteração de senha", "Senha alterada com sucesso");
        notificacaoEmailService.notificar(emailEvent);
    }

    @Transactional
    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void deleteTokenInvalid(){
        List<ResetPasswordToken> tokens = tokenRepository.findAll();
        for(ResetPasswordToken token : tokens){
            if(token.getStatus().equals(ResetPasswordToken.Status.EXPIRADO) || token.getExpireDate().isBefore(LocalDateTime.now())){
                tokenRepository.deleteById(token.getId());
            }
        }
    }


}
