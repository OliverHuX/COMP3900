package com.yyds.recipe.shiro.realms;

import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (StringUtils.EMPTY_STRING.equals(authenticationToken.getPrincipal())) {
            return null;
        }
        String userEmail = authenticationToken.getPrincipal().toString();
        User user = userMapper.getUserByEmail(userEmail);
        if (user == null) {
            return null;
        } else {
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user.getEmail(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
            return simpleAuthenticationInfo;
        }
    }
}