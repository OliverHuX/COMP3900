package com.yyds.recipe.service.impl;

import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import com.yyds.recipe.utils.BcryptPasswordUtil;
import com.yyds.recipe.utils.JwtUtil;
import com.yyds.recipe.utils.SaltGenerator;
import com.yyds.recipe.utils.UUIDGenerator;
import com.yyds.recipe.vo.ErrorCode;
import com.yyds.recipe.vo.ServiceVO;
import com.yyds.recipe.vo.SuccessCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


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
    private static final String EMAIL_VERIFY_TOKEN_PREFIX = "email verify: ";

    // TODO: transactional does not work
    @Transactional
    @Override
    public ServiceVO<?> register(User user, HttpServletRequest request, HttpServletResponse response) {

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
        user.setPassword(BcryptPasswordUtil.encodePassword(user.getPassword()));
        user.setCreateTime(String.valueOf(System.currentTimeMillis()));

        // JWT
        HashMap<String, String> payload = new HashMap<>();
        payload.put("userId", user.getUserId());
        payload.put("email", user.getEmail());
        String registerToken = EMAIL_VERIFY_TOKEN_PREFIX + JwtUtil.createToken(payload) ;

        if (redisTemplate.hasKey(registerToken)) {
            return new ServiceVO<>(ErrorCode.EMAIL_REGISTERED_BUT_NOT_VERIFIED, ErrorCode.EMAIL_REGISTERED_BUT_NOT_VERIFIED_MESSAGE);
        }

        // set in redis
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(registerToken, user, 30, TimeUnit.MINUTES);

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
                    registerToken + "</code></p>", true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            redisTemplate.delete(registerToken);
            return new ServiceVO<>(ErrorCode.MAIL_SERVER_ERROR, ErrorCode.MAIL_SERVER_ERROR_MESSAGE);
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("verify token", registerToken);
        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE, resultMap);

    }

    @Override
    public ServiceVO<?> loginUser(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {

        User user = userMapper.getUserByEmail(loginUser.getEmail());
        if (user == null) {
            return new ServiceVO<>(ErrorCode.EMAIL_NOT_EXISTS_ERROR, ErrorCode.EMAIL_NOT_EXISTS_ERROR_MESSAGE);
        }
        if (!BcryptPasswordUtil.passwordMatch(loginUser.getPassword(), user.getPassword())) {
            return new ServiceVO<>(ErrorCode.PASSWORD_INCORRECT_ERROR, ErrorCode.PASSWORD_INCORRECT_ERROR_MESSAGE);
        }

        String email = loginUser.getEmail();
        String userId = userMapper.getUserIdByEmail(email);
        HashMap<String, String> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("userId", userId);
        String token = JwtUtil.createToken(payload);
        HashMap<String, Object> resultMap = new HashMap<>();

        try {
            ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
            opsForValue.set(token, user, Duration.ofHours(12));
        } catch (Exception e) {
            return new ServiceVO<>(ErrorCode.REDIS_ERROR, ErrorCode.REDIS_ERROR_MESSAGE);
        }

        resultMap.put("userId", userId);
        resultMap.put("token", token);

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE, resultMap);

    }

    @Override
    public ServiceVO<?> logoutUser(String userId, HttpServletRequest request, HttpServletResponse response) {
        // Subject subject = SecurityUtils.getSubject();
        // subject.logout();

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);
    }

    @Override
    @Transactional
    public ServiceVO<?> editUser(User user, HttpServletRequest request, HttpServletResponse response) {

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

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);
    }

    @Override
    public ServiceVO<?> emailVerify(String token) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(token))) {
            return new ServiceVO<>(ErrorCode.EMAIL_VERIFY_ERROR, ErrorCode.EMAIL_VERIFY_ERROR_MESSAGE);
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
