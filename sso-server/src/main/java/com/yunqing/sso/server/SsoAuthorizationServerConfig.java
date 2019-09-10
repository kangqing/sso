package com.yunqing.sso.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 单点登录认证服务器配置
 * @EnableAuthorizationServer 开启标准的OAuth2认证服务器
 */
@Configuration
@EnableAuthorizationServer
public class SsoAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    /**
     * springboot2.0以上注入AuthenticationManager失败这样解决
     */
    private final AuthenticationManager authenticationManager;

    public SsoAuthorizationServerConfig(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }



    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory()
                .withClient("client1")
                .secret("client1secret")
                .redirectUris("http://127.0.0.1:8080/client1/login")
                .authorizedGrantTypes("authorization_code","refresh_token")
                .scopes("all")
                .and()
                .withClient("client2")
                .secret("client2secret")
                .redirectUris("http://127.0.0.1:8060/client2/login")
                .authorizedGrantTypes("authorization_code","refresh_token")
                .scopes("all");
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.tokenStore(jwtTokenStore())//accessTokenConverter(jwtAccessTokenConverter());
        .authenticationManager(authenticationManager);

        if(jwtAccessTokenConverter() != null) {
            endpoints.accessTokenConverter(jwtAccessTokenConverter());
        }

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        /**
         * isAuthenticated() 授权表达式
         */
        security.tokenKeyAccess("isAuthenticated()").passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    /**
     * 只管token存取，不管生成
     * @return
     */
    @Bean
    public TokenStore jwtTokenStore(){

        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * token生成的一些处理
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        /**
         * 实现密签，签名的功能
         */
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        /**
         * 签名的秘钥，每个应用不同，所以放到配置中
         */
        accessTokenConverter.setSigningKey("yunqing");
        return accessTokenConverter;
    }


}
