package com.yyds.recipe.service.impl;

import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import com.yyds.recipe.utils.BcryptPasswordUtil;
import com.yyds.recipe.utils.UUIDGenerator;
import com.yyds.recipe.vo.ErrorCode;
import com.yyds.recipe.vo.ServiceVO;
import com.yyds.recipe.vo.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

import com.yyds.recipe.utils.UserSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private static final String PASSWORD_REGEX_PATTERN = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,30}$";
    private static final int PASSWORD_LENGTH = 6;
    private static final String EMAIL_REGEX_PATTEN = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    private static final String NAME_REGEX_PATTEN = "^[A-Za-z]+$";
    private static final int NAME_LENGTH = 15;
    private static final String BIRTHDAY_REGEX_PATTEN = "^\\d{4}-\\d{1,2}-\\d{1,2}";

    @Transactional
    @Override
    public ServiceVO<?> register(User user, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) {
        // TODO: should check why annotation does not work
        if (user.getFirstName() == null || user.getLastName() == null || user.getGender() == null
                || user.getEmail() == null || user.getPassword() == null || user.getBirthdate() == null) {
            return new ServiceVO<>(ErrorCode.BUSINESS_PARAMETER_ERROR, ErrorCode.BUSINESS_PARAMETER_ERROR_MESSAGE);
        }

        if (user.getNickName() == null) {
            user.setNickName(user.getFirstName() + " " + user.getLastName());
        }

        String userId = UUIDGenerator.createUserId();
        user.setUserId(userId);
        user.setCreateTime(String.valueOf(System.currentTimeMillis()));

        // encode password
        // user.setPassword(BcryptPasswordUtil.encodePassword(user.getPassword()));

        try {
            userMapper.saveUser(user);
        } catch (Exception e) {
            return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
        }

        try {
            userMapper.saveUserAccount(user);
        } catch (Exception e) {
            return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
        }

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE, userId);

    }

    //
    // @SneakyThrows
    // @Transactional(rollbackFor = {RuntimeException.class, Error.class, SQLException.class})
    // @Override
    // public String saveUser(User user) {
    //
    //     // TODO: should check why annotation does not work
    //     if (user.getFirstName() == null || user.getLastName() == null || user.getGender() == null
    //             || user.getEmail() == null || user.getPassword() == null || user.getBirthdate() == null) {
    //         throw new Exception("parameter is wrong");
    //     }
    //
    //     // if user not set nick name, let nickname = firstName + " " + lastName
    //     if (user.getNickName() == null) {
    //         user.setNickName(user.getFirstName() + " " + user.getLastName());
    //     }
    //     // set userId
    //     user.setUserId(UUIDGenerator.createUserId());
    //
    //     user.setCreateTime(String.valueOf(System.currentTimeMillis()));
    //
    //     // encode password
    //     user.setPassword(BcryptPasswordUtil.encodePassword(user.getPassword()));
    //
    //     try {
    //         userMapper.saveUser(user);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         throw e;
    //     }
    //
    //     try {
    //         userMapper.saveUserAccount(user);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         throw e;
    //     }
    //
    //     return user.getUserId();
    //
    // }

    @SneakyThrows
    @Override
    public ServiceVO<?> loginUser(LoginUser loginUser, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) {

//        // match password
//        LoginUser loginUserInfo = userMapper.getLoginUserInfo(loginUser.getEmail());
//        // password
//        // if (!BcryptPasswordUtil.passwordMatch(loginUser.getPassword(), loginUserInfo.getPassword())) {
//        //     throw new Exception();
//        // }
//        return loginUserInfo;

        // see if the email entered exists
        try {
            userMapper.getLoginUserInfo(loginUser.getEmail());
        } catch (Exception e) {
            return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
        }

        LoginUser loginUserInfo = userMapper.getLoginUserInfo(loginUser.getEmail());

        if (!loginUser.getPassword().equals(loginUserInfo.getPassword())) {
            return new ServiceVO<>(ErrorCode.BUSINESS_PARAMETER_ERROR, ErrorCode.BUSINESS_PARAMETER_ERROR_MESSAGE);
        }

        UserSession userSession = new UserSession(loginUser.getUserId());
        httpSession.setAttribute(UserSession.ATTRIBUTE_ID, userSession);
        Cookie userCookie = new Cookie("user-login-cookie", loginUser.getUserId());
        userCookie.setMaxAge(2 * 60 * 60);
        userCookie.setPath(request.getContextPath());
        response.addCookie(userCookie);

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE, loginUserInfo.getUserId());

    }

    @SneakyThrows
    @Override
    public ServiceVO<?> logoutUser(String userId, HttpSession httpSession, HttpServletResponse response) {
        httpSession.removeAttribute(UserSession.ATTRIBUTE_ID);
        Cookie cookie = new Cookie("user-login-cookie", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);

    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, SQLException.class})
    public void editUser(User user) {

        if (user.getUserId() == null) {
            throw new Exception("less userId");
        }

        EditUserException eue = new EditUserException();
        if (!eue.isUserValid(user)) {
            throw new Exception(eue.getExceptionMsg());
        }

        try {
            userMapper.editUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class EditUserException {
        private String exceptionMsg;

        public boolean isUserValid(User user) {
            if (!isNameValid(user.getFirstName())
                    || !isNameValid(user.getLastName())
                    || user.getGender() < 0 || user.getGender() > 2
                    || !user.getEmail().matches(EMAIL_REGEX_PATTEN)
                    || !isNameValid(user.getNickName())
                    || !user.getPassword().matches(PASSWORD_REGEX_PATTERN)
                    || !user.getBirthdate().matches(BIRTHDAY_REGEX_PATTEN))
                return false;
            return true;
        }

        private boolean isNameValid(String name) {
            return name.matches(NAME_REGEX_PATTEN) && name.length() > NAME_LENGTH;
        }
    }

    @SneakyThrows
    @Override
    public void editPassword(String newPassword, String userId) {

        if (newPassword.length() < PASSWORD_LENGTH || !newPassword.matches(PASSWORD_REGEX_PATTERN)) {
            throw new Exception("password's length is less than 6 or password must have digit and word");
        }

        String oldPassword = userMapper.getPasswordByUserid(userId);

        if (oldPassword.equals(newPassword)) {
            throw new Exception("the password should not be same as before");
        }

        userMapper.changePassword(userId, newPassword);

        // if (!BcryptPasswordUtil.passwordMatch(oldPassword, userPassword)) {
        //     throw new Exception("old password does not match");
        // }
        //
        // String encodeNewPassword = BcryptPasswordUtil.encodePassword(newPassword);
        //
        // try {
        //     userMapper.changePassword(userId, encodeNewPassword);
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     throw e;
        // }
    }

}
