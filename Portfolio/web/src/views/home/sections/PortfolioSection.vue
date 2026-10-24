<script setup lang="ts">
import {computed, onBeforeUnmount, onMounted, ref, watch} from 'vue'
import {site} from '@/config/site.ts'
import {usePortfolioStore} from '@/stores/portfolio.ts'
import BorderGlow from '@/components/BorderGlow.vue'

const portfolio = usePortfolioStore()

type Tab = 'projects' | 'certificates' | 'tech'

const tabs: { key: Tab; label: string }[] = [
	{key: 'projects', label: 'Projects'},
	{key: 'certificates', label: 'Certificates'},
	{key: 'tech', label: 'Tech Stack'}
]
const active = ref<Tab>('projects')
const activeIndex = computed(() => tabs.findIndex((t) => t.key === active.value))

const certificates = computed(() => site.certificateList)

// project shown in the details popup
const selected = ref<(typeof portfolio.projects)[number] | null>(null)
// certificate shown in the popup
const selectedCert = ref<(typeof certificates.value)[number] | null>(null)

// lock page scroll while a popup is open (important on phones)
watch(
	() => !!(selected.value || selectedCert.value),
	(open) => {
		document.body.style.overflow = open ? 'hidden' : ''
	}
)

// close popups with the Escape key
const onKey = (e: KeyboardEvent) => {
	if (e.key === 'Escape') {
		selected.value = null
		selectedCert.value = null
	}
}
onMounted(() => window.addEventListener('keydown', onKey))
onBeforeUnmount(() => {
	window.removeEventListener('keydown', onKey)
	document.body.style.overflow = ''
})

// tech stack logos (devicon): skill name -> icon folder name
const iconMap: Record<string, string> = {
	vue: 'vuejs', vuejs: 'vuejs', vue3: 'vuejs',
	typescript: 'typescript', ts: 'typescript',
	javascript: 'javascript', js: 'javascript',
	tailwind: 'tailwindcss', tailwindcss: 'tailwindcss',
	html: 'html5', html5: 'html5',
	css: 'css3', css3: 'css3',
	php: 'php', laravel: 'laravel',
	nextjs: 'nextjs', react: 'react', reactjs: 'react',
	nodejs: 'nodejs', node: 'nodejs', express: 'express',
	mysql: 'mysql', postgresql: 'postgresql', postgres: 'postgresql',
	mongodb: 'mongodb', firebase: 'firebase', redis: 'redis',
	spring: 'spring', springboot: 'spring',
	kotlin: 'kotlin', java: 'java', python: 'python', django: 'django',
	flask: 'flask', fastapi: 'fastapi',
	c: 'c', cpp: 'cplusplus', cplusplus: 'cplusplus', csharp: 'csharp',
	flutter: 'flutter', dart: 'dart', android: 'android', androidstudio: 'androidstudio',
	arduino: 'arduino', raspberrypi: 'raspberrypi', linux: 'linux',
	git: 'git', github: 'github', docker: 'docker', figma: 'figma',
	vite: 'vitejs', bootstrap: 'bootstrap', sass: 'sass', postman: 'postman',
	vscode: 'vscode', nginx: 'nginx', aws: 'amazonwebservices'
}

const iconSrc = (name: string) => {
	const key = name.toLowerCase().replace('c++', 'cpp').replace('c#', 'csharp').replace(/[^a-z0-9]/g, '')
	const slug = iconMap[key]
	return slug ? `https://cdn.jsdelivr.net/gh/devicons/devicon/icons/${slug}/${slug}-original.svg` : ''
}

// names whose logo failed to load -> show a letter instead
const brokenIcons = ref<Set<string>>(new Set())
const markBroken = (name: string) => {
	brokenIcons.value = new Set(brokenIcons.value).add(name)
}
</script>

