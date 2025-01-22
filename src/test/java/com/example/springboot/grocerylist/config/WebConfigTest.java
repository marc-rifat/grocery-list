package com.example.springboot.grocerylist.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {

    @Mock
    private AuthInterceptor authInterceptor;

    @Mock
    private InterceptorRegistry registry;

    @InjectMocks
    private WebConfig webConfig;

    @Test
    void addInterceptors_ShouldRegisterAuthInterceptor() {
        webConfig.addInterceptors(registry);

        verify(registry).addInterceptor(authInterceptor);
    }
} 