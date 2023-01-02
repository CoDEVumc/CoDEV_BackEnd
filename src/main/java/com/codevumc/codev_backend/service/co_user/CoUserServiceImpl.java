package com.codevumc.codev_backend.service.co_user;

import com.codevumc.codev_backend.domain.CoUser;
import com.codevumc.codev_backend.mapper.CoUserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoUserServiceImpl implements CoUserService{
    private CoUserMapper coUserMapper;

    public CoUserServiceImpl(CoUserMapper coUserMapper) { this.coUserMapper = coUserMapper; }

    public List<CoUser> findALlUser() {
        return coUserMapper.findAll();
    }
}