<template>
	<section id="portfolio" class="mx-auto max-w-6xl scroll-mt-20 px-4 py-16 sm:scroll-mt-24 sm:px-6 sm:py-24">
		<!-- heading -->
		<div class="text-center">
			<h2 v-reveal class="text-3xl font-bold tracking-tight sm:text-4xl lg:text-5xl">Portfolio Showcase</h2>
			<p v-reveal="120" class="mx-auto mt-3 max-w-xl text-sm text-white/60 sm:mt-4 sm:text-base">
				Explore my journey through projects, certifications, and technical expertise.
			</p>
		</div>

		<!-- tab bar with sliding highlight -->
		<div v-reveal="220"
			 class="relative mx-auto mt-8 grid max-w-3xl grid-cols-3 gap-1 rounded-full border border-white/10 bg-white/[0.03] p-1.5 sm:mt-10">
			<span class="pointer-events-none absolute inset-y-1.5 left-1.5 rounded-full bg-white/10 transition-transform duration-500 ease-[cubic-bezier(.22,1,.36,1)]"
				  :style="{
					  width: 'calc((100% - 1.25rem) / 3)',
					  transform: `translateX(calc(${activeIndex} * (100% + 0.25rem)))`
				  }"></span>

			<button v-for="t in tabs" :key="t.key" type="button" @click="active = t.key"
					class="relative z-10 whitespace-nowrap rounded-full px-2 py-3 text-xs transition-colors duration-300 sm:px-4 sm:text-sm"
					:class="active === t.key ? 'text-white' : 'text-white/60 hover:text-white'">
				{{ t.label }}
			</button>
		</div>

		<!-- tab content: fades when switching tabs -->
		<Transition name="tab" mode="out-in">
			<div :key="active" class="mt-8 sm:mt-12">
				<!-- PROJECTS -->
				<div v-if="active === 'projects'">
					<p v-if="portfolio.loading" class="text-center text-white/50">Loading...</p>
					<p v-else-if="!portfolio.projects.length" class="text-center text-white/50">No projects yet.</p>

					<div class="grid gap-5 sm:grid-cols-2 sm:gap-6 lg:grid-cols-3">
						<BorderGlow v-for="(project, i) in portfolio.projects" :key="project.id"
									v-reveal="(i % 3) * 100" class="group"
									:border-radius="24" :glow-radius="30" :cone-spread="28">
							<article class="flex flex-1 flex-col p-3 sm:p-4">
								<div class="aspect-video w-full overflow-hidden rounded-2xl bg-white/5">
									<img v-if="project.imageUrl" :src="project.imageUrl" :alt="project.title"
										 loading="lazy" decoding="async"
										 class="h-full w-full object-cover transition duration-700 group-hover:scale-105" />
								</div>

								<div class="flex flex-1 flex-col px-1 pt-4 sm:pt-5">
									<h3 class="text-base font-semibold sm:text-lg">{{ project.title }}</h3>
									<p class="mt-2 line-clamp-2 text-sm text-white/60">{{ project.description }}</p>

									<div class="mt-auto flex items-center justify-between gap-3 pt-5 sm:pt-6">
										<a v-if="project.liveUrl" :href="project.liveUrl" target="_blank" rel="noopener"
										   class="py-2 text-sm text-accent hover:underline">
											Live Demo <i class="bx bx-link-external"></i>
										</a>
										<span v-else class="text-sm text-white/40">No Link</span>

										<button type="button" @click="selected = project"
												class="min-h-10 rounded-full bg-white/10 px-4 py-2 text-sm transition hover:bg-accent hover:text-white">
											Details <i class="bx bx-right-arrow-alt align-middle transition-transform duration-300 group-hover:translate-x-1"></i>
										</button>
									</div>
								</div>
							</article>
						</BorderGlow>
					</div>
				</div>

				<!-- CERTIFICATES -->
				<div v-else-if="active === 'certificates'">
					<p v-if="!certificates.length" class="text-center text-white/50">No certificates yet.</p>

					<div class="grid gap-5 sm:grid-cols-2 sm:gap-6 lg:grid-cols-3">
						<BorderGlow v-for="(c, i) in certificates" :key="c.title"
									v-reveal="(i % 3) * 100" class="group cursor-pointer" @click="selectedCert = c"
									:border-radius="24" :glow-radius="30" :cone-spread="28">
							<article class="flex flex-1 flex-col p-3 sm:p-4">
								<div class="aspect-video w-full overflow-hidden rounded-2xl bg-white/5">
									<img v-if="c.image" :src="c.image" :alt="c.title"
										 loading="lazy" decoding="async"
										 class="h-full w-full object-cover transition duration-700 group-hover:scale-105" />
								</div>
								<div class="flex flex-1 flex-col px-1 pt-4 sm:pt-5">
									<h3 class="text-base font-semibold sm:text-lg">{{ c.title }}</h3>
									<p class="mt-2 text-sm text-white/60">{{ c.issuer }}<span v-if="c.date"> · {{ c.date }}</span></p>
									<p class="mt-auto pt-5 text-sm text-accent sm:pt-6">
										View certificate <i class="bx bx-expand-alt transition-transform duration-300 group-hover:scale-125"></i>
									</p>
								</div>
							</article>
						</BorderGlow>
					</div>
				</div>

				<!-- TECH STACK -->
				<div v-else>
					<p v-if="!site.techStack.length" class="text-center text-white/50">No tech stack yet.</p>

					<div class="mx-auto grid max-w-4xl grid-cols-3 gap-3 sm:grid-cols-4 sm:gap-5 lg:grid-cols-5">
						<BorderGlow v-for="(name, i) in site.techStack" :key="name"
									v-reveal="(i % 3) * 60" class="group aspect-square"
									:border-radius="24" :glow-radius="24" :cone-spread="30">
							<div class="flex flex-1 flex-col items-center justify-center gap-2 p-2 sm:gap-3 sm:p-4">
								<img v-if="iconSrc(name) && !brokenIcons.has(name)" :src="iconSrc(name)"
									 :alt="name" width="48" height="48" loading="lazy" decoding="async" @error="markBroken(name)"
									 class="h-9 w-9 object-contain transition duration-300 group-hover:scale-125 sm:h-12 sm:w-12" />
								<span v-else class="flex h-9 w-9 items-center justify-center rounded-xl bg-white/10 text-lg font-bold transition duration-300 group-hover:scale-125 sm:h-12 sm:w-12 sm:text-xl">
									{{ name.charAt(0).toUpperCase() }}
								</span>
								<span class="text-center text-[11px] leading-tight text-white/80 sm:text-xs">{{ name }}</span>
							</div>
						</BorderGlow>
					</div>
				</div>
			</div>
		</Transition>

		<!-- project details popup -->
		<Transition name="modal">
			<div v-if="selected" class="fixed inset-0 z-50 flex items-center justify-center bg-black/80 p-3 sm:p-4 md:bg-black/70 md:backdrop-blur-sm"
				 @click.self="selected = null">
				<div class="modal-panel max-h-[90dvh] w-full max-w-lg overflow-y-auto overscroll-contain rounded-3xl border border-white/10 bg-ink p-5 sm:p-6">
					<div class="flex items-start justify-between gap-4">
						<h3 class="text-xl font-semibold sm:text-2xl">{{ selected.title }}</h3>
						<button type="button" @click="selected = null" aria-label="Close"
								class="-mr-2 -mt-2 flex h-10 w-10 shrink-0 items-center justify-center text-2xl leading-none text-white/60 transition hover:rotate-90 hover:text-white">
							&times;
						</button>
					</div>

					<img v-if="selected.imageUrl" :src="selected.imageUrl" :alt="selected.title"
						 class="mt-4 aspect-video w-full rounded-2xl object-cover sm:mt-5" />

					<p class="mt-4 text-sm leading-relaxed text-white/70 sm:mt-5">{{ selected.description }}</p>
					<p v-if="selected.techStack" class="mt-4 text-sm text-white/40">{{ selected.techStack }}</p>

					<div class="mt-6 flex gap-5 text-sm">
						<a v-if="selected.repoUrl" :href="selected.repoUrl" target="_blank" rel="noopener"
						   class="py-1 text-accent hover:underline"><i class="bx bxl-github"></i> Code</a>
						<a v-if="selected.liveUrl" :href="selected.liveUrl" target="_blank" rel="noopener"
						   class="py-1 text-accent hover:underline"><i class="bx bx-link-external"></i> Live</a>
					</div>
				</div>
			</div>
		</Transition>

		<!-- certificate popup -->
		<Transition name="modal">
			<div v-if="selectedCert" class="fixed inset-0 z-50 flex items-center justify-center bg-black/85 p-3 sm:p-4 md:bg-black/80 md:backdrop-blur-sm"
				 @click.self="selectedCert = null">
				<div class="modal-panel max-h-[92dvh] w-full max-w-4xl overflow-y-auto overscroll-contain rounded-3xl border border-white/10 bg-ink p-4 sm:p-5">
					<div class="flex items-start justify-between gap-4">
						<div>
							<h3 class="text-lg font-semibold sm:text-xl">{{ selectedCert.title }}</h3>
							<p class="mt-1 text-sm text-white/60">
								{{ selectedCert.issuer }}<span v-if="selectedCert.date"> · {{ selectedCert.date }}</span>
							</p>
						</div>
						<button type="button" @click="selectedCert = null" aria-label="Close"
								class="-mr-2 -mt-2 flex h-10 w-10 shrink-0 items-center justify-center text-2xl leading-none text-white/60 transition hover:rotate-90 hover:text-white">
							&times;
						</button>
					</div>

					<img v-if="selectedCert.image" :src="selectedCert.image" :alt="selectedCert.title"
						 class="mt-4 w-full rounded-2xl object-contain sm:mt-5" />

					<a v-if="selectedCert.url" :href="selectedCert.url" target="_blank" rel="noopener"
					   class="mt-5 inline-block py-1 text-sm text-accent hover:underline">
						Open verify link <i class="bx bx-link-external"></i>
					</a>
				</div>
			</div>
		</Transition>
	</section>
</template>