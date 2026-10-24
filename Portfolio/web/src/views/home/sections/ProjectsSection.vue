<script setup lang="ts">
import {usePortfolioStore} from '@/stores/portfolio.ts'

const portfolio = usePortfolioStore()
</script>

<template>
	<section id="projects" class="mx-auto max-w-6xl px-6 py-24">
		<h2 class="mb-12 text-3xl font-semibold tracking-tight">Selected work</h2>

		<p v-if="portfolio.loading" class="text-white/50">Loading...</p>
		<p v-else-if="!portfolio.projects.length" class="text-white/50">No projects yet.</p>

		<div class="grid gap-6 md:grid-cols-2">
			<article v-for="project in portfolio.projects" :key="project.id"
					 class="group rounded-2xl border border-white/10 p-6 transition hover:border-accent/60">
				<img v-if="project.imageUrl" :src="project.imageUrl" :alt="project.title"
					 class="mb-5 aspect-video w-full rounded-xl object-cover" />
				<div class="flex items-start justify-between gap-4">
					<h3 class="text-xl font-medium">{{ project.title }}</h3>
					<span v-if="project.featured" class="rounded-full bg-accent px-2 py-0.5 text-xs font-medium text-ink">
						Featured
					</span>
				</div>
				<p class="mt-3 text-white/60">{{ project.description }}</p>
				<p v-if="project.techStack" class="mt-4 text-sm text-white/40">{{ project.techStack }}</p>
				<div class="mt-5 flex gap-4 text-sm">
					<a v-if="project.repoUrl" :href="project.repoUrl" target="_blank" rel="noopener"
					   class="text-accent hover:underline"><i class="bx bxl-github"></i> Code</a>
					<a v-if="project.liveUrl" :href="project.liveUrl" target="_blank" rel="noopener"
					   class="text-accent hover:underline"><i class="bx bx-link-external"></i> Live</a>
				</div>
			</article>
		</div>
	</section>
</template>
