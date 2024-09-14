import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // 进度条
import 'nprogress/nprogress.css' // 进度条样式
import { getToken } from '@/utils/auth' // 从cookie里面获得token
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // 进度条配置

const whiteList = ['/login'] // 白名单

router.beforeEach(async(to, from, next) => {
  // 启动进度条
  NProgress.start()

  // 设置页面标题
  document.title = getPageTitle(to.meta.title)

  // 判断用户是否可以登陆
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // 如果已经登陆，重定向到home页面
      next({ path: '/' })
      // 关闭进度条
      NProgress.done()
    } else {
      // determine whether the user has obtained his permission roles through getInfo
      // 判断getInfo里是否有对应的角色符合权限
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      if (hasRoles) {
        // 如果角色符合权限，则直接进入目录
        next()
      } else {
        try {
          // 从getInfo中获得用户的角色roles，得到的roles是一个数组，比如['admin'] or ,['developer','editor']
          const roles = await store.dispatch('user/getInfo')
          // 根据roles生成有权限的路由
          const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
          // 动态生成新的路由
          router.addRoutes(accessRoutes)
          // hack method to ensure that addRoutes is complete
          // 设置replace: true, 导航将不会留下历史记录
          next({ ...to, replace: true })
        } catch (error) {
          // 移除token，跳转到登录页重新登陆
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    // 如果没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 如果在白名单里面，则直接进入
      next()
    } else {
      // 如果是其他页面，则跳转到登录页面
      // console.log(to.path)
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
    next()
  }
})

router.afterEach(() => {
  // 最后关闭进度条
  NProgress.done()
})
