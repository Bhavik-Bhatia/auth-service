package com.ab.auth.helper;

import com.ab.auth.dto.UserDTO;
import com.ab.auth.entity.Device;
import com.ab.auth.entity.User;
import com.ab.auth.repository.DeviceRepository;
import com.ab.auth.repository.UserRepository;
import com.ab.cache_service.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import static com.ab.auth.constants.AuthConstants.CACHE_DEVICE_DETAILS;
import static com.ab.auth.constants.AuthConstants.CACHE_USER_DETAILS;

/**
 * Helper class for User Service
 */
@Component
public class UserHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserHelper.class);

    @Autowired
    private Environment environment;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;


    /**
     * Get user details from cache/DB
     *
     * @param email to get details with
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public User getUserDetails(String email) {
        User userEntity = null;
        try {
//          Get user details via cache
            Map<String, Object> map = new HashMap<>();
            map.put(email + CACHE_USER_DETAILS, null);
            User user = (User) cacheService.cacheOps(map, CacheService.CacheOperation.FETCH).getFirst();
            if (user != null) {
                userEntity = user;
            } else {
//              Either from DB
                userEntity = userRepository.findActiveUserByEmail(email);
            }
        } catch (Exception e) {
            LOGGER.error("Error while getting user details: {}", e.getMessage());
        }
        return userEntity;
    }

    /**
     * Get device details from cache/DB
     *
     * @param userId to get details with
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Device getDeviceDetails(Long userId, String deviceId) {
        Device deviceEntity = null;
        try {
//          Get user details via cache
            Map<String, Object> map = new HashMap<>();
            map.put(userId + CACHE_DEVICE_DETAILS + "#" + deviceId, null);
            Device device = (Device) cacheService.cacheOps(map, CacheService.CacheOperation.FETCH).getFirst();
            if (device != null) {
                deviceEntity = device;
            } else {
//              Either from DB
                deviceEntity = deviceRepository.existsByDeviceIdAndUserId(userId, deviceId);
            }
        } catch (Exception e) {
            LOGGER.error("Error while getting device details: {}", e.getMessage());
        }
        return deviceEntity;
    }

    /**
     * Insert device details into DB and Cache
     *
     * @param device to get details with
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Device insertDeviceDetails(Device device) {
        Device deviceEntity = null;
        try {
            deviceEntity = deviceRepository.save(device);
            Map<String, Object> map = new HashMap<>();
            map.put(device.getUser().getUserId() + CACHE_DEVICE_DETAILS + "#" + deviceEntity.getId(), deviceEntity);
            cacheService.cacheOps(map, CacheService.CacheOperation.INSERT);
        } catch (Exception e) {
            LOGGER.error("Error while inserting device details: {}", e.getMessage());
            throw e;
        }
        return deviceEntity;
    }

    /**
     * Insert user details into DB and Cache
     *
     * @param user to get details with
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User insertUserDetails(User user) {
        User userEntity = null;
        try {
            userEntity = userRepository.save(user);
            Map<String, Object> map = new HashMap<>();
            map.put(user.getEmail() + CACHE_USER_DETAILS, userEntity);
            cacheService.cacheOps(map, CacheService.CacheOperation.INSERT);
        } catch (Exception e) {
            LOGGER.error("Error while inserting user details: {}", e.getMessage());
            throw e;
        }
        return userEntity;
    }

    /**
     * General method to check user exists
     *
     * @param user get email
     * @return Boolean
     */
    public Boolean isUserExists(UserDTO user) {
        LOGGER.debug("Checking if user already exists");
        return userRepository.existsByEmail(user.getEmail());
    }

}
