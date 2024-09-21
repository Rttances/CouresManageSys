<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">

      <div class="title-container">
        <h3 class="title">高校教学信息管理系统</h3>
      </div>

      <el-form-item prop="userId">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="userId"
          v-model="loginForm.userId"
          placeholder="用户名"
          name="userId"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>
      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="密码"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-form-item prop="roleId" style="width: 230px">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-select v-model="loginForm.roleId" placeholder="角色">
          <el-option
            v-for="item in roleOptions"
            :key="item.roleId"
            :value="item.roleId"
            :label="item.roleName"
          />
        </el-select>
      </el-form-item>
      <div class="button-box">
        <el-button class="login-button" :loading="loading" type="primary" @click.native.prevent="handleLogin">登陆</el-button>
      </div>
      <div class="tips">
        <span style="margin-right:20px;">Tips: 教师密码默认为账号后六位</span>
        <!-- <span> password: any</span> -->
      </div>
    </el-form>
  </div>
</template>

<script>
// import { validUserId } from '@/utils/validate'
export default {
  name: 'Login',
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error('请输入用户密码'))
      } else if (value.length < 3) {
        callback(new Error('密码不能小于五位数'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        userId: '',
        password: '',
        roleId: ''
      },
      loginRules: {
        userId: [{ required: true, trigger: 'blur', message: '请输入用户名称' }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        roleId: [{ required: true, trigger: 'blur', message: '请选择登陆角色' }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
      roleOptions: [
        {
          roleId: 1,
          roleName: '管理员'
        },
        {
          roleId: 2,
          roleName: '教师'
        }
      ]
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
    .el-input__inner {
      background-color: #283443 !important;
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .login-button {
    width:100%;
    margin-bottom:20px;
  }

  .tips {
    font-size: 12px;
    color: #88b9f9;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
