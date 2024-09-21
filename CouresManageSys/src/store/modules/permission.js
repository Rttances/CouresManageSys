import { asyncRoutes, constantRoutes } from '@/router'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) { // 如果route对象有meta，切meta里面有roles，则进行判断
    return roles.some(role => route.meta.roles.includes(role)) // 传入的roles是一个数组，查询roles数组里是否和路径里的roles元素匹配，如果不匹配则为false
  } else {
    return true // 如果没有mate，或者meta里面没有roles，则默认存在
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) { // 递归
  const res = []

  routes.forEach(route => { // 传进来一个route，里面是路径对象
    const tmp = { ...route } // 存起来一个
    if (hasPermission(roles, tmp)) { // 通过hasPermission函数进行判断，是否有权限
      if (tmp.children) { // 如果有child，则传入children
        tmp.children = filterAsyncRoutes(tmp.children, roles) // 一直得到tmp
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes) // constantRoutes就是一直有的route
  }
}

const actions = {
  generateRoutes({ commit }, roles) { // 生成路径
    return new Promise(resolve => {
      // let accessedRoutes
      // if (roles.includes('admin')) {
      //   accessedRoutes = asyncRoutes || [] // 如果是admin，则用asyncRoutes
      // } else {
      const accessedRoutes = filterAsyncRoutes(asyncRoutes, roles) // 过滤asyncRoutes里面没有权限的
      // }
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
