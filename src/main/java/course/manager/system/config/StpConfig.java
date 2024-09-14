package course.manager.system.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import course.manager.system.exception.DataException;
import course.manager.system.exception.ParamException;
import course.manager.system.model.vo.PermissionVO;
import course.manager.system.model.vo.RoleVO;
import course.manager.system.model.vo.UserRolePermissionVO;
import course.manager.system.service.RolePermissionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Sa-Token配置
 */
@Component
public class StpConfig implements StpInterface {

    @Resource
    private RolePermissionService rolePermissionService;

    /**
     * 根据userId获取权限集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 权限集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 这里根据 - 分割，一个用户可以对应多个角色，只需要注入当前登录角色的权限集合
        List<String> loginIdList = StrUtil.split((String) loginId, '-');
        if (loginIdList.size() != 2) {
            throw ParamException.paramMissError("loginId 格式错误，应该为：userId - roleId");
        }
        String userId = loginIdList.get(0);
        Long roleId = Long.parseLong(loginIdList.get(1));
        // 根据userId、roleId获取权限集合
        UserRolePermissionVO userRolePermissionVO = rolePermissionService.rolePermissionByUserId(userId, roleId);
        if (Objects.isNull(userRolePermissionVO)
                || CollUtil.isEmpty(userRolePermissionVO.getRoleSet())) {
            throw DataException.dataNotFoundError("未找到该用户的角色信息");
        }
        // 提取权限名称
        return userRolePermissionVO.getPermissionSet()
                .stream()
                .map(PermissionVO::getPermissionName)
                .distinct()
                .toList();
    }

    /**
     * 根据userId获取角色集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 角色集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 这里根据 - 分割，一个用户可以对应多个角色，只需要注入当前登录角色的权限集合
        List<String> loginIdList = StrUtil.split((String) loginId, '-');
        if (loginIdList.size() != 2) {
            throw ParamException.paramMissError("loginId 格式错误，应该为：userId - roleId");
        }
        String userId = loginIdList.get(0);
        Long roleId = Long.parseLong(loginIdList.get(1));
        UserRolePermissionVO userRolePermissionVO = rolePermissionService.rolePermissionByUserId(userId, roleId);
        if (Objects.isNull(userRolePermissionVO)
                || CollUtil.isEmpty(userRolePermissionVO.getRoleSet())) {
            throw DataException.dataNotFoundError("未找到该用户的角色信息");
        }
        // 注入角色名称
        return userRolePermissionVO.getRoleSet()
                .stream()
                .map(RoleVO::getRoleName)
                .distinct()
                .toList();
    }
}
