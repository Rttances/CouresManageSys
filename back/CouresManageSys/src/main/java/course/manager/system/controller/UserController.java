package course.manager.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import course.manager.system.exception.ParamException;
import course.manager.system.model.ro.UserRO;
import course.manager.system.model.vo.ResponseResult;
import course.manager.system.model.vo.RoleVO;
import course.manager.system.model.vo.UserVO;
import course.manager.system.service.RolePermissionService;
import course.manager.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@Tag(name = "用户-User")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RolePermissionService rolePermissionService;

    @GetMapping("/get")
    @Operation(summary = "测试 get 请求")
    public ResponseResult<String> get() {
        return ResponseResult.success("你好世界");
    }

    @PostMapping("/create")
    @Operation(summary = "添加用户")
    public ResponseResult<UserVO> createUser(@RequestBody UserRO userRO) {
        if (Objects.isNull(userRO)) {
            throw ParamException.paramMissError("用户信息不能为空");
        }
        UserVO userVO = userService.createUser(userRO);
        return ResponseResult.success(userVO);
    }

    @GetMapping("/detail/{userId}")
    @Parameter(name = "userId", description = "用户编号")
    @Operation(summary = "根据 UserId 查询用户信息")
    public ResponseResult<UserVO> detailByUserId(@PathVariable("userId") String userId) {
        if (StrUtil.isBlank(userId)) {
            throw ParamException.paramMissError("用户 id 不能为空");
        }
        UserVO userVO = userService.detailByUserId(userId);
        return ResponseResult.success(userVO);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public ResponseResult<UserVO> userLogin(@RequestBody UserRO userRO) {
        UserVO userVO = userService.doLogin(userRO);
        return ResponseResult.success(userVO);
    }

    @GetMapping("/logout")
    public ResponseResult<Void> userLogin() {
        StpUtil.logout();
        return ResponseResult.success(null);
    }

    @GetMapping("/all-role-permission")
    @Operation(summary = "获取所有角色以及权限信息")
    public ResponseResult<List<RoleVO>> userAllPermissions() {
        List<RoleVO> roleVOS = rolePermissionService.allRole();
        return ResponseResult.success(roleVOS);
    }
}
