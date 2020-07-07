package com.leyou.service.Impl;

import com.leyou.mapper.UserMapper;
import com.leyou.pojo.User;
import com.leyou.service.UserService;
import com.leyou.utils.CodecUtils;
import com.leyou.utils.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "user:check:";

    /**
     * 校验数据是否可用
     *
     * @param data
     * @param type
     * @return
     */
    @Override
    public Boolean checkUser(String data, Integer type) {
        User user = new User();
        if (type == 1) {
            user.setUsername(data);
        } else if (type == 2) {
            user.setPhone(data);
        } else {
            return null;
        }

        return userMapper.selectCount(user) == 0;
    }

    /**
     * 发送短信消息 到队列
     * @param phone
     */
    @Override
    public void sendSms(String phone) {
        Map<String, String> smsMap = new HashMap<>();
        //生成6位的验证码
        String code = NumberUtils.generateCode(6);
        //发送信息到队列
        smsMap.put("phone", phone);
        smsMap.put("code", code);
        amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE", "leyou.sms", smsMap);
        //保存验证码到redis
        stringRedisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
    }

    /**
     * 用户注册
     * @param user
     * @param code
     */
    @Override
    public void registerUser(User user, String code) {
        //从redis获取验证码
        String redisCode = stringRedisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        //校验验证码
        if (!StringUtils.equals(code, redisCode)) {
            return;
        }
        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //MD5加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        user.setId(null);
        user.setCreated(new Date());
        //注册用户
        userMapper.insertSelective(user);
        //删除redis中的验证码
        stringRedisTemplate.delete(KEY_PREFIX + user.getPhone());
    }

    /**
     * 查询用户
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        User user1 = userMapper.selectOne(user);
        if(user1==null){
            return null;
        }
        //获取用户输入的密码，进行加盐加密
        password = CodecUtils.md5Hex(password, user1.getSalt());
        //与数据库的用户密码进行比较
        if(StringUtils.equals(password,user1.getPassword())){
            return user1;
        }
        return null;
    }
}
