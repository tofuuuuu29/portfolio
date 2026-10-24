<script setup lang="ts">
import {computed, onBeforeUnmount, onMounted, ref} from 'vue'
import {site} from '@/config/site.ts'

const KEY = 'intro-seen'
const visible = ref(false)
const leaving = ref(false)
const timers: number[] = []

const initials = computed(() => `${site.firstName.trim()[0]}${site.lastName.trim()[0]}`)
const letters = computed(() => [...site.name])

const seen = () => {
	try {
		return sessionStorage.getItem(KEY) === '1'
	} catch {
		return false
	}
}

const finish = () => {
	if (leaving.value) return
	leaving.value = true
	try {
		sessionStorage.setItem(KEY, '1')
	} catch {
		/* storage blocked, ignore */
	}
	timers.push(window.setTimeout(() => {
		visible.value = false
		document.body.style.overflow = ''
	}, 1400))
}

onMounted(() => {
	const force = new URLSearchParams(window.location.search).has('intro')
	const reduce = window.matchMedia('(prefers-reduced-motion: reduce)').matches

	if (!force && (seen() || reduce)) return

	visible.value = true
	document.body.style.overflow = 'hidden'
	timers.push(window.setTimeout(finish, 2600))
})

onBeforeUnmount(() => {
	timers.forEach((t) => clearTimeout(t))
	document.body.style.overflow = ''
})
</script>

<template>
	<div v-if="visible" class="intro" :class="{'is-leaving': leaving}" role="dialog" aria-label="Welcome">
		<div class="intro-inner">
			<div class="intro-mark">{{ initials }}</div>

			<p class="intro-name">
				<span class="sr-only">{{ site.name }}</span>
				<span v-for="(ch, i) in letters" :key="i" class="intro-letter" aria-hidden="true"
					  :style="{animationDelay: `${0.5 + i * 0.045}s`}">{{ ch }}</span>
			</p>

			<p class="intro-role">{{ site.role }}</p>

			<div class="intro-bar"><span></span></div>
		</div>

		<button type="button" class="intro-skip" @click="finish">Skip</button>
	</div>
</template>

<style scoped>
.intro {
	position: fixed;
	inset: 0;
	z-index: 100;
	display: flex;
	align-items: center;
	justify-content: center;
	background: var(--color-ink);
	color: var(--color-paper);
	will-change: transform;
	transition: transform 1s cubic-bezier(0.76, 0, 0.24, 1) 0.35s,
		border-radius 1s cubic-bezier(0.76, 0, 0.24, 1) 0.35s;
}

.intro.is-leaving {
	transform: translateY(-100%);
	border-radius: 0 0 50% 50% / 0 0 10vh 10vh;
}

.intro-inner {
	padding: 0 1.5rem;
	text-align: center;
	transition: opacity 0.45s ease, transform 0.45s ease, filter 0.45s ease;
}

.intro.is-leaving .intro-inner {
	opacity: 0;
	transform: translateY(-14px) scale(0.98);
	filter: blur(4px);
}

.intro-mark {
	font-size: clamp(4rem, 14vw, 8rem);
	font-weight: 800;
	line-height: 1;
	letter-spacing: 0.05em;
	color: var(--color-accent);
	animation: intro-mark 0.9s cubic-bezier(0.22, 1, 0.36, 1) both;
}

.intro-name {
	margin-top: 1.25rem;
	font-size: clamp(0.9rem, 3vw, 1.4rem);
	font-weight: 300;
	letter-spacing: 0.2em;
	text-transform: uppercase;
}

.intro-letter {
	display: inline-block;
	white-space: pre;
	opacity: 0;
	animation: intro-letter 0.5s ease forwards;
}

.intro-role {
	margin-top: 0.75rem;
	font-size: 0.8rem;
	letter-spacing: 0.4em;
	color: color-mix(in oklab, var(--color-paper) 60%, transparent);
	opacity: 0;
	animation: intro-fade 0.6s ease 1.6s forwards;
}

.intro-bar {
	width: 10rem;
	height: 2px;
	margin: 2rem auto 0;
	overflow: hidden;
	border-radius: 9999px;
	background: color-mix(in oklab, var(--color-paper) 15%, transparent);
}

.intro-bar span {
	display: block;
	height: 100%;
	background: var(--color-accent);
	transform: scaleX(0);
	transform-origin: left;
	animation: intro-load 2.3s ease-in-out 0.2s forwards;
}

.intro-skip {
	position: absolute;
	right: 2rem;
	bottom: 2rem;
	font-size: 0.7rem;
	letter-spacing: 0.25em;
	text-transform: uppercase;
	opacity: 0.6;
	transition: opacity 0.2s, color 0.2s;
}

.intro-skip:hover {
	color: var(--color-accent);
	opacity: 1;
}

.intro.is-leaving .intro-skip {
	opacity: 0;
	pointer-events: none;
}

@keyframes intro-mark {
	from { opacity: 0; transform: scale(0.85); filter: blur(10px); }
	to   { opacity: 1; transform: none; filter: blur(0); }
}

@keyframes intro-letter {
	from { opacity: 0; transform: translateY(10px); }
	to   { opacity: 1; transform: none; }
}

@keyframes intro-fade {
	to { opacity: 1; }
}

@keyframes intro-load {
	to { transform: scaleX(1); }
}
</style>