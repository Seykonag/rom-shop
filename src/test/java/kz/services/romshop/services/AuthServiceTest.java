package kz.services.romshop.services;

import kz.services.romshop.dto.JwtAuthDTO;
import kz.services.romshop.dto.LoginDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserService userService;

    @MockBean
    AuthenticationManager authenticationManager;


    @Test
    public void signIn() {
        LoginDTO login = new LoginDTO("admin@gmail.com", "admin");
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        Mockito.when(userDetailsService.loadUserByUsername(login.getEmail())).thenReturn(userDetails);
        Mockito.when(jwtService.generateToken(userDetails)).thenReturn("test-jwt-token");

        Mockito.when(userService.userDetailsService()).thenReturn(userDetailsService);

        JwtAuthDTO jwt = authService.signIn(login);

        Assertions.assertEquals("test-jwt-token", jwt.getToken());
        Assertions.assertEquals("admin@gmail.com", jwt.getEmail());

        Mockito.verify(authenticationManager).authenticate(Mockito.any());
        Mockito.verify(userDetailsService).loadUserByUsername(jwt.getEmail());
        Mockito.verify(jwtService).generateToken(userDetails);
    }
}
