<script setup lang="ts">
import {computed, onMounted, ref} from 'vue'
import type {CSSProperties} from 'vue'
import {useTheme} from '@/composables/useTheme.ts'

const props = withDefaults(defineProps<{
	edgeSensitivity?: number
	glowColor?: string
	backgroundColor?: string
	borderRadius?: number
	glowRadius?: number
	glowIntensity?: number
	coneSpread?: number
	animated?: boolean
	colors?: string[]
	fillOpacity?: number
	flat?: boolean
}>(), {
	edgeSensitivity: 30,
	glowColor: '199 67 51',
	backgroundColor: undefined,
	borderRadius: 28,
	glowRadius: 40,
	glowIntensity: 1,
	coneSpread: 25,
	animated: false,
	colors: () => ['#2a9fd6', '#5cc4f0', '#8b8bf5'],
	fillOpacity: 0.5,
	flat: false
})

const card = ref<HTMLElement | null>(null)

const {theme} = useTheme()
const light = computed(() => theme.value === 'light')

// see-through by default, like the old stat cards
const cardBg = computed(() =>
	props.backgroundColor ?? (light.value ? 'rgb(10 10 10 / 4%)' : 'rgb(255 255 255 / 3%)')
)

/* ---------- css variable builders ---------- */

const parseHSL = (str: string) => {
	const m = str.match(/([\d.]+)\s*([\d.]+)%?\s*([\d.]+)%?/)
	if (!m) return {h: 199, s: 67, l: 51}
	return {h: Number(m[1]), s: Number(m[2]), l: Number(m[3])}
}

const GRADIENT_POSITIONS = ['80% 55%', '69% 34%', '8% 6%', '41% 38%', '86% 85%', '82% 18%', '51% 4%']
const GRADIENT_KEYS = ['--gradient-one', '--gradient-two', '--gradient-three', '--gradient-four', '--gradient-five', '--gradient-six', '--gradient-seven']
const COLOR_MAP = [0, 1, 2, 0, 1, 2, 1]

const cssVars = computed(() => {
	const vars: Record<string, string | number> = {
		'--card-bg': cardBg.value,
		'--edge-sensitivity': props.edgeSensitivity,
		'--border-radius': `${props.borderRadius}px`,
		'--glow-padding': `${props.glowRadius}px`,
		'--cone-spread': props.coneSpread,
		'--fill-opacity': props.fillOpacity
	}

	const {h, s, l} = parseHSL(props.glowColor)
	const base = `${h}deg ${s}% ${l}%`
	const opacities = [100, 60, 50, 40, 30, 20, 10]
	const keys = ['', '-60', '-50', '-40', '-30', '-20', '-10']
	opacities.forEach((o, i) => {
		vars[`--glow-color${keys[i]}`] = `hsl(${base} / ${Math.min(o * props.glowIntensity, 100)}%)`
	})

	for (let i = 0; i < 7; i++) {
		const c = props.colors[Math.min(COLOR_MAP[i], props.colors.length - 1)]
		vars[GRADIENT_KEYS[i]] = `radial-gradient(at ${GRADIENT_POSITIONS[i]}, ${c} 0px, transparent 50%)`
	}
	vars['--gradient-base'] = `linear-gradient(${props.colors[0]} 0 100%)`

	return vars as CSSProperties
})

/* ---------- pointer tracking ---------- */

const center = (el: HTMLElement): [number, number] => {
	const {width, height} = el.getBoundingClientRect()
	return [width / 2, height / 2]
}

const edgeProximity = (el: HTMLElement, x: number, y: number) => {
	const [cx, cy] = center(el)
	const dx = x - cx
	const dy = y - cy
	let kx = Infinity
	let ky = Infinity
	if (dx !== 0) kx = cx / Math.abs(dx)
	if (dy !== 0) ky = cy / Math.abs(dy)
	return Math.min(Math.max(1 / Math.min(kx, ky), 0), 1)
}

const cursorAngle = (el: HTMLElement, x: number, y: number) => {
	const [cx, cy] = center(el)
	const dx = x - cx
	const dy = y - cy
	if (dx === 0 && dy === 0) return 0
	let degrees = Math.atan2(dy, dx) * (180 / Math.PI) + 90
	if (degrees < 0) degrees += 360
	return degrees
}

