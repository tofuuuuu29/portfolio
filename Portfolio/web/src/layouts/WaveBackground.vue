<script setup lang="ts">
import {onBeforeUnmount, onMounted, ref, watch} from 'vue'
import {useTheme} from '@/composables/useTheme.ts'

const el = ref<HTMLElement | null>(null)
let vanta: {
	destroy: () => void
	setOptions: (o: object) => void
	renderer?: { forceContextLoss?: () => void }
} | null = null

const {theme} = useTheme()

const waveColor = {dark: 0x0, light: 0x8a8aac}

function cleanup() {
	if (!vanta) return
	try {
		// Vanta's destroy() leaves the WebGL context alive; release it explicitly
		vanta.renderer?.forceContextLoss?.()
		vanta.destroy()
	} catch (error) {
		console.warn('Wave background cleanup failed', error)
	}
	vanta = null
}

onMounted(() => {
	const VANTA = (window as any).VANTA
	if (!VANTA || !el.value || vanta) return

	try {
		vanta = VANTA.WAVES({
			el: el.value,
			mouseControls: true,
			touchControls: true,
			gyroControls: false,
			minHeight: 200.0,
			minWidth: 200.0,
			scale: 1.0,
			scaleMobile: 1.0,
			color: waveColor[theme.value],
			shininess: 26.0,
			waveHeight: 18.0,
			waveSpeed: 0.35,
			zoom: 1.01
		})
	} catch (error) {
		console.error('Unable to initialize the wave background; WebGL may be unavailable.', error)
	}
})

watch(theme, (t) => vanta?.setOptions({color: waveColor[t]}))

onBeforeUnmount(cleanup)
</script>

<template>
	<!-- outer div: fallback color shown if WebGL is unavailable; inner div: where Vanta draws -->
	<div
		class="fixed inset-0 -z-10"
		:class="theme === 'light' ? 'bg-[#8a8aac]' : 'bg-black'"
	>
		<div ref="el" class="absolute inset-0"></div>
	</div>
</template>