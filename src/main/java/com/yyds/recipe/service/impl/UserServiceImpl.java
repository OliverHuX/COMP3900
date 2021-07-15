package com.yyds.recipe.service.impl;

import com.yyds.recipe.exception.MySqlErrorException;
import com.yyds.recipe.exception.response.ResponseCode;
import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import com.yyds.recipe.utils.BcryptPasswordUtil;
import com.yyds.recipe.utils.JwtUtil;
import com.yyds.recipe.utils.ResponseUtil;
import com.yyds.recipe.utils.UUIDGenerator;
import com.yyds.recipe.vo.ErrorCode;
import com.yyds.recipe.vo.ServiceVO;
import com.yyds.recipe.vo.SuccessCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

    @Override
    public ResponseEntity<?> register(User user, HttpServletRequest request, HttpServletResponse response) {

        // check email if exist
        if (userMapper.getUserByEmail(user.getEmail()) != null) {
            return ResponseUtil.getResponse(ResponseCode.EMAIL_ALREADY_EXISTS_ERROR, null, null);
        }

        if (user.getFirstName() == null || user.getLastName() == null || user.getGender() == null
                || user.getEmail() == null || user.getPassword() == null || user.getBirthdate() == null) {
            return ResponseUtil.getResponse(ResponseCode.BUSINESS_PARAMETER_ERROR, null, null);
        }

        // check email
        if (!user.getEmail().matches(EMAIL_REGEX_PATTEN)) {
            return ResponseUtil.getResponse(ResponseCode.EMAIL_REGEX_ERROR, null, null);
        }

        // check password
        if (!user.getPassword().matches(PASSWORD_REGEX_PATTERN)) {
            return ResponseUtil.getResponse(ResponseCode.PASSWORD_REGEX_ERROR, null, null);
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
        String registerToken = EMAIL_VERIFY_TOKEN_PREFIX + JwtUtil.createToken(payload);

        Boolean isRegistered = redisTemplate.hasKey(registerToken);
        if (isRegistered == null) {
            return ResponseUtil.getResponse(ResponseCode.REDIS_ERROR, null, null);
        }
        if (isRegistered) {
            return ResponseUtil.getResponse(ResponseCode.EMAIL_REGISTERED_BUT_NOT_VERIFIED, null, null);
        }

        // set in redis
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(registerToken, user, 30, TimeUnit.MINUTES);

        // send email
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
            return ResponseUtil.getResponse(ResponseCode.MAIL_SERVER_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> loginUser(User loginUser, HttpServletRequest request, HttpServletResponse response) {

        User user = userMapper.getUserByEmail(loginUser.getEmail());
        if (user == null) {
            return ResponseUtil.getResponse(ResponseCode.EMAIL_NOT_EXISTS_ERROR, null, null);
        }
        if (!BcryptPasswordUtil.passwordMatch(loginUser.getPassword(), user.getPassword())) {
            return ResponseUtil.getResponse(ResponseCode.PASSWORD_INCORRECT_ERROR, null, null);
        }

        String email = loginUser.getEmail();
        String userId = userMapper.getUserIdByEmail(email);
        HashMap<String, String> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("userId", userId);
        String token = JwtUtil.createToken(payload);

        try {
            ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
            opsForValue.set(token, user, Duration.ofHours(12));
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.REDIS_ERROR, null, null);
        }

        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("userId", user.getUserId());
        body.put("token", token);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("token", token);
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, httpHeaders, body);

    }

    @Override
    public ResponseEntity<?> logoutUser(String userId, HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        redisTemplate.delete(token);
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    @Transactional
    public ResponseEntity<?> editUser(User user, HttpServletRequest request, HttpServletResponse response) {

        if (userMapper.getUserByUserId(user.getUserId()) == null) {
            return ResponseUtil.getResponse(ResponseCode.USERID_NOT_FOUND_ERROR, null, null);
        }

        if (user.getUserId() == null || (user.getGender() != null && (user.getGender() > 2 || user.getGender() < 0))) {
            return ResponseUtil.getResponse(ResponseCode.BUSINESS_PARAMETER_ERROR, null, null);
        }

        try {
            userMapper.editUser(user);
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }


    @Override
    public ResponseEntity<?> editPassword(String oldPassword, String newPassword, String userId) {

        if (userMapper.getUserByUserId(userId) == null) {
            return ResponseUtil.getResponse(ResponseCode.USERID_NOT_FOUND_ERROR, null, null);
        }

        if (newPassword.length() < PASSWORD_LENGTH || !newPassword.matches(PASSWORD_REGEX_PATTERN)) {
            return ResponseUtil.getResponse(ResponseCode.PASSWORD_REGEX_ERROR, null, null);
        }

        String encodePassword = BcryptPasswordUtil.encodePassword(newPassword);

        try {
            userMapper.changePassword(userId, encodePassword);
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public ResponseEntity<?> emailVerify(String token) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(token))) {
            return ResponseUtil.getResponse(ResponseCode.EMAIL_VERIFY_ERROR, null, null);
        }

        User user;
        try {
            user = (User) redisTemplate.opsForValue().get(token);
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.REDIS_ERROR, null, null);
        }


        if (user == null) {
            return ResponseUtil.getResponse(ResponseCode.EMAIL_VERIFY_ERROR, null, null);
        }

        User checkedUser = userMapper.getUserByUserId(user.getUserId());
        if (checkedUser != null) {
            redisTemplate.delete(token);
            return ResponseUtil.getResponse(ResponseCode.EMAIL_ALREADY_VERIFIED, null, null);
        }

        try {
            userMapper.saveUser(user);
        } catch (Exception e) {
            throw new MySqlErrorException();
        }


        try {
            userMapper.saveUserAccount(user);
        } catch (Exception e) {
            throw new MySqlErrorException();
        }

        redisTemplate.delete(token);

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);

    }
}