const onPointerMove = (e: PointerEvent) => {
	const el = card.value
	if (!el) return
	const rect = el.getBoundingClientRect()
	const x = e.clientX - rect.left
	const y = e.clientY - rect.top
	el.style.setProperty('--edge-proximity', (edgeProximity(el, x, y) * 100).toFixed(3))
	el.style.setProperty('--cursor-angle', `${cursorAngle(el, x, y).toFixed(3)}deg`)
}

/* ---------- optional intro sweep ---------- */

const easeOutCubic = (x: number) => 1 - Math.pow(1 - x, 3)
const easeInCubic = (x: number) => x * x * x

const animateValue = (o: {
	start?: number
	end?: number
	duration?: number
	delay?: number
	ease?: (x: number) => number
	onUpdate: (v: number) => void
	onEnd?: () => void
}) => {
	const {start = 0, end = 100, duration = 1000, delay = 0, ease = easeOutCubic} = o
	const t0 = performance.now() + delay
	const tick = () => {
		const t = Math.min((performance.now() - t0) / duration, 1)
		o.onUpdate(start + (end - start) * ease(t))
		if (t < 1) requestAnimationFrame(tick)
		else o.onEnd?.()
	}
	setTimeout(() => requestAnimationFrame(tick), delay)
}

onMounted(() => {
	const el = card.value
	if (!props.animated || !el) return

	const angleStart = 110
	const angleEnd = 465
	const setAngle = (v: number) =>
		el.style.setProperty('--cursor-angle', `${(angleEnd - angleStart) * (v / 100) + angleStart}deg`)

	el.classList.add('sweep-active')
	el.style.setProperty('--cursor-angle', `${angleStart}deg`)

	animateValue({duration: 500, onUpdate: (v) => el.style.setProperty('--edge-proximity', String(v))})
	animateValue({ease: easeInCubic, duration: 1500, end: 50, onUpdate: setAngle})
	animateValue({ease: easeOutCubic, delay: 1500, duration: 2250, start: 50, end: 100, onUpdate: setAngle})
	animateValue({
		ease: easeInCubic, delay: 2500, duration: 1500, start: 100, end: 0,
		onUpdate: (v) => el.style.setProperty('--edge-proximity', String(v)),
		onEnd: () => el.classList.remove('sweep-active')
	})
})
</script>

<template>
	<div ref="card" class="border-glow-card"
		 :class="{'border-glow-card--light': light, 'border-glow-card--flat': flat}"
		 :style="cssVars" @pointermove="onPointerMove">
		<span class="edge-light"></span>
		<div class="border-glow-inner">
			<slot/>
		</div>
	</div>
</template>

<style>
.border-glow-card {
	--edge-proximity: 0;
	--cursor-angle: 45deg;
	--edge-sensitivity: 30;
	--color-sensitivity: calc(var(--edge-sensitivity) + 20);
	--border-radius: 28px;
	--glow-padding: 40px;
	--cone-spread: 25;

	position: relative;
	border-radius: var(--border-radius);
	isolation: isolate;
	transform: translate3d(0, 0, 0.01px);
	display: grid;
	border: 1px solid rgb(255 255 255 / 12%);
	background: var(--card-bg, rgb(255 255 255 / 3%));
	overflow: visible;
}

.border-glow-card--light {
	border-color: rgb(24 24 27 / 12%);
}

.border-glow-card--light::after,
.border-glow-card--light > .edge-light {
	mix-blend-mode: normal;
}

.border-glow-card::before,
.border-glow-card::after,
.border-glow-card > .edge-light {
	content: '';
	position: absolute;
	inset: 0;
	border-radius: inherit;
	transition: opacity 0.25s ease-out;
	z-index: -1;
}

.border-glow-card:not(:hover):not(.sweep-active)::before,
.border-glow-card:not(:hover):not(.sweep-active)::after,
.border-glow-card:not(:hover):not(.sweep-active) > .edge-light {
	opacity: 0;
	transition: opacity 0.75s ease-in-out;
}

