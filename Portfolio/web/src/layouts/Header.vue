<script setup lang="ts">
import {nextTick, onBeforeUnmount, onMounted, ref, watch} from 'vue'
import {useRoute} from 'vue-router'
import {site} from '@/config/site.ts'
import {useTheme} from '@/composables/useTheme.ts'

const route = useRoute()
const {theme, toggle} = useTheme()

const links = [
	{label: 'Home', to: '/', hash: ''},
	{label: 'About', to: '/#about', hash: '#about'},
	{label: 'Portfolio', to: '/#portfolio', hash: '#portfolio'},
	{label: 'Contact', to: '/#contact', hash: '#contact'}
]

// --- scroll-aware nav + progress bar ---
const sectionIds = ['about', 'portfolio', 'contact']
const activeHash = ref('')
const progress = ref(0)
let ticking = false

const update = () => {
	ticking = false
	const max = document.documentElement.scrollHeight - window.innerHeight
	progress.value = max > 0 ? Math.min(window.scrollY / max, 1) : 0

	const line = window.innerHeight * 0.4
	let current = ''
	for (const id of sectionIds) {
		const el = document.getElementById(id)
		if (el && el.getBoundingClientRect().top <= line) current = `#${id}`
	}
	// the Contact section is short, so highlight it once we hit the bottom
	if (max > 0 && window.scrollY >= max - 4) current = '#contact'
	activeHash.value = current
}

const onScroll = () => {
	if (ticking) return
	ticking = true
	requestAnimationFrame(update)
}

// --- mobile menu ---
const menuOpen = ref(false)
const closeMenu = () => (menuOpen.value = false)

const onKey = (e: KeyboardEvent) => {
	if (e.key === 'Escape') closeMenu()
}

onMounted(() => {
	window.addEventListener('scroll', onScroll, {passive: true})
	window.addEventListener('resize', onScroll)
	window.addEventListener('keydown', onKey)
	update()
})

onBeforeUnmount(() => {
	window.removeEventListener('scroll', onScroll)
	window.removeEventListener('resize', onScroll)
	window.removeEventListener('keydown', onKey)
})

// sections appear a moment after a route change
watch(() => route.fullPath, () => {
	closeMenu()
	nextTick(() => setTimeout(update, 400))
})

const isActive = (hash: string) => route.name === 'home' && activeHash.value === hash

// --- theme toggle: the icon spins half a turn each click ---
const spin = ref(0)
const onToggle = () => {
	spin.value += 1
	toggle()
}

// --- phone: click the icon to reveal the number ---
const showPhone = ref(false)
</script>

<template>
	<header class="fixed inset-x-0 top-0 z-40 bg-gradient-to-b from-black/70 to-transparent">
		<!-- scroll progress bar -->
		<div class="pointer-events-none absolute inset-x-0 top-0 h-0.5">
			<div class="h-full origin-left bg-accent" :style="{transform: `scaleX(${progress})`}"></div>
		</div>

		<nav class="flex items-center px-[3%] py-4 text-sm">
			<!-- hamburger (phones only) -->
			<button type="button" class="-ml-2 flex h-10 w-10 flex-col items-center justify-center gap-1.5 md:hidden"
					:aria-label="menuOpen ? 'Close menu' : 'Open menu'" :aria-expanded="menuOpen"
					aria-controls="mobile-menu" @click="menuOpen = !menuOpen">
				<span class="h-0.5 w-5 rounded bg-current transition-transform duration-300 motion-reduce:transition-none"
					  :class="menuOpen ? 'translate-y-2 rotate-45' : ''"></span>
				<span class="h-0.5 w-5 rounded bg-current transition-opacity duration-300 motion-reduce:transition-none"
					  :class="menuOpen ? 'opacity-0' : ''"></span>
				<span class="h-0.5 w-5 rounded bg-current transition-transform duration-300 motion-reduce:transition-none"
					  :class="menuOpen ? '-translate-y-2 -rotate-45' : ''"></span>
			</button>

			<!-- desktop links -->
			<ul class="hidden items-center gap-10 md:flex">
				<li v-for="link in links" :key="link.label">
					<RouterLink :to="link.to"
								class="relative inline-block py-1 transition hover:text-accent active:scale-95 motion-reduce:transition-none"
								:class="isActive(link.hash) ? 'text-accent' : 'text-white'">
						{{ link.label }}
						<!-- underline that grows under the active link -->
						<span class="absolute inset-x-0 -bottom-0.5 h-px origin-left bg-accent transition-transform duration-300 motion-reduce:transition-none"
							  :class="isActive(link.hash) ? 'scale-x-100' : 'scale-x-0'"></span>
					</RouterLink>
				</li>
			</ul>

			<div class="ml-auto flex items-center gap-6">
				<!-- phone: click the icon to reveal the number -->
				<div class="hidden items-center gap-2 text-xs lg:flex">
					<button type="button" aria-label="Show phone number" :aria-expanded="showPhone"
							class="text-base transition hover:text-accent active:scale-90"
							@click="showPhone = !showPhone">
						<i class="bx bx-mobile-alt"></i>
					</button>
					<Transition enter-active-class="transition duration-300" leave-active-class="transition duration-200"
								enter-from-class="-translate-x-2 opacity-0" leave-to-class="-translate-x-2 opacity-0">
						<a v-if="showPhone" :href="`tel:${site.phone.replace(/\s/g, '')}`"
						   class="whitespace-nowrap transition hover:text-accent">
							{{ site.phone }}
						</a>
					</Transition>
				</div>

				<button type="button" class="text-xl transition hover:text-accent active:scale-90"
						:aria-label="theme === 'dark' ? 'Switch to light mode' : 'Switch to dark mode'"
						@click="onToggle">
					<i :class="['bx', theme === 'dark' ? 'bx-sun' : 'bx-moon', 'inline-block transition-transform duration-500 motion-reduce:transition-none']"
					   :style="{transform: `rotate(${spin * 180}deg)`}"></i>
				</button>
				<RouterLink to="/#about"
							class="hidden rounded-full border border-white/70 px-5 py-1.5 text-xs transition hover:bg-white hover:text-ink active:scale-95 md:inline-block">
					About me
				</RouterLink>
			</div>
		</nav>

		<!-- mobile menu -->
		<Transition enter-active-class="transition duration-200" leave-active-class="transition duration-150"
					enter-from-class="-translate-y-2 opacity-0" leave-to-class="-translate-y-2 opacity-0">
			<div v-if="menuOpen" id="mobile-menu" class="md:hidden">
				<!-- tap outside to close -->
				<div class="fixed inset-0 -z-10" aria-hidden="true" @click="closeMenu"></div>

				<ul class="absolute inset-x-0 top-full border-y border-white/10 bg-black/90 px-[3%] py-2 backdrop-blur">
					<li v-for="link in links" :key="link.label" class="border-b border-white/10 last:border-b-0">
						<RouterLink :to="link.to" class="flex items-center justify-between py-3.5 text-base transition active:scale-[0.98]"
									:class="isActive(link.hash) ? 'text-accent' : 'text-white'" @click="closeMenu">
							{{ link.label }}
							<i v-if="isActive(link.hash)" class="bx bx-chevron-right text-xl"></i>
						</RouterLink>
					</li>
				</ul>
			</div>
		</Transition>
	</header>
</template>