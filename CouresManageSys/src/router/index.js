import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/home/index'),
        meta: { title: '首页', icon: 'dashboard' }
      }
    ]
  }
]

export const asyncRoutes = [
  {
    path: '/teacher-function',
    component: Layout,
    // redirect: '/teacher-function/course-schedule',
    name: 'Teacher-Function',
    meta: { title: '教师功能', icon: 'el-icon-s-help', roles: ['teacher'] },
    children: [
      {
        path: 'course-schedule',
        name: 'Course-Schedule',
        component: () => import('@/views/course-schedule/index'),
        meta: { title: '自主排课', icon: 'dashboard' }
      }
    ]
  },
  {
    path: '/system-management',
    component: Layout,
    redirect: '/system-management/teacher-management',
    name: 'System-Management',
    meta: { title: '系统管理', icon: 'el-icon-s-help', roles: ['admin'] },
    children: [
      {
        path: 'teacher-management',
        name: 'Teacher-Management',
        component: () => import('@/views/teacher-management/index'),
        meta: { title: '教师管理', icon: 'table' }
      },
      {
        path: 'course-management',
        name: 'Course-Management',
        component: () => import('@/views/course-management/index'),
        meta: { title: '课程管理', icon: 'table' }
      },
      {
        path: 'schedule-management',
        name: 'Schedule-Management',
        component: () => import('@/views/schedule-management/index'),
        meta: { title: '课程表管理', icon: 'table' }
      },
      {
        path: 'class-management',
        name: 'Class-Management',
        component: () => import('@/views/class-management/index'),
        meta: { title: '行政班级管理', icon: 'table' }
      },
      {
        path: 'teach-class-management',
        name: 'Teach-Class-Management',
        component: () => import('@/views/teach-class-management/index'),
        meta: { title: '教学班级管理', icon: 'table' }
      },
      {
        path: 'schedule-situation',
        name: 'Schedule-Situation',
        component: () => import('@/views/schedule-situation/index'),
        meta: { title: '排课情况', icon: 'table' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
