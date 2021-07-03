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
import lombok.SneakyThrows;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Base64;
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

        // Isaac TODO: JWT
        String header = "{";
        header += "alg: HS256";
        header += "typ: JWT}";

        String payload = "{";
        payload += "sub: Register";
        payload += "userId: " + userId;
        payload += "user" + user.toString() + "}";

        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        String secret = sb.toString();
        String jwt;
        try {
            jwt = SaltGenerator.HMACSHA256(SaltGenerator.base64UrlEncode(header) + "." + SaltGenerator.base64UrlEncode(payload), secret);
        } catch (Exception e) {
            e.printStackTrace();
            jwt = header + "." + payload + "." + secret;
        }


        // Kylee TODO: email sender
        if (!isEmailSent(userId)) {
            return new ServiceVO<>(ErrorCode.BUSINESS_PARAMETER_ERROR, ErrorCode.BUSINESS_PARAMETER_ERROR_MESSAGE);
        }

        // Channing TODO: redis
        String userToken = "UserJwtToken";
        ValueOperations<String, Serializable> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(userToken, user, 30, TimeUnit.MINUTES);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("userId", userId);
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

    @Autowired
    private JavaMailSender mailSender;


    @SneakyThrows
    public boolean isEmailSent(String userId) {
        User user = null;

//        try {
//            user = userMapper.getUserbyId(userId);
//        } catch (Exception e) {
//            return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
//        }

        String emailFrom = "YYDS.W09A@gmail.com";

        // Need to change these later
        String emailTo = "YYDS.W09A@gmail.com";
        String token = "abcd123";

        //String emailTo = user.getEmail();
        //String token = userService.createToken();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setSubject("[YYDS] Please Verify Your Email!");
            helper.setFrom(emailFrom);
            helper.setTo(emailTo);


            //        helper.setText("<b>Dear <code>user.getFirstName()</code></b>,<br><p>Welcome to </p><b>YYDS</b>! Please verify" +
            //                       " your account within <b>10 minutes</b> following this link: http://yyds" +
            //                       ".com/<code>token</code></p>", true);

            helper.setText("<b>Dear <code>emailTo</code></b>,<br><p>Welcome to </p><b>YYDS</b>! Please verify" +
                           " your account within <b>10 minutes</b> following this link: http://yyds" +
                           ".com/<code>token</code></p>", true);

            mailSender.send(message);

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
