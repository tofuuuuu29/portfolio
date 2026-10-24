<script setup lang="ts">
import {computed, ref, watch} from 'vue'
import {site} from '@/config/site.ts'
import {usePortfolioStore} from '@/stores/portfolio.ts'
import BorderGlow from '@/components/BorderGlow.vue'
import {useTheme} from '@/composables/useTheme.ts'

const {theme} = useTheme()
const portfolio = usePortfolioStore()

// portraits live in web/public: dark mode uses about.jpg, light mode uses about-light.png
const portrait = computed(() =>
	theme.value === 'light' ? '/about-light.png' : '/about.jpg'
)

// show the initial if the image fails to load; reset when the theme changes
const imageOk = ref(true)
watch(portrait, () => {
	imageOk.value = true
})

// projects and tech stack come from the database; certificates come from site.ts
const stats = computed(() => [
	{label: 'Projects', value: portfolio.projects.length, icon: 'bx-folder-open'},
	{label: 'Certificates', value: site.certificateList.length, icon: 'bx-award'},
	{label: 'Tech Stack', value: portfolio.skills.length, icon: 'bx-layer'}
])

const pad = (n: number) => String(n).padStart(2, '0')

const viewProjects = () => {
	document.getElementById('projects')?.scrollIntoView({behavior: 'smooth'})
}
</script>

<template>
	<section id="about" class="relative isolate min-h-screen overflow-hidden">
		<div
			class="mx-auto flex min-h-screen max-w-6xl flex-col-reverse items-center justify-center gap-12 px-[7%] py-24 lg:flex-row lg:justify-between">
			<!-- left column -->
			<div class="w-full lg:max-w-[56%]">
				<p class="text-xs uppercase tracking-[0.3em] text-white/50">About me</p>

				<!-- bold name -->
				<h2 class="mt-4 text-[clamp(2.5rem,6vw,5.5rem)] font-extrabold leading-[0.95] tracking-tight">
					{{ site.firstName }}<br />{{ site.lastName }}
				</h2>
				<p class="mt-4 text-sm uppercase tracking-widest text-accent">{{ site.role }}</p>

				<!-- short paragraph -->
				<p class="mt-6 max-w-[34rem] text-[clamp(14px,1.1vw,18px)] leading-relaxed text-white/75">
					{{ site.about }}
				</p>

				<!-- buttons -->
				<div class="mt-8 flex flex-wrap gap-3">
					<a
						:href="site.cvUrl"
						download
						class="rounded-md bg-white px-6 py-2.5 text-sm font-medium text-ink transition hover:bg-accent hover:text-white">
						Download CV
					</a>
					<button
						type="button"
						class="rounded-md border border-white/30 px-6 py-2.5 text-sm transition hover:border-accent hover:text-accent"
						@click="viewProjects">
						View Projects
					</button>
				</div>

				<!-- stat cards with border glow -->
				<div class="mt-10 grid max-w-[34rem] grid-cols-3 gap-3">
					<BorderGlow
						v-for="s in stats"
						:key="s.label"
						:border-radius="12"
						:glow-radius="24"
						:cone-spread="28">
						<div class="p-4">
							<div class="flex items-start justify-between">
								<i class="bx text-lg text-white/60" :class="s.icon"></i>
								<span class="text-2xl font-bold leading-none">{{ pad(s.value) }}</span>
							</div>
							<p class="mt-4 text-[10px] uppercase tracking-widest text-white/50">{{ s.label }}</p>
						</div>
					</BorderGlow>
				</div>
			</div>

			<!-- circular portrait with border glow -->
			<BorderGlow class="shrink-0" :border-radius="9999" :glow-radius="40" :cone-spread="30">
				<div class="p-2">
					<div
						class="h-60 w-60 overflow-hidden rounded-full bg-white/5 sm:h-72 sm:w-72 lg:h-80 lg:w-80">
						<img
							v-if="imageOk"
							:src="portrait"
							:alt="site.firstName"
							class="h-full w-full object-cover"
							@error="imageOk = false" />
						<!-- fallback: first initial -->
						<span
							v-else
							class="grid h-full w-full place-items-center text-6xl font-bold text-white/30">
							{{ site.firstName?.[0] }}
						</span>
					</div>
				</div>
			</BorderGlow>
		</div>
	</section>
</template>