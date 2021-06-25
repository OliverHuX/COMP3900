package com.yyds.recipe.service.impl;

import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import com.yyds.recipe.utils.SaltGenerator;
import com.yyds.recipe.utils.UUIDGenerator;
import com.yyds.recipe.vo.ErrorCode;
import com.yyds.recipe.vo.ServiceVO;
import com.yyds.recipe.vo.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

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

        // check email if exist
        if (userMapper.getUserByEmail(user.getEmail()) != null) {
            return new ServiceVO<>(ErrorCode.EMAIL_ALREADY_EXISTS_ERROR, ErrorCode.EMAIL_ALREADY_EXISTS_ERROR_MESSAGE);
        }

        // TODO: should check why annotation does not work
        if (user.getFirstName() == null || user.getLastName() == null || user.getGender() == null
                || user.getEmail() == null || user.getPassword() == null || user.getBirthdate() == null) {
            return new ServiceVO<>(ErrorCode.BUSINESS_PARAMETER_ERROR, ErrorCode.BUSINESS_PARAMETER_ERROR_MESSAGE);
        }

        // check email
        if (!user.getEmail().matches(EMAIL_REGEX_PATTEN)) {
            return new ServiceVO<>(ErrorCode.EMAIL_REGEX_ERROR, ErrorCode.EMAIL_REGEX_ERROR_MESSAGE);
        }

        // check password
        if (!user.getPassword().matches(PASSWORD_REGEX_PATTERN)) {
            return new ServiceVO<>(ErrorCode.PASSWORD_REGEX_ERROR, ErrorCode.PASSWORD_REGEX_ERROR_MESSAGE);
        }

        if (user.getNickName() == null) {
            user.setNickName(user.getFirstName() + " " + user.getLastName());
        }

        String userId = UUIDGenerator.createUserId();
        user.setUserId(userId);
        user.setCreateTime(String.valueOf(System.currentTimeMillis()));

        // TODO: new feature
        String salt = SaltGenerator.getSalt();
        user.setSalt(salt);
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);
        user.setPassword(md5Hash.toHex());


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

    @SneakyThrows
    @Override
    public ServiceVO<?> loginUser(LoginUser loginUser, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) {

        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(new UsernamePasswordToken(loginUser.getEmail(), loginUser.getPassword()));
        } catch (UnknownAccountException e) {
            System.out.println("email error or not exist");
            return new ServiceVO<>(ErrorCode.EMAIL_NOT_EXISTS_ERROR, ErrorCode.EMAIL_NOT_EXISTS_ERROR_MESSAGE);
        } catch (IncorrectCredentialsException e) {
            System.out.println("password error");
            return new ServiceVO<>(ErrorCode.PASSWORD_INCORRECT_ERROR, ErrorCode.PASSWORD_INCORRECT_ERROR_MESSAGE);
        }

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);

    }

    @Override
    public ServiceVO<?> logoutUser(String userId, HttpSession httpSession, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        // TODO: have not deal session and cookie
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

        // if (!BcryptPasswordUtil.passwordMatch(oldPassword, userPassword)) {
        //     throw new Exception("old password does not match");
        // }
        //
        // String encodeNewPassword = BcryptPasswordUtil.encodePassword(newPassword);
        if (oldPassword.equals(newPassword)) {
            throw new Exception("the password should not be same as before");
        }

        userMapper.changePassword(userId, newPassword);

        try {
            // userMapper.changePassword(userId, encodeNewPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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

    @Override
    public User getUserByEmail() {
        return null;
    }

}
