import {createRouter, createWebHistory} from 'vue-router'
import {useAuthStore} from '@/stores/auth.ts'

// custom smooth scroll: works even when the OS/browser disables native smooth scrolling
function smoothScrollTo(targetTop: number, duration = 900) {
	const root = document.documentElement
	const start = window.scrollY
	const distance = targetTop - start
	const startTime = performance.now()
	const ease = (t: number) => 1 - Math.pow(1 - t, 4) // easeOutQuart

	root.style.scrollBehavior = 'auto' // stop the CSS rule from fighting the animation

	const step = (now: number) => {
		const t = Math.min((now - startTime) / duration, 1)
		window.scrollTo(0, start + distance * ease(t))
		if (t < 1) {
			requestAnimationFrame(step)
		} else {
			root.style.scrollBehavior = ''
		}
	}
	requestAnimationFrame(step)
}

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: '/',
			name: 'home',
			meta: {title: 'Home'},
			component: () => import('@/views/home/HomeIndex.vue')
		},
		{
			path: '/login',
			name: 'login',
			meta: {title: 'Login'},
			component: () => import('@/views/auth/login/LoginIndex.vue')
		},
		{
			path: '/dashboard',
			name: 'dashboard',
			meta: {requiresAuth: true, title: 'Dashboard'},
			component: () => import('@/views/dashboard/DashboardIndex.vue')
		}
	],
	scrollBehavior(to, from, savedPosition) {
		if (savedPosition) return savedPosition

		return new Promise((resolve) => {
			// wait a moment when coming from another page so the sections exist
			const wait = from.name && from.name !== to.name ? 350 : 0

			setTimeout(() => {
				if (to.hash) {
					const el = document.querySelector<HTMLElement>(to.hash)
					if (el) {
						const margin = parseFloat(getComputedStyle(el).scrollMarginTop) || 0
						smoothScrollTo(el.getBoundingClientRect().top + window.scrollY - margin)
					}
				} else {
					smoothScrollTo(0)
				}
				resolve(false) // we already scrolled, so tell the router not to
			}, wait)
		})
	}
})

router.beforeEach((to) => {
	const authStore = useAuthStore()
	authStore.loadTokens()

	if (to.meta.requiresAuth && !authStore.accessToken) {
		return {name: 'login'}
	}

	if (to.name === 'login' && authStore.accessToken) {
		return {name: 'dashboard'}
	}

	document.title = to.name === 'home' ? 'Portfolio' : `${to.meta.title ?? 'Portfolio'} | Portfolio`
})

export default router