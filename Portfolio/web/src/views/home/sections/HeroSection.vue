<script setup lang="ts">
import {computed} from 'vue'
import {site} from '@/config/site.ts'
import {useTheme} from '@/composables/useTheme.ts'
import SparkleButton from '@/components/SparkleButton.vue'
import TechText from '@/components/TechText.vue'
import ElectricLogo from '@/components/ElectricLogo.vue'

const {theme} = useTheme()

// letter and accent colors follow dark / light mode
const ink = computed(() => (theme.value === 'light' ? '#0a0a0a' : '#f5f5f4'))
const accent = computed(() => (theme.value === 'light' ? '#1a77a6' : '#2a9fd6'))

// lightning colors: hot core + glow, matched to the site's teal accent
const boltCore = computed(() => (theme.value === 'light' ? '#0b4a6b' : '#d4f1ff'))
const boltGlow = computed(() => (theme.value === 'light' ? '#1a77a6' : '#2a9fd6'))

const firstLine = site.firstName.trim().toUpperCase()
const lastLine = site.lastName.trim().toUpperCase()

const toggleFullscreen = () => {
	if (document.fullscreenElement) document.exitFullscreen()
	else document.documentElement.requestFullscreen()
}
</script>

<template>
	<section class="relative -mt-16 flex h-screen min-h-[560px] items-center justify-between overflow-hidden">
		<div class="relative z-10 px-[8%]">
			<h1 class="sr-only">{{ site.name }}</h1>

			<!-- first name: light weight -->
			<div class="-ml-3 h-[4.5rem] w-[min(90vw,38rem)]" aria-hidden="true">
				<TechText :text="firstLine" align="left" :fontWeight="300" :fontSize="60" :letterSpacing="0"
						  :color="ink" :accentColor="accent" reveal="letter" :reach="200" :specks="15"
						  selection labels draggable sweep />
			</div>

			<!-- last name: extra bold -->
			<div class="-ml-3 -mt-3 h-[7rem] w-[min(90vw,38rem)]" aria-hidden="true">
				<TechText :text="lastLine" align="left" :fontWeight="800" :fontSize="96" :letterSpacing="0"
						  :color="ink" :accentColor="accent" reveal="letter" :reach="200" :specks="15"
						  selection labels draggable sweep />
			</div>

			<p class="mt-3 text-base font-light tracking-[0.4em] md:text-xl">{{ site.role }}</p>

			<div class="mt-8 flex gap-4">
				<SparkleButton to="/#about">About</SparkleButton>
				<SparkleButton to="/#portfolio">Portfolio</SparkleButton>
			</div>
		</div>

		<!-- electric CG logo (right side). On phones it sits faded behind the text. -->
		<div class="pointer-events-none absolute right-[-25%] top-1/2 z-0 h-[55vh] w-[110vw] -translate-y-1/2 opacity-30
					md:pointer-events-auto md:static md:z-10 md:mr-[3%] md:h-[70vh] md:w-[48vw] md:translate-y-0 md:opacity-100"
			 role="img" aria-label="CG logo">
			<ElectricLogo src="/cg-silhouette.png" :color="boltCore" :glowColor="boltGlow"
						  :scale="0.78" :intensity="1" :glow="1" :thickness="1.5" :strands="4" :bend="0.6"
						  :crackle="1.5" :arcs="1" :flicker="0.6" :fill="0.15" :speed="2.5"
						  interactive :cursorIntensity="0.75" :cursorRadius="110" :theme="theme" />
		</div>

		<div class="absolute bottom-8 left-[5%] z-10 flex gap-5 text-xl">
			<a v-for="s in site.socials" :key="s.url" :href="s.url" target="_blank" rel="noopener"
			   :aria-label="s.label" class="transition hover:text-accent">
				<i :class="['bx', s.icon]"></i>
			</a>
		</div>

		<button class="absolute bottom-8 right-[3%] z-10 text-xl transition hover:text-accent"
				aria-label="Toggle fullscreen" @click="toggleFullscreen">
			<i class="bx bx-fullscreen"></i>
		</button>
	</section>
</template>