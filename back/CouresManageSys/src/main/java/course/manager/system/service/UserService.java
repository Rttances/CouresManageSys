package course.manager.system.service;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import course.manager.system.dao.mapper.UserMapper;
import course.manager.system.dao.po.UserPO;
import course.manager.system.exception.AccountException;
import course.manager.system.exception.DataException;
import course.manager.system.exception.ParamException;
import course.manager.system.inverter.UserInverter;
import course.manager.system.model.ro.UserRO;
import course.manager.system.model.vo.RoleVO;
import course.manager.system.model.vo.UserRolePermissionVO;
import course.manager.system.model.vo.UserVO;
import course.manager.system.util.SecretUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class UserService extends ServiceImpl<UserMapper, UserPO> {


    @Resource
    private UserInverter userInverter;

    @Resource
    private RolePermissionService rolePermissionService;

    /**
     * 根据用户 id 查询用户信息
     *
     * @param userId 用户 id
     * @return
     */
    // @Cacheable(value = USER_CACHE, key = "'USER_ID:'+ #userId")
    public UserVO detailByUserId(String userId) {
        if (StrUtil.isBlank(userId)) {
            throw ParamException.paramMissError("用户 id 不能为空");
        }
        UserPO userPO = baseMapper.selectById(userId);
        if (Objects.isNull(userPO)) {
            throw DataException.dataNotFoundError("用户信息查找为空");
        }
        UserVO userVO = userInverter.po2VO(userPO);
        userVO.setUserRolePermissionVO(rolePermissionService.rolePermissionByUserId(userId, null));
        return userVO;
    }

    /**
     * 用户注册
     *
     * @param userRO 用户注册参数，其中 用户 id、密码、角色 id 非空
     * @return
     */
    @Transactional
    public UserVO createUser(UserRO userRO) {
        if (Objects.isNull(userRO)
                || StrUtil.isBlank(userRO.getUserId())
                || StrUtil.isBlank(userRO.getPassword())
                || Objects.isNull(userRO.getRoleId())) {
            throw ParamException.paramMissError("用户信息或用户 id 或用户密码或用户角色 id 不能为空");
        }
        UserPO userPO = userInverter.ro2PO(userRO);
        // 密码加密
        userPO.setPassword(SecretUtil.encryptSM3(userPO.getPassword()));
        int count = baseMapper.insert(userPO);
        if (count == 0) {
            throw DataException.dataInsertError();
        }
        UserRolePermissionVO userRolePermissionVO = rolePermissionService.mapUserRole(userPO.getUserId(), userRO.getRoleId());
        if (Objects.isNull(userRolePermissionVO)) {
            throw DataException.dataInsertError();
        }
        return detailByUserId(userPO.getUserId());
    }

    /**
     * 用户登陆
     *
     * @param userRO 用户登陆参数，其中必需要用户 id 和用户密码
     * @return
     */
    public UserVO doLogin(UserRO userRO) {
        if (Objects.isNull(userRO)
                || StrUtil.isBlank(userRO.getUserId())
                || StrUtil.isBlank(userRO.getPassword())
                || Objects.isNull(userRO.getRoleId())) {
            throw ParamException.paramMissError("用户信息或用户 id 或用户密码或用户角色 id 不能为空");
        }
        // 校验账号是否存在
        UserPO userPO = baseMapper.selectById(userRO.getUserId());
        if (Objects.isNull(userPO)) {
            throw AccountException.accountNotFound();
        }
        // 校验账号密码是否匹配
        Boolean matched = SecretUtil.matchSM3(userRO.getPassword(), userPO.getPassword());
        if (!matched) {
            throw AccountException.passwordError();
        }
        // 查询角色权限信息
        UserRolePermissionVO userRolePermissionVO = rolePermissionService.rolePermissionByUserId(userPO.getUserId(), userRO.getRoleId());
        log.info("查询到的用户角色信息：{}, 权限信息：{}", userRolePermissionVO.getRoleSet(), userRolePermissionVO.getPermissionSet());
        if (CollUtil.isEmpty(userRolePermissionVO.getRoleSet())
                || !CollUtil.contains(userRolePermissionVO.getRoleSet().stream().map(RoleVO::getRoleId).toList(), userRO.getRoleId())) {
            throw AccountException.roleNotFound();
        }
        // 使用SaToken框架登录
        StpUtil.login(userPO.getUserId() + '-' + userRO.getRoleId());
        UserVO userVO = userInverter.po2VO(userPO);
        userVO.setUserRolePermissionVO(userRolePermissionVO);
        return userVO;
    }

}
