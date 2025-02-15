package com.ab.auth.util;

import com.ab.auth.dto.UserDTO;
import com.ab.auth.entity.Device;
import com.ab.auth.entity.User;
import org.springframework.beans.BeanUtils;

public class ModelMapperUtil {


    public static Device getDeviceEntityFromUserEntity(User user, String deviceId) {
        Device device = new Device();
        device.setUser(user);
        device.setDeviceId(deviceId);
        return device;
    }

    public static UserDTO getUserDTOFromUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }


}
