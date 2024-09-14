package course.manager.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import course.manager.system.dao.mapper.PermissionMapper;
import course.manager.system.dao.mapper.RoleMapper;
import course.manager.system.dao.mapper.RolePermissionMapper;
import course.manager.system.dao.mapper.UserRoleMapper;
import course.manager.system.dao.po.PermissionPO;
import course.manager.system.dao.po.RolePO;
import course.manager.system.dao.po.RolePermissionPO;
import course.manager.system.dao.po.UserRolePO;
import course.manager.system.exception.DataException;
import course.manager.system.exception.ParamException;
import course.manager.system.inverter.UserRolePermissionInverter;
import course.manager.system.model.vo.PermissionVO;
import course.manager.system.model.vo.RoleVO;
import course.manager.system.model.vo.UserRolePermissionVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RolePermissionService {
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Resource
    private UserRolePermissionInverter userRolePermissionInverter;

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 根据角色 id 查询角色信息以及权限信息
     *
     * @param userId
     * @return
     */
    public UserRolePermissionVO rolePermissionByUserId(String userId, Long roleId) {
        if (StrUtil.isBlank(userId)) {
            throw ParamException.paramMissError("用户 id 不能为空");
        }
        List<UserRolePO> userRolePOS = userRoleMapper.selectList(Wrappers.<UserRolePO>lambdaQuery()
                .eq(UserRolePO::getUserId, userId)
                .eq(Objects.nonNull(roleId), UserRolePO::getRoleId, roleId));
        if (CollUtil.isEmpty(userRolePOS)) {
            throw DataException.dataNotFoundError("用户角色信息查找为空");
        }
        UserRolePermissionVO result = UserRolePermissionVO.builder()
                .roleSet(ListUtil.list(false))
                .permissionSet(ListUtil.list(false))
                .build();
        // 填充角色信息以及权限信息
        userRolePOS.stream()
                .map(UserRolePO::getRoleId)
                .distinct()
                .forEach(role -> {
                    RoleVO roleVO = detailByRoleId(role);
                    CollUtil.addAll(result.getRoleSet(), roleVO);
                    CollUtil.addAll(result.getPermissionSet(), roleVO.getPermissionSet());
                });
        return result;
    }

    /**
     * 根据权限 id 集合查询权限信息
     *
     * @param permissionIdSet 权限 id 集合
     * @return
     */
    public List<PermissionVO> detailByPermissionIdSet(List<Long> permissionIdSet) {
        if (CollUtil.isEmpty(permissionIdSet)) {
            throw ParamException.paramMissError("权限 id 集合不能为空");
        }
        List<PermissionPO> permissionPOS = permissionMapper.selectList(Wrappers.<PermissionPO>lambdaQuery()
                .in(PermissionPO::getPermissionId, permissionIdSet));
        List<PermissionVO> permissionVOS = userRolePermissionInverter.po2VO(permissionPOS);
        if (CollUtil.isEmpty(permissionVOS)) {
            throw DataException.dataNotFoundError("该权限 id 集合查询到的权限信息为空");
        }
        return permissionVOS;
    }

    /**
     * 根据角色 id 查询角色信息
     *
     * @param roleId 角色 id
     * @return
     */
    public RoleVO detailByRoleId(Long roleId) {
        if (Objects.isNull(roleId)) {
            throw ParamException.paramMissError("角色 id 为空");
        }
        RolePO rolePO = roleMapper.selectById(roleId);
        if (Objects.isNull(rolePO)) {
            throw DataException.dataNotFoundError("角色 id 对应角色信息查询失败");
        }
        List<RolePermissionPO> rolePermissionPOS = rolePermissionMapper.selectList(Wrappers.<RolePermissionPO>lambdaQuery()
                .eq(RolePermissionPO::getRoleId, roleId));
        List<PermissionVO> permissionVOS = null;
        if (CollUtil.isNotEmpty(rolePermissionPOS)) {
            List<Long> permissionIdSet = rolePermissionPOS.stream()
                    .map(RolePermissionPO::getPermissionId)
                    .distinct()
                    .toList();
            permissionVOS = detailByPermissionIdSet(permissionIdSet);
        }

        return userRolePermissionInverter.po2VO(rolePO, permissionVOS);
    }

    /**
     * 对用户赋予角色
     *
     * @param userId 用户 id
     * @param roleId 角色 id
     * @return 用户下面的角色、权限信息
     */
    public UserRolePermissionVO mapUserRole(String userId, Long roleId) {
        if (StrUtil.isBlank(userId) || Objects.isNull(roleId)) {
            throw ParamException.paramMissError("用户 id 或者角色 id 为空");
        }
        RolePO rolePO = roleMapper.selectById(roleId);
        if (Objects.isNull(rolePO)) {
            throw DataException.dataNotFoundError("角色 id 对应角色信息不存在");
        }
        UserRolePO userRolePO = userRoleMapper.selectOne(Wrappers.<UserRolePO>lambdaQuery().eq(UserRolePO::getRoleId, roleId).eq(UserRolePO::getUserId, userId));
        if (Objects.nonNull(userRolePO)) {
            return rolePermissionByUserId(userId, null);
        }
        int inserted = userRoleMapper.insert(new UserRolePO(userId, roleId));
        if (inserted == 0) {
            return null;
        }
        return rolePermissionByUserId(userId, null);
    }

    /**
     * 根据角色名查找角色信息
     *
     * @param roleName 角色名
     * @return 角色信息
     */
    public RoleVO detailByRoleName(String roleName) {
        if (StrUtil.isBlank(roleName)) {
            throw ParamException.paramMissError("查询参数角色名缺失");
        }
        RolePO rolePO = roleMapper.selectOne(Wrappers.<RolePO>lambdaQuery().eq(RolePO::getRoleName, roleName));
        if (Objects.isNull(rolePO)) {
            throw DataException.dataNotFoundError("角色名对应角色信息未找到");
        }
        List<RolePermissionPO> rolePermissionPOS = rolePermissionMapper.selectList(Wrappers.<RolePermissionPO>lambdaQuery()
                .eq(RolePermissionPO::getRoleId, rolePO.getRoleId()));
        List<PermissionVO> permissionVOS = null;
        if (CollUtil.isNotEmpty(rolePermissionPOS)) {
            List<Long> permissionIdSet = rolePermissionPOS.stream()
                    .map(RolePermissionPO::getPermissionId)
                    .distinct()
                    .toList();
            permissionVOS = detailByPermissionIdSet(permissionIdSet);
        }

        return userRolePermissionInverter.po2VO(rolePO, permissionVOS);
    }

    /**
     * 根据角色 id 查询权限信息
     *
     * @param roleId 角色 id
     * @return 权限信息
     */
    public List<PermissionVO> detailPermissionByRoleId(Long roleId) {
        if (Objects.isNull(roleId)) {
            throw ParamException.paramMissError("角色 id 缺失");
        }
        List<RolePermissionPO> rolePermissionPOS = rolePermissionMapper.selectList(Wrappers.<RolePermissionPO>lambdaQuery().eq(RolePermissionPO::getRoleId, roleId));
        if (CollUtil.isEmpty(rolePermissionPOS)) {
            return ListUtil.toList();
        }
        return detailByPermissionIdSet(rolePermissionPOS.stream().map(RolePermissionPO::getPermissionId).toList());
    }

    /**
     * 获取所有角色以及对应的权限信息
     *
     * @return 所有角色以及权限信息
     */
    public List<RoleVO> allRole() {
        List<RolePO> rolePOS = roleMapper.selectList(null);
        if (CollUtil.isEmpty(rolePOS)) {
            throw DataException.dataNotFoundError("查询角色信息失败");
        }
        List<RoleVO> roleVOS = rolePOS.stream()
                .map(role -> {
                    List<PermissionVO> permissionVOS = detailPermissionByRoleId(role.getRoleId());
                    return userRolePermissionInverter.po2VO(role, permissionVOS);
                })
                .toList();
        return roleVOS;
    }

}
