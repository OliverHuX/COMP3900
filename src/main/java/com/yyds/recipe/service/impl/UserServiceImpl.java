package com.yyds.recipe.service.impl;

import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import com.yyds.recipe.utils.JwtUtil;
import com.yyds.recipe.utils.SaltGenerator;
import com.yyds.recipe.utils.UUIDGenerator;
import com.yyds.recipe.utils.UserSession;
import com.yyds.recipe.vo.ErrorCode;
import com.yyds.recipe.vo.ServiceVO;
import com.yyds.recipe.vo.SuccessCode;
import lombok.SneakyThrows;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import static com.yyds.recipe.vo.ErrorCode.EMAIL_VERIFY_ERROR_MESSAGE;

@Service
@EnableTransactionManagement
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailSenderAddress;

    private static final String PASSWORD_REGEX_PATTERN = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{6,20}$";
    private static final int PASSWORD_LENGTH = 6;
    private static final String EMAIL_REGEX_PATTEN = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    // TODO: transactional does not work
    @Transactional
    @Override
    public ServiceVO<?> register(User user, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) {

        // check email if exist
        if (userMapper.getUserByEmail(user.getEmail()) != null) {
            return new ServiceVO<>(ErrorCode.EMAIL_ALREADY_EXISTS_ERROR, ErrorCode.EMAIL_ALREADY_EXISTS_ERROR_MESSAGE);
        }

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

        String salt = SaltGenerator.getSalt();
        user.setSalt(salt);
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);
        user.setPassword(md5Hash.toHex());

        // JWT
        HashMap<String, String> payload = new HashMap<>();
        payload.put("userId", user.getUserId());
        payload.put("email", user.getEmail());
        String userToken = JwtUtil.createToken(payload);

        // set in redis
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(userToken, user, 30, TimeUnit.MINUTES);

        // send email
        // TODO: test email and smtp
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setSubject("[YYDS] Please Verify Your Email!");
            mimeMessageHelper.setFrom(mailSenderAddress);
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setText("<b>Dear <code>" + user.getNickName() + "</code></b>,<br><p>Welcome to </p><b>YYDS</b>! Please verify" +
                    " your account within <b>30 minutes</b> following this link: localhost:8080/emailVerify/" +
                    userToken + "</code></p>", true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            redisTemplate.delete(userToken);
            return new ServiceVO<>(ErrorCode.MAIL_SERVER_ERROR, ErrorCode.MAIL_SERVER_ERROR_MESSAGE);
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("token", userToken);
        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE, resultMap);

    }

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

        String email = loginUser.getEmail();
        String userId = userMapper.getUserIdByEmail(email);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("userId", userId);

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE, resultMap);

    }

    @Override
    public ServiceVO<?> logoutUser(String userId, HttpSession httpSession, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        // TODO: have not deal session and cookie
        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);
    }

    @Override
    @Transactional
    public ServiceVO<?> editUser(User user) {

        if (userMapper.getUserByUserId(user.getUserId()) == null) {
            return new ServiceVO<>(ErrorCode.USERID_NOT_FOUND_ERROR, ErrorCode.USERID_NOT_FOUND_ERROR_MESSAGE);
        }

        if (user.getUserId() == null || (user.getGender() != null && (user.getGender() > 2 || user.getGender() < 0))) {
            return new ServiceVO<>(ErrorCode.BUSINESS_PARAMETER_ERROR, ErrorCode.BUSINESS_PARAMETER_ERROR_MESSAGE);
        }

        try {
            userMapper.editUser(user);
        } catch (Exception e) {
            return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
        }

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);
    }


    @Override
    public ServiceVO<?> editPassword(String oldPassword, String newPassword, String userId) {

        if (userMapper.getUserByUserId(userId) == null) {
            return new ServiceVO<>(ErrorCode.USERID_NOT_FOUND_ERROR, ErrorCode.USERID_NOT_FOUND_ERROR_MESSAGE);
        }

        if (newPassword.length() < PASSWORD_LENGTH || !newPassword.matches(PASSWORD_REGEX_PATTERN)) {
            return new ServiceVO<>(ErrorCode.PASSWORD_REGEX_ERROR, ErrorCode.PASSWORD_REGEX_ERROR_MESSAGE);
        }

        User user = userMapper.getUserByUserId(userId);
        Md5Hash md5Hash = new Md5Hash(oldPassword, user.getSalt(), 1024);
        if (!md5Hash.toHex().equals(user.getPassword())) {
            return new ServiceVO<>(ErrorCode.PASSWORD_INCORRECT_ERROR, ErrorCode.PASSWORD_INCORRECT_ERROR_MESSAGE);
        }

        String salt = SaltGenerator.getSalt();
        md5Hash = new Md5Hash(newPassword, salt, 1024);
        user.setPassword(md5Hash.toHex());
        try {
            userMapper.changePassword(userId, user.getPassword(), salt);
        } catch (Exception e) {
            return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
        }

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);
    }

    @Override
    public ServiceVO<?> emailVerify(String token) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(token))) {
            return new ServiceVO<>(ErrorCode.EMAIL_VERIFY_ERROR, EMAIL_VERIFY_ERROR_MESSAGE);
        }

        User user = null;
        try {
            user = (User) redisTemplate.opsForValue().get(token);
        } catch (Exception e) {
            return new ServiceVO<>(ErrorCode.REDIS_ERROR, ErrorCode.REDIS_ERROR_MESSAGE);
        }

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

        HashMap<String, Object> res = new HashMap<>();
        res.put("userId", user.getUserId());
        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE, res);

    }

    @Override
    public ServiceVO<?> testSqlOnly() {
        int count = userMapper.testSql();
        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE, count);
    }

    @Override
    public ServiceVO<?> testRedisOnly() {
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        opsForValue.set("test", "Minimal trader liver inter performances comprehensive boundaries, float gave bbs arguments donated pad certain, verde dad consolidated leg pierre. ");
        Boolean isExist = redisTemplate.hasKey("test");
        HashMap<String, Object> res = new HashMap<>();
        String text = (String) redisTemplate.opsForValue().get("test");
        res.put("isExist", isExist);
        res.put("text", text);
        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE, res);
    }
}