/* colored mesh-gradient border: only the thin ring is painted, the middle stays see-through */
.border-glow-card::before {
	border: 1px solid transparent;
	background:
		var(--gradient-one) border-box,
		var(--gradient-two) border-box,
		var(--gradient-three) border-box,
		var(--gradient-four) border-box,
		var(--gradient-five) border-box,
		var(--gradient-six) border-box,
		var(--gradient-seven) border-box,
		var(--gradient-base) border-box;

	opacity: calc((var(--edge-proximity) - var(--color-sensitivity)) / (100 - var(--color-sensitivity)));

	mask-image:
		conic-gradient(
			from var(--cursor-angle) at center,
			black calc(var(--cone-spread) * 1%),
			transparent calc((var(--cone-spread) + 15) * 1%),
			transparent calc((100 - var(--cone-spread) - 15) * 1%),
			black calc((100 - var(--cone-spread)) * 1%)
		),
		linear-gradient(#000 0 0),
		linear-gradient(#000 0 0);
	mask-clip: border-box, padding-box, border-box;
	mask-composite: intersect, exclude;
	-webkit-mask-composite: source-in, xor;
}

/* colored mesh-gradient fill near edges */
.border-glow-card::after {
	border: 1px solid transparent;
	background:
		var(--gradient-one) padding-box,
		var(--gradient-two) padding-box,
		var(--gradient-three) padding-box,
		var(--gradient-four) padding-box,
		var(--gradient-five) padding-box,
		var(--gradient-six) padding-box,
		var(--gradient-seven) padding-box,
		var(--gradient-base) padding-box;

	mask-image:
		linear-gradient(to bottom, black, black),
		radial-gradient(ellipse at 50% 50%, black 40%, transparent 65%),
		radial-gradient(ellipse at 66% 66%, black 5%, transparent 40%),
		radial-gradient(ellipse at 33% 33%, black 5%, transparent 40%),
		radial-gradient(ellipse at 66% 33%, black 5%, transparent 40%),
		radial-gradient(ellipse at 33% 66%, black 5%, transparent 40%),
		conic-gradient(from var(--cursor-angle) at center, transparent 5%, black 15%, black 85%, transparent 95%);

	mask-composite: subtract, add, add, add, add, add;
	opacity: calc(var(--fill-opacity, 0.5) * (var(--edge-proximity) - var(--color-sensitivity)) / (100 - var(--color-sensitivity)));
	mix-blend-mode: soft-light;
}

/* outer glow layer */
.border-glow-card > .edge-light {
	inset: calc(var(--glow-padding) * -1);
	pointer-events: none;
	z-index: 1;

	mask-image: conic-gradient(
		from var(--cursor-angle) at center, black 2.5%, transparent 10%, transparent 90%, black 97.5%
	);

	opacity: calc((var(--edge-proximity) - var(--edge-sensitivity)) / (100 - var(--edge-sensitivity)));
	mix-blend-mode: plus-lighter;
}

.border-glow-card > .edge-light::before {
	content: '';
	position: absolute;
	inset: var(--glow-padding);
	border-radius: inherit;
	box-shadow:
		inset 0 0 0 1px var(--glow-color),
		inset 0 0 1px 0 var(--glow-color-60),
		inset 0 0 3px 0 var(--glow-color-50),
		inset 0 0 6px 0 var(--glow-color-40),
		inset 0 0 15px 0 var(--glow-color-30),
		inset 0 0 25px 2px var(--glow-color-20),
		inset 0 0 50px 2px var(--glow-color-10),
		0 0 1px 0 var(--glow-color-60),
		0 0 3px 0 var(--glow-color-50),
		0 0 6px 0 var(--glow-color-40),
		0 0 15px 0 var(--glow-color-30),
		0 0 25px 2px var(--glow-color-20),
		0 0 50px 2px var(--glow-color-10);
}

.border-glow-inner {
	display: flex;
	flex-direction: column;
	position: relative;
	overflow: hidden;
	border-radius: inherit;
	z-index: 1;
}
</style>